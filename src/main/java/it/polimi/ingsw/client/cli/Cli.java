package it.polimi.ingsw.client.cli;

//import com.sun.tools.javac.code.Attribute;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.model.*;

import java.util.List;

public class Cli implements View {

    /**
     * TODO: add link to full rules
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

        System.out.println("\nCreators: Carmine Faino, Christian Di Diego, Federica Di Filippo");
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
        for (i = 0; i < players.size(); i++) {
            if (players.get(i).getMyDeck().getLeftCards().size() < min) {
                min = players.get(i).getMyDeck().getLeftCards().size();
            }
        }
        for (i = 0; i < players.size(); i++) {
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
            for(int i = 0; i < Constants.NUMBEROFKINGDOMS; i++){
                nSDN[i] = p.getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.values()[i]);
                nSE[i] = p.getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.values()[i]);
                hasProf[i] = p.getMyBoard().getProfessorsTable().getHasProf(StudsAndProfsColor.values()[i]);
            }

            StringBuilder board = new StringBuilder();
            board.append(ColorsCli.RESET).append("\n Board of player: "+ p.getNickname() + "\n").append(ColorsCli.RESET);
            for(int j = 0 ; j < Constants.NUMBEROFKINGDOMS ; j++){
                for(int k = 0; k < nSDN[j]; k++){
                    board.append(ColorsCli.getColorByNumber(j)).append(" ●").append(ColorsCli.getColorByNumber(j));
                }
                for(int k = nSDN[j]; k < Constants.MAXSTUDENTSINDINING; k++){
                    if((k + 1) % 3 == 0){
                        board.append(ColorsCli.getColorByNumber(j)).append(" ©").append(ColorsCli.getColorByNumber(j));
                    }else {
                        board.append(ColorsCli.getColorByNumber(j)).append(" ◯").append(ColorsCli.getColorByNumber(j));
                    }
                }
                if(hasProf[j]){
                    board.append(ColorsCli.getColorByNumber(j)).append(" | ⬢  ").append(ColorsCli.getColorByNumber(j));
                }else{
                    board.append(ColorsCli.getColorByNumber(j)).append(" | ⬡  ").append(ColorsCli.getColorByNumber(j));
                }
                board.append(ColorsCli.BLACK).append("\n").append(ColorsCli.BLACK);
            }

            board.append(ColorsCli.RESET).append("Students in entrance: \n").append(ColorsCli.RESET);
            for(int i = 0; i < Constants.NUMBEROFKINGDOMS; i++){
                for(int k = 0; k < nSE[i]; k++){
                    board.append(ColorsCli.getColorByNumber(i)).append("● ").append(ColorsCli.getColorByNumber(i));
                }
            }
            board.append(ColorsCli.RESET).append("\nTowers on board: \n").append(ColorsCli.RESET);
            for(int i = 0; i < nT; i++){
                board.append(ColorsCli.RESET).append("♜ ").append(ColorsCli.BLACK).append(ColorsCli.RESET);
            }
            System.out.println(board.toString());
        }
    }

    @Override
    public void printArchipelagos(List<Archipelago> archipelagos) {
        StringBuilder archipelago = new StringBuilder();
        archipelago.append(ColorsCli.RESET).append("ARCHIPELAGOS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~").append(ColorsCli.RESET);
        System.out.println(archipelago.toString());
        for(int i = 0; i < archipelagos.size(); i++) {
                archipelago = new StringBuilder();
                archipelago.append(ColorsCli.RESET).append("Id archipelago: " + archipelagos.get(i).getIdArchipelago() + " owner: " + archipelagos.get(i).getOwner().getNickname() + "\n").append(ColorsCli.RESET);
                for(Island is : archipelagos.get(i).getBelongingIslands()) {
                    for(int j = 0; j < Constants.NUMBEROFKINGDOMS; j++){
                        for(int k = 0; k < is.getStudentsByColor(StudsAndProfsColor.values()[j]); k++){
                            archipelago.append(ColorsCli.getColorByNumber(j)).append("● ").append(ColorsCli.getColorByNumber(j));
                        }
                    }
                    System.out.println(archipelago.toString());
                }
        }
        archipelago = new StringBuilder();
        archipelago.append(ColorsCli.RESET).append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~").append(ColorsCli.RESET);
        System.out.println(archipelago.toString());
    }

    @Override
    public void printClouds(List<Cloud> clouds) {
        StringBuilder cloud = new StringBuilder();
        cloud.append(ColorsCli.RESET).append("CLOUDS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~").append(ColorsCli.RESET);
        System.out.println(cloud.toString());
        for(int i = 0; i < clouds.size(); i++) {
            if(clouds.get(i).getIsTaken() == false) {
                cloud = new StringBuilder();
                cloud.append(ColorsCli.RESET).append("Id cloud: " + clouds.get(i).getIdCloud() + "\n").append(ColorsCli.RESET);
                for(int j = 0; j < Constants.NUMBEROFKINGDOMS; j++){
                    for(int k = 0; k < clouds.get(i).getStudents()[j]; k++){
                        cloud.append(ColorsCli.getColorByNumber(j)).append("● ").append(ColorsCli.getColorByNumber(j));
                    }
                }
                System.out.println(cloud.toString());
            }
        }
        cloud = new StringBuilder();
        cloud.append(ColorsCli.RESET).append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~").append(ColorsCli.RESET);
        System.out.println(cloud.toString());
    }
}