package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.Cli;
import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionParserTest {
    GameHandler gameHandler;

    @Test
    public void actionParserTest(){
        Cli cli = new Cli("127.0.0.1", 5000);
        Player pl1 = new Player("carmine", ColorOfTower.WHITE);
        gameHandler = new GameHandler(pl1, 3, false);
        //other players login
        gameHandler.addNewPlayer("chri", ColorOfTower.BLACK);
        assertEquals(0, gameHandler.getIsStarted());
        //gameHandler.addNewPlayer("fede", ColorOfTower.GREY);
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
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.BLUE));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.PINK));

        gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer("carmine", "MOVEST B-0,R-0,G-0,Y-0");

       // gameHandler.getController().getTurnController().getActionController().checkActionMoveStudent(recognisePlayer("carmine"), colorToMove, destinations );
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.BLUE));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.PINK));

        assertEquals(0, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.BLUE));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.PINK));

        cli.printBoards(gameHandler.getGame().getListOfPlayer());


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
