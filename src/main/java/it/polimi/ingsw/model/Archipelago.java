package it.polimi.ingsw.model;

import java.util.List;

public class Archipelago {
    private List<Island> belongingIsland;
    private boolean isMNPresent;

    public Archipelago(Island island) {
        belongingIsland.add(island);
        isMNPresent = false;
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
