package it.polimi.ingsw.model;

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

    //Receive the color ( identified by the number) of the student to remove from entrance
    public void removeStudent(StudsAndProfsColor studColor){
        studentsInEntrance[studColor.ordinal()]--;
    }

    //Receive the color ( identified by the number) of the student to add in the entrance
    public void addStudent(StudsAndProfsColor studColor){
        studentsInEntrance[studColor.ordinal()]++;
    }
    //Receive the color ( identified by the number) of the student and return the number of students of that color
    public int getStudentByColor(StudsAndProfsColor studColor){

        return studentsInEntrance[studColor.ordinal()];
    }
}
