package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.StudsAndProfsColor;

public class ProfessorsTable {
    /*This array contains the status of the prof for each color depending on the position.
  0 - RED
  1 - GREEN
  2 - YELLOW
  3 - PINK
  4 - BLUE
 */
    private boolean[] hasProfessor;

    public ProfessorsTable(){
        this.hasProfessor = new boolean[5];
    }

    //Receive the color ( identified by the enum) of the Professor to remove from the board
    public void removeProfessor(StudsAndProfsColor profColor){
        hasProfessor[profColor.ordinal()] = false;
    }

    //Receive the color ( identified by the enum) of the Professor to add in the board
    public void addProfessor(StudsAndProfsColor profColor){
        hasProfessor[profColor.ordinal()] = true;
    }

    //return true if in the table there is the professor of color profColor
    public boolean getHasProf(StudsAndProfsColor profColor){
        return hasProfessor[profColor.ordinal()];
    }
}
