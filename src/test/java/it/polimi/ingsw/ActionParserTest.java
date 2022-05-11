package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.Cli;
import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Check if the actionParser properly parses the messagese of the user
 */
public class ActionParserTest {
    GameHandler gameHandler;

    @Test
    public void actionParserTest(){
        Cli cli = new Cli("127.0.0.1", 5000);
        Player pl1 = new Player("carmine", ColorOfTower.WHITE);
        gameHandler = new GameHandler(pl1, 3, false);
        //other players login
        Player pl2 = new Player("chri", ColorOfTower.BLACK);
        gameHandler.addNewPlayer(pl2);
        assertEquals(0, gameHandler.getIsStarted());
        Player pl3 = new Player("fede", ColorOfTower.GREY);
        gameHandler.addNewPlayer(pl3);
        assertFalse(gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", "CARD abcd"));
        gameHandler.getGame().nextPhase();
        System.out.println(gameHandler.getGame().getCurrentPlayer().getNickname());
        System.out.println(gameHandler.getGame().getPhase());
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.BLUE));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.PINK));

        int[] studentsToAdd = {1,1,1,1,1};
        gameHandler.getGame().getCurrentPlayer().getMyBoard().getEntrance().addStudent(studentsToAdd);
        assertNotEquals(0, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.BLUE));
        assertNotEquals(0, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.RED));
        assertNotEquals(0, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.GREEN));
        assertNotEquals(0, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertNotEquals(0, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.PINK));
        Assertions.assertFalse(gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", "MOVEST "));
        Assertions.assertFalse(gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", "MOVEST B-0R-0G-0Y-0"));
        Assertions.assertFalse(gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", "MOVEST B0,R-0,G-0,Y-0"));
        Assertions.assertFalse(gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", "MOVEST B-,R-0,G-0,Y-0"));
        Assertions.assertFalse(gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", "MOVEST B-A,R-0,G-0,Y-0"));

        assertTrue(gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", "MOVEST B-0,R-0,G-0,Y-0"));

        // gameHandler.getController().getTurnController().getActionController().checkActionMoveStudent(recognisePlayer("carmine"), colorToMove, destinations );
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.BLUE));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.PINK));

        Assertions.assertFalse(gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", "MOVEMN A"));
        Assertions.assertFalse(gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", "CLOUD A"));
        Assertions.assertFalse(gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", "CHARACTER A"));
        Assertions.assertFalse(gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", "MOVETN A"));
        Assertions.assertFalse(gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("felpato", "MOVEMN 2"));



        /*assertEquals(0, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.BLUE));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.PINK));

        // cli.printBoards(gameHandler.getGame().getListOfPlayer());
 */
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