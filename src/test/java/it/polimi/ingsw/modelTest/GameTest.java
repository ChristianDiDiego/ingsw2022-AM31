package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Player pl1 = new Player("carmine", ColorOfTower.WHITE);
    Game game = new Game(3,pl1);
    Player pl2 = new Player("chri", ColorOfTower.BLACK);
    Player pl3 = new Player("fede", ColorOfTower.GREY);

    /**
     * Test that the players order is properly calculated
     */
    @Test
    void findPlayerOrder() {
        pl1.chooseCardToUse(pl1.getMyDeck().getLeftCards().get(9));
        pl2.chooseCardToUse(pl2.getMyDeck().getLeftCards().get(1));
        pl3.chooseCardToUse(pl3.getMyDeck().getLeftCards().get(5));
        List<Player> lista = Arrays.asList(pl2, pl3, pl1);

        game.addPlayer(pl2);
        game.addPlayer(pl3);
        game.findPlayerOrder();
        assertEquals( lista, game.getOrderOfPlayers());

    }

    /**
     * Test that MN is moved properly
     */
    @Test
    void moveMotherNature(){
        game.moveMotherNature(2);
        assertEquals(true,  game.getListOfArchipelagos().get(2).getIsMNPresent());

        game.moveMotherNature(14);
        assertEquals(true,  game.getListOfArchipelagos().get(4).getIsMNPresent());

    }

    /**
     * Check that the current player is properly identified
     */
    @Test
    void calculateCurrentPlayer(){
        game.addPlayer(pl2);
        game.addPlayer(pl3);
        assertEquals(game.getCurrentPlayer(), pl1);

        game.calculateNextPlayerAction();
        assertEquals(game.getCurrentPlayer(), pl2);

    }

    /**
     * Tests if the game has correctly been created
     */
    @Test
    void gameConstructorTest() {
        int[] testArray = {0,0,0,0,0};
        for(Archipelago a : game.getListOfArchipelagos()) {
            for(Island i : a.getBelongingIslands()) {
                if(a.getIdArchipelago() != 0 && a.getIdArchipelago() != 5) {
                    int sum = 0;
                    for(int s = 0; s < Constants.NUMBEROFKINGDOMS; s++) {
                        sum += i.getStudentsByColor(StudsAndProfsColor.values()[s]);
                        if(i.getStudentsByColor(StudsAndProfsColor.values()[s]) == 1) {
                            testArray[s]++;
                        }
                    }
                    assertEquals(1, sum);
                }
            }
        }
        for(int s = 0; s < Constants.NUMBEROFKINGDOMS; s++) {
            assertEquals(2, testArray[s]);
        }
    }
}