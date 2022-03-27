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
            studentsInBag[i] = Constants.NUMBEROFSTUDENTSOFEACHCOLOR;
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
    public int[] pickStudent(){
        int[] studentsToPick = new int[Constants.NUMBEROFKINGDOMS];
        for(int i=0; i<5;i++){
            studentsToPick[i]=0;
        }

        Random random = new Random();
        if(numberPlayer==4) {
            for (int i = 0; i < 3; i++) {
                int value = random.nextInt(5 + 0);
                studentsToPick[i]++;
                studentsInBag[i]--;
            }

        }else {
            for (int i = 0; i < 4; i++) {
                int value = random.nextInt(5 + 0);
                studentsToPick[i]++;
                studentsInBag[i]--;
            }
        }
        return studentsToPick;
    }
}
