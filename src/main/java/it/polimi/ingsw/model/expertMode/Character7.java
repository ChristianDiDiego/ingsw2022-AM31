package it.polimi.ingsw.model.expertMode;

import it.polimi.ingsw.utilities.constants.Constants;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.board.DiningRoom;
import it.polimi.ingsw.model.board.Entrance;

/**
 * image 9 in image folder ;
 * card 10 in list of cards ( in game tutorial)
 * The player can switch max 2 students between entrance and dining room
 */
public class Character7 extends Characters{

    public Character7(Game game) {
        super(1, game);
        id = 7;
        this.descriptionOfPower = "The player can switch max 2 students between entrance and dining room \n" +
                "Usage CHARACTER 7 [COLOR ENTRANCE],[COLOR ENTRANCE],[COLOR DR],[COLOR DR]";
    }


    /**
     * Switch 2 student in the entrance with 2 students in the dining room
     * @param color1ToAdd student in the entrance
     * @param color2ToAdd student in the entrance
     * @param color1ToRemove student in the dining room
     * @param color2ToRemove student in the dining room
     */
    public boolean usePower(StudsAndProfsColor color1ToAdd, StudsAndProfsColor color2ToAdd, StudsAndProfsColor color1ToRemove, StudsAndProfsColor color2ToRemove) {
        //Entrance entrance = game.getCurrentPlayer().getMyBoard().getEntrance();
        //DiningRoom diningRoom = game.getCurrentPlayer().getMyBoard().getDiningRoom();
        if(checkStudent(color1ToAdd, color2ToAdd, color1ToRemove, color2ToRemove)) {
            if (payForUse()) {
                int[] toAdd = {0,0,0,0,0};
                toAdd[color1ToRemove.ordinal()] += 1;
                toAdd[color2ToRemove.ordinal()] += 1;

                game.getCurrentPlayer().getMyBoard().getEntrance().removeStudent(color1ToAdd);
                game.getCurrentPlayer().getMyBoard().getEntrance().removeStudent(color2ToAdd);
                game.getCurrentPlayer().getMyBoard().getDiningRoom().addStudent(color1ToAdd);
                game.getCurrentPlayer().getMyBoard().getDiningRoom().addStudent(color2ToAdd);
                game.getCurrentPlayer().getMyBoard().getDiningRoom().removeStudent(color1ToRemove);
                game.getCurrentPlayer().getMyBoard().getDiningRoom().removeStudent(color2ToRemove);
                game.getCurrentPlayer().getMyBoard().getEntrance().addStudent(toAdd);
                game.assignProfessor(color1ToAdd);
                game.assignProfessor(color2ToAdd);
                game.assignProfessor(color1ToRemove);
                game.assignProfessor(color2ToRemove);
                return true;
            } else {
                return false;
            }
        }else {
            System.out.println("You don't have some of these students");
            return false;
        }
    }

    public boolean checkStudent(StudsAndProfsColor color1ToAdd, StudsAndProfsColor color2ToAdd, StudsAndProfsColor color1ToRemove, StudsAndProfsColor color2ToRemove) {
        if(game.getCurrentPlayer().getMyBoard().getEntrance().getStudentsByColor(color1ToAdd) < 1 && game.getCurrentPlayer().getMyBoard().getEntrance().getStudentsByColor(color2ToAdd) < 1 && game.getCurrentPlayer().getMyBoard().getDiningRoom().getStudentsByColor(color1ToRemove) < 1 && game.getCurrentPlayer().getMyBoard().getDiningRoom().getStudentsByColor(color2ToRemove) < 1) {
            return false;
        } else {
            if(color1ToAdd == color2ToAdd && game.getCurrentPlayer().getMyBoard().getEntrance().getStudentsByColor(color1ToAdd) < 2) {
                return false;
            } else if(color1ToRemove == color2ToRemove && game.getCurrentPlayer().getMyBoard().getDiningRoom().getStudentsByColor(color1ToRemove) < 2) {
                return false;
            }
            return true;
        }
    }
}
