package it.polimi.ingsw.modelTest.expertMode;

import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.expertMode.Character1;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Character1Test {
    Player player1 = new Player("player1", ColorOfTower.BLACK);
    Player player2 = new Player("player2", ColorOfTower.WHITE);
    GameHandler gameHandler = new GameHandler(player1, 2,false);
    Character1 character1 = new Character1(gameHandler.getGame());

    @Test
    void usePower() {
        gameHandler.getGame().addPlayer(player2);
        player1.addCoinsToWallet(20);
        gameHandler.getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.BLUE);
        gameHandler.getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.BLUE);
        gameHandler.getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.BLUE);
        gameHandler.getGame().getCurrentPlayer().getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.BLUE);
        character1.usePower(1);
        assertEquals(character1, gameHandler.getGame().getCurrentPlayer().getUsedCharacter());
        assertEquals(player1, gameHandler.getGame().getListOfArchipelagos().get(0).getOwner());

    }
}