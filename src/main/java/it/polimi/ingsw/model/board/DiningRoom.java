package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.StudsAndProfsColor;

public class DiningRoom {
    private int[] studentsInDN;

    public DiningRoom(){
        // 5 is the number of the different kingdoms
        //TODO: substitute 5 with a parametric number
        this.studentsInDN = new int[5];
    }

    /**
     * Add a student in the dining room
     * @param studColor color of the student that needs to be added
     */
    public void addStudent(StudsAndProfsColor studColor) {
        studentsInDN[studColor.ordinal()]++;
    }


    /**
     * Return the number of students of a color in the dining room
     * @param studColor color of the students to be counted
     * @return number of the students of color studColor in the dining room
     */
    public int getStudentsByColor(StudsAndProfsColor studColor){

        return studentsInDN[studColor.ordinal()];
    }
}
