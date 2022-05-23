package it.polimi.ingsw.FX;

import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.utilities.ListOfArchipelagos;
import it.polimi.ingsw.utilities.ListOfBoards;
import it.polimi.ingsw.utilities.ListOfClouds;
import it.polimi.ingsw.utilities.ListOfPlayers;
import javafx.application.Application;
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
    public Gui(){

    }

    public Thread asyncReadFromSocket(final ObjectInputStream socketIn) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        Object inputObject = socketIn.readObject();
                        synchronized (this) {
                            if (inputObject instanceof String) {
                                System.out.println((String) inputObject);
                                if(inputObject.equals("Game is starting...")){
                                    loginController.switchToMainScene();
                                }else if(((String) inputObject).contains("You are joying in the match with")){
                                    loginController.setNotFirstPlayer();
                                }
                                else if(((String) inputObject).contains("Waiting for other players")){
                                    loginController.setWaitingForOtherPlayers();
                                }
                            } else if (inputObject instanceof ListOfBoards) {
                              //  printBoard(((ListOfBoards) inputObject).getBoards());
                            } else if (inputObject instanceof Deck) {
                             //   printMyDeck((Deck) inputObject);
                            } else if (inputObject instanceof ListOfArchipelagos) {
                             //   printArchipelago(((ListOfArchipelagos) inputObject).getArchipelagos());
                            } else if (inputObject instanceof ListOfClouds) {
                             //   printCloud(((ListOfClouds) inputObject).getClouds());
                            } else if (inputObject instanceof ListOfPlayers) {
                              //  printLastUsedCards(((ListOfPlayers) inputObject).getPlayers());
                            } else {
                                throw new IllegalArgumentException();
                            }
                        }
                    }
                } catch (Exception e) {
                    setActive(false);
                    //termino
                }
            }
        });
     //   t.start();
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
            send(evt.getNewValue().toString());

    }

    public synchronized void send(String message) {
        String toBeSent = message + "\n";
        Scanner scanner = new Scanner(toBeSent);
        socketOut.println(scanner.nextLine());
        socketOut.flush();
        System.out.println("sent " + message);
    }
}
