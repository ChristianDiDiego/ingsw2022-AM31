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

    public void checkActionCard(Player player, int power){
        if(game.getPhase()== Phase.CARD_SELECTION && player == game.getCurrentPlayer()){
            turnController.manageReceivedCard(player, power);

        }else if(game.getPhase()== Phase.CARD_SELECTION || player != game.getCurrentPlayer()){
            System.out.println("non è il tuo turno!!");
        }else if(game.getPhase()!= Phase.CARD_SELECTION && player == game.getCurrentPlayer()){
            System.out.println("hai inviato un'azione non valida, riprova");
        }
    }

//TODO: check if the destination is valid
   public void checkActionMoveStudent(Player player,StudsAndProfsColor[] colors, int[] destinations){
        if(game.getPhase()== Phase.MOVE_STUDENTS && player == game.getCurrentPlayer()){
            //check che l'azione sia valida, in caso aggiorno Model

        }else if(game.getPhase()== Phase.MOVE_STUDENTS || player != game.getCurrentPlayer()){
            System.out.println("non è il tuo turno!!");
       }else if(game.getPhase()!= Phase.MOVE_STUDENTS && player == game.getCurrentPlayer()){
            System.out.println("hai inviato un'azione non valida, riprova");
        }
   }

    public void checkActionMoveMN(Player player,int destination){
        if(game.getPhase()== Phase.MOVE_MN && player == game.getCurrentPlayer()){
            //check che l'azione sia valida, in caso modifico il model

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

    /**
     * Called if a player, adding the tower to an archipelago, run out of them
     */
    public void endGameImmediately(){
        isFinished = 1;
    }
}
