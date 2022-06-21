package it.polimi.ingsw.model;

import java.io.Serial;
import java.io.Serializable;

/**
 * A card of the game , which power can be between 1 and 10
 * is compared with other cards by that value
 */
public class Card implements Comparable<Card>, Serializable {
    @Serial
    private static final long serialVersionUID = 10L;
    private final int power;
    private final int maxSteps;

    public Card(int power, int maxSteps) {
        this.power = power;
        this.maxSteps = maxSteps;
    }

    public int getMaxSteps() {
        return maxSteps;
    }

    public int getPower() {
        return power;
    }

    /**
     * Compare another card with the actual card by the power value
     *
     * @param card to be compared
     * @return 0 if they have the same power
     * 1 if the actual card is greater than the other
     * -1 if the actual card is minor than the other
     */
    @Override
    public int compareTo(Card card) {
        return Integer.compare(getPower(), card.getPower());
    }


}
