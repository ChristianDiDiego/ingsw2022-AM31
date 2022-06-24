package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.client.cli.Cli;
import it.polimi.ingsw.controller.ActionController;
import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.expertMode.*;
import it.polimi.ingsw.utilities.Constants;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests calculateInfluence with 2 and 4 players,
 * unification of different archi.
 * correct movements of students
 * check that characters are correctly called
 */
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
        gameHandler = new GameHandler(pl1, 3,true);
        //other players login
        Player pl2 = new Player("chri", ColorOfTower.BLACK);
        gameHandler.addNewPlayer(pl2);
        assertEquals(0, gameHandler.getIsStarted());
        //gameHandler.addNewPlayer("fede", ColorOfTower.GREY);
        System.out.println(gameHandler.getGame().getCurrentPlayer().getNickname());
        System.out.println(gameHandler.getGame().getPhase());
        recognisePlayer("carmine").getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.PINK);
        recognisePlayer("carmine").getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.PINK);
        recognisePlayer("carmine").getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.PINK);
        recognisePlayer("carmine").getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.PINK);
        recognisePlayer("carmine").getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.PINK);
        recognisePlayer("carmine").getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.PINK);
        recognisePlayer("carmine").getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.PINK);
        recognisePlayer("carmine").getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.PINK);
        recognisePlayer("carmine").getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.PINK);
        recognisePlayer("carmine").getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.PINK);



        assertEquals(0, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.BLUE));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(Constants.MAXSTUDENTSINDINING, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.PINK));

        int[] studentsToAdd = {1,1,1,1,1};
        gameHandler.getGame().getCurrentPlayer().getMyBoard().getEntrance().addStudent(studentsToAdd);
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.BLUE));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.PINK));

        // cli.printBoards(gameHandler.getGame().getListOfPlayer());
        StudsAndProfsColor[] colorToMove = {StudsAndProfsColor.BLUE, StudsAndProfsColor.RED, StudsAndProfsColor.GREEN, StudsAndProfsColor.YELLOW};
        StudsAndProfsColor[] colorsForFalseStatement ={StudsAndProfsColor.BLUE, StudsAndProfsColor.RED, StudsAndProfsColor.GREEN, StudsAndProfsColor.GREEN};
        StudsAndProfsColor[] colorsForFalseFullDining ={StudsAndProfsColor.PINK, StudsAndProfsColor.RED, StudsAndProfsColor.GREEN, StudsAndProfsColor.GREEN};

        int[] destinations = {0,0,0,1};
        int[] destinationsForFalseStatement = {0,0,0,15};
        assertFalse(gameHandler.getController().getTurnController().getActionController().checkActionMoveStudent(recognisePlayer("carmine"), colorToMove, destinations ));
        gameHandler.getGame().nextPhase();
        gameHandler.getGame().nextPhase();
        assertFalse(gameHandler.getController().getTurnController().getActionController().checkActionMoveStudent(recognisePlayer("carmine"), colorsForFalseFullDining, destinations ));
        assertFalse(gameHandler.getController().getTurnController().getActionController().checkActionMoveStudent(recognisePlayer("carmine"), colorsForFalseStatement, destinations ));
        assertFalse(gameHandler.getController().getTurnController().getActionController().checkActionMoveStudent(recognisePlayer("carmine"), colorToMove, destinationsForFalseStatement ));

        gameHandler.getController().getTurnController().getActionController().checkActionMoveStudent(recognisePlayer("carmine"), colorToMove, destinations );
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.BLUE));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(Constants.MAXSTUDENTSINDINING, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.PINK));
        assertEquals(1, gameHandler.getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.BLUE));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.PINK));

        int[] studentsToAdd2 = {1,1,1,1,0};
        gameHandler.getGame().getCurrentPlayer().getMyBoard().getEntrance().addStudent(studentsToAdd2);
        int[] wrongDestinations = {0,13,0,1};
        gameHandler.getController().getTurnController().getActionController().checkActionMoveStudent(recognisePlayer("carmine"), colorToMove, wrongDestinations );
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.BLUE));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(Constants.MAXSTUDENTSINDINING, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.PINK));
        assertEquals(1, gameHandler.getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).getStudentsByColor(StudsAndProfsColor.YELLOW));

        int[] wrongDestinations2 = {0,-1,0,1};
        gameHandler.getController().getTurnController().getActionController().checkActionMoveStudent(recognisePlayer("carmine"), colorToMove, wrongDestinations2 );
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.BLUE));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(1, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(0, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(Constants.MAXSTUDENTSINDINING, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.PINK));
        assertEquals(1, gameHandler.getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).getStudentsByColor(StudsAndProfsColor.YELLOW));



        StudsAndProfsColor totry[] = {StudsAndProfsColor.BLUE};
        int[] d = {0};

        for(int i = 0; i<9 ; i++){
            recognisePlayer("carmine").getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.BLUE);
        }
        int[] add = {1,0,0,0,0};
        recognisePlayer("carmine").getMyBoard().getEntrance().addStudent(add);
        assertEquals(10, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.BLUE));
        gameHandler.getController().getTurnController().getActionController().checkActionMoveStudent(recognisePlayer("carmine"), totry, d );
        assertEquals(10, recognisePlayer("carmine").getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.BLUE));

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
        assertFalse(gameHandler.getController().getTurnController().getActionController().checkActionCharacter(gameHandler.getGame().getCurrentPlayer(), 9, null));
        assertFalse(gameHandler.getController().getTurnController().getActionController().checkActionCharacter(gameHandler.getGame().getListOfPlayer().get(1), 1, null));
        assertFalse(gameHandler.getController().getTurnController().getActionController().checkActionCharacter(gameHandler.getGame().getCurrentPlayer(), 1, null));
        gameHandler.getGame().setPhase(Phase.MOVE_MN);
        gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer(gameHandler.getGame().getCurrentPlayer().getNickname(), "CHARACTER 1 1");
        assertEquals(pl1, gameHandler.getController().getGame().getListOfArchipelagos().get(0).getOwner());
        assertEquals(7,pl1.getMyBoard().getTowersOnBoard().getNumberOfTowers());

    //Character2 Allow to move MN of 2 steps more than the used card
        pl1 = new Player("leo", ColorOfTower.WHITE);
        pl1.setTeam(0);
        pl2 = new Player("Lisa", ColorOfTower.BLACK);
        pl2.setTeam(1);
        gameHandler = new GameHandler(pl1, 2, true);
        gameHandler.addNewPlayer(pl2);
        Character2 character2 = new Character2(gameHandler.getGame());
        gameHandler.getGame().setCharacterPlayable(character2);
        gameHandler.getGame().getCurrentPlayer().addCoinsToWallet(20);
        gameHandler.getGame().getCurrentPlayer().setLastUsedCard(new Card(1,1));
        assertFalse(gameHandler.getController().getTurnController().getActionController().checkActionMoveMN(gameHandler.getGame().getCurrentPlayer(),1));
        gameHandler.getGame().nextPhase();
        gameHandler.getGame().nextPhase();
        //Try to do 2 steps (when max is 1) before playing character 2: fail
        assertFalse(gameHandler.getController().getTurnController().getActionController().checkActionMoveMN(gameHandler.getGame().getCurrentPlayer(),2));
        assertFalse(gameHandler.getController().getTurnController().getActionController().checkActionMoveMN(gameHandler.getGame().getListOfPlayer().get(1),1));
        gameHandler.getController().getTurnController().getActionController().checkActionCharacter(gameHandler.getGame().getCurrentPlayer(), 2, null);
        //Try to do 2 steps (when max is 1) after playing character 2: success
        assertTrue(gameHandler.getController().getTurnController().getActionController().checkActionMoveMN(gameHandler.getGame().getCurrentPlayer(),2));

        //Character 3: Set a forbidden sign to an archipelago to do not allow the calculate of the influence on that island
        //when MN visit the archipelago, the forbidden flag go away

        pl1 = new Player("leo", ColorOfTower.WHITE);
        pl1.setTeam(0);
        pl2 = new Player("Lisa", ColorOfTower.BLACK);
        pl2.setTeam(1);
        gameHandler = new GameHandler(pl1, 2, true);
        gameHandler.addNewPlayer(pl2);
        Character3 character3 = new Character3(gameHandler.getGame());
        gameHandler.getGame().setCharacterPlayable(character3);
        gameHandler.getGame().getCurrentPlayer().addCoinsToWallet(20);
        //get 2 and not 3 because the id of the arc starts from 1
        assertEquals(false,gameHandler.getGame().getListOfArchipelagos().get(2).getIsForbidden());

        assertFalse(gameHandler.getController().getTurnController().getActionController().checkActionCharacter(gameHandler.getGame().getCurrentPlayer(), 3, ""));
        //assertFalse(gameHandler.getController().getTurnController().getActionController().checkActionCharacter(gameHandler.getGame().getCurrentPlayer(), 3, "3"));

        gameHandler.getGame().getCurrentPlayer().addCoinsToWallet(20);
        gameHandler.getGame().setPhase(Phase.MOVE_MN);
        gameHandler.getController().getTurnController().getActionController().checkActionCharacter(gameHandler.getGame().getCurrentPlayer(), 3, "3");
        assertEquals(true, gameHandler.getGame().getListOfArchipelagos().get(2).getIsForbidden());
        gameHandler.getController().getTurnController().getActionController().checkActionCharacter(gameHandler.getGame().getCurrentPlayer(), 3, "3");
        gameHandler.getController().getTurnController().getActionController().checkActionCharacter(gameHandler.getGame().getCurrentPlayer(), 3, "1");
        gameHandler.getController().getTurnController().getActionController().checkActionCharacter(gameHandler.getGame().getCurrentPlayer(), 3, "4");
        gameHandler.getController().getTurnController().getActionController().checkActionCharacter(gameHandler.getGame().getCurrentPlayer(), 3, "5");

        assertEquals(false, gameHandler.getGame().getListOfArchipelagos().get(5).getIsForbidden());
        assertEquals(null, gameHandler.getGame().getListOfArchipelagos().get(5).getOwner());


        //Character 4: calculates the influence without counting towers
        System.out.println("test character 4: ");
        pl1 = new Player("leo", ColorOfTower.WHITE);
        pl1.setTeam(0);
        pl2 = new Player("Lisa", ColorOfTower.BLACK);
        pl2.setTeam(1);
        gameHandler = new GameHandler(pl1, 2, true);
        gameHandler.addNewPlayer(pl2);
        pl1.getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.YELLOW);
        pl2.getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.RED);
        assertEquals(8,pl1.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        assertEquals(8,pl2.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.YELLOW);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.YELLOW);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.RED);
        gameHandler.getGame().setPhase(Phase.MOVE_MN);
        Character4 character4 = new Character4(gameHandler.getGame());
        gameHandler.getGame().setCharacterPlayable(character4);
        gameHandler.getGame().getCurrentPlayer().addCoinsToWallet(20);
        assertEquals(true, gameHandler.getGame().getListOfArchipelagos().get(0).getIsMNPresent());
        gameHandler.getController().getTurnController().getActionController().calculateInfluence();
        assertEquals(pl1, gameHandler.getGame().getListOfArchipelagos().get(0).getOwner());
        assertEquals(7, pl1.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        //at this point player 1 is owner of archipelago 0

        //now I add 2 red students on arch 0, so that there are 3 red, 2 yellow and 1 tower
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.RED);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.RED);

        //pl1 uses character 4, so when the influence is calculated on island 0 the new owner must be pl2
        gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer(gameHandler.getGame().getCurrentPlayer().getNickname(), "CHARACTER 4");

        gameHandler.getGame().setCurrentPlayer(pl1);
        assertEquals(true, gameHandler.getGame().getListOfArchipelagos().get(0).getIsMNPresent());
        gameHandler.getController().getTurnController().getActionController().calculateInfluence();
        assertEquals(pl2, gameHandler.getGame().getListOfArchipelagos().get(0).getOwner());


        //Character 5: When you calculate the influence, the player who play this card has 2 additional points
        System.out.println("test character 5:");
        pl1 = new Player("leo", ColorOfTower.WHITE);
        pl1.setTeam(0);
        pl2 = new Player("Lisa", ColorOfTower.BLACK);
        pl2.setTeam(1);
        gameHandler = new GameHandler(pl1, 2, true);
        gameHandler.addNewPlayer(pl2);
        gameHandler.getGame().setPhase(Phase.MOVE_MN);
        pl1.getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.RED);
        pl2.getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.YELLOW);
        assertEquals(8,pl1.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        assertEquals(8,pl2.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.YELLOW);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.YELLOW);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.RED);
        Character5 character5 = new Character5(gameHandler.getGame());
        gameHandler.getGame().setCharacterPlayable(character5);
        gameHandler.getGame().getCurrentPlayer().addCoinsToWallet(20);
        gameHandler.getController().getTurnController().getActionController().calculateInfluence();
        assertEquals(pl2, gameHandler.getGame().getListOfArchipelagos().get(0).getOwner());
        assertEquals(7, pl2.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        //at this point player 2 is owner of archipelago 0

        //pl1 uses character 5, so he has 3 points when calculating influence
        gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer(gameHandler.getGame().getCurrentPlayer().getNickname(), "CHARACTER 5");

        //now pl1 has 4 points in influence, while pl2 has just 3
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.RED);

        gameHandler.getGame().setCurrentPlayer(pl1);
        gameHandler.getController().getTurnController().getActionController().calculateInfluence();
        assertEquals(pl1, gameHandler.getGame().getListOfArchipelagos().get(0).getOwner());

        //Character6: Choose a color of students that will not be counted for the influence
        System.out.println("test character 6:");
        pl1 = new Player("leo", ColorOfTower.WHITE);
        pl1.setTeam(0);
        pl2 = new Player("Lisa", ColorOfTower.BLACK);
        pl2.setTeam(1);
        gameHandler = new GameHandler(pl1, 2, true);
        gameHandler.addNewPlayer(pl2);
        gameHandler.getGame().setPhase(Phase.MOVE_MN);
        pl1.getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.RED);
        pl2.getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.YELLOW);
        assertEquals(8,pl1.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        assertEquals(8,pl2.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.YELLOW);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.YELLOW);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.RED);
        Character6 character6 = new Character6(gameHandler.getGame());
        gameHandler.getGame().setCharacterPlayable(character6);
        gameHandler.getGame().getCurrentPlayer().addCoinsToWallet(20);
        gameHandler.getController().getTurnController().getActionController().calculateInfluence();
        assertEquals(pl2, gameHandler.getGame().getListOfArchipelagos().get(0).getOwner());
        assertEquals(7, pl2.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        //at this point player 2 is owner of archipelago 0

        assertFalse(gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer(gameHandler.getGame().getCurrentPlayer().getNickname(), "CHARACTER 6 AAA"));

        gameHandler.getController().getTurnController().getActionController().getActionParser().actionSerializer(gameHandler.getGame().getCurrentPlayer().getNickname(), "CHARACTER 6 YELLOW");

        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.RED);
        //now the influence is: 2 yellow and 1 tower for pl2, 2 red for pl1
        //using character 6 it becomes: 1 tower for pl2, 2 red for pl1 --> new owner is pl1

        gameHandler.getController().getTurnController().getActionController().calculateInfluence();
        assertEquals(pl1, gameHandler.getGame().getListOfArchipelagos().get(0).getOwner());

    }

    @Test
    public void checkActionCharacter7(){
        //Character 7: the player can switch max 2 students between entrance and dining room
        Player player1 = new Player("A", ColorOfTower.WHITE);
        player1.setTeam(0);
        Player player2 = new Player("B", ColorOfTower.BLACK);
        player2.setTeam(1);
        gameHandler = new GameHandler(player1, 2, true);
        gameHandler.addNewPlayer(player2);

        gameHandler.getGame().setPhase(Phase.MOVE_MN);
        Character7 character7 = new Character7(gameHandler.getGame());
        gameHandler.getGame().setCharacterPlayable(character7);

        gameHandler.getGame().getCurrentPlayer().addCoinsToWallet(20);

        int[] addEntrance = {1,1,0,0,0};
        gameHandler.getGame().getCurrentPlayer().getMyBoard().getEntrance().addStudent(addEntrance);
        gameHandler.getGame().getCurrentPlayer().getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.PINK);
        gameHandler.getGame().getCurrentPlayer().getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.BLUE);

        assertNotEquals(0, gameHandler.getGame().getCurrentPlayer().getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.RED));
        assertNotEquals(0, gameHandler.getGame().getCurrentPlayer().getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.GREEN));
        assertNotEquals(0, gameHandler.getGame().getCurrentPlayer().getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.PINK));
        assertNotEquals(0, gameHandler.getGame().getCurrentPlayer().getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.BLUE));

        assertEquals(true, character7.checkStudent( StudsAndProfsColor.GREEN,StudsAndProfsColor.RED, StudsAndProfsColor.PINK, StudsAndProfsColor.BLUE));

        assertFalse(gameHandler.getController().getTurnController().getActionController().checkActionCharacter(gameHandler.getGame().getCurrentPlayer(),7,"GREENREDINKBLUE"));
        assertFalse(gameHandler.getController().getTurnController().getActionController().checkActionCharacter(gameHandler.getGame().getCurrentPlayer(),7,"GREEN,RED,PINK,aaaaa"));
        gameHandler.getController().getTurnController().getActionController().checkActionCharacter(gameHandler.getGame().getCurrentPlayer(),7,"GREEN,RED,PINK,BLUE");
        assertNotEquals(0, gameHandler.getGame().getCurrentPlayer().getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.PINK));
        assertNotEquals(0, gameHandler.getGame().getCurrentPlayer().getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.BLUE));
        assertNotEquals(0, gameHandler.getGame().getCurrentPlayer().getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.GREEN));
        assertNotEquals(0, gameHandler.getGame().getCurrentPlayer().getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.RED));
    }

    @Test
    public void checkActionCharacter8(){
        //Character 8: when this card is used, assignProfessor assign the professor to the player who use the card also if the number of students in the dining rooms of the two players is the same"
        Player player1 = new Player("A", ColorOfTower.WHITE);
        player1.setTeam(0);
        Player player2 = new Player("B", ColorOfTower.BLACK);
        player2.setTeam(1);
        gameHandler = new GameHandler(player1, 2, true);
        gameHandler.addNewPlayer(player2);
        gameHandler.getGame().setPhase(Phase.MOVE_MN);

        Character8 character8 = new Character8(gameHandler.getGame());
        gameHandler.getGame().setCharacterPlayable(character8);

        gameHandler.getGame().getCurrentPlayer().getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.RED);
        gameHandler.getGame().getCurrentPlayer().getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.RED);
        gameHandler.getGame().assignProfessor(StudsAndProfsColor.RED);
        assertEquals(true, gameHandler.getGame().getCurrentPlayer().getMyBoard().getProfessorsTable().getHasProf(StudsAndProfsColor.RED));

        gameHandler.getGame().setCurrentPlayer(player2);
        gameHandler.getGame().getCurrentPlayer().addCoinsToWallet(20);
        gameHandler.getController().getTurnController().getActionController().checkActionCharacter(gameHandler.getGame().getCurrentPlayer(), 8, "");
        gameHandler.getGame().getCurrentPlayer().getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.RED);
        gameHandler.getGame().getCurrentPlayer().getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.RED);

        gameHandler.getGame().assignProfessor(StudsAndProfsColor.RED);
        assertEquals(true, gameHandler.getGame().getCurrentPlayer().getMyBoard().getProfessorsTable().getHasProf(StudsAndProfsColor.RED));
    }

    @Test
    public void checkActionCloud(){
        Player pl1 = new Player("carmine", ColorOfTower.WHITE);
        gameHandler = new GameHandler(pl1, 2,false);
        //other players login
        Player pl2 = new Player("chri", ColorOfTower.BLACK);
        gameHandler.addNewPlayer(pl2);
        gameHandler.getGame().nextPhase();
        gameHandler.getGame().nextPhase();
        assertFalse(gameHandler.getController().getTurnController().getActionController().checkActionCloud(gameHandler.getGame().getCurrentPlayer(),0));
        gameHandler.getGame().nextPhase();
        gameHandler.getGame().getListOfClouds().get(0).changeStatus();
        assertFalse(gameHandler.getController().getTurnController().getActionController().checkActionCloud(gameHandler.getGame().getCurrentPlayer(),0));
        assertFalse(gameHandler.getController().getTurnController().getActionController().checkActionCloud(gameHandler.getGame().getCurrentPlayer(),3));
        assertFalse(gameHandler.getController().getTurnController().getActionController().checkActionCloud(gameHandler.getGame().getListOfPlayer().get(1),0));

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