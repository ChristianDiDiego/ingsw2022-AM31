package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exceptions.board.TowersBoardEmptyException;
import it.polimi.ingsw.exceptions.board.TowersBoardFullException;

public class TowersOnBoard {

    private int numberOfTowers;

    public TowersOnBoard(){
        this.numberOfTowers = 0;
    }
    //Todo : add a parameter for the max number of towers
    public void addTower() throws TowersBoardFullException {
        if (numberOfTowers <8){
            numberOfTowers++;
        }
        else {
            throw new TowersBoardFullException();
        }

    }
    public void removeTower() throws TowersBoardEmptyException {
        if (numberOfTowers >0){
            numberOfTowers--;
        }
        else {
            throw new TowersBoardEmptyException();
        }
    }

    //Return the number of towers on the board of the player
    public int getNumberOfTowers() {
        return numberOfTowers;
    }
}
