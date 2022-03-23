package it.polimi.ingsw.model;

public class DiningRoom {
    private int[] studentsInDN;

    public DiningRoom(){
        this.studentsInDN = new int[5];
    }

    //Receive the color ( identified by the enum) of the student to add in the Dining Room
    public void addStudent(StudsAndProfsColor studColor){
        studentsInDN[studColor.ordinal()]++;
    }
    //Receive the color ( identified by the enum) of the student and return the number of students of that color
    public int getStudentByColor(StudsAndProfsColor studColor){

        return studentsInDN[studColor.ordinal()];
    }
}
