package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.board.DiningRoom;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Check if the methods of DiningRoom class work
 */
public class DiningRoomTest {
    private DiningRoom diningRoom = new DiningRoom();

    /**
     * Test that the students are properly added to the dining room
     */
    @Test
    public void addAndGetTest(){
        assertEquals(0, diningRoom.getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(0, diningRoom.getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(0, diningRoom.getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(0, diningRoom.getStudentsByColor(StudsAndProfsColor.PINK));
        assertEquals(0, diningRoom.getStudentsByColor(StudsAndProfsColor.BLUE));
        diningRoom.addStudent(StudsAndProfsColor.RED);
        diningRoom.addStudent(StudsAndProfsColor.RED);
        diningRoom.addStudent(StudsAndProfsColor.RED);
        diningRoom.addStudent(StudsAndProfsColor.BLUE);
        diningRoom.addStudent(StudsAndProfsColor.GREEN);
        assertEquals(3, diningRoom.getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(1, diningRoom.getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(0, diningRoom.getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(0, diningRoom.getStudentsByColor(StudsAndProfsColor.PINK));
        assertEquals(1, diningRoom.getStudentsByColor(StudsAndProfsColor.BLUE));

    }

}
