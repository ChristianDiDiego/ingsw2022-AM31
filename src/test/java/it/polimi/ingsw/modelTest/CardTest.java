package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Deck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void getMaxSteps() {
        Deck deck = new Deck();
        List<Card> cards = deck.getLeftCards();

        for(Card e : cards){
            assertTrue(e.getMaxSteps()>=1 && e.getMaxSteps()<=5);
        }

    }

    @Test
    void getPower() {
        Deck deck = new Deck();
        List<Card> cards = deck.getLeftCards();

        for(Card e : cards){
            assertTrue(e.getPower()>=1 && e.getPower()<=10);
        }
    }

    @Test
    void compareTo() {
        Deck deck = new Deck();
        List<Card> cards = deck.getLeftCards();
        Card usedToTest = new Card(3,2);
        assertEquals(0,usedToTest.compareTo(cards.get(2)));
    }
}