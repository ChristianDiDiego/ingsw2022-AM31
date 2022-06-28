package it.polimi.ingsw.model.expertMode;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Phase;

/**
 * image 3 in image folder ;
 * card 4 in list of cards ( in game tutorial)
 * Allow to move MN of 2 steps more than the used card
 */
public class Character2 extends Characters {

    public Character2(Game game) {
        super(1, game);
        id = 2;
        descriptionOfPower = "Allow to move MN of 2 steps more than the used card";
        bonusSteps = 2;
    }

    /**
     * sets usedCharacter in player
     */
    public boolean usePower() {
        if (payForUse()) {
            if (game.getPhase() == Phase.MOVE_STUDENTS || game.getPhase() == Phase.MOVE_MN) {
                game.getCurrentPlayer().setUsedCharacter(this);
            } else {
                System.out.println("you can't use this power anymore");
            }
            return true;
        } else {
            return false;
        }
    }
}