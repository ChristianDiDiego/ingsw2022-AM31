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
                // PROPOSAL OF NEW CALCULATE INFLUENCE:
                Player oldOwner;
                int[] influences = new int[game.getNumberOfPlayers()];
                for(int i = 0; i< game.getNumberOfPlayers(); i++){
                    influences[i] = 0;
                }

                if(a.getOwner() == null){
                    oldOwner = null;
                }else {
                    oldOwner = a.getOwner();
                }

                //Calcola l'influenza di ogni giocatore sull'arcipelago a
                //assegna questo valore incrementando il vettore influences alla posizione
                //corrispondente al proprio team:
                //In tal modo, sia se si sta giocando a squadre che tutti contro tutti
                //vengono calcolate correttamente
                for(Player p : game.getOrderOfPlayers()){
                    //Se oldowner non è nullo e il suo numero di squadra coincide con il player su cui
                    //stiamo iterando, aggiunge all'influenza del suo team il numero di torri(=numero di isole)
                    if(oldOwner != null && oldOwner.getTeam() == p.getTeam()){
                        influences[p.getTeam()] = 0;
                    }
                    for(int c = 0; c < Constants.NUMBEROFKINGDOMS; c++){
                        for(Island i : a.getBelongingIslands()){
                            if(i.getAllStudents()[c] > 0 && p.getMyBoard().getProfessorsTable().getHasProf(StudsAndProfsColor.values()[c])) {
                                influences[p.getTeam()] += i.getAllStudents()[c];
                            }
                        }
                    }
                }
                //Trova il massimo nel vettore influenza e si salva il team corrispondente
                int maxInfluence = 0;
                int teamMaxInfluence = 0;
                for(int i = 0; i< influences.length; i++){
                    if(influences[i] > maxInfluence){
                        maxInfluence = influences[i];
                        teamMaxInfluence = i;
                    }
                }
                boolean tie = false;
                //Controlla se ci sono due giocatori diversi con la stessa influenza:
                //in quel caso si ha un pareggio
                for(int i = 0; i< influences.length; i++){
                    if(influences[i] == maxInfluence && i != teamMaxInfluence){
                        tie = true;
                        break;
                    }
                }

                  /*Procede con il cambiamento di torri solo se:
                  - Almeno un giocatore ha l'influenza su quell'isola (maxInfluence >0)
                  - l'arcipelago non era di nessuno O è cambia il team proprietario
                  - non c'è un pareggio

                   */
                if(maxInfluence > 0 && (oldOwner == null || oldOwner.getTeam() != teamMaxInfluence) && !tie){
                    for(int i = 0; i < a.getBelongingIslands().size(); i++) {
                        //Only if the newOwner is different from the oldOwner (or this was null) change the towers

                        for(Player p : game.getOrderOfPlayers()){
                            if(oldOwner != null && oldOwner.getTeam() == p.getTeam() && p.getColorOfTowers() != null){
                                p.getMyBoard().getTowersOnBoard().addTower();
                            }
                            if(p.getTeam() == teamMaxInfluence && p.getColorOfTowers() != null){
                                p.getMyBoard().getTowersOnBoard().removeTower();
                                a.changeOwner(p);
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

