package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.client.cli.Cli;
import it.polimi.ingsw.controller.ActionController;
import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionControllerTest {
    private ActionController actionController;
    GameHandler gameHandler;
    @Test
    void calculateInfluence() {
        /*
        Player pl1 = new Player("carmine", ColorOfTower.WHITE);
        GameHandler gameHandler = new GameHandler(pl1, 3);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.RED);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.RED);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.BLUE);
        gameHandler.getController().getGame().;
        */
    }

    @Test
    void checkUnification() {
    }

    @Test
    void checkActionMoveStudent(){
        Cli cli = new Cli("127.0.0.1", 5000);
        Player pl1 = new Player("carmine", ColorOfTower.WHITE);
        gameHandler = new GameHandler(pl1, 3,false);
        //other players login
        Player pl2 = new Player("chri", ColorOfTower.BLACK);
        gameHandler.addNewPlayer(pl2);
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

        // cli.printBoards(gameHandler.getGame().getListOfPlayer());
        StudsAndProfsColor[] colorToMove = {StudsAndProfsColor.BLUE, StudsAndProfsColor.RED, StudsAndProfsColor.GREEN, StudsAndProfsColor.YELLOW};
        int[] destinations = {0,0,0,0};
        gameHandler.getController().getTurnController().getActionController().checkActionMoveStudent(recognisePlayer("carmine"), colorToMove, destinations );
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


        for(Player p : gameHandler.getGame().getListOfPlayer()){
            cli.printBoard(p.getMyBoard());
        }

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