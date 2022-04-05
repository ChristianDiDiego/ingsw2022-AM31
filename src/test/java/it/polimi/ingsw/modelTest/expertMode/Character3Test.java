package it.polimi.ingsw.modelTest.expertMode;

import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.expertMode.Character3;
import it.polimi.ingsw.model.expertMode.Character5;
import it.polimi.ingsw.model.expertMode.Character7;
import it.polimi.ingsw.model.expertMode.Character8;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Character3Test {
    Player player1 = new Player("player1", ColorOfTower.BLACK);
    Player player2 = new Player("player2", ColorOfTower.WHITE);
    Game game = new Game(2, player1);
    Character3 character3 = new Character3(game);
    GameHandler gameHandler = new GameHandler(player1, 2);

    /**
     * check if usePower correclty sets the forbidden parameter in chosen archipelago
     * and if players can use this power only four times
     */
    @Test
    void usePower() {
        game.addPlayer(player2);
        player1.addCoinsToWallet(20);
        assertEquals(false,game.getListOfArchipelagos().get(2).getIsForbidden());
        character3.usePower(2);
        assertEquals(true, game.getListOfArchipelagos().get(2).getIsForbidden());
        character3.usePower(3);
        character3.usePower(1);
        character3.usePower(4);
        character3.usePower(5);
        assertEquals(false, game.getListOfArchipelagos().get(5).getIsForbidden());
        assertEquals(null, game.getListOfArchipelagos().get(5).getOwner());
    }
}