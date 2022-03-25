package it.polimi.ingsw.model.board;

public class Board {

    private Entrance entrance;
    private DiningRoom diningRoom;
    private ProfessorsTable professorsTable;
    private TowersOnBoard towersOnBoard;


    public Board() {
        this.entrance = new Entrance();
        this.diningRoom = new DiningRoom();
        this.professorsTable = new ProfessorsTable();
        this.towersOnBoard = new TowersOnBoard();
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
