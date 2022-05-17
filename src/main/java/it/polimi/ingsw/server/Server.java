package it.polimi.ingsw.server;

import it.polimi.ingsw.utilities.ErrorMessage;
import it.polimi.ingsw.utilities.constants.Constants;
import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.RemoteView;

import java.io.IOException;
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
public class Server {
    private int numberOfPlayers;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(128);
    private Map<Player, SocketClientConnection> waitingConnection = new HashMap<>();
    GameHandler gameHandler;
    private List<List<SocketClientConnection>> listOfGames = new ArrayList<>();

    //Deregister connection
    public synchronized void deregisterConnection(SocketClientConnection c) {
        for(List<SocketClientConnection> l : listOfGames){
            for(SocketClientConnection s : l){
                System.out.println("I'm confronting " + s.getNickname());
                if(s == c){
                    for(SocketClientConnection toRemove : l){
                        System.out.println("I'm confroning" + toRemove.getNickname());
                        if(!toRemove.getNickname().equals(c.getNickname())){
                            System.out.println("I'm sending to " + toRemove.getNickname());
                            toRemove.send("User " + c.getNickname() + " closed the connection. \n Exiting from the game...");
                            toRemove.setPlayerQuitted(true);
                            toRemove.closeConnection();
                        }

                    }
                    listOfGames.remove(l);
                    break;
                }
            }
        }
        if(!waitingConnection.isEmpty()){
            for(SocketClientConnection s : waitingConnection.values()){
                        System.out.println("I'm confroning" + s.getNickname());
                        if(s != c) {
                            System.out.println("I'm sending to " + s.getNickname());
                            s.send("User " + c.getNickname() + " closed the connection. \n Exiting from the game...");
                            s.setPlayerQuitted(true);
                            s.closeConnection();
                        }
                }
            waitingConnection.clear();
            }
        }

    /**
     * checks if c is the last still active connection, in case
     * removes it from list of active games
     * @param c
     */
    public synchronized void checkEmptyGames(SocketClientConnection c) {
        for(List<SocketClientConnection> l : listOfGames){
            for(SocketClientConnection s : l){
                if(s.getNickname().equals(c.getNickname())) {
                    if (l.size() == 1) {
                        listOfGames.remove(l);
                    }
                }
            }
        }

    }

    //Wait for another player
    public synchronized void lobby(SocketClientConnection c){
        List<Player> keys = new ArrayList<>(waitingConnection.keySet());
        ColorOfTower color = null;
        //I moved nickname here so when other player connect the others receive his name
        String nickname = c.askNickname();
        while(!checkNickname(nickname)){
            c.asyncSend(ErrorMessage.DuplicateNickname);
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            nickname = c.askNickname();
        }
        c.setNickname(nickname);

        for(int i = 0; i < keys.size(); i++){
            SocketClientConnection connection = waitingConnection.get(keys.get(i));
            connection.asyncSend("Connected User: " + nickname);
        }
        if(waitingConnection.size() == 0) {
            numberOfPlayers = c.askHowManyPlayers();
            while (numberOfPlayers <= 0 || numberOfPlayers > Constants.MAXPLAYERS){
                c.asyncSend(ErrorMessage.NumberOfPlayersNotValid);
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                numberOfPlayers = c.askHowManyPlayers();
            };

            int mode = -1;
            mode = c.askMode();

            while(mode == -1){
                c.asyncSend(ErrorMessage.ModeNotValid);
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                mode = c.askMode();
            }
            color = c.askColor();
            while(color == null ){
                c.asyncSend( ErrorMessage.ActionNotValid);
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                color = c.askColor();
            }
            Player player1 = new Player(nickname, color);
            player1.setTeam(0);

            waitingConnection.put(player1, c);
            gameHandler = new GameHandler(player1, numberOfPlayers, mode == 1);
            RemoteView remV1 = new RemoteView(player1, c, gameHandler.getGame(), gameHandler.getController().getTurnController().getActionController().getActionParser());
            c.addPropertyChangeListener(remV1);
            gameHandler.addPropertyChangeListener(remV1);
            gameHandler.getGame().addPropertyChangeListener(remV1);
            gameHandler.getController().getTurnController().getActionController().addPropertyChangeListener(remV1);
            System.out.println("devo aggiunger listener al parser");
            gameHandler.getController().getTurnController().getActionController().getActionParser().addPropertyChangeListener(remV1);
            System.out.println("ho aggiunto listener al parser");
            gameHandler.getController().getTurnController().addPropertyChangeListener(remV1);

        } else {
            if(numberOfPlayers != 4 || (waitingConnection.size() + 1) % 2 != 0){
                color = c.askColor();
                while (color == null || !checkColorTower(color)){
                    if(color == null) {
                        c.asyncSend(ErrorMessage.ActionNotValid);
                        try {
                            TimeUnit.MILLISECONDS.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        color = c.askColor();
                    }else{
                        c.asyncSend(ErrorMessage.ColorNotValid);
                        try {
                            TimeUnit.MILLISECONDS.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        color = c.askColor();
                    }
                }
            }else{
                //Only the first member of the team take the towers
                color = null;
            }

            Player player = new Player(nickname, color);
            /*
            if 4 players:
            if size 1--> 0; 2 --> 1 ; 3--> 1
            else for 2,3 players team is a progressive number
             */
            if(numberOfPlayers == 4){
                if(waitingConnection.size() == 1){
                    player.setTeam(0);
                }else{
                    player.setTeam(1);
                }
                // player.setTeam(Math.round(waitingConnection.size() / 4));
            }else{
                player.setTeam(waitingConnection.size());
            }
            //player.setTeam(Math.round(waitingConnection.size() / 4));
            waitingConnection.put(player, c);
            RemoteView remV = new RemoteView(player, c, gameHandler.getGame(), gameHandler.getController().getTurnController().getActionController().getActionParser());
            c.addPropertyChangeListener(remV);
            gameHandler.addPropertyChangeListener(remV);
            gameHandler.getGame().addPropertyChangeListener(remV);
            gameHandler.getController().getTurnController().getActionController().addPropertyChangeListener(remV);
            gameHandler.getController().getTurnController().addPropertyChangeListener(remV);
            gameHandler.getController().getTurnController().getActionController().getActionParser().addPropertyChangeListener(remV);
            gameHandler.addNewPlayer(player);
        }

        keys = new ArrayList<>(waitingConnection.keySet());
        if(waitingConnection.size() < numberOfPlayers){
            c.asyncSend("Waiting for other players");
        } else if (waitingConnection.size() == numberOfPlayers) {
            for(int i = 0; i < waitingConnection.size(); i++){
                //SocketClientConnection connection = waitingConnection.get(keys.get(i));
                //connection.asyncSend("Number of player reached! Starting the game... ");
            }
            System.out.println("Number of player reached! Starting the game... ");

            List<SocketClientConnection> temp = new ArrayList<>();
            for(Player p : waitingConnection.keySet()) {
                temp.add(waitingConnection.get(p));
                listOfGames.add(temp);
            }

            waitingConnection.clear();
        }
    }

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    /**
     * Check if the nickname chosen has already been taken
     * @param nameToCheck nickname to check
     * @return true if the nickname is available, false otherwise
     */
    public boolean checkNickname(String nameToCheck){
        for(Player p : waitingConnection.keySet()){
            if(p.getNickname().toUpperCase().equals(nameToCheck.toUpperCase())){
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the chosed color is already been taken
     * @param colorOfTower color of the tower to check
     * @return True if the color is still available, false if it is already been taken
     */
    public boolean checkColorTower(ColorOfTower colorOfTower){
        if(gameHandler.getGame().getNumberOfPlayers() == 2 ||gameHandler.getGame().getNumberOfPlayers() == 4){
            if(colorOfTower == ColorOfTower.GREY){
                return false;
            }
        }
        for(Player p : waitingConnection.keySet()){
            if(p.getColorOfTowers() == colorOfTower){
                return false;
            }
        }
        return true;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void run(){
        int connections = 0;
        System.out.println("Server is running");
        while(true){
            try {
                Socket newSocket = serverSocket.accept();
                connections++;
                System.out.println("Ready for the new connection - " + connections);
                SocketClientConnection socketConnection = new SocketClientConnection(newSocket, this);
                executor.submit(socketConnection);
                //socketConnection.run();
            } catch (IOException e) {
                System.out.println("Connection Error!");
            }
        }
    }
}