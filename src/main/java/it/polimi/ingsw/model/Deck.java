package it.polimi.ingsw.model;

import it.polimi.ingsw.utilities.constants.Constants;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class Deck {
    private List<Card> playerCards;
    private PropertyChangeSupport support;

    public Deck(){
        this.playerCards = new ArrayList<>();
        int maxSteps = 1;
            for (int power = 1; power <= Constants.NUMBEROFCARDSINDECK; power++) {
                Card temp = new Card(power, maxSteps);
                playerCards.add(temp);
                power = power + 1;
                temp = new Card(power, maxSteps);
                playerCards.add(temp);
                maxSteps++;
            }
    }

    /**
     * @return the list of left cards in deck
     */
    public List<Card> getLeftCards() {
        return playerCards;
    }

    /**
     * removes the chosen card from deck
     * @param cardToUse card to be picked from the deck
     * @return true if the card has been correctly removed, false otherwise
     */
    public boolean useCard(Card cardToUse){
        for(Card c : this.getLeftCards()){
            if(c.getPower() == cardToUse.getPower()){
                playerCards.remove(c);
                support.firePropertyChange("usedCard", null, null);
                return true;
            }
        }
        return false;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

}
