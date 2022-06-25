package it.polimi.ingsw.server;

import it.polimi.ingsw.utilities.ErrorMessage;
import it.polimi.ingsw.utilities.EventName;
import it.polimi.ingsw.utilities.ServerMessage;
import it.polimi.ingsw.utilities.Constants;
import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.RemoteView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 Manage the connections of the clients and starts configuration of the game
 when the expected number of players is connected
 */
public class Server implements PropertyChangeListener {
    private int numberOfPlayers;
    private final ServerSocket serverSocket;
    private final ExecutorService executor = Executors.newFixedThreadPool(128);
    private final Map<Player, SocketClientConnection> waitingConnection = new HashMap<>();

    private final Map<GameHandler, Map<Player, SocketClientConnection>> mapGameWaitingConnection = new HashMap<>();

    private final Map<GameHandler, List<RemoteView>> mapGameRemoteViews = new HashMap<>();

    GameHandler gameHandler;
    private final List<List<SocketClientConnection>> listOfConnections = new ArrayList<>();
    private boolean setupAborted;

    /*
    listOfGames contains all the gameHandler of the matches that are currently playing
    when the server is turned off, they are all saved on a file
    when the server is turned on again and someone connect:
    - if the username used by the player is NOT contained in any saved matches,
        starts a new match in the usual way
    - if the user is contained in one of the old matches, a new waitingConnection is created for that game
      and associated to it thanks to mapGameWaitingConnection
     */
    private List<GameHandler> listOfGames = new ArrayList<>();


    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }


    /**
     * Called when a client is disconnected/quit
     * Notify the other players that the game is over and delete the game from the list of games
     * If the players where still in the setup, delete the connections from the waiting list
     *
     * @param c SocketClientConnection of the player
     */
    public synchronized void deregisterConnection(SocketClientConnection c) {
        String userStartedDisconnection = null;
        out:
        for (List<SocketClientConnection> l : listOfConnections) {
            for (SocketClientConnection s : l) {
                userStartedDisconnection = c.getNickname();
                if (s == c) {
                    for (SocketClientConnection toRemove : l) {
                        if (!toRemove.getNickname().equals(userStartedDisconnection)) {
                            System.out.println("I'm sending message of closing connection to " + toRemove.getNickname());
                            toRemove.send(String.format(ServerMessage.userClosedConnection, userStartedDisconnection));
                            toRemove.setPlayerQuitted(true);
                            toRemove.closeConnection();
                        }
                    }
                    listOfConnections.remove(l);
                    break out;
                }
            }
        }
        if (!waitingConnection.isEmpty()) {
            for (SocketClientConnection s : waitingConnection.values()) {
                if (s != c) {
                    System.out.println("I'm sending message of closing connection to " + s.getNickname());
                    s.send(String.format(ServerMessage.userClosedConnection, userStartedDisconnection));
                    s.setPlayerQuitted(true);
                    s.closeConnection();
                }
            }
            setupAborted = true;
            waitingConnection.clear();
        }
        deleteGameByUser(userStartedDisconnection);
    }

    /**
     * Find the game of the user that is disconnecting and
     * delete it from the list of games
     *
     * @param userStartedDisconnection nickname
     */
    private void deleteGameByUser(String userStartedDisconnection) {
        GameHandler gameToDelete = null;
        for (GameHandler g : listOfGames) {
            for (Player p : g.getGame().getListOfPlayer()) {
                if (p.getNickname().equals(userStartedDisconnection)) {
                    gameToDelete = g;
                    break;
                }
            }
        }
        if (gameToDelete != null) {
            listOfGames.remove(gameToDelete);
        }
    }

    /**
     * checks if c is the last still active connection, in case
     * removes it from list of active games
     *
     * @param c SocketClientConnection of the player
     */
    public synchronized void checkEmptyGames(SocketClientConnection c) {
        try {
            for (List<SocketClientConnection> l : listOfConnections) {
                for (SocketClientConnection s : l) {
                    if (s.getNickname().equals(c.getNickname())) {
                        if (l.size() == 1) {
                            System.out.println(c.getNickname() + " is the last player");
                            listOfConnections.remove(l);
                            deleteGameByUser(c.getNickname());
                        }
                        l.remove(s);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when a new player connects
     * If the player chooses a username of a saved game, call setupOldMatch
     * Otherwise call setupNewMatch
     *
     * @param c SocketClientConnection of the player
     */
    public synchronized void lobby(SocketClientConnection c) {
        setupAborted = false;
        if (!listOfGames.isEmpty()) {
            if (!waitingConnection.isEmpty()) {
                c.asyncSend(ServerMessage.ongoingMatches + " Otherwise \n");
            } else {
                c.asyncSend(ServerMessage.ongoingMatches);
            }
        }
        if (!waitingConnection.isEmpty()) {
            StringBuilder nickOfOtherPlayers = new StringBuilder(ServerMessage.joiningMessage);
            for (Player p : waitingConnection.keySet()) {
                nickOfOtherPlayers.append(p.getNickname()).append(" ");
            }
            c.asyncSend(nickOfOtherPlayers.toString());
        }


        //I moved nickname here so when other player connect the others receive his name
        String nickname = c.askNickname();
        if (nickname.equalsIgnoreCase(Constants.QUIT)) setupAborted = true;
        while (!setupAborted && !checkNickname(nickname)) {
            c.asyncSend(ErrorMessage.DuplicateNickname);
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            nickname = c.askNickname();
        }
        if (!setupAborted) {
            c.setNickname(nickname);

            GameHandler savedGame = checkPlayerAlreadyExists(nickname, c);

            if (savedGame == null) {
                setupNewMatch(nickname, c);
            } else {
                setupOldMatch(nickname, savedGame);
            }
        }


    }

    /**
     * Detect if the connected player is the first or it needs to be added to a match
     *
     * @param nickname of the player
     * @param c        SocketClientConnection of the player
     */
    private void setupNewMatch(String nickname, SocketClientConnection c) {
        List<Player> keys = new ArrayList<>(waitingConnection.keySet());
        if (waitingConnection.size() == 0) {
            registerFirstPlayer(nickname, c);
        } else {
            for (Player p : keys) {
                SocketClientConnection connection = waitingConnection.get(p);
                connection.asyncSend(ServerMessage.connectedUser + nickname);
            }
            registerOtherPlayers(nickname, c);
        }
        if (!setupAborted) {
            if (waitingConnection.size() < numberOfPlayers) {
                c.asyncSend(ServerMessage.waitingOtherPlayers);
            } else if (waitingConnection.size() == numberOfPlayers) {

                System.out.println("Number of player reached! Starting the game... ");

                List<SocketClientConnection> temp = new ArrayList<>();
                for (Player p : waitingConnection.keySet()) {
                    temp.add(waitingConnection.get(p));
                    listOfConnections.add(temp);
                }

                listOfGames.add(gameHandler);
                waitingConnection.clear();
            }
        }
    }

    /**
     * If called, it means that the player used a nickname of a saved game
     * When all the old players are connected, resend the situation of the game
     *
     * @param nickname  of the player
     * @param savedGame old game that has been restored
     */
    private void setupOldMatch(String nickname, GameHandler savedGame) {
        for (SocketClientConnection s : mapGameWaitingConnection.get(savedGame).values()) {
            s.asyncSend(ServerMessage.connectedUser + nickname);
        }
        StringBuilder nickOfPlayerToWait = new StringBuilder();
        for (Player p : savedGame.getGame().getListOfPlayer()) {
            if (!mapGameWaitingConnection.get(savedGame).containsKey(p)) {
                nickOfPlayerToWait.append(p.getNickname()).append(" ");
            }
        }
        if (mapGameWaitingConnection.get(savedGame).size() < savedGame.getPlayersNumber()) {
            for (SocketClientConnection s : mapGameWaitingConnection.get(savedGame).values()) {
                s.asyncSend(ServerMessage.waitingOldPlayers + nickOfPlayerToWait);
            }
        } else {

            System.out.println("Number of player reached! Starting the game... ");

            List<SocketClientConnection> temp = new ArrayList<>();
            for (Player p : mapGameWaitingConnection.get(savedGame).keySet()) {
                temp.add(mapGameWaitingConnection.get(savedGame).get(p));
                listOfConnections.add(temp);
            }
            for (SocketClientConnection s : mapGameWaitingConnection.get(savedGame).values()) {
                s.send(ServerMessage.startingGame);
            }
            for (RemoteView rem : mapGameRemoteViews.get(savedGame)) {
                rem.resendSituation();
            }

            mapGameWaitingConnection.remove(savedGame);
        }
    }

    /**
     * Check if the nickname chosen has already been taken
     *
     * @param nameToCheck nickname to check
     * @return true if the nickname is available globally, false otherwise
     */
    public boolean checkNickname(String nameToCheck) {
        for (Player p : waitingConnection.keySet()) {
            if (p.getNickname().equalsIgnoreCase(nameToCheck)) {
                return false;
            }
        }

        /*Use the mapGameRemoteViews because in this way if there was an old match
         * with a player nickname "nameToCheck", if it has not re-logged yet
         * it's allowed to do it, otherwise return that the nick is already used
         */
        for (GameHandler g : mapGameRemoteViews.keySet()) {
            for (RemoteView r : mapGameRemoteViews.get(g)) {
                if (r.getPlayer().getNickname().equalsIgnoreCase(nameToCheck)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if the chosed color is already been taken
     *
     * @param colorOfTower color of the tower to check
     * @return True if the color is still available, false if it is already been taken
     */
    public boolean checkColorTower(ColorOfTower colorOfTower) {
        if (gameHandler.getGame().getNumberOfPlayers() == 2 || gameHandler.getGame().getNumberOfPlayers() == 4) {
            if (colorOfTower == ColorOfTower.GREY) {
                return false;
            }
        }
        for (Player p : waitingConnection.keySet()) {
            if (p.getColorOfTowers() == colorOfTower) {
                return false;
            }
        }
        return true;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * Save the games that are currently going on the server
     */
    private void saveGames() {
        try {
            // Create a file to write game system
            FileOutputStream out = new FileOutputStream(Constants.NAMEFILEFORSAVEMATCHES);

            // Code to write instance of GamingWorld will go here
            // Create an object output stream, linked to out
            ObjectOutputStream objectOut = new ObjectOutputStream(out);

            // Write game system to object store
            objectOut.writeObject(listOfGames);

            // Close object output stream
            objectOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unable to create game data");
        }
    }

    /**
     * Restore the games saved in the file and put them in the list of games
     */
    private void restoreGame() {
        // Create a file input stream
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(Constants.NAMEFILEFORSAVEMATCHES);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Create an object input stream
        ObjectInputStream objectIn = null;
        try {
            objectIn = new ObjectInputStream(fin);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Read an object in from object store, and cast it to a mapGameConnection
        try {
            //listOfGames save te current 
            listOfGames = (List<GameHandler>) objectIn.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (!listOfGames.isEmpty()) {
            System.out.println("Games already saved: " + listOfGames.size());
        }

        // Set the object stream to standard output
    }

    /**
     * Called if the connected player is the first of the game, it asks the setup info (number of the players, mode)
     * Create a new game adding the first player
     *
     * @param nickname of the first player of the game
     * @param c        SocketClientConnection of the first player of the game
     */
    private void registerFirstPlayer(String nickname, SocketClientConnection c) {
        ColorOfTower color = null;
        numberOfPlayers = c.askHowManyPlayers();
        if (numberOfPlayers == -2) {
            setupAborted = true;
            return;
        }
        while ((numberOfPlayers <= 0 || numberOfPlayers > Constants.MAXPLAYERS) && !setupAborted) {
            c.asyncSend(ErrorMessage.NumberOfPlayersNotValid);
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            numberOfPlayers = c.askHowManyPlayers();
        }


        int mode = c.askMode();
        if (mode == -2) setupAborted = true;
        while (!setupAborted && mode == -1) {
            c.asyncSend(ErrorMessage.ModeNotValid);
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            mode = c.askMode();
        }
        if (setupAborted) return;
        color = c.askColor();
        while (!setupAborted && color == null) {
            c.asyncSend(ErrorMessage.ActionNotValid);
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            color = c.askColor();
        }
        if (setupAborted) return;
        Player player1 = new Player(nickname, color);
        player1.setTeam(0);

        waitingConnection.put(player1, c);
        gameHandler = new GameHandler(player1, numberOfPlayers, mode == 1);
        RemoteView remV1 = new RemoteView(player1, c, gameHandler.getGame(), gameHandler.getController().getTurnController().getActionController().getActionParser());
        c.addPropertyChangeListener(remV1);
        gameHandler.addPropertyChangeListener(remV1);
        gameHandler.getGame().addPropertyChangeListener(remV1);
        gameHandler.getGame().addPropertyChangeListener(this);
        gameHandler.getController().getTurnController().getActionController().addPropertyChangeListener(remV1);

        gameHandler.getController().getTurnController().getActionController().getActionParser().addPropertyChangeListener(remV1);
        gameHandler.getController().getTurnController().addPropertyChangeListener(remV1);
    }

    /**
     * Called when the player is logging in a game; assign to the player the number of the team;
     * add it to the game
     *
     * @param nickname of the player
     * @param c        of the player
     */
    private void registerOtherPlayers(String nickname, SocketClientConnection c) {
        ColorOfTower color = null;
        if (numberOfPlayers != 4 || (waitingConnection.size() + 1) % 2 != 0) {
            color = c.askColor();
            while ((color == null || !checkColorTower(color)) && !setupAborted) {
                if (color == null) {
                    c.asyncSend(ErrorMessage.ActionNotValid);
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    c.asyncSend(ErrorMessage.ColorNotValid);
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                color = c.askColor();
            }
            if (setupAborted) return;
        } else {
            //Only the first member of the team take the towers
            color = null;
        }

        Player player = new Player(nickname, color);
            /*
            if 4 players:
            if size 1--> 0; 2 --> 1 ; 3--> 1
            else for 2,3 players team is a progressive number
             */
        if (numberOfPlayers == 4) {
            if (waitingConnection.size() == 1) {
                player.setTeam(0);
            } else {
                player.setTeam(1);
            }
        } else {
            player.setTeam(waitingConnection.size());
        }

        waitingConnection.put(player, c);
        createRemoteView(c, gameHandler, player);
        gameHandler.addNewPlayer(player);
    }

    /**
     * If already exists a saved match with that username, return the gameHandler of that game
     *
     * @param nickname to chekc
     * @param c        of the player
     * @return gameHandler of the old game
     */
    private GameHandler checkPlayerAlreadyExists(String nickname, SocketClientConnection c) {
        for (GameHandler g : listOfGames) {
            for (Player p : g.getGame().getListOfPlayer()) {
                if (p.getNickname().equalsIgnoreCase(nickname)) {
                    g.setNewController();
                    RemoteView remV = createRemoteView(c, g, p);
                    boolean gameAlreadyExisting = false;
                    for (GameHandler game : mapGameRemoteViews.keySet()) {
                        if (game.equals(g)) {
                            mapGameRemoteViews.get(game).add(remV);
                            gameAlreadyExisting = true;
                        }
                    }
                    if (!gameAlreadyExisting) {
                        List<RemoteView> newList = new ArrayList<>();
                        newList.add(remV);
                        mapGameRemoteViews.put(g, newList);
                    }

                    for (GameHandler game : mapGameWaitingConnection.keySet()) {
                        if (game.equals(g)) {
                            mapGameWaitingConnection.get(game).put(p, c);
                            return g;
                        }
                    }
                    HashMap<Player, SocketClientConnection> newMap = new HashMap<>();
                    newMap.put(p, c);
                    mapGameWaitingConnection.put(g, newMap);
                    return g;
                }
            }
        }
        return null;
    }

    private RemoteView createRemoteView(SocketClientConnection c, GameHandler g, Player p) {
        RemoteView remV = new RemoteView(p, c, g.getGame(), g.getController().getTurnController().getActionController().getActionParser());
        c.addPropertyChangeListener(remV);
        g.addPropertyChangeListener(remV);
        g.getGame().addPropertyChangeListener(remV);
        g.getController().getTurnController().getActionController().addPropertyChangeListener(remV);
        g.getController().getTurnController().addPropertyChangeListener(remV);
        g.getController().getTurnController().getActionController().getActionParser().addPropertyChangeListener(remV);
        return remV;
    }

    /**
     * Called when the server is turned on, try to restore the saved games (if they exists)
     * Wait for the connections
     */
    public void run() {

        Runtime.getRuntime().addShutdownHook(new Thread(this::saveGames));

        int connections = 0;
        System.out.println("Server is running");

        //If a game has been saved, it will restore it
        File gamesSaved = new File(Constants.NAMEFILEFORSAVEMATCHES);
        if (gamesSaved.isFile()) {
            restoreGame();
        }

        while (true) {
            try {
                Socket newSocket = serverSocket.accept();
                connections++;
                System.out.println("Ready for the new connection - " + connections);
                SocketClientConnection socketConnection = new SocketClientConnection(newSocket, this);
                executor.submit(socketConnection);
            } catch (IOException e) {
                System.out.println("Connection Error!");
            }
        }
    }

    @Override
    public synchronized void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(EventName.PhaseChanged)){
            saveGames();
        }
    }
}