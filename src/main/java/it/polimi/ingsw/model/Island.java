package it.polimi.ingsw.model;

import it.polimi.ingsw.utilities.Constants;

import java.io.Serial;
import java.io.Serializable;

/**
 * Island of an archipelago; contains the students that are on it
 */
public class Island implements Serializable {

    @Serial
    private static final long serialVersionUID = 8L;
    private final int idIsland;
    private final int[] studentOnIsland = new int[Constants.NUMBEROFKINGDOMS];
    /*This array contains the number of students for each color depending on the position.
      0 - RED
      1 - GREEN
      2 - YELLOW
      3 - PINK
      4 - BLUE
     */

    public Island(int idIsland) {
        this.idIsland = idIsland;
        for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
            studentOnIsland[i] = 0;
        }
    }

    public int getIdIsland() {
        return idIsland;
    }

    /**
     * @return array containing the number of students present on the island divided by color
     */
    public int[] getAllStudents() {
        return studentOnIsland;
    }

    /**
     * Calculate the number of the students of a color present on the island
     *
     * @param studColor color of the students to be counted
     * @return integer number of the students of studColor on the island
     */
    public int getStudentsByColor(StudsAndProfsColor studColor) {
        return studentOnIsland[studColor.ordinal()];
    }

    /**
     * increments the counter of che received student's color in the island
     *
     * @param studColor color of the student to be added
     */
    public void addStudent(StudsAndProfsColor studColor) {
        studentOnIsland[studColor.ordinal()] += 1;
    }

}
