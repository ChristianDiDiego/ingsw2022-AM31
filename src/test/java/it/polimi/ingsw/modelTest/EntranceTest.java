package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.board.Entrance;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Check if the methods of Entrance class work
 */
public class EntranceTest {
    private Entrance entrance = new Entrance();
    int[] i = {1,0,1,1,4};

    /**
     * Test that the students are properly added to the Entrance
     */
    @Test
    public void addAndGetTest(){
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.PINK));
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.BLUE));
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.BLUE));

        entrance.addStudent(i);
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(1, entrance.getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(1, entrance.getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(1, entrance.getStudentsByColor(StudsAndProfsColor.PINK));
        assertEquals(4, entrance.getStudentsByColor(StudsAndProfsColor.BLUE));
    }

    /**
     * Test that the students are properly removed from the entrance
     */
    @Test
    public void removeAndGetTest(){
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.PINK));
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.BLUE));
        entrance.addStudent(i);
        entrance.removeStudent(StudsAndProfsColor.BLUE);
        entrance.removeStudent(StudsAndProfsColor.BLUE);

        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(1, entrance.getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(1, entrance.getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(1, entrance.getStudentsByColor(StudsAndProfsColor.PINK));
        assertEquals(2, entrance.getStudentsByColor(StudsAndProfsColor.BLUE));

    }


}
