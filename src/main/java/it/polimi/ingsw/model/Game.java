package it.polimi.ingsw.model;

import it.polimi.ingsw.constants.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Game {
    public static final int MAXPLAYERS = 4;
    private List<Player> listOfPlayers ;
    private List<Archipelago> listOfArchipelagos;
    private List<Cloud> listOfClouds;
    private List<Player> orderOfPlayers;
    private Bag bag;
    private int numberOfPlayers;
    private Player currentPlayer;
    private Phase phase;

    public Game(int numberOfPlayers, Player player){
        this.listOfPlayers = new ArrayList<>();
        this.listOfArchipelagos = new ArrayList<>();
        this.listOfClouds = new ArrayList<>();
        this.orderOfPlayers = new ArrayList<>();
        listOfPlayers.add(player);
        orderOfPlayers.add(player);
        this.bag = new Bag(numberOfPlayers);
        this.currentPlayer = player;

        for(int i = 0; i< Constants.NUMBEROFISLANDS; i++){
            Archipelago arc = new Archipelago(i);
            listOfArchipelagos.add(arc);
        }
        for(int i = 0; i < numberOfPlayers; i++){
            Cloud cloud = new Cloud(i);
            listOfClouds.add(cloud);
        }

        //creo un array con 2studenti per colore e poi pesco random per ogni isola tranne la 0 e la 5



    }

    public void addPlayer(Player player){
        listOfPlayers.add(player);
        orderOfPlayers.add(player);
    }

    public Player getCurrentPlayer(){
        return this.currentPlayer;
    }

    /**
     * For each player, according to the power of the card used, calculate his order in the next turn
     */
    public void findPlayerOrder(){

        Collections.sort(orderOfPlayers, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o1.getLastUsedCard().compareTo(o2.getLastUsedCard());
            }
        });

    }

    public List<Player> getOrderOfPlayers(){
        return orderOfPlayers;
    }

    public void moveMotherNature(int steps){
        int index = 0;
        for(Archipelago arci : listOfArchipelagos){
            if(arci.getIsMNPresent()){
                index = arci.getIdArchipelago();
                arci.changeMNPresence();
            }
        }
        index = (index + steps)%listOfArchipelagos.size();
        for(Archipelago arci : listOfArchipelagos){
            if(arci.getIdArchipelago() == index){
                arci.changeMNPresence();
            }
        }

    }
    public List<Archipelago> getListOfArchipelagos(){
        return listOfArchipelagos;
    }

    public void calculateCurrentPlayer(){
        int index = 0;
        for( Player p : orderOfPlayers){
            if(p.getNickname().equals(currentPlayer.getNickname())){
                break;
            }
            index ++;
        }
        index = index + 1;
        currentPlayer = orderOfPlayers.get(index);
    }

    public void assignProfessor(StudsAndProfsColor color){
        Player player = currentPlayer;
        int max = 0;
        for(Player p : listOfPlayers){
            if(p != currentPlayer){
                if(p.getMyBoard().getDiningRoom().getStudentsByColor(color) > max){
                    player = p;
                    max = p.getMyBoard().getDiningRoom().getStudentsByColor(color);
                }
            }
        }
        if(currentPlayer.getMyBoard().getDiningRoom().getStudentsByColor(color) == player.getMyBoard().getDiningRoom().getStudentsByColor(color)){
            player.getMyBoard().getProfessorsTable().removeProfessor(color);
        }
        if(currentPlayer.getMyBoard().getDiningRoom().getStudentsByColor(color) > player.getMyBoard().getDiningRoom().getStudentsByColor(color)){
            player.getMyBoard().getProfessorsTable().removeProfessor(color);
            currentPlayer.getMyBoard().getProfessorsTable().addProfessor(color);
        }
    }

    public void unifyArchipelagos(Archipelago actualArchipelago, Archipelago archToBeUnified){
        for(Island island: archToBeUnified.getBelongingIslands()){
            actualArchipelago.addIsland(island);
        }
        listOfArchipelagos.remove(archToBeUnified);

    }

    public Phase getPhase(){
        return phase;
    }

    public void nextPhase(){
        switch (phase){
            case CARD_SELECTION:
                phase = Phase.MOVE_STUDENTS;
                break;
            case MOVE_STUDENTS:
                phase = Phase.MOVE_MN;
                break;
            case MOVE_MN:
                phase = Phase.CLOUD_SELECTION;
                break;
            case CLOUD_SELECTION:
                phase = Phase.CARD_SELECTION;
                break;
        }
    }

    public Bag getBag(){
        return bag;
    }

    public void moveStudents(StudsAndProfsColor colorToMove, int destination){
        if(destination == Constants.DININGROOMDESTINATION){
                currentPlayer.getMyBoard().getEntrance().removeStudent(colorToMove);
                currentPlayer.getMyBoard().getDiningRoom().addStudent(colorToMove);
        }
        else{
                currentPlayer.getMyBoard().getEntrance().removeStudent(colorToMove);
                for(Archipelago arch : listOfArchipelagos){
                    for(Island island : arch.getBelongingIslands()){
                        if(island.getIdIsland() == destination){
                            island.addStudent(colorToMove);
                        }
                    }
                }
        }
    }
}
