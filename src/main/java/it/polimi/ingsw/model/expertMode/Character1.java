package it.polimi.ingsw.model.expertMode;

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
     *
     * @param idArchipelago where the calculation will be calculated
     */
    public boolean usePower(int idArchipelago) {
        if (payForUse()) {
            game.getCurrentPlayer().setUsedCharacter(this);
            if (game.getArchipelagoById(idArchipelago) != null) {
                Archipelago a = game.getArchipelagoById(idArchipelago);
                if (!a.getIsForbidden()) {
                    // PROPOSAL OF NEW CALCULATE INFLUENCE:
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
                        /*
                        if oldOwner is not null and his team's number corresponds with p,
                        add number of towers to team's influence
                         */
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
                        checkUnification(game.getArchipelagoById(idArchipelago));
                    }
                } else if (a.getIsMNPresent() && a.getIsForbidden()) {
                    game.getArchipelagoById(idArchipelago).setIsForbidden(false);
                }
            }
            return true;
        } else {
            return false;
        }
    }
}