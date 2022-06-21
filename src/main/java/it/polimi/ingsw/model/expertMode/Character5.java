package it.polimi.ingsw.model.expertMode;

import it.polimi.ingsw.model.*;

/**
 * image 7 in image folder ;
 * card 8 in list of cards ( in game tutorial)
 * When you calculate the influence, the player who play this card has 2 additional points
 */
public class Character5 extends Characters {

    public Character5(Game game) {
        super(2, game);
        id = 5;
        this.descriptionOfPower = "When you calculate the influence, the player who play this card has 2 additional points";
        bonusInfluence = 2;
    }

    public boolean usePower() {
        if (payForUse()) {
            game.getCurrentPlayer().setUsedCharacter(this);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Calculate the influence with the 2 additional points;
     */

    public boolean calculateInfluence() {
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
                    In this way, both if is a game with 2 or 4 players
                    the influence is correctly calculated
                     */
                for (Player p : game.getOrderOfPlayers()) {
                    if (p.getUsedCharacter() == this) {
                        influences[p.getTeam()] += 2;
                    }
                    //Se oldowner non è nullo e il suo numero di squadra coincide con il player su cui
                    //stiamo iterando, aggiunge all'influenza del suo team il numero di torri(=numero di isole)
                    if (oldOwner != null && oldOwner.getTeam() == p.getTeam()) {
                        influences[p.getTeam()] = a.getBelongingIslands().size();
                    }
                    Character4.findInfluence(a, influences, p);
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
                        return true;
                    }
                    break;
                }
            } else if (a.getIsMNPresent() && a.getIsForbidden()) {
                a.setIsForbidden(false);
                break;
            }
        }
        return false;
    }

}
