package it.polimi.ingsw.client.cli;

//import com.sun.tools.javac.code.Attribute;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.utilities.ListOfArchipelagos;
import it.polimi.ingsw.utilities.ListOfBoards;
import it.polimi.ingsw.utilities.ListOfClouds;
import it.polimi.ingsw.utilities.ListOfPlayers;
import it.polimi.ingsw.utilities.constants.Constants;
import it.polimi.ingsw.model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.List;

/**
 * This class contains the methods that allow the client to read messages/object from the server and print them
 */
public class Cli{
    //private final PrintStream output;
    //private final Scanner input;
    private final String ip;
    private final int port;
    private boolean active = true;
    private Object lockPrint;
    Socket socket;

    private boolean serverAlive;


    public Cli(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.lockPrint = new Object();
    }

    public synchronized boolean isActive() {
        return active;
    }

    public synchronized void setActive(boolean active) {
        this.active = active;
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
                                if(inputObject.equals("pong")) {
                                    serverAlive = true;
                                } else if(inputObject.equals("ping")) {
                                    //
                                }
                                System.out.println((String) inputObject);
                            } else if (inputObject instanceof ListOfBoards) {
                                printBoard(((ListOfBoards) inputObject).getBoards());
                            } else if (inputObject instanceof Deck) {
                                printMyDeck((Deck) inputObject);
                            } else if (inputObject instanceof ListOfArchipelagos) {
                                printArchipelago(((ListOfArchipelagos) inputObject).getArchipelagos());
                            } else if (inputObject instanceof ListOfClouds) {
                                printCloud(((ListOfClouds) inputObject).getClouds());
                            } else if (inputObject instanceof ListOfPlayers) {
                                printLastUsedCards(((ListOfPlayers) inputObject).getPlayers());
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
        t.start();
        return t;
    }


    /**
     * in modo asincono mando la mia scelta
     *
     * @param stdin
     * @param socketOut
     * @return
     */
    public Thread asyncWriteToSocket(final Scanner stdin,final PrintWriter socketOut) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        String inputLine = stdin.nextLine();
                        if(inputLine.length() > 0) {
                            socketOut.println(inputLine);
                            socketOut.flush();
                        }else{
                            System.out.println("Null input is not valid");
                        }
                    }
                } catch (Exception e) {
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }

    /**
     * TODO: add link to full rules
     * ha un metodo run che chiama il setup del nuovo player, poi chiama riceviInput
     * ha un metodo riceviInput che è sempre in ascolto e ogni volta
     * invia tutti i messaggi che riceve al parser
     */

    public void printLogo () {
        System.out.println("\n" +
                "███████╗██████╗ ██╗   ██╗ █████╗ ███╗   ██╗████████╗██╗███████╗\n" +
                "██╔════╝██╔══██╗╚██╗ ██╔╝██╔══██╗████╗  ██║╚══██╔══╝██║██╔════╝\n" +
                "█████╗  ██████╔╝ ╚████╔╝ ███████║██╔██╗ ██║   ██║   ██║███████╗\n" +
                "██╔══╝  ██╔══██╗  ╚██╔╝  ██╔══██║██║╚██╗██║   ██║   ██║╚════██║\n" +
                "███████╗██║  ██║   ██║   ██║  ██║██║ ╚████║   ██║   ██║███████║\n" +
                "╚══════╝╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═══╝   ╚═╝   ╚═╝╚══════╝\n" +
                "                                                               \n");

        System.out.println("\nCreators: Carmine Faino, Christian Di Diego, Federica Di Filippo");
    }

    public void printMyDeck (Deck deck){
        synchronized (lockPrint){
            System.out.println("YOUR DECK~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            for (Card c : deck.getLeftCards()) {
                System.out.println("    Power: " + c.getPower() + " Steps: " + c.getMaxSteps());
            }
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        }
    }

    public void printLastUsedCards (List<Player> players) {
        synchronized (lockPrint) {
            System.out.println("\nCARDS PLAYED IN THIS TURN~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            int min = Constants.NUMBEROFCARDSINDECK;
            for (Player p : players) {
                if (p.getMyDeck().getLeftCards().size() < min) {
                    min = p.getMyDeck().getLeftCards().size();
                }
            }
            for (Player p : players) {
                if (p.getMyDeck().getLeftCards().size() == min && p.getLastUsedCard() != null) {
                    System.out.print("    Player " + p.getNickname() + " choose the card:");
                    System.out.println(" Power: " + p.getLastUsedCard().getPower() + " Steps: " + p.getLastUsedCard().getMaxSteps());
                }
            }
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        }
    }

    public void printBoard(List<Board> boards) {
        synchronized (lockPrint) {
            String green, red, yellow, pink, blue;
            System.out.println("BOARDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
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
                boardString.append(ColorsCli.RESET).append("    Board of player: " + b.getNickname() + "\n").append(ColorsCli.RESET);
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

                boardString.append(ColorsCli.RESET).append("    Students in entrance: \n    ").append(ColorsCli.RESET);
                for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
                    for (int k = 0; k < nSE[i]; k++) {
                        boardString.append(ColorsCli.getColorByNumber(i)).append("● ").append(ColorsCli.getColorByNumber(i));
                    }
                }
                boardString.append(ColorsCli.RESET).append("\n    Towers on board: \n    ").append(ColorsCli.RESET);
                for (int i = 0; i < nT; i++) {
                    boardString.append(ColorsCli.RESET).append("♜ ").append(ColorsCli.BLACK).append(ColorsCli.RESET);
                }
                boardString.append(ColorsCli.RESET).append("\n").append(ColorsCli.RESET);
                System.out.println(boardString.toString());
            }
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        }
    }

    public void printArchipelago(List<Archipelago> archipelagos) {
        synchronized (lockPrint) {
            StringBuilder archipelago = new StringBuilder();
            archipelago.append(ColorsCli.RESET).append("ARCHIPELAGOS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~").append(ColorsCli.RESET);
            System.out.println(archipelago.toString());
            for (Archipelago a : archipelagos) {
                int numberTower = a.getBelongingIslands().size();
                archipelago = new StringBuilder();
                archipelago.append(ColorsCli.RESET).append("    Id archipelago: " + a.getIdArchipelago() + (a.getOwner() == null ? "" : " owner: " + a.getOwner().getNickname() + " team: " + a.getOwner().getTeam()) + (a.getIsMNPresent() == false ? "" : " MN is here") + (a.getIsForbidden() == false ? "" : " \uD83D\uDEAB") + "\n    ").append(ColorsCli.RESET);
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
                System.out.println(archipelago.toString());
            }

            archipelago = new StringBuilder();
            archipelago.append(ColorsCli.RESET).append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n").append(ColorsCli.RESET);
            System.out.println(archipelago.toString());
        }
    }

    public void printCloud (List<Cloud> clouds) {
        synchronized (lockPrint) {
            StringBuilder cloud = new StringBuilder();
            cloud.append(ColorsCli.RESET).append("CLOUDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~").append(ColorsCli.RESET);
            System.out.println(cloud.toString());
            for (Cloud c : clouds)
                if (c.getIsTaken() == false) {
                    cloud = new StringBuilder();
                    cloud.append(ColorsCli.RESET).append("    Id cloud: " + c.getIdCloud() + "\n    ").append(ColorsCli.RESET);
                    for (int j = 0; j < Constants.NUMBEROFKINGDOMS; j++) {
                        for (int k = 0; k < c.getStudents()[j]; k++) {
                            cloud.append(ColorsCli.getColorByNumber(j)).append("● ").append(ColorsCli.RESET);
                        }
                    }
                    System.out.println(cloud.toString());
                }

            cloud = new StringBuilder();
            cloud.append(ColorsCli.RESET).append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n").append(ColorsCli.RESET);
            System.out.println(cloud.toString());
        }
    }

    public Thread pingToServer(InetAddress geek) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        Thread.sleep(20000);
                        if(geek.isReachable(5000)) {
                            System.out.println("is reachable");
                        } else {
                            System.out.println("Non raggiungibile");
                        }
                    }
                } catch (Exception e) {
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }

    public void run() throws IOException {
        printLogo();
        socket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress(ip, port);
        socket.connect(socketAddress);
        System.out.println("Connection established");
        serverAlive = true;
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
        Scanner stdin = new Scanner(System.in);
        InetAddress geek = InetAddress.getByName(ip);


        try{
            Thread t0 = asyncReadFromSocket(socketIn);
            Thread t1 = asyncWriteToSocket(stdin, socketOut);
            Thread t2 = pingToServer(geek);
            t0.join();
            t1.join();
            t2.join();
            while (isActive());
            t0.interrupt();
            t1.interrupt();
            t2.interrupt();
        } catch(InterruptedException | NoSuchElementException e){
            System.out.println("Connection closed from the client side");
        } finally {
            stdin.close();
            socketIn.close();
            socketOut.close();
        }
    }
}