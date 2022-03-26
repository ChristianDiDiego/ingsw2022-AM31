package it.polimi.ingsw.model.board;

public class TowersOnBoard {

    private int numberOfTowers;

    public TowersOnBoard(){
        this.numberOfTowers = 0;
    }

    /**
     * Add a tower on the table of the player
     */
    //Todo : add a parameter for the max number of towers
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
