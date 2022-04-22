package it.polimi.ingsw.model.board;

import java.io.Serial;
import java.io.Serializable;

public class TowersOnBoard implements Serializable {
    @Serial
    private static final long serialVersionUID = 6L;

    private int numberOfTowers;

    public TowersOnBoard(){
        this.numberOfTowers = 0;
    }

    /**
     * Add a tower on the table of the player
     */
    public void addTower() {
        numberOfTowers++;
    }

    /**
     * Remove a tower from the table of the player
     */
    public void removeTower() {
        numberOfTowers--;
    }

    /**
     * Get the number of towers on the table of the player
     * @return the number of towers on the table
     */
    public int getNumberOfTowers() {
        return numberOfTowers;
    }
}
