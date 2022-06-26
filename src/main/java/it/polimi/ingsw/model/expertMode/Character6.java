package it.polimi.ingsw.model.expertMode;

import it.polimi.ingsw.utilities.Constants;
import it.polimi.ingsw.model.*;

/**
 * image 8 in image folder ;
 * card 9 in list of cards ( in game tutorial)
 * Choose a color of students that will not be counted for the influence
 */
public class Character6 extends Characters {
    private StudsAndProfsColor color;

    public Character6(Game game) {
        super(3, game);
        id = 6;
        this.descriptionOfPower = "Choose a color of students that will not be counted for the influence" +
                "Usage: CHARACTER 6 [COLOR]";
    }

    public boolean usePower(StudsAndProfsColor color) {
        if (payForUse()) {
            game.getCurrentPlayer().setUsedCharacter(this);
            this.color = color;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Calculates the influence without the contribution of the students of the chosen color
     */
    public void calculateInfluence() {
        System.out.println("color to be ignored: " + color.toString());
        int value = color.ordinal();
        for (Archipelago a : game.getListOfArchipelagos()) {
            if (a.getIsMNPresent() && !a.getIsForbidden()) {
                Player oldOwner;
                int[] influences = new int[game.getNumberOfPlayers()];
                for (int i = 0; i < game.getNumberOfPlayers(); i++) {
                    influences[i] = 0;
                }

                if (a.getOwner() == null) {
                    oldOwner = null;
                } else {
                    oldOwner = a.getOwner();
                }

                    /*
                    Calculate the influence of each player on the archipelago a
                    give this value increasing the influences vector at the position
                    of the team:
                    In this way, both if is a game with 2 or 4 players the influence is correctly calculated
                     */
                for (Player p : game.getOrderOfPlayers()) {
                    /*
                    if oldOwner is not null and his team's number corresponds with p,
                    add number of towers to team's influence
                     */
                    if (oldOwner != null && oldOwner.getTeam() == p.getTeam()) {
                        influences[p.getTeam()] = a.getBelongingIslands().size();
                    }
                    for (int c = 0; c < Constants.NUMBEROFKINGDOMS; c++) {
                        for (Island i : a.getBelongingIslands()) {
                            if (i.getAllStudents()[c] > 0 && p.getMyBoard().getProfessorsTable().getHasProf(StudsAndProfsColor.values()[c]) && c != value) {
                                influences[p.getTeam()] += i.getAllStudents()[c];
                            }
                        }
                    }
                }
                //Find the max in the vector influence and save the team number
                int maxInfluence = 0;
                int teamMaxInfluence = 0;
                for (int i = 0; i < influences.length; i++) {
                    if (influences[i] > maxInfluence) {
                        maxInfluence = influences[i];
                        teamMaxInfluence = i;
                    }
                }
                boolean tie = false;
                //Check if there are two different players with the same influence (tie)
                for (int i = 0; i < influences.length; i++) {
                    if (influences[i] == maxInfluence && i != teamMaxInfluence) {
                        tie = true;
                        break;
                    }
                }

                    /*
                    Change the tower only if these conditions are verified:
                    - at least 1 player has the influence ont he archipelago (maxInfluence>0)
                    - the archipelago had no owner or the owner changes
                    - there is no tie
                     */
                if (maxInfluence > 0 && (oldOwner == null || oldOwner.getTeam() != teamMaxInfluence) && !tie) {
                    for (int i = 0; i < a.getBelongingIslands().size(); i++) {
                        //Only if the newOwner is different from the oldOwner (or this was null) change the towers

                        for (Player p : game.getOrderOfPlayers()) {
                            if (oldOwner != null && oldOwner.getTeam() == p.getTeam() && p.getColorOfTowers() != null) {
                                p.getMyBoard().getTowersOnBoard().addTower();
                            }
                            if (p.getTeam() == teamMaxInfluence && p.getColorOfTowers() != null) {
                                p.getMyBoard().getTowersOnBoard().removeTower();
                                a.changeOwner(p);
                            }
                        }
                    }
                    checkUnification(a);
                    if (game.getListOfArchipelagos().size() < 4) {
                        return;
                    }
                    break;
                }
            } else if (a.getIsMNPresent() && a.getIsForbidden()) {
                a.setIsForbidden(false);
                break;
            }
        }
    }

}
