package it.polimi.ingsw.model;

public class Cloud {
    private int idCloud;
    private int[] studentsOnCloud = new int[5];
    private boolean isTaken;

    public Cloud(int idCloud) {
        this.idCloud = idCloud;
        isTaken = false;
        for(int i = 0; i < 5; i++) {
            studentsOnCloud[i] = 0;
        }
    }

    public int getIdCloud() {
        return idCloud;
    }

    public int[] getStudents() {
        return studentsOnCloud;
    }

    public boolean getIsTaken() {
        return isTaken;
    }

    public void addStudents(int[] toAdd) {
        for(int i = 0; i < 5; i++) {
            studentsOnCloud[i] += toAdd[i];
        }
    }

    public void removeStudents() {
        for(int i = 0; i < 5; i++) {
            studentsOnCloud[i] = 0;
        }
    }

    public void changeStatus() {
        if (isTaken == false) {
            this.isTaken = true;
        } else {
            this.isTaken = false;
        }
    }

}
