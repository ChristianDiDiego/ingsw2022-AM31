package it.polimi.ingsw.model.expertMode;


import it.polimi.ingsw.model.Archipelago;
import it.polimi.ingsw.model.Game;

/**
 * image 4 in image folder ;
 * card 5 in list of cards ( in game tutorial)
 * Set a forbidden sign to an archipelago to do not allow the calculate of the influence on that island
 * when MN visit the archipelago, the forbidden flag go away
 * Can be used only 4 times in the same moment ( only 4 forbidden signs)
 */
public class Character3 extends Characters {
    private int forbiddenSigns;

    public Character3(Game game) {

        super(2, game);
        id = 3;
        descriptionOfPower = "Set a forbidden sign to an archipelago to do not allow the calculate of the influence on that island" +
                "Usage: CHARACTER 3 [IDARCHIPELAGO]";
        forbiddenSigns = 4;
    }

    /**
     * uses the forbiddenSign on the chosen archipelago
     *
     * @param idArchipelago where to put the forbidden
     */
    public boolean usePower(int idArchipelago) {
        if (payForUse()) {
            if (forbiddenSigns > 0) {
                forbiddenSigns--;
                for (Archipelago a : game.getListOfArchipelagos()) {
                    if (a.getIdArchipelago() == idArchipelago) {
                        a.setIsForbidden(true);
                        return true;
                    }
                }
                System.out.println("this archipelago doesn't exist");
            } else {
                System.out.println("there are not forbidden signs left");
            }
        }
        return false;

    }

}
