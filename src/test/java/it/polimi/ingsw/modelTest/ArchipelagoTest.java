package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.model.Archipelago;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.utilities.constants.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArchipelagoTest {

    private Archipelago archi = new Archipelago(2);
    private Archipelago archiMN = new Archipelago(Constants.IDSTARTINGARCMN);

    /**
     * Check if an island is properly added to an archipelago
     */
    @Test
    void addIsland() {
        assertEquals(1, archi.getBelongingIslands().size());
        archi.addIsland(new Island(3));
        assertEquals(2, archi.getBelongingIslands().size());
    }

    /**
     * Check if the presence of MN is correctly checked
     */
    @Test
    void changeMNPresence() {
        assertEquals(true, archiMN.getIsMNPresent());
        assertEquals(false, archi.getIsMNPresent());
    }

    /**
     * Check if the ownership of an archipelago is properly changed
     */
    @Test
    void changeOwner() {
        Player player = new Player("Pippo", ColorOfTower.BLACK);
        assertEquals(null, archi.getOwner());
        archi.changeOwner(player);
        assertEquals(player, archi.getOwner());
    }
}