package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.expertMode.Characters;

import java.io.Serializable;

/**
 * Represents a player of the game with his features and his board, deck, wallet (if in expert mode),..
 */
public class Player implements Serializable {
    private final String nickname;
    private final Deck myDeck;
    private final ColorOfTower colorOfTowers;
    private Card lastUsedCard;
    private final Board myBoard;
    private int wallet;
    private Characters usedCharacter;
    private int team;

    public Player(String nickname, ColorOfTower colorOfTowers) {
        this.nickname = nickname;
        this.colorOfTowers = colorOfTowers;
        this.myBoard = new Board(nickname, colorOfTowers);
        myDeck = new Deck();
        wallet = 0;
        usedCharacter = null;
    }

    public String getNickname() {
        return nickname;
    }

    public Card getLastUsedCard() {
        return lastUsedCard;
    }

    public void setLastUsedCard(Card card) {
        this.lastUsedCard = card;
    }

    /**
     * receives the card chosen by player and removes it from deck
     *
     * @param toUse card chosen
     */
    public void chooseCardToUse(Card toUse) {
        if (myDeck.useCard(toUse)) {
            lastUsedCard = toUse;
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

    public int getWallet() {
        return wallet;
    }

    public void addCoinsToWallet(int coins) {
        this.wallet += coins;
    }

    public void removeCoinsFromWallet(int coins) {
        this.wallet -= coins;
    }

    public Characters getUsedCharacter() {
        return usedCharacter;
    }

    public void setUsedCharacter(Characters usedCharacter) {
        this.usedCharacter = usedCharacter;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }
}