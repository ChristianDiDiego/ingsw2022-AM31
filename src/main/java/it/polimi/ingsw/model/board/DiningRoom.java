package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exceptions.board.DiningRoomFullException;
import it.polimi.ingsw.model.StudsAndProfsColor;

public class DiningRoom {
    private int[] studentsInDN;

    public DiningRoom(){
        this.studentsInDN = new int[5];
    }

    /**
     * Receive the color ( identified by the enum) of the student to add in the Dining Room
     * @param studColor
     * @throws DiningRoomFullException
     */
    public void addStudent(StudsAndProfsColor studColor) throws DiningRoomFullException {
        if(studentsInDN[studColor.ordinal()] < 10){
            studentsInDN[studColor.ordinal()]++;
        }else{
            throw new DiningRoomFullException(studColor);
        }
    }


    /**
     * Receive the color ( identified by the enum) of the student and return the number of students of that color
     * @param studColor
     * @return int
     */
    public int getStudentsByColor(StudsAndProfsColor studColor){

        return studentsInDN[studColor.ordinal()];
    }
}
