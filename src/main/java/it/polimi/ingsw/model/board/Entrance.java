package it.polimi.ingsw.model.board;

import it.polimi.ingsw.exceptions.board.EntranceFullException;
import it.polimi.ingsw.exceptions.board.NoStudentsOfColorException;
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
        studentsInEntrance = new int[5];
    }

    //Receive the color ( identified by the enum) of the student to remove from entrance
    public void removeStudent(StudsAndProfsColor studColor) throws NoStudentsOfColorException{
        if(studentsInEntrance[studColor.ordinal()] == 0){
            throw new NoStudentsOfColorException(studColor);
        }else {
            studentsInEntrance[studColor.ordinal()]--;
        }
    }

    //Receive the color ( identified by the enum) of the student to add in the entrance

    //TODO: SUBSTITUTE 7 with a parametric number
    public void addStudent(StudsAndProfsColor studColor) throws EntranceFullException{
        if(getNumberOfStudentsInEntrance() == 7){
            throw new EntranceFullException();
        }else{
            studentsInEntrance[studColor.ordinal()]++;
        }
    }

    //Receive the color ( identified by the enum) of the student and return the number of students of that color
    public int getStudentsByColor(StudsAndProfsColor studColor){

        return studentsInEntrance[studColor.ordinal()];
    }

    //Calculate the number of students on the entrance
    private int getNumberOfStudentsInEntrance(){
        int tot = 0;
        for(int i = 0; i < 5; i++){
            tot += studentsInEntrance[i];
        }
        return tot;
    }

}
