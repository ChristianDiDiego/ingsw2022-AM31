package it.polimi.ingsw.exceptions.board;

import it.polimi.ingsw.model.StudsAndProfsColor;

public class NoStudentsOfColorException extends Exception{
    public NoStudentsOfColorException(StudsAndProfsColor color){
        super("The entrance does not contain students of color " + color.toString());
    }

}
