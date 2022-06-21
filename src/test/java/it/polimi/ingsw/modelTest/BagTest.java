package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.Bag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests if the students are correctly picked from the bag
 */
class BagTest {

    /**
     * Check that the correct number of students is picked from the bag
     */
    @Test
    void getNumberOfLeftStudentsAndPickTest() {
        Bag bag = new Bag();
        assertEquals(120, bag.getNumberOfLeftStudents());

        bag.pickStudent(4);
        assertEquals(116, bag.getNumberOfLeftStudents());

        Bag bag2 = new Bag();
        assertEquals(120, bag2.getNumberOfLeftStudents());

        bag2.pickStudent(3);
        assertEquals(117, bag2.getNumberOfLeftStudents());


    }

}