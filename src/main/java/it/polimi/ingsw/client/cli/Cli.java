package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.utilities.*;
import it.polimi.ingsw.utilities.Constants;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utilities.Lists.ListOfArchipelagos;
import it.polimi.ingsw.utilities.Lists.ListOfBoards;
import it.polimi.ingsw.utilities.Lists.ListOfClouds;
import it.polimi.ingsw.utilities.Lists.ListOfPlayers;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.List;

/**
 * This class contains the methods that allow the client to read messages/object from the server and print them
 */
public class Cli {
    private final String ip;
    private final int port;
    private boolean active = true;
    private final Object lockPrint;
    Socket socket;
    static PrintStream ps = new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8);


    public Cli(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.lockPrint = new Object();
    }

    /**
     * Returns the value of active which indicates whether the cli is still active
     *
     * @return active
     */
    public synchronized boolean isActive() {
        return active;
    }

    /**
     * Set the value of active
     *
     * @param active true when the connection is active, false otherwise
     */
    public synchronized void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Thread that allows to read messages from the socket asynchronously.
     * based on the type received, invoke a different print method
     *
     * @param socketIn ObjectInputStream coming from the server
     */
    public Thread asyncReadFromSocket(final ObjectInputStream socketIn) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        Object inputObject = socketIn.readObject();
                        synchronized (this) {
                            switch (inputObject) {
                                case String s -> manageString(s);
                                case ListOfBoards listOfBoards -> printBoard(listOfBoards.getBoards());
                                case Deck deck -> printMyDeck(deck);
                                case ListOfArchipelagos listOfArchipelagos ->
                                        printArchipelago(listOfArchipelagos.getArchipelagos());
                                case ListOfClouds listOfClouds -> printCloud(listOfClouds.getClouds());
                                case ListOfPlayers listOfPlayers -> printLastUsedCards(listOfPlayers.getPlayers());
                                case Integer ignored -> doNothing();
                                case null, default -> throw new IllegalArgumentException();
                            }
                        }
                    }
                } catch (Exception e) {
                    setActive(false);
                    System.out.println("Server unreachable, press enter to exit from the game");
                }
            }
        });
        t.start();
        return t;
    }


    /**
     * Thread that allows to send messages asynchronously
     *
     * @param stdin     Scanner of the standard input
     * @param socketOut PrintWriter of the socket where the message will be sent
     */
    public Thread asyncWriteToSocket(final Scanner stdin, final PrintWriter socketOut) {
        Thread t = new Thread(() -> {
            try {
                while (isActive()) {
                    String inputLine = stdin.nextLine();
                    if (inputLine.length() > 0) {
                        socketOut.println(inputLine);
                        socketOut.flush();
                    } else {
                        ps.println("Null input is not valid");
                    }
                }
            } catch (Exception e) {
                setActive(false);
            }
        });
        t.start();
        return t;
    }

    /**
     * Prints the logo of the game
     */
    public void printLogo() {
        ps.println("""
                ███████ ██████  ██  █████  ███    ██ ████████ ██    ██ ███████\s
                ██      ██   ██ ██ ██   ██ ████   ██    ██     ██  ██  ██     \s
                █████   ██████  ██ ███████ ██ ██  ██    ██      ████   ███████\s
                ██      ██   ██ ██ ██   ██ ██  ██ ██    ██       ██         ██\s
                ███████ ██   ██ ██ ██   ██ ██   ████    ██       ██    ███████\s
                                                                              \s
                                                                              \s""");
        ps.println("\nCreators: Carmine Faino, Christian Di Diego, Federica Di Filippo");
    }

    /**
     * Receives a deck from asyncReadFromSocket and prints it
     *
     * @param deck to be printed
     */
    public void printMyDeck(Deck deck) {
        synchronized (lockPrint) {
            ps.println("YOUR DECK~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            for (Card c : deck.getLeftCards()) {
                ps.println("    Power: " + c.getPower() + " Steps: " + c.getMaxSteps());
            }
            ps.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        }
    }

    /**
     * Receives the list of the match's players and print the last card played by each player
     *
     * @param players list of the players of the game
     */
    public void printLastUsedCards(List<Player> players) {
        synchronized (lockPrint) {
            ps.println("\nCARDS PLAYED IN THIS TURN~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            int min = Constants.NUMBEROFCARDSINDECK;
            for (Player p : players) {
                if (p.getMyDeck().getLeftCards().size() < min) {
                    min = p.getMyDeck().getLeftCards().size();
                }
            }
            for (Player p : players) {
                if (p.getMyDeck().getLeftCards().size() == min && p.getLastUsedCard() != null) {
                    if(p.getLastUsedCard().getPower() != 0) {
                        ps.print("    Player " + p.getNickname() + " choose the card:");
                        ps.println(" Power: " + p.getLastUsedCard().getPower() + " Steps: " + p.getLastUsedCard().getMaxSteps());
                    }
                }
            }
            ps.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        }
    }

    /**
     * Receives the list of players' boards and prints each of the boards
     *
     * @param boards to be printed
     */
    public void printBoard(List<Board> boards) {
        synchronized (lockPrint) {
            ps.println("BOARDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            for (Board b : boards) {
                int[] nSDN = new int[]{0, 0, 0, 0, 0};
                int[] nSE = new int[]{0, 0, 0, 0, 0};
                int nT = b.getTowersOnBoard().getNumberOfTowers();
                boolean[] hasProf = new boolean[]{false, false, false, false, false};
                for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
                    nSDN[i] = b.getDiningRoom().getStudentsByColor(StudsAndProfsColor.values()[i]);
                    nSE[i] = b.getEntrance().getStudentsByColor(StudsAndProfsColor.values()[i]);
                    hasProf[i] = b.getProfessorsTable().getHasProf(StudsAndProfsColor.values()[i]);
                }

                StringBuilder boardString = new StringBuilder();

                //print player's nickname
                boardString.append(ColorsCli.RESET).append("    Board of player: ").append(b.getNickname()).append("\n").append(ColorsCli.RESET);

                //print dining room
                for (int j = 0; j < Constants.NUMBEROFKINGDOMS; j++) {
                    boardString.append(ColorsCli.RESET).append("    ").append(ColorsCli.RESET);
                    for (int k = 0; k < nSDN[j]; k++) {
                        boardString.append(ColorsCli.getColorByNumber(j)).append("● ").append(ColorsCli.getColorByNumber(j));
                    }
                    for (int k = nSDN[j]; k < Constants.MAXSTUDENTSINDINING; k++) {
                        if ((k + 1) % 3 == 0) {
                            boardString.append(ColorsCli.getColorByNumber(j)).append("© ").append(ColorsCli.getColorByNumber(j));
                        } else {
                            boardString.append(ColorsCli.getColorByNumber(j)).append("◯ ").append(ColorsCli.getColorByNumber(j));
                        }
                    }
                    if (hasProf[j]) {
                        boardString.append(ColorsCli.getColorByNumber(j)).append(" | ⬢  ").append(ColorsCli.getColorByNumber(j));
                    } else {
                        boardString.append(ColorsCli.getColorByNumber(j)).append(" | ⬡  ").append(ColorsCli.getColorByNumber(j));
                    }
                    boardString.append(ColorsCli.BLACK).append("\n").append(ColorsCli.BLACK);
                }

                //print entrance room
                boardString.append(ColorsCli.RESET).append("    Students in entrance: \n    ").append(ColorsCli.RESET);
                for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
                    for (int k = 0; k < nSE[i]; k++) {
                        boardString.append(ColorsCli.getColorByNumber(i)).append("● ").append(ColorsCli.getColorByNumber(i));
                    }
                }

                //print towers
                boardString.append(ColorsCli.RESET).append("\n    Towers on board: \n    ").append(ColorsCli.RESET);
                for (int i = 0; i < nT; i++) {
                    boardString.append(ColorsCli.RESET).append("♜ ").append(ColorsCli.BLACK).append(ColorsCli.RESET);
                }
                boardString.append(ColorsCli.RESET).append("\n").append(ColorsCli.RESET);
                ps.println(boardString);
            }
            ps.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        }
    }

    /**
     * Receives the list of archipelagos still present and prints them
     *
     * @param archipelagos to be printed
     */
    public void printArchipelago(List<Archipelago> archipelagos) {
        synchronized (lockPrint) {
            StringBuilder archipelago = new StringBuilder();
            archipelago.append(ColorsCli.RESET).append("ARCHIPELAGOS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~").append(ColorsCli.RESET);
            ps.println(archipelago);
            for (Archipelago a : archipelagos) {
                int numberTower = a.getBelongingIslands().size();
                archipelago = new StringBuilder();

                archipelago.append(ColorsCli.RESET).append("    Id archipelago: ").append(a.getIdArchipelago())
                        .append(a.getOwner() == null ? "" : " owner: " + a.getOwner().getNickname() + " team: " + a.getOwner().getTeam())
                        .append(!a.getIsMNPresent() ? "" : " MN is here").append(!a.getIsForbidden() ? "" : " \uD83D\uDEAB")
                        .append("\n    ").append(ColorsCli.RESET);
                for (Island is : a.getBelongingIslands()) {
                    for (int j = 0; j < Constants.NUMBEROFKINGDOMS; j++) {
                        for (int k = 0; k < is.getStudentsByColor(StudsAndProfsColor.values()[j]); k++) {
                            archipelago.append(ColorsCli.getColorByNumber(j)).append("● ").append(ColorsCli.RESET);
                        }
                    }
                }
                if (a.getOwner() != null) {
                    for (int i = 0; i < numberTower; i++) {
                        archipelago.append(ColorsCli.RESET).append("♜ ").append(ColorsCli.RESET);
                    }
                }
                ps.println(archipelago);
            }

            archipelago = new StringBuilder();
            archipelago.append(ColorsCli.RESET).append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n").append(ColorsCli.RESET);
            ps.println(archipelago);
        }
    }

    /**
     * Receives the list of current turn's clouds and prints them
     *
     * @param clouds to be printed
     */
    public void printCloud(List<Cloud> clouds) {
        synchronized (lockPrint) {
            StringBuilder cloud = new StringBuilder();
            cloud.append(ColorsCli.RESET).append("CLOUDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~").append(ColorsCli.RESET);
            ps.println(cloud);
            for (Cloud c : clouds)
                if (!c.getIsTaken()) {
                    cloud = new StringBuilder();
                    cloud.append(ColorsCli.RESET).append("    Id cloud: ").append(c.getIdCloud()).append("\n    ").append(ColorsCli.RESET);
                    for (int j = 0; j < Constants.NUMBEROFKINGDOMS; j++) {
                        for (int k = 0; k < c.getStudents()[j]; k++) {
                            cloud.append(ColorsCli.getColorByNumber(j)).append("● ").append(ColorsCli.RESET);
                        }
                    }
                    ps.println(cloud);
                }

            cloud = new StringBuilder();
            cloud.append(ColorsCli.RESET).append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n").append(ColorsCli.RESET);
            ps.println(cloud);
        }
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
        t.start();
        return t;
    }

    /**
     * Receives a string and prints it. If the string is a string that indicates that
     * the connection is closed, the cli closes
     *
     * @param s string to be printed
     */
    private void manageString(String s) {
        ps.println(s);
        if (s.equalsIgnoreCase(ServerMessage.connectionClosed)) {
            System.exit(0);
        }
    }

    private void doNothing(){}

    /**
     * Run method of the class CLI
     *
     * @throws IOException exception
     */
    public void run() throws IOException {
        printLogo();
        try {
            socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(ip, port);
            socket.connect(socketAddress);
            ps.println("Connection established");
        } catch (Exception e) {
            System.out.println("Connection refused, application will now close...");
            System.exit(0);
        }

        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
        Scanner stdin = new Scanner(System.in);

        try{
            Thread t0 = asyncReadFromSocket(socketIn);
            Thread t1 = asyncWriteToSocket(stdin, socketOut);
            Thread t2 = pingToServer();
            t0.join();
            t1.join();
            t2.join();
            while (isActive());
            t0.interrupt();
            t1.interrupt();
            t2.interrupt();
        } catch(InterruptedException | NoSuchElementException e){
            ps.println("Connection closed from the client side");
        } finally {
            stdin.close();
            socketIn.close();
            socketOut.close();
        }
    }
}