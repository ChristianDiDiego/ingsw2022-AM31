package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.board.DiningRoomFullException;
import it.polimi.ingsw.exceptions.board.EntranceFullException;
import it.polimi.ingsw.exceptions.board.NoStudentsOfColorException;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.board.Entrance;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class EntranceTest {
    private Entrance entrance;

    @Test
    public void addAndGetTest(){
    entrance = new Entrance();

        try {
            entrance.addStudent(StudsAndProfsColor.BLUE);
            entrance.addStudent(StudsAndProfsColor.BLUE);
            entrance.addStudent(StudsAndProfsColor.BLUE);
            entrance.addStudent(StudsAndProfsColor.BLUE);
            entrance.addStudent(StudsAndProfsColor.YELLOW);
            entrance.addStudent(StudsAndProfsColor.GREEN);
            entrance.addStudent(StudsAndProfsColor.PINK);
        } catch (EntranceFullException e) {
            e.printStackTrace();
        }
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(1, entrance.getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(1, entrance.getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(1, entrance.getStudentsByColor(StudsAndProfsColor.PINK));
        assertEquals(4, entrance.getStudentsByColor(StudsAndProfsColor.BLUE));
    }

    @Test
    public void entranceFullException(){
        entrance = new Entrance();
        assertThrows(EntranceFullException.class, () ->{
            entrance.addStudent(StudsAndProfsColor.BLUE);
            entrance.addStudent(StudsAndProfsColor.BLUE);
            entrance.addStudent(StudsAndProfsColor.BLUE);
            entrance.addStudent(StudsAndProfsColor.BLUE);
            entrance.addStudent(StudsAndProfsColor.YELLOW);
            entrance.addStudent(StudsAndProfsColor.GREEN);
            entrance.addStudent(StudsAndProfsColor.PINK);
            entrance.addStudent(StudsAndProfsColor.PINK);
        });

    }

    @Test
    public void removeAndGetTest(){
        entrance = new Entrance();
        try {
            entrance.addStudent(StudsAndProfsColor.BLUE);
            entrance.addStudent(StudsAndProfsColor.BLUE);
            entrance.addStudent(StudsAndProfsColor.BLUE);
            entrance.addStudent(StudsAndProfsColor.BLUE);
            entrance.addStudent(StudsAndProfsColor.YELLOW);
            entrance.addStudent(StudsAndProfsColor.GREEN);
            entrance.addStudent(StudsAndProfsColor.PINK);
        } catch (EntranceFullException e) {
            e.printStackTrace();
        }
        try {
            entrance.removeStudent(StudsAndProfsColor.BLUE);
            entrance.removeStudent(StudsAndProfsColor.BLUE);
        } catch (NoStudentsOfColorException e) {
            e.printStackTrace();
        }
        assertEquals(0, entrance.getStudentsByColor(StudsAndProfsColor.RED));
        assertEquals(1, entrance.getStudentsByColor(StudsAndProfsColor.GREEN));
        assertEquals(1, entrance.getStudentsByColor(StudsAndProfsColor.YELLOW));
        assertEquals(1, entrance.getStudentsByColor(StudsAndProfsColor.PINK));
        assertEquals(2, entrance.getStudentsByColor(StudsAndProfsColor.BLUE));

    }

    @Test
    public void noStudentsOfColorException(){
        entrance = new Entrance();
        try {
            entrance.addStudent(StudsAndProfsColor.BLUE);
            entrance.addStudent(StudsAndProfsColor.YELLOW);
            entrance.addStudent(StudsAndProfsColor.GREEN);
            entrance.addStudent(StudsAndProfsColor.PINK);
        } catch (EntranceFullException e) {
            e.printStackTrace();
        }
        assertThrows(NoStudentsOfColorException.class, () ->{
            entrance.removeStudent(StudsAndProfsColor.RED);
        });

    }
}
