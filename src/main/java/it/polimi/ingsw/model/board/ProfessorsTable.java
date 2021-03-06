package it.polimi.ingsw.model.board;

import it.polimi.ingsw.utilities.Constants;
import it.polimi.ingsw.model.StudsAndProfsColor;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents the professors table allowing to remove or add them
 */
public class ProfessorsTable implements Serializable {
    /*This array contains the status of the prof for each color depending on the position.
    0 - RED
    1 - GREEN
    2 - YELLOW
    3 - PINK
    4 - BLUE
     */

    @Serial
    private static final long serialVersionUID = 5L;
    private final boolean[] hasProfessor;

    public ProfessorsTable() {
        this.hasProfessor = new boolean[Constants.NUMBEROFKINGDOMS];
    }

    /**
     * Remove a professor from the player's board
     *
     * @param profColor color of the Professor to be removed
     */
    public void removeProfessor(StudsAndProfsColor profColor) {
        hasProfessor[profColor.ordinal()] = false;
    }

    /**
     * Add a professor in the player's board
     *
     * @param profColor Color of the professor to be added
     */
    public void addProfessor(StudsAndProfsColor profColor) {
        hasProfessor[profColor.ordinal()] = true;
    }

    /**
     * Check if the player has a professor
     *
     * @param profColor color of the Professor to be checked
     * @return true if the professor of color profColor is on the board, false otherwise
     */
    public boolean getHasProf(StudsAndProfsColor profColor) {
        return hasProfessor[profColor.ordinal()];
    }

    /**
     * @return number of professors on board
     */
    public int getNumberOfProf() {
        int number = 0;

        for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
            if (hasProfessor[i]) {
                number++;
            }
        }
        return number;
    }
}