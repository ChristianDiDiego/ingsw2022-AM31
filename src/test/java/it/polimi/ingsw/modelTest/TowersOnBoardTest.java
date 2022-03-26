package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.board.TowersOnBoard;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TowersOnBoardTest {
    private TowersOnBoard towersOnBoard = new TowersOnBoard();

    @Test
    public void addAndGetTest(){
            towersOnBoard.addTower();
            towersOnBoard.addTower();
            towersOnBoard.addTower();
      assertEquals(3, towersOnBoard.getNumberOfTowers());

    }

    @Test
    public void removeTowers(){
            towersOnBoard.addTower();
            towersOnBoard.addTower();
            towersOnBoard.addTower();
            towersOnBoard.removeTower();
            towersOnBoard.removeTower();
        assertEquals(1, towersOnBoard.getNumberOfTowers());

    }

}
