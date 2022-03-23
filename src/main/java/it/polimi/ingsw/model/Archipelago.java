package it.polimi.ingsw.model;

import java.util.List;

public class Archipelago {
    private int idArchipelago;
    private List<Island> belongingIsland;
    private boolean isMNPresent;

    public Archipelago(int idArchipelago) {
        this.idArchipelago = idArchipelago;
        Island temp = new Island(idArchipelago);
        belongingIsland.add(temp);
        isMNPresent = false;
    }

    public int getIdArchipelago() {
        return idArchipelago;
    }

    public boolean getIsMNPresent() {
        return isMNPresent;
    }

    public void addIsland(Island island) {
        belongingIsland.add(island);
    }

    /* public void calculateInfluence() {     //to add in future

    } */

    public void changeMNPresence() {
        if (isMNPresent == false) {
            this.isMNPresent = true;
        } else {
            this.isMNPresent = false;
        }
    }
}
