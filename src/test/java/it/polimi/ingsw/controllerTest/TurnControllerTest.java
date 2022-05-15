package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests that the cards are properly played
 */
class TurnControllerTest {
    GameHandler gameHandler;

    @Test
    void startTurn() {
    }

    @Test
    void checkActionCard() {
        Player pl1 = new Player("carmine", ColorOfTower.WHITE);
        gameHandler = new GameHandler(pl1, 3,false);
        Player pl2 = new Player("chri", ColorOfTower.BLACK);
        gameHandler.addNewPlayer(pl2);
        assertEquals(0, gameHandler.getIsStarted());
        Player pl3 = new Player("fede", ColorOfTower.GREY);
        gameHandler.addNewPlayer(pl3);
        pl2.setLastUsedCard(new Card(5,3));
        assertFalse(gameHandler.getController().getTurnController().checkActionCard(recognisePlayer("fede"), 5));
        assertFalse(gameHandler.getController().getTurnController().checkActionCard(recognisePlayer("carmine"), 20));
        assertTrue(gameHandler.getController().getTurnController().checkActionCard(recognisePlayer("carmine"), 1));

        pl2.setLastUsedCard(new Card(0,3));
        gameHandler.getController().getTurnController().checkActionCard(recognisePlayer("carmine"), 2);
        gameHandler.getController().getTurnController().checkActionCard(recognisePlayer("carmine"), 3);
        gameHandler.getController().getTurnController().checkActionCard(recognisePlayer("carmine"), 4);
        gameHandler.getController().getTurnController().checkActionCard(recognisePlayer("carmine"), 5);
        gameHandler.getController().getTurnController().checkActionCard(recognisePlayer("carmine"), 6);
        gameHandler.getController().getTurnController().checkActionCard(recognisePlayer("carmine"), 7);
        gameHandler.getController().getTurnController().checkActionCard(recognisePlayer("carmine"), 8);
        gameHandler.getController().getTurnController().checkActionCard(recognisePlayer("carmine"), 9);
        assertFalse(gameHandler.getController().getTurnController().checkActionCard(recognisePlayer("carmine"), 1));
    }
    private Player recognisePlayer(String nickname){
        for(Player player :gameHandler.getController().getTurnController().getActionController().getGame().getOrderOfPlayers()){
            if(player.getNickname().equals(nickname)) {
                return player;
            }
        }
        return null;
    }
}