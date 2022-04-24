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
import java.net.Socket;
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

    public Cli(String ip, int port) {
        this.ip = ip;
        this.port = port;
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

                        if(inputObject instanceof String) {
                            System.out.println((String) inputObject);
                        }else if(inputObject instanceof ListOfBoards) {
                            printBoard(((ListOfBoards) inputObject).getBoards());
                        }else if(inputObject instanceof Deck) {
                            printMyDeck((Deck) inputObject);
                        }else if(inputObject instanceof ListOfArchipelagos) {
                            printArchipelago(((ListOfArchipelagos) inputObject).getArchipelagos());
                        }else if(inputObject instanceof ListOfClouds) {
                            printCloud(((ListOfClouds) inputObject).getClouds());
                        }else if(inputObject instanceof ListOfPlayers) {
                            printLastUsedCards(((ListOfPlayers) inputObject).getPlayers());
                        }
                        else {
                            throw new IllegalArgumentException();
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
    public Thread asyncWriteToSocket(final Scanner stdin, final PrintWriter socketOut) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        //mando sia board che la stringa
                        String inputLine = stdin.nextLine();
                        //in inputline salvo quello che leggo da tastiera
                        socketOut.println(inputLine);
                        socketOut.flush();
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
            for (Card c : deck.getLeftCards()) {
                System.out.println("Power: " + c.getPower() + " Steps: " + c.getMaxSteps() + "\n");
            }
        }

        public void printLastUsedCards (List<Player> players) {
            System.out.println("CARDS PLAYED IN THIS TURN~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            int min = 10;
            int i;
            for (i = 0; i < players.size(); i++) {
                if (players.get(i).getMyDeck().getLeftCards().size() < min) {
                    min = players.get(i).getMyDeck().getLeftCards().size();
                }
            }
            for (i = 0; i < players.size(); i++) {
                if (players.get(i).getMyDeck().getLeftCards().size() == min) {
                    System.out.println("Player " + players.get(i).getNickname() + " choose the card:" + "\n");
                    System.out.println("Power: " + players.get(i).getLastUsedCard().getPower() + " Steps: " + players.get(i).getLastUsedCard().getMaxSteps());
                }
            }
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        }

        public void printBoard(List<Board> boards) {
            String green, red, yellow, pink, blue;
            System.out.println("BOARDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            for(Board b : boards) {
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
                boardString.append(ColorsCli.RESET).append("\n Board of player: " + b.getNickname() + "\n").append(ColorsCli.RESET);
                for (int j = 0; j < Constants.NUMBEROFKINGDOMS; j++) {
                    for (int k = 0; k < nSDN[j]; k++) {
                        boardString.append(ColorsCli.getColorByNumber(j)).append(" ●").append(ColorsCli.getColorByNumber(j));
                    }
                    for (int k = nSDN[j]; k < Constants.MAXSTUDENTSINDINING; k++) {
                        if ((k + 1) % 3 == 0) {
                            boardString.append(ColorsCli.getColorByNumber(j)).append(" ©").append(ColorsCli.getColorByNumber(j));
                        } else {
                            boardString.append(ColorsCli.getColorByNumber(j)).append(" ◯").append(ColorsCli.getColorByNumber(j));
                        }
                    }
                    if (hasProf[j]) {
                        boardString.append(ColorsCli.getColorByNumber(j)).append(" | ⬢  ").append(ColorsCli.getColorByNumber(j));
                    } else {
                        boardString.append(ColorsCli.getColorByNumber(j)).append(" | ⬡  ").append(ColorsCli.getColorByNumber(j));
                    }
                    boardString.append(ColorsCli.BLACK).append("\n").append(ColorsCli.BLACK);
                }

                boardString.append(ColorsCli.RESET).append(" Students in entrance: \n").append(ColorsCli.RESET);
                for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
                    for (int k = 0; k < nSE[i]; k++) {
                        boardString.append(ColorsCli.getColorByNumber(i)).append(" ●").append(ColorsCli.getColorByNumber(i));
                    }
                }
                boardString.append(ColorsCli.RESET).append("\n Towers on board: \n").append(ColorsCli.RESET);
                for (int i = 0; i < nT; i++) {
                    boardString.append(ColorsCli.RESET).append(" ♜").append(ColorsCli.BLACK).append(ColorsCli.RESET);
                }
                System.out.println(boardString.toString());
            }
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        }

        public void printArchipelago(List<Archipelago> archipelagos) {
            StringBuilder archipelago = new StringBuilder();
            archipelago.append(ColorsCli.RESET).append("ARCHIPELAGOS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~").append(ColorsCli.RESET);
            System.out.println(archipelago.toString());
            for(Archipelago a : archipelagos) {
                archipelago = new StringBuilder();
                archipelago.append(ColorsCli.RESET).append(" Id archipelago: " + a.getIdArchipelago() + (a.getOwner() == null ? "" : " owner: " + a.getOwner().getNickname()) + (a.getIsMNPresent() == false ? "" : " MN is here") + "\n").append(ColorsCli.RESET);
                for (Island is : a.getBelongingIslands()) {
                    for (int j = 0; j < Constants.NUMBEROFKINGDOMS; j++) {
                        for (int k = 0; k < is.getStudentsByColor(StudsAndProfsColor.values()[j]); k++) {
                            archipelago.append(ColorsCli.getColorByNumber(j)).append(" ●").append(ColorsCli.RESET);
                        }
                    }
                    System.out.println(archipelago.toString());
                }
            }

            archipelago = new StringBuilder();
            archipelago.append(ColorsCli.RESET).append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n").append(ColorsCli.RESET);
            System.out.println(archipelago.toString());
        }

        public void printCloud (List<Cloud> clouds) {
            StringBuilder cloud = new StringBuilder();
            cloud.append(ColorsCli.RESET).append("CLOUDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~").append(ColorsCli.RESET);
            System.out.println(cloud.toString());
            for(Cloud c : clouds)
                if (c.getIsTaken() == false) {
                    cloud = new StringBuilder();
                    cloud.append(ColorsCli.RESET).append(" Id cloud: " + c.getIdCloud() + "\n").append(ColorsCli.RESET);
                    for (int j = 0; j < Constants.NUMBEROFKINGDOMS; j++) {
                        for (int k = 0; k < c.getStudents()[j]; k++) {
                            cloud.append(ColorsCli.getColorByNumber(j)).append(" ●").append(ColorsCli.RESET);
                        }
                    }
                    System.out.println(cloud.toString());
                }

            cloud = new StringBuilder();
            cloud.append(ColorsCli.RESET).append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n").append(ColorsCli.RESET);
            System.out.println(cloud.toString());
        }


    public void run() throws IOException {
        printLogo();
        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
        Scanner stdin = new Scanner(System.in);

        try{
            Thread t0 = asyncReadFromSocket(socketIn);
            Thread t1 = asyncWriteToSocket(stdin, socketOut);
            t0.join();
            t1.join();
        } catch(InterruptedException | NoSuchElementException e){
            System.out.println("Connection closed from the client side");
        } finally {
            stdin.close();
            socketIn.close();
            socketOut.close();
            socket.close();
        }
    }
}