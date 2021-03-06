package it.polimi.ingsw.modelTest.expertMode;

import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.expertMode.Character5;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests if the power of the character 5 works
 */
class Character5Test {
    Player player1 = new Player("player1", ColorOfTower.BLACK);
    Player player2 = new Player("player2", ColorOfTower.WHITE);
    Game game = new Game(2, player1,false);
    Character5 character5 = new Character5(game);
    GameHandler gameHandler = new GameHandler(player1, 2,false);

    /**
     * Even if the two players have the same influence,
     * player1 conquers the archipelago because he played character5
     */
    @Test
    void calculateInfluence() {
        player1.addCoinsToWallet(20);
        player1.getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.RED);
        player2.getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.BLUE);
        game.getListOfArchipelagos().get(1).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.RED);
        game.getListOfArchipelagos().get(1).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.BLUE);
        character5.usePower();
        player1.getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.RED);
        player2.getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.BLUE);
        game.moveMotherNature(1);
        character5.calculateInfluence();
        assertEquals(player1, game.getListOfArchipelagos().get(1).getOwner());
    }
}