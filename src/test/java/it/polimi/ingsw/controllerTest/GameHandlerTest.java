package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameHandlerTest {
    private GameHandler gameHandler;

    /**
     * Check if players are correctly added to the game
     */
    @Test
    void addNewPlayer() {
        Player pl1 = new Player("carmine", ColorOfTower.WHITE);
        gameHandler = new GameHandler(pl1, 3,false);
        Player pl2 = new Player("chri", ColorOfTower.BLACK);
        Player pl3 = new Player("fede", ColorOfTower.GREY);
        gameHandler.addNewPlayer(pl2);
        gameHandler.addNewPlayer(pl3);
        assertEquals(3, gameHandler.getGame().getOrderOfPlayers().size());
        assertEquals(1, gameHandler.getIsStarted());

        /*gameHandler = new GameHandler(pl1, 3,false);
        Player pl4 = new Player("chri", ColorOfTower.BLACK);
        Player pl5 = new Player("fede", ColorOfTower.BLACK);
        gameHandler.addNewPlayer(pl4);
        gameHandler.addNewPlayer(pl5);
        assertEquals(2, gameHandler.getGame().getOrderOfPlayers().size());
        assertEquals(0, gameHandler.getIsStarted());
         */
    }

    /**
     * Check that if a color of a tower is already taken cannot be chosen again
     */
    @Test
    void checkColorTower() {
        Player pl1 = new Player("carmine", ColorOfTower.WHITE);
        gameHandler = new GameHandler(pl1, 3,false);
        Player pl2 = new Player("chri", ColorOfTower.BLACK);
        gameHandler.addNewPlayer(pl2);
        //assertFalse(gameHandler.checkColorTower(ColorOfTower.WHITE));
        //assertTrue(gameHandler.checkColorTower(ColorOfTower.GREY));

    }

    /**
     * Check if the game is properly started when the number of players decided by the
     * first player is reached
     */
    @Test
    void startGame() {

        Player pl1 = new Player("carmine", ColorOfTower.WHITE);
        gameHandler = new GameHandler(pl1, 3,false);
        assertEquals(gameHandler, gameHandler.getController().getGameHandler());
        assertEquals(gameHandler.getGame(), gameHandler.getController().getGame());
        assertEquals(0, gameHandler.getIsStarted());
        for(Player p: gameHandler.getGame().getOrderOfPlayers()){
           assertEquals(0, p.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        }
        Player pl2 = new Player("chri", ColorOfTower.BLACK);
        gameHandler.addNewPlayer(pl2);
        assertEquals(0, gameHandler.getIsStarted());
        for(Player p: gameHandler.getGame().getOrderOfPlayers()){
            assertEquals(0, p.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        }
        Player pl3 = new Player("fede", ColorOfTower.GREY);
        gameHandler.addNewPlayer(pl3);
        assertEquals(1, gameHandler.getIsStarted());
        for(Player p: gameHandler.getGame().getOrderOfPlayers()){
            assertEquals(6, p.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        }

    }

}