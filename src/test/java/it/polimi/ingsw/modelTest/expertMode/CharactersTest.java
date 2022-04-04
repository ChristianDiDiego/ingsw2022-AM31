package it.polimi.ingsw.modelTest.expertMode;

import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.expertMode.Character5;
import it.polimi.ingsw.model.expertMode.Character7;
import it.polimi.ingsw.model.expertMode.Character8;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharactersTest {
    Player player1 = new Player("player1", ColorOfTower.BLACK);
    Player player2 = new Player("player2", ColorOfTower.WHITE);
    Game game = new Game(2, player1);
    Character7 character7 = new Character7(game);
    Character5 character5 = new Character5(game);
    Character8 character8 = new Character8(game);

    @Test
    void payForUse() {
        game.addPlayer(player2);
        player1.addCoinsToWallet(5);
        player2.addCoinsToWallet(5);
        character5.usePower();
        assertEquals(5, player1.getUsedCharacter());
    }

    @Test
    void getDescriptionOfPower() {
        String string = "When you calculate the influence, the player who play this card has 2 additional points";
        assertEquals(string, character5.getDescriptionOfPower());
    }

    @Test
    void getAlreadyUsed() {
        game.addPlayer(player2);
        player1.addCoinsToWallet(5);
        character5.usePower();
        assertEquals(true, character5.getAlreadyUsed());
        character5.usePower();
        assertEquals(0, player1.getWallet());
    }

    @Test
    void testCharacter7() {
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

    @Test
    void testCharacter8() {
        game.addPlayer(player2);
        player1.getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.RED);
        player2.getMyBoard().getDiningRoom().addStudent(StudsAndProfsColor.RED);
        player1.addCoinsToWallet(5);
        character8.usePower();
        character8.assignProfessor(StudsAndProfsColor.RED);
        assertEquals(true, player1.getMyBoard().getProfessorsTable().getHasProf(StudsAndProfsColor.RED));
    }
}