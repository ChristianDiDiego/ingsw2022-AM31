package it.polimi.ingsw.exceptions.board;


public class TowersBoardFullException extends Exception {
    public TowersBoardFullException(){
        super("The space for the towers on the board is full");
    }
}
