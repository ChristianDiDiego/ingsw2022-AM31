package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.model.Bag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {

    @Test
    void getNumberOfLeftStudentsAndPickTest() {
        Bag bag = new Bag(3);
        assertEquals(130, bag.getNumberOfLeftStudents());

        bag.pickStudent();
        assertEquals(126, bag.getNumberOfLeftStudents());

        Bag bag2 = new Bag(4);
        assertEquals(130, bag2.getNumberOfLeftStudents());

        bag2.pickStudent();
        assertEquals(127, bag2.getNumberOfLeftStudents());


    }

}