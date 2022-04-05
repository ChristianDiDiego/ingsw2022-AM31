package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.Cli;
import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MatchTest {
    GameHandler gameHandler;

    @Test
    public void matchTest(){
        Cli cli = new Cli();
        //First player login
        cli.printLogo();
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
        cli.printBoards(gameHandler.getGame().getListOfPlayer());
        cli.printArchipelagos(gameHandler.getGame().getListOfArchipelagos());
        assertEquals(Phase.CARD_SELECTION, gameHandler.getGame().getPhase());
        cli.printMyDeck(gameHandler.getGame().getCurrentPlayer().getMyDeck());
        gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", "CARD 1");

        assertEquals("chri", gameHandler.getGame().getCurrentPlayer().getNickname());
        assertEquals(Phase.CARD_SELECTION, gameHandler.getGame().getPhase());
        //cli.printMyDeck(gameHandler.getGame().getCurrentPlayer().getMyDeck());
        gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("chri", "CARD 2");

        assertEquals("fede", gameHandler.getGame().getCurrentPlayer().getNickname());
        assertEquals(Phase.CARD_SELECTION, gameHandler.getGame().getPhase());
        //cli.printMyDeck(gameHandler.getGame().getCurrentPlayer().getMyDeck());
        gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("fede", "CARD 3");

        assertEquals(Phase.MOVE_STUDENTS, gameHandler.getGame().getPhase());
      //  cli.printMyDeck(gameHandler.getGame().getCurrentPlayer().getMyDeck());

        assertEquals("carmine", gameHandler.getGame().getCurrentPlayer().getNickname());
        cli.printBoards(gameHandler.getGame().getListOfPlayer());
        //start the game with current player setted as rules say
        String colorToAdd = "MOVEST ";
        int i = 0;
        for(StudsAndProfsColor color : StudsAndProfsColor.values()){
            if(i<4){
                if (gameHandler.getGame().getCurrentPlayer().getMyBoard().getEntrance().getStudentsByColor(color)>0){
                    if(i== 3){
                        colorToAdd = colorToAdd + color.toString().charAt(0) + "-0";
                    }else{
                        colorToAdd = colorToAdd + color.toString().charAt(0) + "-0,";
                    }
                    i++;
                }
            }

        }

        assertFalse(gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("chri", "MOVEST R-0,B-2,Y-1"));
        assertFalse(gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", "MOVEST G-0,B-0,Y-0"));
        assertTrue(gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", colorToAdd));
        cli.printBoards(gameHandler.getGame().getListOfPlayer());
        //Not working when island without owner
        assertEquals(Phase.MOVE_MN, gameHandler.getGame().getPhase());
        assertTrue(gameHandler.getGame().getListOfArchipelagos().get(0).getIsMNPresent());
        assertFalse(gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", "MOVEMN 3"));
        assertTrue(gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", "MOVEMN 1"));
        assertFalse(gameHandler.getGame().getListOfArchipelagos().get(0).getIsMNPresent());
        assertTrue(gameHandler.getGame().getListOfArchipelagos().get(1).getIsMNPresent());


        //gameHandler.getGame().nextPhase();
        assertEquals(Phase.CLOUD_SELECTION, gameHandler.getGame().getPhase());
        cli.printClouds(gameHandler.getGame().getListOfClouds());
        assertFalse(gameHandler.getGame().getListOfClouds().get(1).getIsTaken());
        assertTrue(gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", "CLOUD 1"));
        System.out.println(gameHandler.getGame().getListOfClouds().get(1).getIsTaken());
        assertTrue(gameHandler.getGame().getListOfClouds().get(1).getIsTaken());

        assertEquals(Phase.MOVE_STUDENTS, gameHandler.getGame().getPhase());
        assertEquals("chri", gameHandler.getGame().getCurrentPlayer().getNickname());

    }
}
