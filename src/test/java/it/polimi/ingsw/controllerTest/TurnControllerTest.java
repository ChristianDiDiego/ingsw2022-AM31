package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.controller.GameHandler;
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

        gameHandler.getController().getTurnController().checkActionCard(recognisePlayer("carmine"), 2);

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