package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    /**
     * Check if in the deck there are the correct cards
     */
    @Test
    void getLeftCardsTest() {
        Deck deck = new Deck();
        Card realCard = new Card(4,2);
        assertTrue(0 == realCard.compareTo(deck.getLeftCards().get(3)) );

        Card falseCard = new Card(3,1);
        assertFalse(0 == realCard.compareTo(deck.getLeftCards().get(2)) );

    }

    /**
     * Check if when a player uses cards they are removed from deck
     */
    @Test
    void useCardTest() {
        Deck deck = new Deck();
        Card test = new Card(2,1);
        deck.useCard(deck.getLeftCards().get(1));
        assertFalse(0 == test.compareTo(deck.getLeftCards().get(1)) );

    }
}