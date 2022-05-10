package it.polimi.ingsw.modelTest.expertMode;

import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.expertMode.Character4;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests if the power of the character 4 works
 */
class Character4Test {
    Player player1 = new Player("player1", ColorOfTower.BLACK);
    Player player2 = new Player("player2", ColorOfTower.WHITE);
    GameHandler gameHandler = new GameHandler(player1, 2,false);
    Character4 character4 = new Character4(gameHandler.getGame());

    /**
     * check if character is correctly assigned to currentPlayer
     */
    @Test
    void usePower(){
        gameHandler.getGame().addPlayer(player2);
        character4.usePower();
        assertNotEquals(character4, gameHandler.getGame().getCurrentPlayer().getUsedCharacter());
        player1.addCoinsToWallet(20);
        character4.usePower();
        assertEquals(character4, gameHandler.getGame().getCurrentPlayer().getUsedCharacter());
    }

    /**
     * check if the influence is calculated without counting towers
    */
    @Test
    void calculateInfluence(){
//Case 2 players, oldOwner = null
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
        gameHandler.getController().getTurnController().getActionController().calculateInfluence();
        assertEquals(pl1, gameHandler.getController().getGame().getListOfArchipelagos().get(0).getOwner());
        assertEquals(7,pl1.getMyBoard().getTowersOnBoard().getNumberOfTowers());

        pl1.getMyBoard().getProfessorsTable().removeProfessor(StudsAndProfsColor.YELLOW);
        Character4 character4 = new Character4(gameHandler.getController().getGame());
        gameHandler.getController().getGame().getCurrentPlayer().setUsedCharacter(character4);
        gameHandler.getController().getTurnController().getActionController().calculateInfluence();
        assertEquals(pl2, gameHandler.getController().getGame().getListOfArchipelagos().get(0).getOwner());

    }

}