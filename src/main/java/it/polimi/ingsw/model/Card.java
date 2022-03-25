package it.polimi.ingsw.model;

public class Card implements Comparable<Card> {
    private final int power;
    private final int maxSteps;

    public Card(int power, int maxSteps) {
        this.power = power;
        this.maxSteps =maxSteps;
    }

    public int getMaxSteps() {
        return maxSteps;
    }

    public int getPower() {
        return power;
    }

    @Override
    public int compareTo(Card card){
       return Integer.compare(getPower(), card.getPower());
    }


}
