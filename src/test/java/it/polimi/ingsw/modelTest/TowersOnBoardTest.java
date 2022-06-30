package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.board.TowersOnBoard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Check if the methods of TowersOnBoard class work
 */
public class TowersOnBoardTest {
    private TowersOnBoard towersOnBoard = new TowersOnBoard();

    /**
     * Check that a tower is properly added to the board
     */
    @Test
    public void addAndGetTest(){
        assertEquals(0, towersOnBoard.getNumberOfTowers());
        towersOnBoard.addTower();
        towersOnBoard.addTower();
        towersOnBoard.addTower();
      assertEquals(3, towersOnBoard.getNumberOfTowers());

    }

    /**
     * Check that a tower is properly removed from the board
     */
    @Test
    public void removeTowers(){
        assertEquals(0, towersOnBoard.getNumberOfTowers());
            towersOnBoard.addTower();
            towersOnBoard.addTower();
            towersOnBoard.addTower();
            towersOnBoard.removeTower();
            towersOnBoard.removeTower();
        assertEquals(1, towersOnBoard.getNumberOfTowers());

    }

}
