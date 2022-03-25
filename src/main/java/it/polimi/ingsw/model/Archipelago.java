package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Archipelago {
    private int idArchipelago;
    private List<Island> belongingIsland;
    private boolean isMNPresent;

    public Archipelago(int idArchipelago) {
        this.idArchipelago = idArchipelago;
        Island temp = new Island(idArchipelago);
        this.belongingIsland = new ArrayList<>();
        belongingIsland.add(temp);
        if(idArchipelago == 0){
            isMNPresent = true;
        }else{
            isMNPresent = false;
        }
    }

    public int getIdArchipelago() {
        return idArchipelago;
    }

    public boolean getIsMNPresent() {
        return isMNPresent;
    }

    /**
     * If we merge 2 Archipelagos, add the second Archipelago's islands to this one
     * @param island
     */
    public void addIsland(Island island) {
        belongingIsland.add(island);
    }

    /* public void calculateInfluence() {     //to add in future

    } */

    /**
     * change the flag that shows if Mother Nature is on the Archipelago
     */
    public void changeMNPresence() {
        if (isMNPresent == false) {
            this.isMNPresent = true;
        } else {
            this.isMNPresent = false;
        }
    }

    public List<Island> getBelongingIsland(){
        return belongingIsland;
    }
}
