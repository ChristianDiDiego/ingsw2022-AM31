package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests if two cards are properly compared;
 * Check if the power of a card is properly read
 * Check if the maxSteps of a card is properly read
 */
class CardTest {
    /**
     * Check if the maxSteps of a card is properly read
     */
    @Test
    void getMaxSteps() {
        Deck deck = new Deck();
        List<Card> cards = deck.getLeftCards();

        for(Card e : cards){
            assertTrue(e.getMaxSteps()>=1 && e.getMaxSteps()<=5);
        }

    }

    /**
     * Check if the power of a card is properly read
     */
    @Test
    void getPower() {
        Deck deck = new Deck();
        List<Card> cards = deck.getLeftCards();

        for(Card e : cards){
            assertTrue(e.getPower()>=1 && e.getPower()<=10);
        }
    }

    /**
     * Check that two cards are properly compared
     */
    @Test
    void compareTo() {
        Deck deck = new Deck();
        List<Card> cards = deck.getLeftCards();
        Card usedToTest = new Card(3,2);
        assertEquals(0,usedToTest.compareTo(cards.get(2)));
    }
}