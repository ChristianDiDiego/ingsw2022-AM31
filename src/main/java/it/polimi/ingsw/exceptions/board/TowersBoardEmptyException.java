package it.polimi.ingsw.exceptions.board;

public class TowersBoardEmptyException extends Exception {
    public TowersBoardEmptyException() {
        super("The space for the towers on the board is empty");
    }
}
