package it.polimi.ingsw.model.expertMode;


import it.polimi.ingsw.model.Game;

/**
 * image 4 in image folder ;
 * card 5 in list of cards ( in game tutorial)
 * Set a forbidden sign to an archipelago to do not allow the calculate of the influence on that island
 * when MN visit the archipelago, the forbidden flag go away
 * Can be usedonly 4 times in the same moment ( only 4 forbidden signs)
 */
public class Character2 extends Characters{

    public Character2(Game game) {
        super(2, game);
    }

    @Override
    public void usePower() {

    }

}
