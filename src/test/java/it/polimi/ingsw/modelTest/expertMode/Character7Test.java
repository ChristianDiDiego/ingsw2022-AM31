package it.polimi.ingsw.modelTest.expertMode;

import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.expertMode.Character3;
import it.polimi.ingsw.model.expertMode.Character5;
import it.polimi.ingsw.model.expertMode.Character7;
import it.polimi.ingsw.model.expertMode.Character8;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Character7Test {
    Player player1 = new Player("player1", ColorOfTower.BLACK);
    Player player2 = new Player("player2", ColorOfTower.WHITE);
    Game game = new Game(2, player1);
    Character7 character7 = new Character7(game);
    GameHandler gameHandler = new GameHandler(player1, 2);

    /**
     * check if 2 entrance's students have correctly been switched with 2 coming from dining room
     */
    @Test
    void usePower() {
        player1.addCoinsToWallet(5);
        int[] toAdd = {2,3,0,0,0};
        player1.getMyBoard().getEntrance().addStudent(toAdd);
        player1.getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.YELLOW);
        player1.getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.YELLOW);
        player1.getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.YELLOW);
        character7.usePower(StudsAndProfsColor.GREEN, StudsAndProfsColor.GREEN, StudsAndProfsColor.YELLOW, StudsAndProfsColor.YELLOW);
        assertEquals(1, player1.getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(2, player1.getMyBoard().getEntrance().getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(2, player1.getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(1, player1.getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.YELLOW));
    }
}