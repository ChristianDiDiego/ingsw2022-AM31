package it.polimi.ingsw.server;

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

public class Server {
    private int port;
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
                if(s == c){
                    for(SocketClientConnection toRemove : l){
                        toRemove.closeConnection();
                    }
                    listOfGames.remove(l);
                }
            }
        }
    }

    //Wait for another player
    public synchronized void lobby(SocketClientConnection c){
        List<Player> keys = new ArrayList<>(waitingConnection.keySet());
        ColorOfTower color = null;
        //I moved nickname here so when other player connect the others receive his name
        String nickname ;
        while(!checkNickname(nickname = c.askNickname()));
        for(int i = 0; i < keys.size(); i++){
            SocketClientConnection connection = waitingConnection.get(keys.get(i));
            connection.asyncSend("Connected User: " + nickname);
        }
        if(waitingConnection.size() == 0) {
            numberOfPlayers = c.askHowManyPlayers();
            while (numberOfPlayers < 0 || numberOfPlayers > Constants.MAXPLAYERS){
               numberOfPlayers = c.askHowManyPlayers();
            };
            //TODO: add check if the inserted mode is fine

            int mode = -1;
            while(mode == -1){
                mode = c.askMode();
            }
            color = c.askColor();
            while(color == null ){
                color = c.askColor();
            }
            Player player1 = new Player(nickname, color);
            waitingConnection.put(player1, c);
            gameHandler = new GameHandler(player1, numberOfPlayers, Boolean.parseBoolean(String.valueOf(mode)));
            RemoteView remV1 = new RemoteView(player1, c, gameHandler.getGame(), gameHandler.getController().getTurnController().getActionController().getActionParser());
            c.addPropertyChangeListener(remV1);
            //remV1.addPropertyChangeListener(gameHandler.getGame());
            gameHandler.getGame().addPropertyChangeListener(remV1);
            //gameHandler.getGame().addEventListener(remV1);
            //controllerListener.addPropertyChangeListener(controller);

        } else {
            while (color == null || !checkColorTower(color)){
                color = c.askColor();
            }
            Player player = new Player(nickname, color);
            waitingConnection.put(player, c);
            RemoteView remV = new RemoteView(player, c, gameHandler.getGame(), gameHandler.getController().getTurnController().getActionController().getActionParser());
            c.addPropertyChangeListener(remV);
            gameHandler.getGame().addPropertyChangeListener(remV);
            gameHandler.addNewPlayer(player);
        }

        keys = new ArrayList<>(waitingConnection.keySet());
        if(waitingConnection.size() < numberOfPlayers){
            c.asyncSend("Waiting for other players");
        }
        else if (waitingConnection.size() == numberOfPlayers) {
            for(int i = 0; i < waitingConnection.size(); i++){
                SocketClientConnection connection = waitingConnection.get(keys.get(i));
                connection.asyncSend("Number of player reached! Starting the game... ");
            }System.out.println("Number of player reached! Starting the game... ");

            /*
            List<SocketClientConnection> temp = new ArrayList<>();
            int i = 0;
            for(Player p : waitingConnection.keySet()) {
                RemoteView rw = new RemoteView(p, waitingConnection.get(p));
                temp.add(waitingConnection.get(p));
                i++;
                gameHandler.getGame().addObserver(rw);
                gameHandler.getGame().addPropertyChangeListener(rw);
                rw.addObserver(gameHandler.getController().getTurnController().getActionController().getActionParser());
                listOfGames.add(temp);
            }

             */
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
        for(Player p : waitingConnection.keySet()){
            if(p.getColorOfTowers() == colorOfTower){
                return false;
            }
        }
        return true;
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