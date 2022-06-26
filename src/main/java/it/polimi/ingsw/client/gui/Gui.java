package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.utilities.*;
import it.polimi.ingsw.utilities.Constants;
import it.polimi.ingsw.utilities.Lists.ListOfArchipelagos;
import it.polimi.ingsw.utilities.Lists.ListOfBoards;
import it.polimi.ingsw.utilities.Lists.ListOfClouds;
import it.polimi.ingsw.utilities.Lists.ListOfPlayers;
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
import java.net.*;
import java.util.*;

/**
 * This class contains the methods that allow the client to read messages/object from the server
 * and shows them in the game's window
 */
public class Gui extends Application implements PropertyChangeListener {
    Socket socket;
    private boolean active = true;
    PrintWriter socketOut;

    private LoginController loginController;
    private MainSceneController mainSceneController = null;
    private BoardSceneController boardSceneController;
    private CharacterSceneController characterSceneController;

    private String nickname = null;

    private final List<String> idCharacters = new ArrayList<>();
    private final List<String> charactersDescription = new ArrayList<>();
    private final List<Integer> charactersPrice = new ArrayList<>();
    private String ip;
    private int port;
    private Stage loginStage;

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
     * @param socketIn ObjectInputStream from the server
     * @return the thread
     */
    public Thread asyncReadFromSocket(final ObjectInputStream socketIn) {

        return new Thread(() -> {
            try {
                while (isActive()) {
                    Object inputObject = socketIn.readObject();
                    if (inputObject != null) {

                        switch (inputObject) {
                            case String stringReceived -> manageStringInput(stringReceived);
                            case ListOfBoards listOfBoards -> manageListOfBoards(listOfBoards);
                            case Deck deck -> manageDeck(deck);
                            case ListOfArchipelagos listOfArchipelagos -> manageListOfArchipelagos(listOfArchipelagos);
                            case ListOfClouds listOfClouds -> manageListOfClouds(listOfClouds);
                            case ListOfPlayers listOfPlayers -> manageListOfPlayers(listOfPlayers);
                            case Integer coin -> manageCoins(coin);
                            default -> System.out.println("Unexpected argument received from the server");
                        }

                    }

                }
            } catch (Exception e) {
                setActive(false);
            }
        });
    }

    /**
     * recognizes the OS in use and sends a ping using the specific command of the OS
     *
     * @return true if the ip is still active
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean ping() throws IOException, InterruptedException {
        String ping = new String();
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

    /**
     * Thread that every 10 seconds sends a ping to the server and
     * waits 5 seconds maximum to receive the reply. if the answer does
     * not arrive it means that the server is offline and the cli will be closed
     *
     * @return the created thread
     */
    public Thread pingToServer() {
        Thread t = new Thread(() -> {
            try {
                while (isActive()) {
                    Thread.sleep(10000);
                    if(!ping()) {
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
     * @throws IOException exception
     */
    public void run() throws IOException {
        try{
            socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(ip, port);
            socket.connect(socketAddress);
            System.out.println("Connection established");

            ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
            socketOut = new PrintWriter(socket.getOutputStream());

            Thread t0 = asyncReadFromSocket(socketIn);
            Thread t2 = pingToServer();

            t0.start();
            t2.start();
        } catch (ConnectException e) {
            //host and port combination not valid
            System.out.println("Connection refused, application will now close...");
            System.exit(0);
        } catch (SocketException e) {
            System.out.println("Connection refused, application will now close...");
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Connection refused, application will now close...");
            System.exit(0);
        }

    }


    /**
     * Start method for javafx. it also allows to enter
     * the ip and the port of the server
     */
    @Override
    public void start(Stage stage) {
        Scanner in;
        try {
            in = new Scanner(System.in);
            System.out.println("Insert ip: ");
            String read = in.nextLine();
            ip = read;
            while(ip.length() < 1) {
                System.out.println("Ip not valid, try again:");
                read = in.nextLine();
                ip = read;
            }
            System.out.println("Insert port: ");
            read = in.nextLine();
            while (read.length() < 2) {
                System.out.println("Port not valid, try again: ");
                read = in.nextLine();
            }
            try {
                port = Integer.parseInt(read);
            } catch (InputMismatchException e) {
                System.err.println("Numeric format requested, application will now close...");
                System.exit(0);
            } catch (NumberFormatException e) {
                System.err.println("Numeric format requested, application will now close...");
                System.exit(0);
            }
        } catch (NoSuchElementException e) {
            System.err.println("Error! " + e.getMessage());
        }
        try {
            System.out.println("You selected the GUI interface, have fun!\nStarting...");
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
            Parent root = loader.load();
            loginController = loader.getController();
            loginController.addPropertyChangeListener(this);
            Scene scene = new Scene(root);
            loginStage = new Stage();
            loginStage.setScene(scene);
            loginStage.setTitle("Eriantys | Login");
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/eriantys_logo.jpg")));
            loginStage.getIcons().add(icon);
            loginStage.setResizable(false);
            loginStage.show();

            loginStage.setOnCloseRequest(event -> {
                event.consume();
                logout(loginStage);
            });

            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows to quit from game just closing the game's window
     */
    private void logout(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to quit!");
        alert.setContentText("Are you sure?");

        //noinspection OptionalGetWithoutIsPresent
        if (alert.showAndWait().get() == ButtonType.OK) {
            send(Constants.QUIT);
            setActive(false);
            System.out.println("You successfully quitted");
            stage.close();
        }
    }

    /**
     * Main method of the class
     */
    public void main(String[] args) {
        launch(args);
    }

    /**
     * Send the string that are coming from the gui interface
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        send(evt.getNewValue().toString());
        if ("username".equals(evt.getPropertyName())) {
            setNickname(evt.getNewValue().toString());
        }
    }

    /**
     * Allows to send messages through the socket
     *
     * @param message to be sent
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
     * @param nickname choosed by the player
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Read a string and switch the correct method
     *
     * @param inputString string arrived from the server
     */
    private void manageStringInput(String inputString) {
        switch (inputString){
            case ServerMessage.startingGame -> startingGame();
            case ServerMessage.askNickname -> Platform.runLater(() -> loginController.allowSetup());
            case ServerMessage.connectionClosed -> setActive(false);
            case ServerMessage.askColor2_4Players -> Platform.runLater(()->loginController.showColorChoice(false));
            case ServerMessage.askColor3Players-> Platform.runLater(()->loginController.showColorChoice(true));
            case ServerMessage.waitingOtherPlayers, ServerMessage.waitingOldPlayers ->  loginController.setWaitingForOtherPlayers();
            case ErrorMessage.CardAlreadyTaken -> setCardClickable();
            case ErrorMessage.notEnoughCoinsOrWrongAction -> sendErrorCharacters(inputString);
            case ErrorMessage.DuplicateNickname -> Platform.runLater(() -> loginController.usernameAlreadyUsed(inputString));
            case ErrorMessage.ColorNotValid ->  Platform.runLater(() -> loginController.colorAlreadyUsed(inputString));
            default -> manageNotSwitchableString(inputString);
        }

        if (mainSceneController != null) {
            Platform.runLater(() -> {
                if (inputString.contains("YOU WON")) {
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

    private void manageNotSwitchableString(String inputString){
        //NOTE: I cannot use a switch because I need to check if strings CONTAINS a word
        if (inputString.contains("For this round you can do")) {
            setMaxSteps(inputString);
        } else if (inputString.contains("of your students from entrance")) {
            setMaxStudentsToMove(inputString);
        } else if (inputString.contains("Available coins")) {
            getCoins(inputString);
        } else if (inputString.contains("Character:") && !(inputString.contains("Playable"))) {
            getCharacters(inputString);
        } else if (inputString.contains(ServerMessage.joiningMessage)) {
            loginController.setNotFirstPlayer();
        }
    }

    /**
     * Remove last digit from number till only one digit is left
     *
     * @param n number to be formatted
     * @return remained digit
     */
    public static int firstDigit(int n) {
        while (n >= 10)
            n /= 10;
        return n;
    }

    /**
     * Receives the list of players' boards and shows in mainScene each of the them
     *
     * @param listOfBoards list of boards received from the server
     */
    private void manageListOfBoards(ListOfBoards listOfBoards) {
        Board board = findPlayerBoard(listOfBoards);
        Platform.runLater(() -> {
            mainSceneController.printMyBoard(board);
            boardSceneController.setReceivedBoards(listOfBoards.getBoards());
            boardSceneController.showAllBoards();
        });
    }

    /**
     * Receives the list of the match's players and shows the last card played by each player
     * in boardScene
     *
     * @param listOfPlayers list of players received from the server
     */
    private void manageListOfPlayers(ListOfPlayers listOfPlayers) {
        Platform.runLater(() -> boardSceneController.printLastUsedCard(listOfPlayers.getPlayers()));
    }

    /**
     * From the list of boards of all player return the board of the player who is using
     * this GUI
     *
     * @param listOfBoards list of boards received from the server
     * @return the board of the player who is using
     */
    private Board findPlayerBoard(ListOfBoards listOfBoards) {
        for (Board b : listOfBoards.getBoards()) {
            if (b.getNickname().equals(nickname)) {
                return b;
            }
        }
        return null;
    }

    /**
     * Receives the list of archipelagos still present and shows them in mainScene
     *
     * @param listOfArchipelagos list of archipelagos from the server
     */
    private void manageListOfArchipelagos(ListOfArchipelagos listOfArchipelagos) {
        Platform.runLater(() -> {
            mainSceneController.printArchipelagos(listOfArchipelagos.getArchipelagos());
            characterSceneController.setArchipelagos(listOfArchipelagos.getArchipelagos());
        });
    }

    /**
     * Receives the number of coins of this player and shows it in mainScene
     *
     * @param coin of the player
     */
    private void manageCoins(int coin) {
        Platform.runLater(() -> {
            mainSceneController.printCoin(coin);
            characterSceneController.setWallet(coin);
        });
    }

    /**
     * Receives the list of current turn's clouds and shows them in mainScene
     *
     * @param listOfClouds from the server
     */
    private void manageListOfClouds(ListOfClouds listOfClouds) {
        Platform.runLater(() -> mainSceneController.printClouds(listOfClouds.getClouds()));
    }

    /**
     * Receives the deck of this player and shows it in mainScene
     *
     * @param deck to be managed
     */
    private void manageDeck(Deck deck) {
        mainSceneController.setCardsClickable(true);
        Platform.runLater(() -> mainSceneController.printDeck(deck));
    }

    private void setCardClickable() {
        Platform.runLater(() -> mainSceneController.setCardsClickable(true));
    }

    /**
     * Add listeners to GUI and set the controller of each scene
     */
    private void startingGame() {
        try {
            mainSceneController = loginController.getMainSceneController();
            mainSceneController.addPropertyChangeListener(this);
            boardSceneController = mainSceneController.getBoardSceneLoader().getController();
            characterSceneController = mainSceneController.getCharacterSceneLoader().getController();
            characterSceneController.addPropertyChangeListener(this);
            Platform.runLater(() -> loginController.switchToMainScene());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Based on the number of players, it sets the maximum number of moves
     * that can be done in each turn
     *
     * @param inputString received from the server
     */
    private void setMaxStudentsToMove(String inputString) {
        int intFromInputString = Integer.parseInt(inputString.replaceAll("\\D", ""));
        int maxNOfStudents = firstDigit(intFromInputString);
        mainSceneController.setCardsClickable(false);
        mainSceneController.setMaxNumberOfMovedStudents(maxNOfStudents);
    }

    /**
     * Obtains the number of coins from the string received by the socket
     *
     * @param inputString inputString received from the server
     */
    private void getCoins(String inputString) {
        String[] input = inputString.split(" ");
        int coin = Integer.parseInt(input[2]);
        manageCoins(coin);
    }

    /**
     * Obtains the character of this match from the string received by the socket
     *
     * @param inputString inputString received from the server
     */
    private void getCharacters(String inputString) {
        if (idCharacters.size() < Constants.NUMBEROFPLAYABLECHARACTERS) {
            String[] input = inputString.split(" ");
            StringBuilder description = new StringBuilder();
            idCharacters.add(input[1]);
            int i = 4;
            //TODO: togliere usage
            while (!(input[i].contains("Price:"))) {
                description.append(" ").append(input[i]);
                i++;
            }
            charactersDescription.add(description.toString());
            int price = Integer.parseInt(input[i + 1]);
            charactersPrice.add(price);
        }
        if (charactersDescription.size() == Constants.NUMBEROFPLAYABLECHARACTERS) {
            characterSceneController.printCharacters(idCharacters, charactersDescription, charactersPrice);
        }
    }

    /**
     * Remove the literals from the string and convert it to an integer
     * to get the number of steps that mn is allowed to do in this match
     *
     * @param inputString inputString received from the server
     */
    private void setMaxSteps(String inputString) {
        mainSceneController.setMaxStepsMN(Integer.parseInt(inputString.replaceAll("\\D", "")));
    }

    /**
     * Send the error that occured to the character scene
     *
     * @param error message
     */
    private void sendErrorCharacters(String error) {
        Platform.runLater(() -> characterSceneController.setErrorMessage(error));
    }

}
