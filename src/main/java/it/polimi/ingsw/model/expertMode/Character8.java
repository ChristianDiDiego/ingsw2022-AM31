package it.polimi.ingsw.model.expertMode;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.utilities.Constants;

/**
 * image 12 in image folder;
 * card 2 in list of cards ( in game tutorial)
 * When this card is used, assignProfessor assign the professor to the player who use the card
 * also if the number of students in the dining rooms of the two players is the same
 */
public class Character8 extends Characters {

    public Character8(Game game) {
        super(2, game);
        id = 8;
        this.descriptionOfPower = "When this card is used, assignProfessor assign the professor to the player who use the card also if the number of students in the dining rooms of the two players is the same ";
    }

    public boolean usePower() {
        if (payForUse()) {
            for(int i = 0; i < Constants.NUMBEROFKINGDOMS; i++){
                if(game.getCurrentPlayer().getMyBoard().getDiningRoom().getStudentsByColor(StudsAndProfsColor.values()[i]) > 0){
                    assignProfessor(StudsAndProfsColor.values()[i]);
                }
            }
            game.getCurrentPlayer().setUsedCharacter(this);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Assign the professor even if the students are equal in number
     */
    public void assignProfessor(StudsAndProfsColor color) {
        Player player = game.getCurrentPlayer();
        int max = 0;
        for (Player p : game.getListOfPlayer()) {
            if (p != game.getCurrentPlayer()) {
                if (p.getMyBoard().getDiningRoom().getStudentsByColor(color) > max) {
                    player = p;
                    max = p.getMyBoard().getDiningRoom().getStudentsByColor(color);
                }
            }
        }
        if (game.getCurrentPlayer().getMyBoard().getDiningRoom().getStudentsByColor(color) >= player.getMyBoard().getDiningRoom().getStudentsByColor(color)) {
            player.getMyBoard().getProfessorsTable().removeProfessor(color);
            game.getCurrentPlayer().getMyBoard().getProfessorsTable().addProfessor(color);
        }
    }
}