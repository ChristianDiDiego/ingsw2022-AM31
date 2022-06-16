package it.polimi.ingsw.model.board;

import it.polimi.ingsw.utilities.constants.Constants;
import it.polimi.ingsw.model.StudsAndProfsColor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serial;
import java.io.Serializable;

/**
 * Represents the entrance of a player; allow the movements of the students from/for the entrance
 */
public class Entrance implements Serializable {
    /*This array contains the number of students for each color depending on the position.
      0 - RED
      1 - GREEN
      2 - YELLOW
      3 - PINK
      4 - BLUE
     */

    @Serial
    private static final long serialVersionUID = 4L;
    private int[] studentsInEntrance;
    private PropertyChangeSupport support;

    public Entrance(){
        studentsInEntrance = new int[Constants.NUMBEROFKINGDOMS];
        this.support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    /**
     * Remove a student from the entrance
     * @param studColor color of the student to be removed
     */
    public void removeStudent(StudsAndProfsColor studColor) {
            studentsInEntrance[studColor.ordinal()]--;
            support.firePropertyChange("RemovedStudentFromEntrance", 0, 1);
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
}
