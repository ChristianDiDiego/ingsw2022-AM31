package it.polimi.ingsw.model.expertMode;


import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Phase;

/**
 * image 3 in image folder ;
 * card 4 in list of cards ( in game tutorial)
 * Allow to move MN of 2 steps more than the used card
 */
public class Character2 extends Characters{

    public Character2(Game game) {
        super(1, game);
        descriptionOfPower = "Allow to move MN of 2 steps more than the used card";
        bonus = 2;
    }

    /**
     * sets usedCharacter in player
     */
    public void usePower() {
        if(payForUse()){
            if(game.getPhase() == Phase.MOVE_STUDENTS){
                game.getCurrentPlayer().setUsedCharacter(this);
            }
        }
    }

    public int getBonus(){
        return bonus;
    }

}
