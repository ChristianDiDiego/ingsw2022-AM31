package it.polimi.ingsw.modelTest.expertMode;

import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.expertMode.Character4;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Character4Test {
    Player player1 = new Player("player1", ColorOfTower.BLACK);
    Player player2 = new Player("player2", ColorOfTower.WHITE);
    GameHandler gameHandler = new GameHandler(player1, 2,false);
    Character4 character4 = new Character4(gameHandler.getGame());

    /**
     * check if character is correctly assigned to currentPlayer
     */
    @Test
    void usePower(){
        gameHandler.getGame().addPlayer(player2);
        character4.usePower();
        assertNotEquals(character4, gameHandler.getGame().getCurrentPlayer().getUsedCharacter());
        player1.addCoinsToWallet(20);
        character4.usePower();
        assertEquals(character4, gameHandler.getGame().getCurrentPlayer().getUsedCharacter());
    }

    /**
     * check if influence is calculated without counting towers
     * TODO: check if it works

    @Test
    void calculateInfluence(){

        gameHandler.getGame().addPlayer(player2);
        gameHandler.getGame().getListOfArchipelagos().get(2).getBelongingIslands().get(0).addStudent(StudsAndProfsColor.BLUE);
        gameHandler.getGame().getCurrentPlayer().getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.BLUE);
        gameHandler.getGame().moveMotherNature(2);
        gameHandler.getController().getTurnController().getActionController().calculateInfluence();
        assertEquals(player1, gameHandler.getGame().getListOfArchipelagos().get(2).getOwner());
        gameHandler.getGame().getCurrentPlayer().getMyBoard().getProfessorsTable().removeProfessor(StudsAndProfsColor.BLUE);
        gameHandler.getGame().getListOfPlayer().get(1).getMyBoard().getProfessorsTable().addProfessor(StudsAndProfsColor.BLUE);
        assertEquals(true, gameHandler.getGame().getListOfPlayer().get(1).getMyBoard().getProfessorsTable().getHasProf(StudsAndProfsColor.BLUE));
        character4.calculateInfluence();
        assertEquals(player2, gameHandler.getGame().getListOfArchipelagos().get(2).getOwner());


    }
     */
}