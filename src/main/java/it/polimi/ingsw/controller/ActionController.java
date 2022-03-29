package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;

public class ActionController {
    private Phase phase;
    private Game game;
    private Player currentPlayer;
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

    public void endPlayerTurn(){
        //reimposta la fase a move students
    }

    public boolean checkReceivedAction(Phase action){
        //riceve un'azione, controlla che sia la fase giusta,
        //chiama sendActionToGame
        //mette fase successiva (tranne se la fase era l'ultima che allora chiama end turn

        return true;
    }

    public void sendActionToGame(){

    }
    //chiamato da checkRA
    //a seconda della fase c'è switch case che fa le cose nel game

    public Player getCurrentPlayer(){
        return game.getCurrentPlayer();
    }
    //questa chiama quella sopra

    public boolean checkCurrentPlayer(){
        return true;
    }

    public Player calculateInfluence(){
        return currentPlayer; //non ritorna questo ma il player che ha influenza maggiore sull'isola
    }

    public void checkUnification(){
        //guarda su che arcipelago sia MN e controlla se è da unificare con prec o succ
        //in caso chiama unifyarchipelagos
    }

    public void endGameImmediately(){
        isFinished = 1;
        //chiamata se cambia l'influenza e quindi posiziono nuove torri, finisce istantaneamente la partita
    }
}
