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
    private Player pl1;
    private Player pl2;
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
        ColorOfTower color;
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

            boolean mode = c.askMode();
            color = c.askColor();
            Player player1 = new Player(nickname, color);
            pl1 = player1;
            waitingConnection.put(player1, c);
            gameHandler = new GameHandler(player1, numberOfPlayers, mode);
            RemoteView remV1 = new RemoteView(player1, c);
            gameHandler.getGame().addPropertyChangeListener(remV1);

        } else {
            //while(!checkColorTower(color = c.askColor()));
            color = c.askColor();
            Player player = new Player(nickname, color);
            pl2 = player;
            waitingConnection.put(player, c);
            RemoteView remV = new RemoteView(player, c);
            gameHandler.getGame().addPropertyChangeListener(remV);
            gameHandler.addNewPlayer(nickname, color);

        }

        keys = new ArrayList<>(waitingConnection.keySet());
        if(waitingConnection.size() < numberOfPlayers){
            c.asyncSend("Waiting for other players");
        }
        if (waitingConnection.size() == numberOfPlayers) {
            for(int i = 0; i < waitingConnection.size(); i++){
                SocketClientConnection connection = waitingConnection.get(keys.get(i));
                connection.asyncSend("Number of player reached! Starting the game... ");
            }
            List<SocketClientConnection> temp = new ArrayList<>();

            /*
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
            } catch (IOException e) {
                System.out.println("Connection Error!");
            }
        }
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
}