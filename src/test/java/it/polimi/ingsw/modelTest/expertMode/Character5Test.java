package it.polimi.ingsw.modelTest.expertMode;

import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.expertMode.Character5;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Character5Test {
    Player player1 = new Player("player1", ColorOfTower.BLACK);
    Player player2 = new Player("player2", ColorOfTower.WHITE);
    Game game = new Game(2, player1);
    Character5 character5 = new Character5(game);
    GameHandler gameHandler = new GameHandler(player1, 2);

    /**
     * Even if the two players have the same number of blue students,
     * player2 conquers the archipelago because he played character5
     */
    @Test
    void calculateInfluence() {
        player1.addCoinsToWallet(20);
        player1.getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.BLUE);
        player2.getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.BLUE);
        character5.usePower();
        game.getCurrentPlayer().getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.BLUE);
        game.moveMotherNature(1);
        character5.calculateInfluence();
        assertEquals(player1, game.getListOfArchipelagos().get(1).getOwner());
    }
}