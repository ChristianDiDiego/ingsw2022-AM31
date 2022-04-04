package it.polimi.ingsw.model.expertMode;

import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.model.board.Board;
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
        this.descriptionOfPower = "The player can switch max 2 students between entrance and dining room";
    }


    /**
     * Switch 2 student in the entrance with 2 students in the dining room
     * @param color1ToAdd student in the entrance
     * @param color2ToAdd student in the entrance
     * @param color1ToRemove student in the dining room
     * @param color2ToRemove student in the dining room
     */
    public void usePower(StudsAndProfsColor color1ToAdd, StudsAndProfsColor color2ToAdd, StudsAndProfsColor color1ToRemove, StudsAndProfsColor color2ToRemove) {
        Entrance entrance = game.getCurrentPlayer().getMyBoard().getEntrance();
        DiningRoom diningRoom = game.getCurrentPlayer().getMyBoard().getDiningRoom();
        if(entrance.getStudentsByColor(color1ToAdd) > 0 && entrance.getStudentsByColor(color2ToAdd) > 0 && diningRoom.getStudentsByColor(color1ToRemove) > 0 && diningRoom.getStudentsByColor(color2ToRemove) > 0) {
            if (payForUse()) {
                int[] toAdd = new int[Constants.NUMBEROFKINGDOMS];
                toAdd[color1ToRemove.ordinal()]++;
                toAdd[color2ToRemove.ordinal()]++;

                entrance.removeStudent(color1ToAdd);
                entrance.removeStudent(color2ToAdd);
                diningRoom.addStudent(color1ToAdd);
                diningRoom.addStudent(color1ToAdd);
                diningRoom.removeStudent(color1ToRemove);
                diningRoom.removeStudent(color2ToRemove);
                entrance.addStudent(toAdd);
            }
        } else {
            System.out.println("You don't have these students");
        }
    }
}
