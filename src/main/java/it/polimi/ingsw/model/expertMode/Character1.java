package it.polimi.ingsw.model.expertMode;

import it.polimi.ingsw.utilities.constants.Constants;
import it.polimi.ingsw.model.*;

/**
 * image 2 in image folder ;
 * card 3 in list of cards ( in game tutorial)
 * Choose an island and calculate the influence as if MN is on that island
 */
public class Character1 extends Characters {


    public Character1(Game game) {
        super(3, game);
        id = 1;
        descriptionOfPower = "Choose an island and calculate the influence as if MN is on that island \n " +
                                "Usage: CHARACTER 1 [IDARCHIPELAGO]";
    }


    /**
     * calculates asynch influence in the chosen archipelago
     * @param idArchipelago
     */
    public boolean usePower(int idArchipelago) {
        if (payForUse()) {
            game.getCurrentPlayer().setUsedCharacter(this);
            Player newOwner;
            Player oldOwner;
            int maxInfluence = 0;
            for (Archipelago a : game.getListOfArchipelagos()) {
                if (a.getIdArchipelago() == idArchipelago) {
                    if (a.getOwner() == null) {
                        oldOwner = null;
                        newOwner = game.getCurrentPlayer();

                    } else {
                        oldOwner = a.getOwner();
                        newOwner = a.getOwner();
                        maxInfluence = a.getBelongingIslands().size();
                    }

                    for (int c = 0; c < Constants.NUMBEROFKINGDOMS; c++) {
                        for (Island i : a.getBelongingIslands()) {
                            if (i.getAllStudents()[c] > 0 && newOwner.getMyBoard().getProfessorsTable().getHasProf(StudsAndProfsColor.values()[c])) {
                                maxInfluence += i.getAllStudents()[c];
                            }
                        }
                    }
                    for (Player p : game.getOrderOfPlayers()) {
                        if (p != newOwner) {
                            int newInfluence = 0;
                            for (int c = 0; c < Constants.NUMBEROFKINGDOMS; c++) {
                                for (Island i : a.getBelongingIslands()) {
                                    if (i.getAllStudents()[c] > 0 && p.getMyBoard().getProfessorsTable().getHasProf(StudsAndProfsColor.values()[c])) {
                                        newInfluence += i.getAllStudents()[c];
                                    }
                                }

                            }
                            if (newInfluence > maxInfluence) {
                                newOwner = p;
                                maxInfluence = newInfluence;
                            }
                        }
                    }

                    if (maxInfluence > 0) {
                        a.changeOwner(newOwner);
                        for (int i = 0; i < a.getBelongingIslands().size(); i++) {
                            if (oldOwner != null) {
                                oldOwner.getMyBoard().getTowersOnBoard().removeTower();
                            }
                            newOwner.getMyBoard().getTowersOnBoard().removeTower();
                        }
                        checkUnification(a);
                    }
                }
            }
            return true;
        }else{
            return false;
        }
    }

}

