package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.model.Bag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {

    /**
     * Check that the correct number of students is picked from the bag
     */
    @Test
    void getNumberOfLeftStudentsAndPickTest() {
        Bag bag = new Bag(3);
        assertEquals(120, bag.getNumberOfLeftStudents());

        bag.pickStudent(4);
        assertEquals(116, bag.getNumberOfLeftStudents());

        Bag bag2 = new Bag(4);
        assertEquals(120, bag2.getNumberOfLeftStudents());

        bag2.pickStudent(3);
        assertEquals(117, bag2.getNumberOfLeftStudents());


    }

}