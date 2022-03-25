package it.polimi.ingsw.model;

public class Island {
    private int idIsland;
    private int[] studentOnIsland = new int[5];
    private Player owner;

    public Island(int idIsland) {
        this.idIsland = idIsland;
        for(int i = 0; i < 5; i++) {
            studentOnIsland[i] = 0;
        }
    }
    public int getIdIsland(){
        return idIsland;
    }

    public Player getOwner() {
        return owner;
    }

    public int[] getAllStudents() {
        return studentOnIsland;
    }

    /**
     * returns the number of student of the selected color present on the island
     * @param color
     * @return
     */
    public int getStudentsByColor(int color) {
        return studentOnIsland[color];
    }

    /**
     * increments the counter of che received student's color in the island
     * @param color
     */
    public void addStudent(int color) {
        studentOnIsland[color] += 1;
    }

    /**
     * if a player conquered the island the method changes the owner
     * @param player
     */
    public void changeOwner(Player player) {
        this.owner = player;
    }
}
