package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.Cli;
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
        Cli cli = new Cli();
        //First player login
        Player pl1 = new Player("carmine", ColorOfTower.WHITE);
        gameHandler = new GameHandler(pl1, 3);
        //other players login
        gameHandler.addNewPlayer("chri", ColorOfTower.BLACK);
        assertEquals(0, gameHandler.getIsStarted());
        gameHandler.addNewPlayer("fede", ColorOfTower.GREY);
        assertEquals(3, gameHandler.getGame().getOrderOfPlayers().size());
        assertEquals(1, gameHandler.getIsStarted());
        assertEquals("carmine", gameHandler.getGame().getCurrentPlayer().getNickname());
        //The game is on: receive the cards from the players
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
        cli.printBoards(gameHandler.getGame().getListOfPlayer());
        //start the game with current player setted as rules say
        gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("chri", "MOVEST R-0,B-2,Y-1");
        gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", "MOVEST G-0,B-0,Y-0");
        gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", "MOVEST G-0,B-0,Y-0,P-0");
        cli.printBoards(gameHandler.getGame().getListOfPlayer());
    }
}
