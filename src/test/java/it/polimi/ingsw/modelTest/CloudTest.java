package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.Cloud;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CloudTest {
    Cloud cloud = new Cloud(3);

    /**
     * Check if the students are properly added to a cloud
     */
    @Test
    void addStudents() {
        assertEquals(true, cloud.getIsTaken());
        int[] toAdd = new int[]{2, 1, 3, 1, 2};
        cloud.addStudents(toAdd);
        assertEquals(false, cloud.getIsTaken());
    }

    /**
     * Check if the students are properly removed from a cloud
     */
    @Test
    void removeStudents() {
        int[] toAdd = new int[]{2, 1, 3, 1, 2};
        cloud.addStudents(toAdd);
        cloud.removeStudents();
        for(int i = 0; i < 5; i++) {
            assertEquals(0, cloud.getStudents()[i]);
        }
        assertEquals(true, cloud.getIsTaken());
    }
}