package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void getNickname() {
        Player player = new Player("PAOLO", ColorOfTower.GREY);
        assertEquals("PAOLO", player.getNickname());
    }

    @Test
    void getLastUsedCardAndChooseCardTuUse() {
        Player player = new Player("PAOLO", ColorOfTower.GREY);
        assertEquals(null, player.getLastUsedCard());

        Card card = new Card(3,2);
        player.chooseCardToUse(card);
        assertEquals(card, player.getLastUsedCard());

    }
}