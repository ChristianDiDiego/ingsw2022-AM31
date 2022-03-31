package it.polimi.ingsw.model;

import it.polimi.ingsw.constants.Constants;

import java.util.Random;

public class Bag {
    private int[] studentsInBag;
    private int numberPlayer;

    public Bag(int numberPlayer){
        this.numberPlayer= numberPlayer;
        studentsInBag= new int[Constants.NUMBEROFKINGDOMS];
        for(int i=0; i<Constants.NUMBEROFKINGDOMS; i++){
            studentsInBag[i] = Constants.NUMBEROFSTUDENTSOFEACHCOLOR - 2;
        }
    }

    public int getNumberOfLeftStudents(){
        int temp = 0;
        for(int i=0; i<Constants.NUMBEROFKINGDOMS; i++){
            temp = temp + studentsInBag[i];
        }
        return temp;
    }

    /**
     * Return a random generated array of students that will be moved from bag to clouds
     * @return
     */
    public int[] pickStudent(int studToPick){
        int[] studentsToPick = new int[Constants.NUMBEROFKINGDOMS];
        for(int i=0; i<Constants.NUMBEROFKINGDOMS;i++){
            studentsToPick[i]=0;
        }
        Random random = new Random();
        int i = 0;
            while (i < studToPick) {
                int value = random.nextInt(5 + 0);
                if(studentsInBag[value] > 0) {
                    studentsToPick[value]++;
                    studentsInBag[value]--;
                    i++;
                }
            }
        return studentsToPick;
    }
}
