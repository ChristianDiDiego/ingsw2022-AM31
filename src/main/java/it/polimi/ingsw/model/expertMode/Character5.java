package it.polimi.ingsw.model.expertMode;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.model.*;

/**
 * image 7 in image folder ;
 * card 8 in list of cards ( in game tutorial)
 * When you calculate the influence, the player who play this card has 2 additional points
 */
public class Character5 extends Characters{

    public Character5(Game game) {
        super(2, game);
        this.descriptionOfPower = "When you calculate the influence, the player who play this card has 2 additional points";
        id = 5;
        bonusInfluence = 2;
    }

    public void usePower() {
        if(payForUse()) {
            game.getCurrentPlayer().setUsedCharacter(this);
        }
    }

    /**
     * Calculate the influence with the 2 additional points;
     */
    public void calculateInfluence() {
        for(Archipelago a : game.getListOfArchipelagos()){
            if(a.getIsMNPresent() && a.getIsForbidden() == false){
                Player newOwner;
                Player oldOwner;
                int maxInfluence = 0;
                if(a.getOwner() == null){
                    oldOwner = null;
                    newOwner = game.getCurrentPlayer();

                }else {
                    oldOwner = a.getOwner();
                    newOwner = a.getOwner();
                    maxInfluence = a.getBelongingIslands().size();
                    if(a.getOwner().getUsedCharacter() == this) {
                        maxInfluence += bonusInfluence;
                    }
                }

                for(int c = 0; c < Constants.NUMBEROFKINGDOMS; c++){
                    for(Island i : a.getBelongingIslands()){
                        if(i.getAllStudents()[c] > 0 && newOwner.getMyBoard().getProfessorsTable().getHasProf(StudsAndProfsColor.values()[c])) {
                            maxInfluence += i.getAllStudents()[c];
                        }
                    }
                }
                for(Player p : game.getOrderOfPlayers()){
                    if(p != newOwner){
                        int newInfluence = 0;
                        for(int c = 0; c < Constants.NUMBEROFKINGDOMS; c++){
                            for(Island i: a.getBelongingIslands()){
                                if(i.getAllStudents()[c] > 0 && p.getMyBoard().getProfessorsTable().getHasProf(StudsAndProfsColor.values()[c])) {
                                    newInfluence += i.getAllStudents()[c];
                                }
                            }

                        }
                        if(p.getUsedCharacter() == this) {
                            newInfluence += bonusInfluence;
                        }
                        if(newInfluence > maxInfluence){
                            newOwner = p;
                            maxInfluence = newInfluence;
                        }
                    }
                }

                if(maxInfluence > 0){
                    a.changeOwner(newOwner);
                    for(int i = 0; i < a.getBelongingIslands().size(); i++) {
                        if(oldOwner != null){
                            oldOwner.getMyBoard().getTowersOnBoard().removeTower();
                        }
                        newOwner.getMyBoard().getTowersOnBoard().removeTower();
                    }
                    checkUnification(a);
                }
            }else if (a.getIsMNPresent() && a.getIsForbidden() == true){
                a.setIsForbidden(false);
            }
        }
    }

}
