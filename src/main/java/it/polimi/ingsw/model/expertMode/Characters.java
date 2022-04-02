package it.polimi.ingsw.model.expertMode;

import it.polimi.ingsw.model.Game;

/**
 * Every character has the function usePower which is called by the actionController
 * ActionController check if a player played a character when a player use a card
 * TODO:add charactersUsed to the player ; add game parameter to all characters
 * turnController set characterUsed of the player to null at the beginning of the turn
 */
public abstract class Characters {
    private Game game;
    private  int price;
    private boolean alreadyUsed;

    public Characters(int price, Game game){
        alreadyUsed = false;
        this.price = price;
        this.game = game;
    }

    public abstract void usePower();

    public int getPrice(){
        return this.price;
    }

    public  boolean getAlreadyUsed() {
        return this.alreadyUsed;
    }
}
