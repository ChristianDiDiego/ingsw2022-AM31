package it.polimi.ingsw.model.board;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.model.StudsAndProfsColor;

public class DiningRoom {
    private int[] studentsInDR;

    public DiningRoom(){
        this.studentsInDR = new int[Constants.NUMBEROFKINGDOMS];
    }

    /**
     * Add a student in the dining room
     * @param studColor color of the student that needs to be added
     */
    public void addStudent(StudsAndProfsColor studColor) {
        studentsInDR[studColor.ordinal()]++;
    }

    /**
     * Remove a student from the dining room
     * @param studColor color of the student that needs to be removed
     */
    public void removeStudent(StudsAndProfsColor studColor) {
        studentsInDR[studColor.ordinal()]--;
    }


    /**
     * Return the number of students of a color in the dining room
     * @param studColor color of the students to be counted
     * @return number of the students of color studColor in the dining room
     */
    public int getStudentsByColor(StudsAndProfsColor studColor){

        return studentsInDR[studColor.ordinal()];
    }
}
