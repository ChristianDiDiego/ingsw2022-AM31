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
    
    /**
     * at the beginning of each turn adds a student in the cloud
     * @param toAdd
     */
    public void addStudents(int[] toAdd) {
        for(int i = 0; i < 5; i++) {
            studentsOnCloud[i] += toAdd[i];
        }
    }

    /**
     * when a cloud is chosen it removes all the students from it
     */
    public void removeStudents() {
        for(int i = 0; i < 5; i++) {
            studentsOnCloud[i] = 0;
        }
    }

    /**
     * it sets the status of de cloud -if it has already been chosen or not
     */
    public void changeStatus() {
        if (isTaken == false) {
            this.isTaken = true;
        } else {
            this.isTaken = false;
        }
    }

}
