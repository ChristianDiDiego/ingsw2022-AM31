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
        //Place randomly two students of each color on the islands (one student per island)
        //except on the island where MN is present and on the opposite one
        for(int i = 0; i < Constants.NUMBEROFKINGDOMS; i++){
            studentsForIslands[i] = 2;
        }
        Random random = new Random();
        for(Archipelago a : this.listOfArchipelagos){
            for(Island i : a.getBelongingIslands()) {
                if(a.getIdArchipelago() != 0 && a.getIdArchipelago() !=5) {
                    int value = random.nextInt(Constants.NUMBEROFKINGDOMS);
                    while (studentsForIslands[value] <= 0) {
                        value = random.nextInt(Constants.NUMBEROFKINGDOMS);
                    }
                    i.addStudent(StudsAndProfsColor.values()[value]);
                    studentsForIslands[value]--;
                }
            }
        }


    }

    /**
     * Add a new player to the list of players
     * @param player to be added to the game
     */
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

    //TODO: add an observer that when MN change the position calculate the influence
    /**
     * Move Mother Nature from the current archipelago to another one
     * @param steps number of steps that MN needs to do
     */
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
        calculateInfluence();
    }
    public List<Archipelago> getListOfArchipelagos(){
        return listOfArchipelagos;
    }

    /**
     * Calculate who is the current player according to the ordered list of players
     */
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

    /**
     * Check if a professor needs to be assigned to a player and do it
     * @param color of the professor to check
     */
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

    /**
     * Unify two archipelagos adding the islands of the archToBeUnified to the one of the actualArchipelago
     * @param actualArchipelago Archipelago where MN is present
     * @param archToBeUnified Archipelago to be unified to the actual one
     */
    public void unifyArchipelagos(Archipelago actualArchipelago, Archipelago archToBeUnified){
        for(Island island: archToBeUnified.getBelongingIslands()){
            actualArchipelago.addIsland(island);
        }
        listOfArchipelagos.remove(archToBeUnified);

    }

    public Phase getPhase(){
        return phase;
    }

    /**
     * Decide the nextPhase of the match
     */
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
                if(getCurrentPlayer()==orderOfPlayers.get(orderOfPlayers.size()-1)){
                    phase = Phase.CARD_SELECTION;
                }else{
                    phase = Phase.MOVE_STUDENTS;
                }
                break;
        }
    }

    public Bag getBag(){
        return bag;
    }

    //TODO: decide if move it to turn controller
    /**
     * Move a student from the entrance of a player to the dining room or to an archipelago
     * @param colorToMove color of the student to be picked from the entrance
     * @param destination of the student (0 for dining room, [1...12] for an archipelago
     */
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

    public void getCoinFromBank(int coins) {
        bank -= coins;
    }

    public int getBank() {
        return bank;
    }

    /**
     * Add an amount of coins to the bank of the game
     * @param coins to be added
     */
    public void addCoinInBank(int coins) {
        bank += coins;
    }

    public void setBank(int bank) {
        bank = bank;
    }

    /**
     * Calculate the influence on the archipelago where MN is present
     * Menage the tower situation of an archipelago and of the players after that
     * Call checkUnification to check if the archipelago needs to be unified to another one
     */
    public void calculateInfluence(){
        for(Archipelago a: getListOfArchipelagos()){
            if(a.getIsMNPresent()){
                Player newOwner;
                Player oldOwner;
                int maxInfluence =0;
                if(a.getOwner() == null){
                    oldOwner = null;
                    newOwner = getCurrentPlayer();

                }else {
                    oldOwner = a.getOwner();
                    newOwner = a.getOwner();
                    maxInfluence = a.getBelongingIslands().size();
                }

                for(int c=0; c< Constants.NUMBEROFKINGDOMS; c++){
                    for(Island i: a.getBelongingIslands()){
                        if(i.getAllStudents()[c] > 0 && a.getOwner().getMyBoard().getProfessorsTable().getHasProf(StudsAndProfsColor.values()[c])) {
                            maxInfluence += i.getAllStudents()[c];
                        }
                    }
                }
                for(Player p : getOrderOfPlayers()){
                    if(p != newOwner){
                        int newInfluence = 0;
                        for(int c=0; c< Constants.NUMBEROFKINGDOMS; c++){
                            for(Island i: a.getBelongingIslands()){
                                if(i.getAllStudents()[c] > 0 && p.getMyBoard().getProfessorsTable().getHasProf(StudsAndProfsColor.values()[c])) {
                                    newInfluence += i.getAllStudents()[c];
                                }
                            }

                        }
                        if(newInfluence > maxInfluence){
                            newOwner = p;
                            maxInfluence = newInfluence;
                        }
                    }
                }

                if(maxInfluence > 0){
                    a.changeOwner(newOwner);
                    for(int i = 0; i < a.getBelongingIslands().size(); i++) {
                        if(oldOwner != null){
                            oldOwner.getMyBoard().getTowersOnBoard().removeTower();
                        }
                        newOwner.getMyBoard().getTowersOnBoard().removeTower();
                    }
                  //  checkUnification(a);
                }
            }
        }
    }
}
