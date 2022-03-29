package it.polimi.ingsw.controller;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.model.*;

import static it.polimi.ingsw.model.ColorOfTower.BLACK;

public class ActionController {
    private Phase phase;
    private Game game;
    private int isFinished;

    public ActionController(Game game){
        this.game = game;
        isFinished = 0;
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

    public void checkReceivedAction(Player player, Phase sentPhase, Action sentAction){

    }

    public void sendActionToGame(Action action){

    }
    //chiamato da checkRA
    //a seconda della fase c'è switch case che fa le cose nel game

    public Player getCurrentPlayer(){
        return game.getCurrentPlayer();
    }
    //questa chiama quella sopra

    public void calculateInfluence(){
        for(Archipelago a: game.getListOfArchipelagos()){
            if(a.getIsMNPresent()==true){
                Player newOwner;
                Player oldOwner;
                int maxInfluence =0;
                if(a.getOwner() == null){
                    oldOwner = null;
                    newOwner = game.getCurrentPlayer();

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
        //guarda su che arcipelago sia MN e controlla se è da unificare con prec o succ
        //in caso chiama unifyarchipelagos
    }

    public void endGameImmediately(){
        isFinished = 1;
        //chiamata se cambia l'influenza e quindi posiziono nuove torri, finisce istantaneamente la partita
    }
}
