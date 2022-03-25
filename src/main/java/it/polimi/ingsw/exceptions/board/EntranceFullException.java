package it.polimi.ingsw.exceptions.board;

public class EntranceFullException extends Exception{
    public EntranceFullException(){
        super("The entrance is full");
    }
}
