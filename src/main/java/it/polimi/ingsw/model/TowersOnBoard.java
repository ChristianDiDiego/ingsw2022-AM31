package it.polimi.ingsw.model;

public class TowersOnBoard {

    private int numberOfTowers;

    public TowersOnBoard(){
        this.numberOfTowers = 0;
    }

    public void addTower(){
        numberOfTowers++;
    }
    public void removeTower(){
        numberOfTowers--;
    }

    //Return the number of towers on the board of the player
    public int getNumberOfTowers() {
        return numberOfTowers;
    }
}
