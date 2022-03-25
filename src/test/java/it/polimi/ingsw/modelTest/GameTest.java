package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Player pl1 = new Player("carmine", ColorOfTower.WHITE);
    Game game = new Game(3,pl1);
    Player pl2 = new Player("chri", ColorOfTower.BLACK);
    Player pl3 = new Player("fede", ColorOfTower.GREY);

    @Test
    void addPlayer() {
    }

    @Test
    void getCurrentPlayer() {
    }

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
    @Test
    void moveMotherNature(){
        game.moveMotherNature(2);
        assertEquals(true,  game.getListOfArchipelagos().get(2).getIsMNPresent());

        game.moveMotherNature(14);
        assertEquals(true,  game.getListOfArchipelagos().get(4).getIsMNPresent());

    }

    @Test
    void calculateCurrentPlayer(){
        game.addPlayer(pl2);
        game.addPlayer(pl3);
        assertEquals(game.getCurrentPlayer(), pl1);

        game.calculateCurrentPlayer();
        assertEquals(game.getCurrentPlayer(), pl2);

    }
}