package it.polimi.ingsw.model.expertMode;

import it.polimi.ingsw.model.Game;

/**
 * image 7 in image folder ;
 * card 8 in list of cards ( in game tutorial)
 * When you calculate the influence, the player who play this card has 2 additional points
 */
public class Character4 extends Characters{

    public Character4(Game game) {
        super(2, game);
    }

    @Override
    public void usePower(int value) {

    }

}
