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

/**
 * This class contains the methods that allow the client to read messages/object from the server
 * and shows them in the game's window
 */
public class Gui extends Application implements PropertyChangeListener {
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
    private List<Integer> charactersPrice = new ArrayList<>();
    private String ip;
    private int port;
    public synchronized boolean isActive() {
        return active;
    }

    public synchronized void setActive(boolean active) {
        this.active = active;
    }

    public Gui() {

    }

    /**
     * Thread that allows to read messages from the socket asynchronously.
     * based on the type received, invoke a different print method
     *
     * @param socketIn
     * @return the thread
     */
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
                                case ListOfPlayers listOfPlayers-> manageListOfPlayers(listOfPlayers);
                                case Integer coin -> manageCoins(coin);
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

    /**
     * Thread that every 10 seconds sends a ping to the server and
     * waits 5 seconds maximum to receive the reply. if the answer does
     * not arrive it means that the server is offline and the cli will be closed
     *
     * @param geek is the ip address of the server
     */
    public Thread pingToServer(InetAddress geek) {
        Thread t = new Thread(() -> {
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
        });
        return t;
    }

    /**
     * Run method of the class GUI
     *
     * @throws IOException
     */
    public void run() throws IOException {
        socket = new Socket();
        System.out.println("Mi connetto a "+ ip + " " + port);
        SocketAddress socketAddress = new InetSocketAddress(ip, port);
        socket.connect(socketAddress);
        System.out.println("Connection established");

        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        socketOut = new PrintWriter(socket.getOutputStream());
        InetAddress geek = socket.getInetAddress();

        Thread t0 = asyncReadFromSocket(socketIn);
        Thread t2 = pingToServer(geek);

        t0.start();
        t2.start();
    }


    /**
     * Start method for javafx. it also allows to enter
     * the ip and the port of the server
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Scanner in;
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

    /**
     * Allows to quit from game just closing the game's window
     *
     * @param stage
     */
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

    /**
     * Main method of the class
     *
     * @param args
     */
    public void main(String[] args) {
        launch(args);
    }

    /**
     * Send the string that are coming from the gui interface
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        send(evt.getNewValue().toString());
        switch(evt.getPropertyName()){
           case "username" -> setNickname(evt.getNewValue().toString());
        }
    }

    /**
     * Allows to send messages through the socket
     *
     * @param message
     */
    public synchronized void send(String message) {
        String toBeSent = message + "\n";
        Scanner scanner = new Scanner(toBeSent);
        socketOut.println(scanner.nextLine());
        socketOut.flush();
        System.out.println("sent " + message);
    }

    /**
     * @return the player's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Set the player's nickname
     *
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Read a string and switch the correct method
     *
     * @param inputString
     */
    private void manageStringInput(String inputString){
        //NOTE: I cannot use a switch because I need to check if strings CONTAINS a word, not always EQUALS
        System.out.println(inputString);
        if(inputString.equals(ServerMessage.startingGame)){startingGame();}

        else if(inputString.contains(ServerMessage.joiningMessage)){loginController.setNotFirstPlayer();}

        else if(inputString.equalsIgnoreCase(ServerMessage.askNickname)){Platform.runLater(()-> {loginController.allowSetup();});}

        else if(inputString.equalsIgnoreCase(ErrorMessage.DuplicateNickname)){Platform.runLater(()-> {loginController.usernameAlreadyUsed(inputString);});}

        else if(inputString.equalsIgnoreCase(ServerMessage.connectionClosed)){setActive(false);}
        else if(inputString.equalsIgnoreCase(ErrorMessage.ColorNotValid)){Platform.runLater(()-> {loginController.colorAlreadyUsed(inputString);});}

        else if(inputString.equalsIgnoreCase(ServerMessage.waitingOtherPlayers) || inputString.contains(ServerMessage.waitingOldPlayers)){
            loginController.setWaitingForOtherPlayers();}

        else if(inputString.contains("For this round you can do")){setMaxSteps(inputString);}

        else if (inputString.contains("of your students from entrance")){setMaxStudentsToMove(inputString);}

        else if(inputString.contains("Available coins")) {getCoins(inputString);}

        else if(inputString.contains("Character:") && !(inputString.contains("Playable"))) {getCharacters(inputString);}


        if(mainSceneController != null){
            Platform.runLater(()-> {
                if(inputString.contains("YOU WON")) {
                    mainSceneController.setMessageForUserText("End game. You won");
                    mainSceneController.setEndGame(true);
                } else if (inputString.contains("YOU LOST")) {
                    mainSceneController.setMessageForUserText("End game. You lost");
                    mainSceneController.setEndGame(true);
                } else {
                    mainSceneController.setMessageForUserText(inputString.split("\n")[0]);
                }
            });
        }

    }

    /**
     * Remove last digit from number till only one digit is left
     *
     * @param n
     * @return
     */
    public static int firstDigit(int n) {
        while (n >= 10)
            n /= 10;
        return n;
    }

    /**
     * Receives the list of players' boards and shows in mainScene each of the them
     *
     * @param listOfBoards
     */
    private void manageListOfBoards(ListOfBoards listOfBoards){
        Board board = findPlayerBoard(listOfBoards);
        Platform.runLater(()-> {
            mainSceneController.printMyBoard(board);
            boardSceneController.setReceivedBoards(listOfBoards.getBoards());
            boardSceneController.showAllBoards();
        });
    }

    /**
     * Receives the list of the match's players and shows the last card played by each player
     * in boardScene
     *
     * @param listOfPlayers
     */
    private void manageListOfPlayers(ListOfPlayers listOfPlayers) {
        Platform.runLater(()-> {
            boardSceneController.printLastUsedCard(listOfPlayers.getPlayers());
        });
    }

    /**
     * From the list of boards of all player return the board of the player who is using
     * this GUI
     *
     * @param listOfBoards
     * @return
     */
    private Board findPlayerBoard(ListOfBoards listOfBoards){
        for(Board b : listOfBoards.getBoards()){
            if(b.getNickname().equals(nickname)){
                return b;
            }
        }
        return null;
    }

    /**
     * Receives the list of archipelagos still present and shows them in mainScene
     *
     * @param listOfArchipelagos
     */
    private void manageListOfArchipelagos(ListOfArchipelagos listOfArchipelagos){
        Platform.runLater(()-> {
            mainSceneController.printArchipelagos(listOfArchipelagos.getArchipelagos());
            characterSceneController.setArchipelagos(listOfArchipelagos.getArchipelagos());
        });
    }

    /**
     * Receives the number of coins of this player and shows it in mainScene
     *
     * @param coin
     */
    private void manageCoins(int coin){
        Platform.runLater(()-> {
            mainSceneController.printCoin(coin);
            characterSceneController.setWallet(coin);
        });
    }

    /**
     * Receives the list of current turn's clouds and shows them in mainScene
     *
     * @param listOfClouds
     */
    private void manageListOfClouds(ListOfClouds listOfClouds){
        System.out.println("received list of clouds");
        Platform.runLater(()->{
            mainSceneController.printClouds(listOfClouds.getClouds());
        });
    }

    /**
     * Receives the deck of this player and shows it in mainScene
     *
     * @param deck
     */
    private void manageDeck(Deck deck){
        System.out.println("received deck");
        mainSceneController.setCardsClickable(true);
        Platform.runLater(()->{
            mainSceneController.printDeck(deck);
        });
    }

    /**
     * Add listeners to GUI and set the controller of each scene
     */
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

    /**
     * Based on the number of players, it sets the maximum number of moves
     * that can be done in each turn
     *
     * @param inputString
     */
    private void setMaxStudentsToMove(String inputString){
        int intFromInputString = Integer.parseInt(inputString.replaceAll("[\\D]", ""));
        int maxNOfStudents = firstDigit(intFromInputString);
        mainSceneController.setCardsClickable(false);
        mainSceneController.setMaxNumberOfMovedStudents(maxNOfStudents);
    }

    /**
     * Obtains the number of coins from the string received by the socket
     *
     * @param inputString
     */
    private void getCoins(String inputString){
        String[] input = inputString.split(" ");
        int coin = Integer.parseInt(input[2]);
        manageCoins(coin);
    }

    /**
     * Obtains the character of this match from the string received by the socket
     *
     * @param inputString
     */
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
            int price = Integer.parseInt(input[i + 1]);
            charactersPrice.add(price);
        }
        if(charactersDescription.size() == Constants.NUMBEROFPLAYABLECHARACTERS){
            characterSceneController.printCharacters(idCharacters, charactersDescription, charactersPrice);
        }
    }

    /**
     * Remove the literals from the string and convert it to an integer
     * to get the number of steps that mn is allowed to do in this match
     *
     * @param inputString
     */
    private void setMaxSteps(String inputString){
        mainSceneController.setMaxStepsMN(Integer.parseInt(inputString.replaceAll("[\\D]", "")));
    }

}
