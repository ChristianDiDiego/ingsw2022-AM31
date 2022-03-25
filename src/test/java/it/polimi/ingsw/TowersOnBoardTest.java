package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.board.TowersBoardEmptyException;
import it.polimi.ingsw.exceptions.board.TowersBoardFullException;
import it.polimi.ingsw.model.board.TowersOnBoard;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TowersOnBoardTest {
    private TowersOnBoard towersOnBoard;

    @Test
    public void addAndGetTest(){
        towersOnBoard = new TowersOnBoard();
        try {
            towersOnBoard.addTower();
            towersOnBoard.addTower();
            towersOnBoard.addTower();
        } catch (TowersBoardFullException e) {
            e.printStackTrace();
        }
      assertEquals(3, towersOnBoard.getNumberOfTowers());

    }

    @Test
    public void towersBoardFullException(){
        towersOnBoard = new TowersOnBoard();
        assertThrows(TowersBoardFullException.class, ()->{
            towersOnBoard.addTower();
            towersOnBoard.addTower();
            towersOnBoard.addTower();
            towersOnBoard.addTower();
            towersOnBoard.addTower();
            towersOnBoard.addTower();
            towersOnBoard.addTower();
            towersOnBoard.addTower();
            towersOnBoard.addTower();
        });
    }

    @Test
    public void removeTowers(){
        towersOnBoard = new TowersOnBoard();
        try {
            towersOnBoard.addTower();
            towersOnBoard.addTower();
            towersOnBoard.addTower();
        } catch (TowersBoardFullException e) {
            e.printStackTrace();
        }
        try {
            towersOnBoard.removeTower();
            towersOnBoard.removeTower();
        } catch (TowersBoardEmptyException e) {
            e.printStackTrace();
        }
        assertEquals(1, towersOnBoard.getNumberOfTowers());

    }

    @Test
    public void TowersBoardEmptyException(){
        towersOnBoard = new TowersOnBoard();
        assertThrows(TowersBoardEmptyException.class, ()->{
            towersOnBoard.removeTower();
        });
    }
}
