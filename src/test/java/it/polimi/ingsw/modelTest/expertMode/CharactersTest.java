package it.polimi.ingsw.modelTest.expertMode;

import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.expertMode.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests if: the player correctly pay in order to use the card
 * show the description of a card
 * get if a card has been already used
 */
class CharactersTest {
    Player player1 = new Player("player1", ColorOfTower.BLACK);
    Player player2 = new Player("player2", ColorOfTower.WHITE);
    Game game = new Game(2, player1,false);
    Character2 character2 = new Character2(game);
    Character3 character3 = new Character3(game);
    Character7 character7 = new Character7(game);
    Character5 character5 = new Character5(game);
    Character8 character8 = new Character8(game);
    GameHandler gameHandler = new GameHandler(player1, 2,false);

    /**
     * check if correctly removes coins from player's wallet
     * and if correctly puts character in player's usedCharacter
     *
     */
    @Test
    void payForUse() {
        game.addPlayer(player2);
        character5.usePower();
        assertEquals(null, player1.getUsedCharacter());
        assertFalse(character5.usePower());
        player1.addCoinsToWallet(5);
        character5.usePower();
        assertEquals(5, player1.getUsedCharacter().getId());
    }

    /**
     * check is description of powers is correclty returned
     */
    @Test
    void getDescriptionOfPower() {
        String string = "When you calculate the influence, the player who play this card has 2 additional points ";
        assertEquals(string, character5.getDescriptionOfPower());
    }

    /**
     * check if the parameter alreadyUsed of character becomes true after the first use
     */
    @Test
    void getAlreadyUsedTest() {
        game.addPlayer(player2);
        assertFalse(character5.usePower());
        player1.addCoinsToWallet(5);
        character5.usePower();
        assertEquals(true, character5.getAlreadyUsed());
        character5.usePower();
        assertEquals(0, player1.getWallet());
    }

    @Test
    void getPrice(){
        assertEquals(2,character5.getPrice());
    }

    @Test
    void getBonusStepsTest(){
        assertEquals(2, character2.getBonusSteps());
        assertEquals(0,character5.getBonusSteps());
    }

    @Test
    void getBonusInfluence(){
        assertEquals(0, character2.getBonusInfluence());
        assertEquals(2,character5.getBonusInfluence());
    }

    @Test
    void getIdTest() {
        assertEquals(2,character2.getId());
    }

}