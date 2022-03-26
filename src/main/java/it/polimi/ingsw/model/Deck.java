package it.polimi.ingsw.model;

import it.polimi.ingsw.constants.Constants;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private List<Card> playerCards;

    public Deck(){
        this.playerCards = new ArrayList<>();

        for(int i = 0; i< Constants.NUMBEROFCARDSINDECK; i++){
            Card temp = new Card(i+1,9 ); //to add: max number of steps of each card
            playerCards.add(temp);
        }

    }

    /**
     * return the list of left cards in deck
     * @return
     */
    public List<Card> getLeftCards() {
        return playerCards;
    }

    /**
     * removes the chosen card from deck
     * @param cardToUse
     * @return
     */
    public Card useCard(Card cardToUse){
        playerCards.remove(cardToUse);
        return cardToUse;
    }
}
