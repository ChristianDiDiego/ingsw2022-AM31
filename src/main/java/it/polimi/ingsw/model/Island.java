package it.polimi.ingsw.model;

public class Island {
    private int idIsland;
    private int[] studentOnIsland = new int[5];
    /*This array contains the number of students for each color depending on the position.
      0 - RED
      1 - GREEN
      2 - YELLOW
      3 - PINK
      4 - BLUE
     */
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

    public Player getOwner() throws NullPointerException {
        if(owner != null) {
            return owner;
        } else {
            throw new NullPointerException();
        }
    }

    public int[] getAllStudents() {
        return studentOnIsland;
    }

    /**
     * returns the number of student of the selected color present on the island
     * @param studColor
     * @return integer
     */
    public int getStudentsByColor(StudsAndProfsColor studColor) {
        return studentOnIsland[studColor.ordinal()];
    }

    /**
     * increments the counter of che received student's color in the island
     * @param studColor
     */
    public void addStudent(StudsAndProfsColor studColor) {
        studentOnIsland[studColor.ordinal()] += 1;
    }

    /**
     * if a player conquered the island the method changes the owner
     * @param player
     */
    public void changeOwner(Player player) {
        this.owner = player;
    }
}
