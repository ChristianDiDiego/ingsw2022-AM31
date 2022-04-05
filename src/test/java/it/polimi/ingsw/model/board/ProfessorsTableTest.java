package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.StudsAndProfsColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorsTableTest {
    ProfessorsTable professorsTable = new ProfessorsTable();

    @Test
    void removeProfessor() {
        assertFalse(professorsTable.getHasProf(StudsAndProfsColor.RED));
        assertFalse(professorsTable.getHasProf(StudsAndProfsColor.BLUE));
        professorsTable.addProfessor(StudsAndProfsColor.RED);
        professorsTable.addProfessor(StudsAndProfsColor.BLUE);
        professorsTable.removeProfessor(StudsAndProfsColor.RED);
        assertFalse(professorsTable.getHasProf(StudsAndProfsColor.RED));
        assertTrue(professorsTable.getHasProf(StudsAndProfsColor.BLUE));
    }

    @Test
    void addProfessor() {
        assertFalse(professorsTable.getHasProf(StudsAndProfsColor.RED));
        assertFalse(professorsTable.getHasProf(StudsAndProfsColor.BLUE));
        professorsTable.addProfessor(StudsAndProfsColor.RED);
        professorsTable.addProfessor(StudsAndProfsColor.BLUE);
        assertTrue(professorsTable.getHasProf(StudsAndProfsColor.RED));
        assertTrue(professorsTable.getHasProf(StudsAndProfsColor.BLUE));


    }
}