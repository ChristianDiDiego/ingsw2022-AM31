package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.board.DiningRoom;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class DiningRoomTest {
    private DiningRoom diningRoom = new DiningRoom();

    @Test
    public void addAndGetTest(){
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
