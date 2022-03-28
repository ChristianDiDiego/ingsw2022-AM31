package it.polimi.ingsw.model;

import it.polimi.ingsw.constants.Constants;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private List<Card> playerCards;

    public Deck(){
        this.playerCards = new ArrayList<>();
        int maxSteps = 1;
            for (int power = 1; power <= Constants.NUMBEROFCARDSINDECK; power++) {
                Card temp = new Card(power, maxSteps);
                playerCards.add(temp);
                power=power+1;
                temp = new Card(power, maxSteps);
                playerCards.add(temp);
                maxSteps++;
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
    public boolean useCard(Card cardToUse){
        return playerCards.remove(cardToUse);
    }
}
