package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

/**
 * Container of the different controllers and for the game
 */
public class Controller {
    private final Game game;
    private final GameHandler gameHandler;
    private final TurnController turnController;

    public Controller(Game game, GameHandler gameHandler) {
        this.gameHandler = gameHandler;
        this.game = game;
        this.turnController = new TurnController(this, gameHandler, this.game);
    }

    public TurnController getTurnController() {
        return turnController;
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    public Game getGame() {
        return game;
    }
}
