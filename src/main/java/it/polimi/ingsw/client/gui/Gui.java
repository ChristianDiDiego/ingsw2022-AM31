package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.utilities.*;
import it.polimi.ingsw.utilities.constants.Constants;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
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
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Gui extends Application implements PropertyChangeListener {
    //private  String ip;
    //private int port = 5000;
    Socket socket;
    private boolean active = true;
    PrintWriter socketOut;

    private Scene scene;
    private LoginController loginController;
    private MainSceneController mainSceneController = null;
    private BoardSceneController boardSceneController;
    private CharacterSceneController characterSceneController;

    private String nickname = null;

    private List<String> idCharacters = new ArrayList<>();
    private List<String> charactersDescription = new ArrayList<>();
    public synchronized boolean isActive() {
        return active;
    }

    public synchronized void setActive(boolean active) {
        this.active = active;
    }


    public Gui(String ip, int port) {
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
                        if(inputObject !=null){

                            switch (inputObject) {
                                case String stringReceived -> manageStringInput(stringReceived);
                                case ListOfBoards listOfBoards -> manageListOfBoards(listOfBoards);
                                case Deck deck -> manageDeck(deck);
                                case ListOfArchipelagos listOfArchipelagos -> manageListOfArchipelagos(listOfArchipelagos);
                                case ListOfClouds listOfClouds -> manageListOfClouds(listOfClouds);
                                case ListOfPlayers listOfPlayers-> System.out.println("received list of players");//manageListOfPlayers((ListOfPlayers) inputObject);
                                default -> System.out.println("Unexpected argument received from the server");
                            }

                        }

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

    public boolean ping(String ip) throws IOException, InterruptedException {
        String ping;
        if(System.getProperty("os.name").startsWith("Windows")) {
            ping = "ping -n 1 " + ip;
        } else {
            ping = "ping -c 1 " + ip;
        }

        Process p1 = java.lang.Runtime.getRuntime().exec(ping);
        int returnVal = 0;
        returnVal = p1.waitFor();
        boolean reachable = (returnVal == 0);
        return reachable;
    }

    public Thread pingToServer(String ip) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        Thread.sleep(10000);
                        if(!ping(ip)) {
                            System.out.println("The server is unreachable, exiting...");
                            System.exit(0);
                        }
                    }
                } catch (Exception e) {
                    setActive(false);
                }
            }
        });
        //t.start();
        return t;
    }

    public void run() throws IOException {
        Scanner in;
        String ip = new String();
        int port = 0;
        try {
            in = new Scanner(System.in);
            System.out.println("Inserire ip: ");
            String read = in.nextLine();
            ip = read;
            System.out.println("Inserire porta: ");
            read = in.nextLine();
            port = Integer.parseInt(read);
        } catch (NoSuchElementException e) {
            System.err.println("Error! " + e.getMessage());
        }
        socket = new Socket();
        System.out.println("Mi connetto a "+ ip + " " + port);
        SocketAddress socketAddress = new InetSocketAddress(ip, port);
        socket.connect(socketAddress);
        System.out.println("Connection established");

        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        socketOut = new PrintWriter(socket.getOutputStream());
        InetAddress geek = InetAddress.getByName(ip);


       // try{
            Thread t0 = asyncReadFromSocket(socketIn);
            Thread t2 = pingToServer(ip);

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
            stage.setTitle("Eriantys");
            Image icon = new Image(getClass().getResourceAsStream("/images/eriantys_logo.jpg"));
            stage.getIcons().add(icon);
            stage.show();

            stage.setOnCloseRequest(event -> {
                event.consume();
                logout(stage);
            });

            run();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void logout(Stage stage){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to quit!");
        alert.setContentText("Are you sure?");

        if (alert.showAndWait().get() == ButtonType.OK){
            send(Constants.QUIT);
            setActive(false);
            System.out.println("You successfully quitted");
            stage.close();
        }
    }

    public void main(String[] args) {
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
        //NOTE: I cannot use a switch because I need to check if strings CONTAINS a word, not always EQUALS
        System.out.println(inputString);
        if(inputString.equals(ServerMessage.startingGame)){startingGame();}

        else if(inputString.contains(ServerMessage.joiningMessage)){loginController.setNotFirstPlayer();}

        else if(inputString.equalsIgnoreCase(ServerMessage.askNickname)){Platform.runLater(()-> {loginController.allowSetup();});}

        else if(inputString.equalsIgnoreCase(ErrorMessage.DuplicateNickname)){Platform.runLater(()-> {loginController.usernameAlreadyUsed(inputString);});}

        else if(inputString.equalsIgnoreCase(ErrorMessage.ColorNotValid)){Platform.runLater(()-> {loginController.colorAlreadyUsed(inputString);});}

        else if(inputString.equalsIgnoreCase(ServerMessage.waitingOtherPlayers) || inputString.equalsIgnoreCase(ServerMessage.waitingOldPlayers)){
            loginController.setWaitingForOtherPlayers();}

        else if(inputString.contains("For this round you can do")){setMaxSteps(inputString);}

        else if (inputString.contains("of your students from entrance")){setMaxStudentsToMove(inputString);}

        else if(inputString.contains("Available coins")) {getCoins(inputString);}

        else if(inputString.contains("Character:") && !(inputString.contains("Playable"))) {getCharacters(inputString);}


        if(mainSceneController != null){
            Platform.runLater(()-> {
                mainSceneController.setMessageForUserText(inputString.split("\n")[0]);
            });
        }

    }

    public static int firstDigit(int n)
    {
        // Remove last digit from number
        // till only one digit is left
        while (n >= 10)
            n /= 10;

        // return the first digit
        return n;
    }

    private void manageListOfBoards(ListOfBoards listOfBoards){
        Board board = findPlayerBoard(listOfBoards);
        Platform.runLater(()-> {
            mainSceneController.printMyBoard(board);
            boardSceneController.setReceivedBoards(listOfBoards.getBoards());
            boardSceneController.showAllBoards();
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

    private void manageCoins(int coin){
        Platform.runLater(()-> {
            mainSceneController.printCoin(coin);
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
        mainSceneController.setCardsClickable(true);
        Platform.runLater(()->{
            mainSceneController.printDeck(deck);
        });
    }

    private void startingGame(){
        try {
            mainSceneController = loginController.switchToMainScene();
            mainSceneController.addPropertyChangeListener(this);
            boardSceneController = mainSceneController.getBoardSceneLoader().getController();
            characterSceneController = mainSceneController.getCharacterSceneLoader().getController();
            characterSceneController.addPropertyChangeListener(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setMaxStudentsToMove(String inputString){
        int intFromInputString = Integer.parseInt(inputString.replaceAll("[\\D]", ""));
        //the max number of students will be the first digit received
        int maxNOfStudents = firstDigit(intFromInputString);
        mainSceneController.setCardsClickable(false);
        mainSceneController.setMaxNumberOfMovedStudents(maxNOfStudents);
    }

    private void getCoins(String inputString){
        String[] input = inputString.split(" ");
        int coin = Integer.parseInt(input[2]);
        manageCoins(coin);
    }

    private void getCharacters(String inputString){
        if(idCharacters.size() < Constants.NUMBEROFPLAYABLECHARACTERS){
            String[] input = inputString.split(" ");
            String description = new String();
            idCharacters.add(input[1]);
            int i = 4;
            //TODO: togliere usage
            while(!(input[i].contains("Price:"))) {
                description = description + " " + input[i];
                i++;
            }
            charactersDescription.add(description);
        }
        if(charactersDescription.size() == Constants.NUMBEROFPLAYABLECHARACTERS){
            characterSceneController.printCharacters(idCharacters,charactersDescription);
        }
    }

    private void setMaxSteps(String inputString){
        //remove the literals from the string and convert it to an integer
        // to get the number of steps that mn is allowed to do in this match
        mainSceneController.setMaxStepsMN(Integer.parseInt(inputString.replaceAll("[\\D]", "")));
    }

}
