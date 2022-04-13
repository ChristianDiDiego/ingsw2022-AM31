package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Player;

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

    //Deregister connection
    public synchronized void deregisterConnection(SocketClientConnection c) {

    }

    //Wait for another player
    public synchronized void lobby(SocketClientConnection c){
        List<Player> keys = new ArrayList<>(waitingConnection.keySet());
        ColorOfTower color;
        String nickname;
        for(int i = 0; i < keys.size(); i++){
            SocketClientConnection connection = waitingConnection.get(keys.get(i));
            connection.asyncSend("Connected User: " + keys.get(i));
        }

        if(waitingConnection.size() == 0) {

            while(!checkNickname(nickname = c.askNickname()));
            numberOfPlayers = c.askHowManyPlayers();
            while (numberOfPlayers < 0 || numberOfPlayers > Constants.MAXPLAYERS){
               numberOfPlayers = c.askHowManyPlayers();
            };
            //TODO: add check if the inserted mode is fine
            boolean mode = c.askMode();
            color = c.askColor();
            c.asyncSend("Waiting for other players");
            Player player1 = new Player(nickname, color);
            waitingConnection.put(player1, c);
            gameHandler = new GameHandler(player1, numberOfPlayers, mode);
        } else if(waitingConnection.size() < numberOfPlayers - 1) {
            nickname = c.askNickname();
            color = c.askColor();
            Player player = new Player(nickname, color);
            gameHandler.addNewPlayer(nickname, color);
            waitingConnection.put(player, c);
            c.asyncSend("Waiting for other players");
        }

        keys = new ArrayList<>(waitingConnection.keySet());

        //TODO: check numero di giocatori valido


        if (waitingConnection.size() == numberOfPlayers) {

            //TODO: vedere con i listener

            waitingConnection.clear();


        }
    }

    public Server() throws IOException {
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

}