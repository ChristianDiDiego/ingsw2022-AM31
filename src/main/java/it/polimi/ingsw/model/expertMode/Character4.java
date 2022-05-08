package it.polimi.ingsw.model.expertMode;

import it.polimi.ingsw.utilities.constants.Constants;
import it.polimi.ingsw.model.*;

/**
 * image 5 in image folder ;
 * card 6 in list of cards ( in game tutorial)
 * When calculate the influence does not count the towers
 */
public class Character4 extends Characters {

    public Character4(Game game) {
        super(3, game);
        id = 4;
        descriptionOfPower = " When calculate the influence does not count the towers";
    }


    /**
     * sets usedCharacter in current player
     */
    public boolean usePower() {
        if(payForUse()){
            game.getCurrentPlayer().setUsedCharacter(this);
            return true;
        }else{
            return false;
        }
    }

    /**
     * calculates influence without counting towers
     */
    public void calculateInfluence(){
        for(Archipelago a : game.getListOfArchipelagos()){
            //TODO: add message "influence not calculated because forbidden";
            // calculate influence for 4 players
            if(a.getIsMNPresent() && a.getIsForbidden() == false){
                Player newOwner;
                Player oldOwner;
                int maxInfluence = 0;
                if(a.getOwner() == null){
                    System.out.println("No old owner");
                    oldOwner = null;
                    newOwner = game.getCurrentPlayer();

                }else {
                    oldOwner = a.getOwner();
                    newOwner = a.getOwner();
                    maxInfluence = 0;
                }
                //Sum the influence of the players who have the same team number of the newOwner
                //for instance, if the game has 2 or 3 players each of them has a different number of the team
                for(Player p : game.getOrderOfPlayers()){
                    if(p.getTeam() == newOwner.getTeam()){
                        for(int c = 0; c < Constants.NUMBEROFKINGDOMS; c++){
                            for(Island i : a.getBelongingIslands()){
                                if(i.getAllStudents()[c] > 0 && p.getMyBoard().getProfessorsTable().getHasProf(StudsAndProfsColor.values()[c])) {
                                    maxInfluence += i.getAllStudents()[c];
                                }
                            }
                        }
                    }
                }

                Player alreadyCalculated = null;
                int newInfluence = 0;
                //Sum the influence of the players who have different team number than the newOwner
                //for instance, if the game has 2 or 3 players each of them has a different number of the team
                //Reset the value of newInfluence only if p has a different number of team than the player
                //who we already calculated the influence; in this way, when 2 or 3 players are playing it is resetted every time
                //but in case of 4 players the influence of the teammates is summed
                for(Player p : game.getOrderOfPlayers()){
                    if(p != newOwner && p.getTeam() != newOwner.getTeam()){
                        if(alreadyCalculated == null || alreadyCalculated.getTeam() != p.getTeam()){
                            newInfluence = 0;
                        }
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
                        alreadyCalculated = p;
                    }
                }
                if(maxInfluence == newInfluence){
                    newOwner = oldOwner;
                }

                if(maxInfluence > 0){
                    a.changeOwner(newOwner);
                    System.out.println("The new owner is " + newOwner.getNickname());
                    for(int i = 0; i < a.getBelongingIslands().size(); i++) {
                        //Only if the newOwner is different from the oldOwner (or this was null) change the towers
                        if(oldOwner != null && oldOwner.getTeam() != newOwner.getTeam()){
                            System.out.println("I should not enter int this if ");
                            for(Player p : game.getOrderOfPlayers()){
                                if(oldOwner.getTeam() == p.getTeam() && p.getColorOfTowers() != null){
                                    p.getMyBoard().getTowersOnBoard().addTower();
                                }
                                if(newOwner.getTeam() == p.getTeam() && p.getColorOfTowers() != null){
                                    p.getMyBoard().getTowersOnBoard().removeTower();
                                    //checkWinner(newOwner);
                                    //TODO: if the number of towers finish, the game is over
                                }
                            }
                        }else if(oldOwner == null){
                            for(Player p : game.getOrderOfPlayers()){
                                if(newOwner.getTeam() == p.getTeam() && p.getColorOfTowers() != null){
                                    p.getMyBoard().getTowersOnBoard().removeTower();
                                }
                            }
                        }
                    }
                    checkUnification(a);
                    break;
                }
            } else if (a.getIsMNPresent() && a.getIsForbidden() == true){
                a.setIsForbidden(false);
                break;
            }
        }
    }



    /*public void calculateInfluence(){
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

                for(int c = 0; c < Constants.NUMBEROFKINGDOMS; c++){
                    for(Island i: a.getBelongingIslands()){
                        if(i.getAllStudents()[c] > 0 && newOwner.getMyBoard().getProfessorsTable().getHasProf(StudsAndProfsColor.values()[c])) {
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
    }*/
}

