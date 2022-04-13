package it.polimi.ingsw.model.board;

import it.polimi.ingsw.utilities.constants.Constants;
import it.polimi.ingsw.model.StudsAndProfsColor;

public class Entrance {
    /*This array contains the number of students for each color depending on the position.
      0 - RED
      1 - GREEN
      2 - YELLOW
      3 - PINK
      4 - BLUE
     */
    private int[] studentsInEntrance;
    public Entrance(){
        studentsInEntrance = new int[Constants.NUMBEROFKINGDOMS];
    }

    /**
     * Remove a student from the entrance
     * @param studColor color of the student to be removed
     */

    public void removeStudent(StudsAndProfsColor studColor) {
            studentsInEntrance[studColor.ordinal()]--;
    }

    /**
     * Add a student in the entrance
     * @param toAdd array of students to add
     */
    public void addStudent(int[] toAdd){
        for(int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
           studentsInEntrance[i] += toAdd[i];
        }
    }

    /**
     * Return the number of students of a color in the dining room
     * @param studColor color of the students to be counted
     * @return number of the students of color studColor in the entrance
     */
    public int getStudentsByColor(StudsAndProfsColor studColor){
        return studentsInEntrance[studColor.ordinal()];
    }


    /**
     * Calculate the total number of students in the entrance
     * @return Total number of students in entrance
     */
    /*
    public int getNumberOfStudentsInEntrance(){
        int tot = 0;
        for(int i = 0; i < Constants.NUMBEROFKINGDOMS; i++){
            tot += studentsInEntrance[i];
        }
        return tot;
    }

    */

}
