package it.polimi.ingsw.client.cli;

import com.sun.tools.javac.code.Attribute;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.*;

import java.util.List;

public class Cli implements View {

    /**
     * ha un metodo run che chiama il setup del nuovo player, poi chiama riceviInput
     * ha un metodo riceviInput che è sempre in ascolto e ogni volta
     * invia tutti i messaggi che riceve al parser
     */

    @Override
    public void printLogo() {
        System.out.println("\n" +
                "███████╗██████╗ ██╗   ██╗ █████╗ ███╗   ██╗████████╗██╗███████╗\n" +
                "██╔════╝██╔══██╗╚██╗ ██╔╝██╔══██╗████╗  ██║╚══██╔══╝██║██╔════╝\n" +
                "█████╗  ██████╔╝ ╚████╔╝ ███████║██╔██╗ ██║   ██║   ██║███████╗\n" +
                "██╔══╝  ██╔══██╗  ╚██╔╝  ██╔══██║██║╚██╗██║   ██║   ██║╚════██║\n" +
                "███████╗██║  ██║   ██║   ██║  ██║██║ ╚████║   ██║   ██║███████║\n" +
                "╚══════╝╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═══╝   ╚═╝   ╚═╝╚══════╝\n" +
                "                                                               \n");
    }

    @Override
    public void printMyDeck(Deck deck) {
        for(Card c : deck.getLeftCards()) {
            System.out.println("Power: " + c.getPower() + " Steps: " + c.getMaxSteps() + "\n");
        }
    }

    @Override
    public void printLastUsedCards(List<Player> players) {
        int min = 10;
        int i;
        for (i = 0; i < players.size() - 1; i++) {
            if (players.get(i).getMyDeck().getLeftCards().size() < min) {
                min = players.get(i).getMyDeck().getLeftCards().size();
            }
        }
        for (i = 0; i < players.size() - 1; i++) {
            if(players.get(i).getMyDeck().getLeftCards().size() == min) {
                System.out.println("Player " + players.get(i).getNickname() + " choose the card:" + "\n");
                System.out.println("Power: " + players.get(i).getLastUsedCard().getPower() + " Steps: " + players.get(i).getLastUsedCard().getMaxSteps());
            }
        }
    }

    @Override
    public void printBoards(List<Player> players) {
        for(Player p : players){
            String green, red, yellow, pink, blue;
            int[] nSDN = new int[]{0,0,0,0,0};
            int[] nSE = new int[]{0,0,0,0,0};
            int nT = p.getMyBoard().getTowersOnBoard().getNumberOfTowers();
            boolean[] hasProf =  new boolean[]{false, false, false, false, false};
            for(int i=0; i<5; i++){
                nSDN[i] = p.getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.values()[i]);
                nSE[i] = p.getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.values()[i]);
                hasProf[i] = p.getMyBoard().getProfessorsTable().getHasProf(StudsAndProfsColor.values()[i]);
            }

            StringBuilder board = new StringBuilder();
            board.append(ColorsCli.BLACK).append("\n Board of player: "+ p + "\n").append(ColorsCli.BLACK);
            for(int j=0 ; j<5 ; j++){
                for(int k = 0; k<nSDN[j]; k++){
                    board.append(ColorsCli.getColorByNumber(j)).append(" ●").append(ColorsCli.getColorByNumber(j));
                }
                for(int k=nSDN[j]; k<10; k++){
                    board.append(ColorsCli.getColorByNumber(j)).append(" ◯").append(ColorsCli.getColorByNumber(j));
                }
                if(hasProf[j]){
                    board.append(ColorsCli.getColorByNumber(j)).append(" | ⬢  ").append(ColorsCli.getColorByNumber(j));
                }else{
                    board.append(ColorsCli.getColorByNumber(j)).append(" | ⬡  ").append(ColorsCli.getColorByNumber(j));
                }
                board.append(ColorsCli.BLACK).append("\n").append(ColorsCli.BLACK);
            }

            board.append(ColorsCli.BLACK).append("Students in entrance: \n").append(ColorsCli.BLACK);
            for(int i=0; i<5; i++){
                for(int k=0; k<nSE[i]; k++){
                    board.append(ColorsCli.getColorByNumber(i)).append("● ").append(ColorsCli.getColorByNumber(i));
                }
                board.append(ColorsCli.BLACK).append("\n").append(ColorsCli.BLACK);
            }
            board.append(ColorsCli.BLACK).append("Towers on board: \n").append(ColorsCli.BLACK);
            for(int i = 0; i<nT; i++){
                board.append(ColorsCli.BLACK).append("♜ ").append(ColorsCli.BLACK).append(ColorsCli.RESET);
            }
            System.out.println(board.toString());

        }
    }

    @Override
    public void printArchipelagos(List<Archipelago> archipelagos) {
        int red, green, yellow, pink, blue;
        System.out.println(
                "ARCHIPELAGOS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + "\n");
        for(int c = 0; c < archipelagos.size() - 1; c++) {
            System.out.println("Id archipelago: " + archipelagos.get(c).getIdArchipelago() + "\n");
            red = 0;
            green = 0;
            yellow = 0;
            pink = 0;
            blue = 0;
            for(Island i : archipelagos.get(c).getBelongingIslands()) {
                red += i.getStudentsByColor(StudsAndProfsColor.RED);
                green += i.getStudentsByColor(StudsAndProfsColor.GREEN);
                yellow += i.getStudentsByColor(StudsAndProfsColor.YELLOW);
                pink += i.getStudentsByColor(StudsAndProfsColor.PINK);
                blue += i.getStudentsByColor(StudsAndProfsColor.BLUE);
            }
            System.out.println("Red: " + red + "\n");
            System.out.println("Green: " + green + "\n");
            System.out.println("Yellow: " + yellow + "\n");
            System.out.println("Pink: " + pink + "\n");
            System.out.println("Blue: " + blue + "\n");
        }
        System.out.println(
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + "\n");
    }

    @Override
    public void printClouds(List<Cloud> clouds) {
        System.out.println(
                "CLOUDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + "\n"
        );
        for(int i = 0; i < clouds.size() - 1; i++) {
            if(clouds.get(i).getIsTaken() == false) {
                System.out.println("Id cloud: " + clouds.get(i).getIdCloud() + "\n");
                System.out.println("Red: " + clouds.get(i).getStudents()[0] + "\n");
                System.out.println("Green: " + clouds.get(i).getStudents()[1] + "\n");
                System.out.println("Yellow: " + clouds.get(i).getStudents()[2] + "\n");
                System.out.println("Pink: " + clouds.get(i).getStudents()[3] + "\n");
                System.out.println("Blue: " + clouds.get(i).getStudents()[4] + "\n");
            }
        }
        System.out.println(
                "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + "\n"
        );
    }
}



