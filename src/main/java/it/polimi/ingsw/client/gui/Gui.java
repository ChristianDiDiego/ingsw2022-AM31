package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.utilities.ListOfArchipelagos;
import it.polimi.ingsw.utilities.ListOfBoards;
import it.polimi.ingsw.utilities.ListOfClouds;
import it.polimi.ingsw.utilities.ListOfPlayers;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

public class Gui extends Application implements PropertyChangeListener {
    private  String ip = "127.0.0.1";
    private int port = 5000;
    Socket socket;
    private boolean active = true;
    PrintWriter socketOut;

    private Scene scene;
    private LoginController loginController;
    private MainSceneController mainSceneController;
    private BoardSceneController boardSceneController;

    private String nickname = null;
    public synchronized boolean isActive() {
        return active;
    }

    public synchronized void setActive(boolean active) {
        this.active = active;
    }


    public Gui(String ip, int port) {
        this.ip = ip;
        this.port = port;
        System.out.println("Received ip " + ip +" port " + port);
    }

    public Gui() {

    }

    public Thread asyncReadFromSocket(final ObjectInputStream socketIn) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        Object inputObject = socketIn.readObject();
                        System.out.println("something received");

                        switch (inputObject) {
                            case String stringReceived -> manageStringInput(stringReceived);
                            case ListOfBoards listOfBoards -> manageListOfBoards(listOfBoards);
                            case Deck deck -> manageDeck(deck);
                            case ListOfArchipelagos listOfArchipelagos -> manageListOfArchipelagos(listOfArchipelagos);
                            case ListOfClouds listOfClouds -> manageListOfClouds(listOfClouds);
                            case ListOfPlayers listOfPlayers-> System.out.println("received list of players");//manageListOfPlayers((ListOfPlayers) inputObject);
                            default -> System.out.println("Unexpected argument received from the server");
                        }

                        System.out.println("active status " + active);
                    }
                } catch (Exception e) {
                    System.out.println("set active to false from read froom socket");
                    e.printStackTrace();
                    setActive(false);
                }
            }
        });
        return t;
    }

    public Thread pingToServer(InetAddress geek) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        Thread.sleep(10000);
                        if(!geek.isReachable(5000)) {
                            System.out.println("The server is unreachable, exiting...");
                            System.exit(0);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("set active to false");
                    setActive(false);
                }
            }
        });
       // t.start();
        return t;
    }

    public void run() throws IOException {
        socket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress(ip, port);
        socket.connect(socketAddress);
        System.out.println("Connection established");

        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        socketOut = new PrintWriter(socket.getOutputStream());
        InetAddress geek = InetAddress.getByName(ip);


       // try{
            Thread t0 = asyncReadFromSocket(socketIn);
            Thread t2 = pingToServer(geek);

            t0.start();
            t2.start();
            /*
            t2.join();
            while (isActive());
            t0.interrupt();
            t2.interrupt();
        } catch(InterruptedException | NoSuchElementException e){
            System.out.println("Connection closed from the client side");
        } finally {
            socketIn.close();
           // socketOut.close();
        }

         */
    }


    @Override
    public void start(Stage stage) throws Exception {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
            Parent root = loader.load();
            loginController = loader.getController();
            loginController.addPropertyChangeListener(this);


            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            run();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
        //send args to launch method and start
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //Send the string that are coming from the gui interface (MainSceneController)
            send(evt.getNewValue().toString());
            switch(evt.getPropertyName()){
               case "username" -> setNickname(evt.getNewValue().toString());
            }




    }

    public synchronized void send(String message) {
        String toBeSent = message + "\n";
        Scanner scanner = new Scanner(toBeSent);
        socketOut.println(scanner.nextLine());
        socketOut.flush();
        System.out.println("sent " + message);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private void manageStringInput(String inputString){
        System.out.println(inputString);
        if(inputString.equals("Game is starting...")){
            try {
                mainSceneController = loginController.switchToMainScene();
                mainSceneController.addPropertyChangeListener(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else if(inputString.contains("You are joying in the match with")){
            loginController.setNotFirstPlayer();
        }
        else if(inputString.contains("Waiting for other players")){
            loginController.setWaitingForOtherPlayers();
        }
    }

    private void manageListOfBoards(ListOfBoards listOfBoards){
        Board board = findPlayerBoard(listOfBoards);
        Platform.runLater(()-> {
            mainSceneController.printMyBoard(board);
            boardSceneController = mainSceneController.getBoardSceneLoader().getController();
            boardSceneController.setReceivedBoards(listOfBoards.getBoards());
            boardSceneController.showAllBoards();
            boardSceneController.printCiao();
        });
    }

    private Board findPlayerBoard(ListOfBoards listOfBoards){
        for(Board b : listOfBoards.getBoards()){
            if(b.getNickname().equals(nickname)){
                return b;
            }
        }
        return null;
    }

    private void manageListOfArchipelagos(ListOfArchipelagos listOfArchipelagos){
        Platform.runLater(()-> {
            mainSceneController.printArchipelagos(listOfArchipelagos.getArchipelagos());
        });
    }

    private void manageListOfClouds(ListOfClouds listOfClouds){
        System.out.println("received list of clouds");
        Platform.runLater(()->{
            mainSceneController.printClouds(listOfClouds.getClouds());
        });
    }

    private void manageDeck(Deck deck){
        System.out.println("received deck");
        Platform.runLater(()->{
            mainSceneController.printDeck(deck);
        });
    }
}
