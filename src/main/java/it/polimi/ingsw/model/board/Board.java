package it.polimi.ingsw.model.board;

import java.beans.PropertyChangeSupport;

public class Board {

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
}
