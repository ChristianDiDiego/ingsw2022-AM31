package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.Archipelago;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArchipelagoTest {

    private Archipelago archi = new Archipelago(2);
    private Archipelago archiMN = new Archipelago(0);

    @Test
    void addIsland() {
        assertEquals(1, archi.getBelongingIslands().size());
        archi.addIsland(new Island(3));
        assertEquals(2, archi.getBelongingIslands().size());
    }

    @Test
    void changeMNPresence() {
        assertEquals(true, archiMN.getIsMNPresent());
        assertEquals(false, archi.getIsMNPresent());
    }

    @Test
    void changeOwner() {
        Player player = new Player("Pippo", ColorOfTower.BLACK);
        archi.changeOwner(player);
        assertEquals(player, archi.getOwner());
    }
}