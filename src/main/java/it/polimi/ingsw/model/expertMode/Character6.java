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
            //TODO: add message "influence not calculated because forbidden";
            // calculate influence for 4 players
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

                //Calcola l'influenza di ogni giocatore sull'arcipelago a
                //assegna questo valore incrementando il vettore influences alla posizione
                //corrispondente al proprio team:
                //In tal modo, sia se si sta giocando a squadre che tutti contro tutti
                //vengono calcolate correttamente
                for (Player p : game.getOrderOfPlayers()) {
                    //Se oldowner non è nullo e il suo numero di squadra coincide con il player su cui
                    //stiamo iterando, aggiunge all'influenza del suo team il numero di torri(=numero di isole)
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
                //Trova il massimo nel vettore influenza e si salva il team corrispondente
                int maxInfluence = 0;
                int teamMaxInfluence = 0;
                for (int i = 0; i < influences.length; i++) {
                    if (influences[i] > maxInfluence) {
                        maxInfluence = influences[i];
                        teamMaxInfluence = i;
                    }
                }
                boolean tie = false;
                //Controlla se ci sono due giocatori diversi con la stessa influenza:
                //in quel caso si ha un pareggio
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
                                //TODO: if the number of towers finish, the game is over
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
