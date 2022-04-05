package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatchTest {
    GameHandler gameHandler;

    @Test
    public void matchTest(){
        Player pl1 = new Player("carmine", ColorOfTower.WHITE);
        gameHandler = new GameHandler(pl1, 3);
        gameHandler.addNewPlayer("chri", ColorOfTower.BLACK);
        assertEquals(0, gameHandler.getIsStarted());
        gameHandler.addNewPlayer("fede", ColorOfTower.GREY);
        assertEquals(3, gameHandler.getGame().getOrderOfPlayers().size());
        assertEquals(1, gameHandler.getIsStarted());
        assertEquals("carmine", gameHandler.getGame().getCurrentPlayer().getNickname());
        assertEquals(Phase.CARD_SELECTION, gameHandler.getGame().getPhase());
        gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", "CARD 1");
        assertEquals("chri", gameHandler.getGame().getCurrentPlayer().getNickname());
        assertEquals(Phase.CARD_SELECTION, gameHandler.getGame().getPhase());
        gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("chri", "CARD 2");
        assertEquals("fede", gameHandler.getGame().getCurrentPlayer().getNickname());
        assertEquals(Phase.CARD_SELECTION, gameHandler.getGame().getPhase());
        gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("fede", "CARD 3");
        assertEquals(Phase.MOVE_STUDENTS, gameHandler.getGame().getPhase());
        assertEquals("carmine", gameHandler.getGame().getCurrentPlayer().getNickname());

    }
}
