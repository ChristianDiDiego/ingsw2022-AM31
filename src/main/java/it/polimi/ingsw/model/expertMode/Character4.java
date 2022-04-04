package it.polimi.ingsw.model.expertMode;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.model.*;

/**
 * image 5 in image folder ;
 * card 6 in list of cards ( in game tutorial)
 * When calculate the influence does not count the towers
 */
public class Character4 extends Characters {

    public Character4(Game game) {
        super(3, game);
        descriptionOfPower = " When calculate the influence does not count the towers";
    }


    /**
     * sets usedCharacter in current player
     * @param value is not used
     */
    @Override
    public void usePower(int value) {
        if(payForUse()){
            game.getCurrentPlayer().setUsedCharacter(4);
        }
    }

    /**
     * calculates influence without counting towers
     */
    public void calculateInfluence(){
        for(Archipelago a: game.getListOfArchipelagos()){
            if(a.getIsMNPresent() && a.getIsForbidden() == false){
                Player newOwner;
                Player oldOwner;
                int maxInfluence =0;
                if(a.getOwner() == null){
                    oldOwner = null;
                    newOwner = game.getCurrentPlayer();

                }else {
                    oldOwner = a.getOwner();
                    newOwner = a.getOwner();
                    maxInfluence = 0;
                }

                for(int c=0; c< Constants.NUMBEROFKINGDOMS; c++){
                    for(Island i: a.getBelongingIslands()){
                        if(i.getAllStudents()[c] > 0 && a.getOwner().getMyBoard().getProfessorsTable().getHasProf(StudsAndProfsColor.values()[c])) {
                            maxInfluence += i.getAllStudents()[c];
                        }
                    }
                }
                for(Player p : game.getOrderOfPlayers()){
                    if(p != newOwner){
                        int newInfluence = 0;
                        for(int c=0; c< Constants.NUMBEROFKINGDOMS; c++){
                            for(Island i: a.getBelongingIslands()){
                                if(i.getAllStudents()[c] > 0 && p.getMyBoard().getProfessorsTable().getHasProf(StudsAndProfsColor.values()[c])) {
                                    newInfluence += i.getAllStudents()[c];
                                }
                            }

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

