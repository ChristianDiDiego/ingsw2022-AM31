package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionParserTest {
    GameHandler gameHandler;

    @Test
    public void actionParserTest(){
        Player pl1 = new Player("carmine", ColorOfTower.WHITE);
        gameHandler = new GameHandler(pl1, 3);
        gameHandler.addNewPlayer("chri", ColorOfTower.BLACK);
        assertEquals(0, gameHandler.getIsStarted());
        gameHandler.addNewPlayer("fede", ColorOfTower.GREY);
        gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", "CARD 2");
    }

}
