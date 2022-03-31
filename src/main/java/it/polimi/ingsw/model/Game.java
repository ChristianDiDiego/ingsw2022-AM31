package it.polimi.ingsw.model;

import it.polimi.ingsw.constants.Constants;

import java.util.*;

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
    private boolean endTurn;
    private int bank;

    //Aggiungi expertMode come parametro
    public Game(int numberOfPlayers, Player player){
        this.listOfPlayers = new ArrayList<>();
        this.listOfArchipelagos = new ArrayList<>();
        this.listOfClouds = new ArrayList<>();
        this.orderOfPlayers = new ArrayList<>();
        this.numberOfPlayers = numberOfPlayers;
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

        int[] studentsForIslands = new int[Constants.NUMBEROFKINGDOMS];
        for(int i = 0; i < Constants.NUMBEROFKINGDOMS; i++){
            studentsForIslands[i] = 2;
        }
        Random random = new Random();
        for(Archipelago a : this.listOfArchipelagos){
            for(Island i : a.getBelongingIslands()) {
                if(a.getIdArchipelago() != 0 && a.getIdArchipelago() !=5) {
                    int value = random.nextInt(5 + 0);
                    while (studentsForIslands[value] <= 0) {
                        value = random.nextInt(5 + 0);
                    }
                    i.addStudent(StudsAndProfsColor.values()[value]);
                    studentsForIslands[value]--;
                }
            }
        }


    }


    public void addPlayer(Player player){
        listOfPlayers.add(player);
        orderOfPlayers.add(player);
    }
    public List<Cloud> getListOfClouds(){
        return this.listOfClouds;
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
            case MOVE_STUDENTS:
                phase = Phase.MOVE_MN;
                break;
            case MOVE_MN:
                phase = Phase.CLOUD_SELECTION;
                break;
            case CLOUD_SELECTION:
                phase = Phase.MOVE_STUDENTS;
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

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /*
    For each color counts the number of students of that color in the arch
    and get the dominantColor
    for every player, check which player has that prof

    ToDo: if another player already have influence, count also the tower
          if the influence is the same for two players, the towers are not going to anyone
     */
    public void calculateInfluence(Archipelago arch){
        int highestNumberOfStuds = 0;
        StudsAndProfsColor dominantColor = StudsAndProfsColor.RED;

        for(StudsAndProfsColor color : StudsAndProfsColor.values()){
            int tempNOfStuds = 0;
            for(Island island : arch.getBelongingIslands()){
                tempNOfStuds = island.getStudentsByColor(color);
            }
            if(tempNOfStuds > highestNumberOfStuds){
                highestNumberOfStuds = tempNOfStuds;
                dominantColor = color;
            }

        }
        for(Player player : listOfPlayers){
            if(player.getMyBoard().getProfessorsTable().getHasProf(dominantColor)){
                //if the ownership of arch is of another player, add towers to his table and remove from the other
                //if the same player remains, do not change the number of towers on his board
                //change ownership of arch
            }
        }


    }

    public void getCoinFromBank(int coins) {
        bank -= coins;
    }

    public int getBank() {
        return bank;
    }

    public void addCoinInBank(int coins) {
        bank += coins;
    }

    public void setBank(int bank) {
        bank = bank;
    }
}
