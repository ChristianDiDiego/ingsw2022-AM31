package it.polimi.ingsw.modelTest.expertMode;

import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.expertMode.Character1;
import it.polimi.ingsw.model.expertMode.Character4;
import it.polimi.ingsw.model.expertMode.Character6;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests if the power of the character 6 works
 */
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
        //Case 2 players, oldOwner = null
        Player pl1 = new Player("leo", ColorOfTower.WHITE);
        pl1.setTeam(0);
        Player pl2 = new Player("Lisa", ColorOfTower.BLACK);
        pl2.setTeam(1);
        GameHandler gameHandler = new GameHandler(pl1, 2, true);
        gameHandler.addNewPlayer(pl2);
        pl1.getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.YELLOW);
        pl2.getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.RED);
        assertEquals(8,pl1.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        assertEquals(8,pl2.getMyBoard().getTowersOnBoard().getNumberOfTowers());
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.YELLOW);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.YELLOW);
        gameHandler.getController().getGame().getListOfArchipelagos().get(0).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.RED);
        gameHandler.getController().getGame().getCurrentPlayer().addCoinsToWallet(20);
        Character6 character6 = new Character6(gameHandler.getController().getGame());
        character6.usePower(StudsAndProfsColor.YELLOW);

        gameHandler.getController().getGame().getCurrentPlayer().setUsedCharacter(character6);
        gameHandler.getController().getTurnController().getActionController().calculateInfluence();

        assertEquals(pl2, gameHandler.getController().getGame().getListOfArchipelagos().get(0).getOwner());
        assertEquals(7,pl2.getMyBoard().getTowersOnBoard().getNumberOfTowers());

    }

}