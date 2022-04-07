package it.polimi.ingsw.modelTest.expertMode;

import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.expertMode.Character1;
import it.polimi.ingsw.model.expertMode.Character6;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Character6Test {
    Player player1 = new Player("player1", ColorOfTower.BLACK);
    Player player2 = new Player("player2", ColorOfTower.WHITE);
    Game game = new Game(2, player1,false);
    Character6 character6 = new Character6(game);
    GameHandler gameHandler = new GameHandler(player1, 2,false);

    @Test
    void usePower() {
        player1.addCoinsToWallet(20);
        character6.usePower(StudsAndProfsColor.BLUE);
        assertEquals(character6, game.getCurrentPlayer().getUsedCharacter());
    }

    /**
     * Even if the player has the professor of the blue color and on the archipelago
     * there are 3 blue students, he cannot conquer it due to the character6
     */
    @Test
    void calculateInfluence() {
        player1.addCoinsToWallet(20);
        character6.usePower(StudsAndProfsColor.BLUE);
        game.getListOfArchipelagos().get(1).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.BLUE);
        game.getListOfArchipelagos().get(1).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.BLUE);
        game.getListOfArchipelagos().get(1).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.BLUE);
        game.getCurrentPlayer().getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.BLUE);
        game.moveMotherNature(1);
        character6.calculateInfluence();
        assertEquals(null, game.getListOfArchipelagos().get(1).getOwner());
    }
}