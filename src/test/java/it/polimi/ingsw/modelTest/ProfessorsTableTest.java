package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.board.ProfessorsTable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Check if the methods of ProfessorsTable class work
 * tests if the professors are properly removed/added
 */
class ProfessorsTableTest {
    ProfessorsTable professorsTable = new ProfessorsTable();

    @Test
    void removeProfessor() {
        assertFalse(professorsTable.getHasProf(StudsAndProfsColor.RED));
        assertFalse(professorsTable.getHasProf(StudsAndProfsColor.BLUE));
        assertEquals(0,professorsTable.getNumberOfProf());
        professorsTable.addProfessor(StudsAndProfsColor.RED);
        professorsTable.addProfessor(StudsAndProfsColor.BLUE);
        professorsTable.removeProfessor(StudsAndProfsColor.RED);
        assertFalse(professorsTable.getHasProf(StudsAndProfsColor.RED));
        assertTrue(professorsTable.getHasProf(StudsAndProfsColor.BLUE));
        assertEquals(1,professorsTable.getNumberOfProf());
    }

    @Test
    void addProfessor() {
        assertFalse(professorsTable.getHasProf(StudsAndProfsColor.RED));
        assertFalse(professorsTable.getHasProf(StudsAndProfsColor.BLUE));
        assertEquals(0,professorsTable.getNumberOfProf());
        professorsTable.addProfessor(StudsAndProfsColor.RED);
        professorsTable.addProfessor(StudsAndProfsColor.BLUE);
        assertTrue(professorsTable.getHasProf(StudsAndProfsColor.RED));
        assertTrue(professorsTable.getHasProf(StudsAndProfsColor.BLUE));
        assertEquals(2,professorsTable.getNumberOfProf());


    }
}