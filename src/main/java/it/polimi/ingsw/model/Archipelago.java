package it.polimi.ingsw.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Archipelago implements Serializable {
    @Serial
    private static final long serialVersionUID = 7L;

    private int idArchipelago;
    private List<Island> belongingIsland;
    private boolean isMNPresent;
    private Player owner;
    private boolean isForbidden;

    public Archipelago(int idArchipelago) {
        this.idArchipelago = idArchipelago;
        Island temp = new Island(idArchipelago);
        this.belongingIsland = new ArrayList<>();
        belongingIsland.add(temp);
        isForbidden = false;
        isMNPresent = idArchipelago == 1;
    }

    public int getIdArchipelago() {
        return idArchipelago;
    }

    public boolean getIsMNPresent() {
        return isMNPresent;
    }

    public List<Island> getBelongingIslands() {
        return belongingIsland;
    }

    /**
     * Find who is the owner of an archipelago
     * @return the player who is the owner of the archipelago
     * @throws NullPointerException if the owner is null
     */
    public Player getOwner()  {
        return owner;
    }

    /**
     * If we merge 2 Archipelagos, add the second Archipelago's islands to this one
     * @param island to be added to the archipelago
     */
    public void addIsland(Island island) {
        belongingIsland.add(island);
    }

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

    /**
     * if a player conquered the archipelago the method changes the owner
     * @param newOwner
     */
    public void changeOwner(Player newOwner) {
        this.owner = newOwner;
    }

    public boolean getIsForbidden() {
        return isForbidden;
    }

    public void setIsForbidden(boolean forbidden) {
        isForbidden = forbidden;
    }
}
