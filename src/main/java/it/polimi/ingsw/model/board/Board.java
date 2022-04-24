package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Game;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;

/**
 * Board of a player (container of Entrance, Dining Room, Profs table and towers)
 */
public class Board implements Serializable {

    @Serial
    private static final long serialVersionUID = 30L;

    private Entrance entrance;
    private DiningRoom diningRoom;
    private ProfessorsTable professorsTable;
    private TowersOnBoard towersOnBoard;
    private String nickname;


    public Board(String nickname) {
        this.entrance = new Entrance();
        this.diningRoom = new DiningRoom();
        this.professorsTable = new ProfessorsTable();
        this.towersOnBoard = new TowersOnBoard();
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public Entrance getEntrance() {
        return entrance;
    }

    public DiningRoom getDiningRoom() {
        return diningRoom;
    }

    public ProfessorsTable getProfessorsTable() {
        return professorsTable;
    }

    public TowersOnBoard getTowersOnBoard() {
        return towersOnBoard;
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }


}
