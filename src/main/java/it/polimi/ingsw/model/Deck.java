package it.polimi.ingsw.model;

import java.util.List;

public class Deck {
    private List<Card> playerCards;

    public Deck(){
        for(int i=0; i<10; i++){
            Card temp = new Card(i+1,9 ); //to add: max number of steps of each card
            playerCards.add(temp);
        }

    }

    public List<Card> getLeftCards() {
        return playerCards;
    }

    public Card useCard(Card cardToUse){
        playerCards.remove(cardToUse);
        return cardToUse;
    }
}
