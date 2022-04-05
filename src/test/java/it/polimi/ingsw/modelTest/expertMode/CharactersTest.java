package it.polimi.ingsw.modelTest.expertMode;

import it.polimi.ingsw.controller.ActionController;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.controller.TurnController;
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

class CharactersTest {
    Player player1 = new Player("player1", ColorOfTower.BLACK);
    Player player2 = new Player("player2", ColorOfTower.WHITE);
    Game game = new Game(2, player1);
    Character3 character3 = new Character3(game);
    Character7 character7 = new Character7(game);
    Character5 character5 = new Character5(game);
    Character8 character8 = new Character8(game);
    GameHandler gameHandler = new GameHandler(player1, 2);

    /**
     * check if correctly removes coins from player's wallet
     * and if correctly puts character in player's usedCharacter
     *
     */
    @Test
    void payForUse() {
        game.addPlayer(player2);
        character5.usePower();
        assertEquals(0, player1.getUsedCharacter());
        player1.addCoinsToWallet(5);
        character5.usePower();
        assertEquals(5, player1.getUsedCharacter());
    }

    /**
     * check is description of powers is correclty returned
     */
    @Test
    void getDescriptionOfPower() {
        String string = "When you calculate the influence, the player who play this card has 2 additional points";
        assertEquals(string, character5.getDescriptionOfPower());
    }

    /**
     * check if the parameter alreadyUsed of character becomes true after the first use
     */
    @Test
    void getAlreadyUsed() {
        game.addPlayer(player2);
        player1.addCoinsToWallet(5);
        character5.usePower();
        assertEquals(true, character5.getAlreadyUsed());
        character5.usePower();
        assertEquals(0, player1.getWallet());
    }

    /**
     * check if usePower correclty sets the forbidden parameter in chosen archipelago
     * and if players can use this power only four times
     */
    @Test
    void testCharacter3(){
        game.addPlayer(player2);
        player1.addCoinsToWallet(20);
        assertEquals(false,game.getListOfArchipelagos().get(2).getIsForbidden());
        character3.usePower(2);
        assertEquals(true, game.getListOfArchipelagos().get(2).getIsForbidden());
        character3.usePower(3);
        character3.usePower(1);
        character3.usePower(4);
        character3.usePower(5);
        assertEquals(false, game.getListOfArchipelagos().get(5).getIsForbidden());
        assertEquals(null, game.getListOfArchipelagos().get(5).getOwner());


    }

    /**
     * check if 2 entrance's students have correctly been switched with 2 coming from dining room
     */
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

    /**
     * check if the professor is correctly assigned even if the number of players' students are the same
     */
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