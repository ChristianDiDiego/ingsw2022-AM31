package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Phase;

public class TurnController {
    private Controller controller;
    private ActionController actionController;
    private GameHandler gameHandler;

    public TurnController(Controller controller, GameHandler gameHandler, Game game){
        this.controller = controller;
        this.actionController = new ActionController(game);
        this.gameHandler = gameHandler;
    }

    public Controller getController() {
        return controller;
    }

    public ActionController getActionController() {
        return actionController;
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    public void startTurn(){}
    //aggiunge gli studenti sulle nuvole
    //riceve tutte le carte
    //controlla che nessuno giochi carta usata da altri se size del  deck è >1
    //chiama in caso EndGame se ho finito gli studenti o se il 10 turno

    public void endTurn(){}

    public void endGame(){
        gameHandler.setIsStarted(-1);
        //notifyAll per gamehandler che chiama endgame
    }
}


