package it.polimi.ingsw.model;

import it.polimi.ingsw.utilities.Constants;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serial;
import java.io.Serializable;

/**
 * Cloud from where the player can pick the students at the end of his turn
 */
public class Cloud implements Serializable {
    @Serial
    private static final long serialVersionUID = 11L;
    private final int idCloud;
    private final int[] studentsOnCloud = new int[Constants.NUMBEROFKINGDOMS];
    /*This array contains the number of students for each color depending on the position.
      0 - RED
      1 - GREEN
      2 - YELLOW
      3 - PINK
      4 - BLUE
     */
    private boolean isTaken;
    private final PropertyChangeSupport support;

    public Cloud(int idCloud) {
        this.support = new PropertyChangeSupport(this);
        this.idCloud = idCloud;
        isTaken = true;
        for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
            studentsOnCloud[i] = 0;
        }
    }

    public int getIdCloud() {
        return idCloud;
    }

    /**
     * get the array containing the students on the cloud
     *
     * @return the array of students on the cloud
     */
    public int[] getStudents() {
        return studentsOnCloud;
    }

    public boolean getIsTaken() {
        return isTaken;
    }

    /**
     * Called at the beginning of each turn to add students to the cloud
     *
     * @param toAdd array that contains that students that needs to be added to the cloud
     */
    public void addStudents(int[] toAdd) {
        for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
            studentsOnCloud[i] += toAdd[i];
        }
        changeStatus();
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    /**
     * When a cloud is chosen it removes all the students from it
     */
    public void removeStudents() {
        for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
            studentsOnCloud[i] = 0;
        }
        changeStatus();
    }

    /**
     * it sets the status of de cloud -if it has already been chosen or not
     */
    public void changeStatus() {
        this.isTaken = !isTaken;
    }
}