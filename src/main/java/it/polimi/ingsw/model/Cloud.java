package it.polimi.ingsw.model;

public class Cloud {
    private int idCloud;
    private int[] studentOnCloud = new int[5];
    private boolean isTaken;

    public Cloud(int idCloud) {
        this.idCloud = idCloud;
        isTaken = false;
        for(int i = 0; i < 5; i++) {
            studentOnCloud[i] = 0;
        }
    }

    public int getIdCloud() {
        return idCloud;
    }

    public int[] getStudents() {
        return studentOnCloud;
    }

    public boolean getIsTaken() {
        return isTaken;
    }

    public void addStudents(int[] toAdd) {
        for(int i = 0; i < 5; i++) {
            studentOnCloud[i] += toAdd[i];
        }
    }

    public void removeStudents() {
        for(int i = 0; i < 5; i++) {
            studentOnCloud[i] = 0;
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
