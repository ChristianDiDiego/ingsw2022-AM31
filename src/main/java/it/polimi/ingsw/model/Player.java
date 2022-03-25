package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;

public class Player {
    private String nickname;
    private Deck myDeck;
    private ColorOfTower colorOfTowers;
    private Card lastUsedCard;
    private Board myBoard;

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
    public void chooseCardToUse(Card toUse) {
       lastUsedCard= myDeck.useCard(toUse);
    }



    public Board getMyBoard() {
        return myBoard;
    }
}
