package it.polimi.ingsw.client.cli;

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



