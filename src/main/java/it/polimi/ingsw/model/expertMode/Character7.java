package it.polimi.ingsw.model.expertMode;

import it.polimi.ingsw.model.Game;

/**
 * image 9 in image folder ;
 * card 10 in list of cards ( in game tutorial)
 * The player can switch max 2 students between entrance and dining room
 */
public class Character7 extends Characters{

    public Character7(Game game) {
        super(1, game);
        this.descriptionOfPower = "The player can switch max 2 students between entrance and dining room";
    }

    @Override
    public void usePower(int value) {

    }

    @Override
    public int getPrice() {
        return 0;
    }
}
