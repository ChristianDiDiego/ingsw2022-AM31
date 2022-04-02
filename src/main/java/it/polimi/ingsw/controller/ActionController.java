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
