package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.ActionParser;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.model.*;

import static it.polimi.ingsw.model.ColorOfTower.BLACK;

//TODO: Checks need to be finished checking that the actions are correct
// (enough players in entrance to be moved etc.)
// Add tests!!!!!!

public class ActionController {
    private Phase phase;
    private Game game;
    private int isFinished;
    private ActionParser actionParser;
    private TurnController turnController;

    public ActionController(Game game, TurnController turnController){
        this.game = game;
        isFinished = 0;
        actionParser = new ActionParser(this);
        this.turnController = turnController;
    }

    public Phase getPhase() {
        return phase;
    }

    public Game getGame() {
        return game;
    }

    /**
     * finisce il turno di ogni giocatore, aggiorna la fase 2 volte per saltare quella della selezione carte
     */
    public void endPlayerTurn(){
        game.nextPhase();
        game.nextPhase();
        phase = game.getPhase();

    }

    /**
     * Calculate the influence on the archipelago where MN is present
     * Menage the tower situation of an archipelago and of the players after that
     * Call checkUnification to check if the archipelago needs to be unified to another one
     */
    public void calculateInfluence(){
        for(Archipelago a: game.getListOfArchipelagos()){
            if(a.getIsMNPresent() && a.getIsForbidden() == false){
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
                for(Player p : game.getOrderOfPlayers()){
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
                      checkUnification(a);
                }
            }else if (a.getIsMNPresent() && a.getIsForbidden() == true){
                a.setIsForbidden(false);
            }
        }
    }
    /**
     * Check if an archipelago has to be unified with previous or next,
     * if yes, it calls game.unifyArchipelagos()
     * @param a archipelago to be checked
     */
    public void checkUnification(Archipelago a){
        int index = game.getListOfArchipelagos().indexOf(a);
        int previous = index - 1;
        int next = index + 1;
        if(index == 0) {
            previous = game.getListOfArchipelagos().size() - 1;
        }
        if(index == game.getListOfArchipelagos().size() - 1) {
            next = 0;
        }

        if(a.getOwner() == game.getListOfArchipelagos().get(previous).getOwner()) {
            game.unifyArchipelagos(a, game.getListOfArchipelagos().get(previous));
        }
        if(a.getOwner() == game.getListOfArchipelagos().get(next).getOwner()) {
            game.unifyArchipelagos(a, game.getListOfArchipelagos().get(next));
        }

    }

    //TODO: leave here only the controller part of the movement?
    /**
     * Check if a movement of a student from the entrance of the player's board is allowed
     * Call assignProfessor to check if a prof needs to be added
     * (TODO: add a listener that does it when the dining room change?)
     * @param player that do the movement
     * @param colors of the students that the player wants to move
     * @param destinations of the students (0 = dining room, [1..12] number of the archipelago
     */
//TODO: check if the destination is valid
   public boolean checkActionMoveStudent(Player player,StudsAndProfsColor[] colors, int[] destinations){
       StudsAndProfsColor color;
       int destination;
       if(game.getPhase()== Phase.MOVE_STUDENTS && player == game.getCurrentPlayer()) {
           if (colors.length == turnController.getGameHandler().getNumberOfMovements()) {
               for (int i = 0; i < destinations.length; i++) {
                   color = colors[i];
                   destination = destinations[i];

                   if (player.getMyBoard().getEntrance().getStudentsByColor(color) == 0) {
                       System.out.println("You do not have a student of color " + color.toString());
                       return false;
                   } else {
                       if (destination == 0) {
                           if (player.getMyBoard().getDiningRoom().getStudentsByColor(color) == Constants.MAXSTUDENTSINDINING) {
                               System.out.println("Your dining room of the color " + color.toString() + " is full");
                               return false;
                           } else {
                               player.getMyBoard().getEntrance().removeStudent(color);
                               player.getMyBoard().getDiningRoom().addStudent(color);
                               game.assignProfessor(color);
                               return true;
                           }
                       } else {
                           player.getMyBoard().getEntrance().removeStudent(color);
                           for (Archipelago arc : game.getListOfArchipelagos()) {
                               for (Island island : arc.getBelongingIslands()) {
                                   if (island.getIdIsland() == destination) {
                                       island.addStudent(color);
                                       return true;
                                   }
                               }
                           }
                           System.out.println("Destination not valid");
                           return false;
                       }
                   }
               }
           }else {
               System.out.println("You can move only " + turnController.getGameHandler().getNumberOfMovements() + " students");
               return false;
           }
       }else if(game.getPhase()== Phase.MOVE_STUDENTS || player != game.getCurrentPlayer()){
               System.out.println("non è il tuo turno!!");
               return false;
           }else {
               System.out.println("hai inviato un'azione non valida, riprova");
               return false;
           }
       return false;
   }

    //TODO: add an observer that when MN change the position calculate the influence
    /**
     * Check if a player can move MN of the steps that he sent
     * @param player that want to move MN
     * @param steps that ask to MN to do
     * @return true if the player can do the steps, false otherwise
     */
    public boolean checkActionMoveMN(Player player,int steps){
        if(game.getPhase()== Phase.MOVE_MN && player == game.getCurrentPlayer()){

                if(player.getUsedCharacter() == 1? steps <= player.getLastUsedCard().getMaxSteps() : steps <= player.getLastUsedCard().getMaxSteps()+2 ){
                    game.moveMotherNature(steps);
                    calculateInfluence();
                    return true;
                }else {
                    System.out.println("The card that you played does not allow you to do these steps!" +
                            "(you can do max " + player.getLastUsedCard().getMaxSteps() + " steps)");
                    return false;
                }

        }else if(game.getPhase()== Phase.MOVE_MN || player != game.getCurrentPlayer()){
            System.out.println("non è il tuo turno!!");
            return false;
        }else{
            System.out.println("hai inviato un'azione non valida, riprova");
            return false;
        }
    }

    /**
     * Check if a cloud can be picked and do it
     * @param player that wants to pick a cloud
     * @param cloudId id of the cloud to pick
     */
    public boolean checkActionCloud(Player player,int cloudId){
        if(game.getPhase()== Phase.CLOUD_SELECTION && player == game.getCurrentPlayer()){
            for(Cloud cloud : game.getListOfClouds()){
                if (cloud.getIdCloud() == cloudId){
                    if(!cloud.getIsTaken()){
                        player.getMyBoard().getEntrance().addStudent(cloud.getStudents());
                        return true;
                    }else{
                        System.out.println("Cloud already taken");
                        return false;
                    }
                }else{
                    System.out.println("Number of the cloud not valid");
                    return false;
                }
            }

        }else if(game.getPhase()== Phase.CLOUD_SELECTION || player != game.getCurrentPlayer()){
            System.out.println("non è il tuo turno!!");
            return false;
        }else {
            System.out.println("hai inviato un'azione non valida, riprova");
            return false;
        }
        return false;
    }


    public Player getCurrentPlayer(){
        return game.getCurrentPlayer();
    }
    //questa chiama quella sopra

    /**
     * Called if a player, adding the tower to an archipelago, run out of them
     */
    public void endGameImmediately(){
        isFinished = 1;
    }

    public TurnController getTurnController() {
        return turnController;
    }
}
