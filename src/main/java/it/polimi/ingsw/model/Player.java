package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;

public class Player {
    private String nickname;
    private Deck myDeck;
    private ColorOfTower colorOfTowers;
    private Card lastUsedCard;
    private Board myBoard;
    private int coins;

    public Player(String nickname, ColorOfTower colorOfTowers){
        this.nickname = nickname;
        this.colorOfTowers = colorOfTowers;
        this.myBoard = new Board();
        myDeck = new Deck();
    }

    public String getNickname() {
        return nickname;
    }

    public Card getLastUsedCard() {
        return lastUsedCard;
    }

    /**
     * receives the card chosen by player and removes it from deck
     * @param toUse
     */
    public boolean chooseCardToUse(Card toUse) {
       if(myDeck.useCard(toUse)) {
           lastUsedCard = toUse;
           return true;
       }else{
           return false;
       }

    }

    public Board getMyBoard() {
        return myBoard;
    }

    public Deck getMyDeck() {
        return myDeck;
    }

    public ColorOfTower getColorOfTowers() {
        return colorOfTowers;
    }

    public int getCoins() {
        return coins;
    }

    public void addCoins(int coins) {
        this.coins += coins;
    }
    public void removeCoins(int coins) {
        this.coins -= coins;
    }
}
