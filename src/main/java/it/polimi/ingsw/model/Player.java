package it.polimi.ingsw.model;

public class Player {
    private String nickname;
    private Deck myDeck;
    private ColorOfTower colorOfTowers;
    private Card lastUsedCard;
   // private Board myBoard;

    public Player(String nickname, ColorOfTower colorOfTowers){
        this.nickname = nickname;
        this.colorOfTowers = colorOfTowers;
       // myBoard = new Board();
        myDeck = new Deck();
    }

    public String getNickname() {
        return nickname;
    }

    public void chooseCardToUse(Card cardToBeUsed) {
       lastUsedCard= myDeck.useCard(cardToBeUsed);
    }

    public Card getLastUsedCard() {
        return lastUsedCard;
    }

   // public Board getMyBoard() {
   //     return myBoard;
   // }
}
