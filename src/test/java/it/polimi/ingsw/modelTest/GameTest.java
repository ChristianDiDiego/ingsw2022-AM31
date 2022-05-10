package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.utilities.constants.Constants;
import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Check if the methods of Game class work
 */
class GameTest {

    Player pl1 = new Player("carmine", ColorOfTower.WHITE);
    Game game = new Game(3,pl1,false);
    Player pl2 = new Player("chri", ColorOfTower.BLACK);
    Player pl3 = new Player("fede", ColorOfTower.GREY);

    /**
     * Test that the players order is properly calculated
     */
    @Test
    void findPlayerOrder() {
        game.addPlayer(pl2);
        game.addPlayer(pl3);
        List<Player> lista1 = Arrays.asList(pl1, pl2, pl3);
        game.findPlayerOrder();
        //First we test if the player have not played the card yet if
        //the findplayerorder preserve the order of null objects
        assertEquals(lista1, game.getOrderOfPlayers());

        pl1.chooseCardToUse(pl1.getMyDeck().getLeftCards().get(9));
        pl2.chooseCardToUse(pl2.getMyDeck().getLeftCards().get(1));
        pl3.chooseCardToUse(pl3.getMyDeck().getLeftCards().get(5));
        List<Player> lista2 = Arrays.asList(pl2, pl3, pl1);
        game.findPlayerOrder();
        assertEquals( lista2, game.getOrderOfPlayers());

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
        game.unifyArchipelagos(game.getListOfArchipelagos().get(2),game.getListOfArchipelagos().get(1) );
        assertEquals(11, game.getListOfArchipelagos().size());
        game.moveMotherNature(9);
        for(Archipelago a: game.getListOfArchipelagos()){
            if(a.getIsMNPresent()){
                System.out.println("MN is in arc with index " +game.getListOfArchipelagos().indexOf(a) + " and id " + a.getIdArchipelago());
            }
        }

        assertEquals(true,  game.getListOfArchipelagos().get(1).getIsMNPresent());

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
                if(a.getIdArchipelago() != Constants.IDSTARTINGARCMN && a.getIdArchipelago() != Constants.IDSTARTINGOPPOSITEARC) {
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

    /**
     * Test if the game in expert mode is properly created
     */
    @Test
    public void expertModeTest(){
        Game game = new Game(3,pl1,true);

        assertEquals(20, game.getBank());
        assertEquals(3, game.getCharactersPlayable().length);
        game.getCoinFromBank(2);
        assertEquals(18, game.getBank());
    }

    @Test
    public void getArchipelagoByIdTest(){
        assertEquals(game.getListOfArchipelagos().get(1), game.getArchipelagoById(2));
    }
}