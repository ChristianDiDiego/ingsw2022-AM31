package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.exceptions.board.DiningRoomFullException;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.board.DiningRoom;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class DiningRoomTest {
    private DiningRoom diningRoom;

    @BeforeEach
    void before(){
        diningRoom = new DiningRoom();
    }
    @Test
    public void addAndGetTest(){
        diningRoom = new DiningRoom();
        try {
            diningRoom.addStudent(StudsAndProfsColor.RED);
            diningRoom.addStudent(StudsAndProfsColor.RED);
            diningRoom.addStudent(StudsAndProfsColor.RED);
            diningRoom.addStudent(StudsAndProfsColor.BLUE);
            diningRoom.addStudent(StudsAndProfsColor.GREEN);
        } catch (DiningRoomFullException e) {
            e.printStackTrace();
        }
        assertEquals(3, diningRoom.getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(1, diningRoom.getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(0, diningRoom.getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(0, diningRoom.getStudentsByColor(StudsAndProfsColor.PINK));
        assertEquals(1, diningRoom.getStudentsByColor(StudsAndProfsColor.BLUE));

    }

    @Test
    public void diningRoomFullException(){
        diningRoom = new DiningRoom();
        assertThrows(DiningRoomFullException.class, () ->{
            diningRoom.addStudent(StudsAndProfsColor.RED);
            diningRoom.addStudent(StudsAndProfsColor.RED);
            diningRoom.addStudent(StudsAndProfsColor.RED);
            diningRoom.addStudent(StudsAndProfsColor.RED);
            diningRoom.addStudent(StudsAndProfsColor.RED);
            diningRoom.addStudent(StudsAndProfsColor.RED);
            diningRoom.addStudent(StudsAndProfsColor.RED);
            diningRoom.addStudent(StudsAndProfsColor.RED);
            diningRoom.addStudent(StudsAndProfsColor.RED);
            diningRoom.addStudent(StudsAndProfsColor.RED);
            diningRoom.addStudent(StudsAndProfsColor.RED);
            diningRoom.addStudent(StudsAndProfsColor.RED);
        });

    }
}
