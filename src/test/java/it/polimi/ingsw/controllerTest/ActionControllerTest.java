package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.client.cli.Cli;
import it.polimi.ingsw.controller.ActionController;
import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.expertMode.Character1;
import it.polimi.ingsw.utilities.ListOfBoards;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActionControllerTest {
    private ActionController actionController;
    GameHandler gameHandler;
    @Test
    void calculateInfluence() {
        //Case 2 players, oldOwner = null
        Player pl1 = new Player("leo", ColorOfTower.WHITE);
        pl1.setTeam(0);
        Player pl2 = new Player("Lisa", ColorOfTower.BLACK);
        pl2.setTeam(1);
        GameHandler gameHandler = new GameHandler(pl1, 2, false);
        gameHandler.addNewPlayer(pl2);
        pl1.getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.YELLOW);
        pl2.getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.RED);
        assertEquals(8,pl1.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        assertEquals(8,pl2.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.YELLOW);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.YELLOW);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.RED);
        gameHandler.getController().getTurnController().getActionController().calculateInfluence();
        assertEquals(pl1, gameHandler.getController().getGame().getListOfArchipelagos().get(0).getOwner());
        assertEquals(7,pl1.getMyBoard().getTowersOnBoard().getNumberOfTowers());

        //Case 4 players, oldOwner = null
        pl1 = new Player("Bianca", ColorOfTower.WHITE);
        pl1.setTeam(0);
        pl2 = new Player("Bianca2", null);
        pl2.setTeam(0);
        Player pl3 = new Player("Nera", ColorOfTower.BLACK);
        pl3.setTeam(1);
        Player pl4 = new Player("Nera2", null);
        pl4.setTeam(1);
        gameHandler = new GameHandler(pl1, 4, false);
        gameHandler.addNewPlayer(pl2);
        gameHandler.addNewPlayer(pl3);
        gameHandler.addNewPlayer(pl4);
        pl1.getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.BLUE);
        pl1.getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.YELLOW);
        pl2.getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.GREEN);
        pl3.getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.RED);
        pl4.getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.PINK);
        assertEquals(8,pl1.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        assertEquals(0,pl2.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        assertNull(pl2.getColorOfTowers());
        assertEquals(8,pl3.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        assertEquals(0,pl4.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        assertNull(pl4.getColorOfTowers());
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.YELLOW);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.YELLOW);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.RED);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.RED);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.GREEN);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.PINK);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.BLUE);
        gameHandler.getController().getTurnController().getActionController().calculateInfluence();

        assertEquals(pl1.getTeam(), gameHandler.getController().getGame().getListOfArchipelagos().get(0).getOwner().getTeam());
        assertEquals(7,pl1.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        assertEquals(0,pl2.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        assertEquals(8,pl3.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        assertEquals(0,pl4.getMyBoard().getTowersOnBoard().getNumberOfTowers());
    }

    @Test
    void checkUnification() {
        Player pl1 = new Player("carmine", ColorOfTower.WHITE);
        gameHandler = new GameHandler(pl1, 3,false);
        //other players login
        Player pl2 = new Player("fede", ColorOfTower.BLACK);
        gameHandler.addNewPlayer(pl2);
        gameHandler.getGame().getListOfArchipelagos().get(1).changeOwner(pl1);
        gameHandler.getGame().getListOfArchipelagos().get(2).changeOwner(pl1);
        gameHandler.getGame().moveMotherNature(2);
        assertEquals(true,gameHandler.getGame().getListOfArchipelagos().get(2).getIsMNPresent());
        gameHandler.getController().getTurnController().getActionController().checkUnification(gameHandler.getGame().getListOfArchipelagos().get(2));
        assertEquals(11,gameHandler.getGame().getListOfArchipelagos().size());
        assertEquals(true,gameHandler.getGame().getListOfArchipelagos().get(1).getIsMNPresent());
        gameHandler.getGame().moveMotherNature(10);
        assertEquals(true,gameHandler.getGame().getListOfArchipelagos().get(0).getIsMNPresent());
        gameHandler.getGame().getListOfArchipelagos().get(0).changeOwner(pl1);
        gameHandler.getController().getTurnController().getActionController().checkUnification(gameHandler.getGame().getListOfArchipelagos().get(0));
        assertEquals(10,gameHandler.getGame().getListOfArchipelagos().size());
        assertEquals(true,gameHandler.getGame().getListOfArchipelagos().get(0).getIsMNPresent());

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

        List<Board> boards = new ArrayList<>();
        for(Player p : gameHandler.getGame().getListOfPlayer()) {
            boards.add(p.getMyBoard());
        }
        cli.printBoard(boards);

    }

    @Test
    public void checkColorsTest(){
        Player pl1 = new Player("leo", ColorOfTower.WHITE);
        gameHandler = new GameHandler(pl1, 2,false);
        int[] students = {1,1,2,1,2};
        pl1.getMyBoard().getEntrance().addStudent(students);
        assertEquals(1, pl1.getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(1, pl1.getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(2, pl1.getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(1, pl1.getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.PINK));
        assertEquals(2, pl1.getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.BLUE));
        StudsAndProfsColor[] colors = {StudsAndProfsColor.RED,StudsAndProfsColor.RED,StudsAndProfsColor.RED};
        assertFalse(gameHandler.getController().getTurnController().getActionController().checkColors(pl1, colors));
        StudsAndProfsColor[] colors2 = {StudsAndProfsColor.BLUE,StudsAndProfsColor.BLUE,StudsAndProfsColor.YELLOW};
        assertTrue(gameHandler.getController().getTurnController().getActionController().checkColors(pl1, colors2));
    }

    @Test
    public void checkActionCharacter(){
    //Character1:
        Player pl1 = new Player("leo", ColorOfTower.WHITE);
        pl1.setTeam(0);
        Player pl2 = new Player("Lisa", ColorOfTower.BLACK);
        pl2.setTeam(1);
        GameHandler gameHandler = new GameHandler(pl1, 2, true);
        gameHandler.addNewPlayer(pl2);
        pl1.getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.YELLOW);
        pl2.getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.RED);
        assertEquals(8,pl1.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        assertEquals(8,pl2.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.YELLOW);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.YELLOW);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.RED);
        Character1 character1 = new Character1(gameHandler.getGame());
        gameHandler.getGame().setCharacterPlayable(character1);
        gameHandler.getGame().getCurrentPlayer().addCoinsToWallet(20);
        assertFalse(gameHandler.getController().getTurnController().getActionController().checkActionCharacter(gameHandler.getGame().getCurrentPlayer(), 1, null));
        gameHandler.getController().getTurnController().getActionController().checkActionCharacter(gameHandler.getGame().getCurrentPlayer(), 1, "1");
        assertEquals(pl1, gameHandler.getController().getGame().getListOfArchipelagos().get(0).getOwner());
        assertEquals(7,pl1.getMyBoard().getTowersOnBoard().getNumberOfTowers());



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