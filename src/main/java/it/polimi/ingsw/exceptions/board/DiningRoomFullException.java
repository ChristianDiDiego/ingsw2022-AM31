package it.polimi.ingsw.exceptions.board;

import it.polimi.ingsw.model.StudsAndProfsColor;

public class DiningRoomFullException extends Exception{
    public DiningRoomFullException(StudsAndProfsColor color){
        super("The dining room of the color " + color.toString() + " is full");
    }
}
