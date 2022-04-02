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
   public void checkActionMoveStudent(Player player,StudsAndProfsColor[] colors, int[] destinations){
       StudsAndProfsColor color;
       int destination;
       for(int i = 0 ; i < destinations.length ; i++ ){
          color = colors[i];
          destination = destinations[i];
           if(game.getPhase()== Phase.MOVE_STUDENTS && player == game.getCurrentPlayer()){
               if(player.getMyBoard().getEntrance().getStudentsByColor(color) == 0){
                   System.out.println("You do not have a student of color " + color.toString());
               }else{
                   if(destination == 0){
                       if(player.getMyBoard().getDiningRoom().getStudentsByColor(color) == Constants.MAXSTUDENTSINDINING){
                           System.out.println("Your dining room of the color " + color.toString() + " is full");
                       }else{
                           player.getMyBoard().getEntrance().removeStudent(color);
                           player.getMyBoard().getDiningRoom().addStudent(color);
                           game.assignProfessor(color);
                       }
                   }else{
                       player.getMyBoard().getEntrance().removeStudent(color);
                       for (Archipelago arc : game.getListOfArchipelagos()){
                           for (Island island : arc.getBelongingIslands()){
                               if(island.getIdIsland() == destination){
                                   island.addStudent(color);
                               }
                           }
                       }
                   }
               }

           }else if(game.getPhase()== Phase.MOVE_STUDENTS || player != game.getCurrentPlayer()){
               System.out.println("non è il tuo turno!!");
           }else if(game.getPhase()!= Phase.MOVE_STUDENTS && player == game.getCurrentPlayer()){
               System.out.println("hai inviato un'azione non valida, riprova");
           }
       }
   }

    //TODO: add an observer that when MN change the position calculate the influence
    /**
     * Check if a player can move MN of the steps that he sent
     * @param player that want to move MN
     * @param steps that ask to MN to do
     */
    public void checkActionMoveMN(Player player,int steps){
        if(game.getPhase()== Phase.MOVE_MN && player == game.getCurrentPlayer()){
                if(steps <= player.getLastUsedCard().getMaxSteps()){
                    game.moveMotherNature(steps);
                    calculateInfluence();
                }else {
                    System.out.println("The card that you played does not allow you to do these steps!" +
                            "(you can do max " + player.getLastUsedCard().getMaxSteps() + " steps)");
                }

        }else if(game.getPhase()== Phase.MOVE_MN || player != game.getCurrentPlayer()){
            System.out.println("non è il tuo turno!!");
        }else if(game.getPhase()!= Phase.MOVE_MN && player == game.getCurrentPlayer()){
            System.out.println("hai inviato un'azione non valida, riprova");
        }
    }

    /**
     * Check if
     * @param player
     * @param cloudId
     */
    public void checkActionCloud(Player player,int cloudId){
        if(game.getPhase()== Phase.CLOUD_SELECTION && player == game.getCurrentPlayer()){
            //check che l'azione sia valida, in caso modifico il model

        }else if(game.getPhase()== Phase.CLOUD_SELECTION || player != game.getCurrentPlayer()){
            System.out.println("non è il tuo turno!!");
        }else if(game.getPhase()!= Phase.CLOUD_SELECTION && player == game.getCurrentPlayer()){
            System.out.println("hai inviato un'azione non valida, riprova");
        }
    }

   public void sendActionToGame(){}

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
}
