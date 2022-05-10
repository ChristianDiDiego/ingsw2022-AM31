package it.polimi.ingsw.modelTest.expertMode;

import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.expertMode.Character1;
import it.polimi.ingsw.model.expertMode.Character4;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests if the power of the character 1 works
 */
class Character1Test {
    Player player1 = new Player("player1", ColorOfTower.BLACK);
    Player player2 = new Player("player2", ColorOfTower.WHITE);
    GameHandler gameHandler = new GameHandler(player1, 2,false);
    Character1 character1 = new Character1(gameHandler.getGame());

    @Test
    void usePower() {
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
        gameHandler.getGame().getCurrentPlayer().addCoinsToWallet(20);
        character1.usePower(1);
        assertEquals(pl1, gameHandler.getController().getGame().getListOfArchipelagos().get(0).getOwner());
        assertEquals(7,pl1.getMyBoard().getTowersOnBoard().getNumberOfTowers());


    }
}