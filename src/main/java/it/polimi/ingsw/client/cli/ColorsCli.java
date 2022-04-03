package it.polimi.ingsw.client.cli;

public enum ColorsCli {
    CLEAR("\033[H\033[2J"),
    RESET("\033[0m"); //Color end string, color reset

    public static final String BLACK ="\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PINK = "\033[1;35m";  // PINK
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\u001B[37m"; //WHITE

    private final String toPrint;

    ColorsCli(String toPrint) {
        this.toPrint = toPrint;
    }

    static String getColorByNumber(int n){
        switch (n){
            case 0:
                return GREEN;
            case 1:
                return RED;
            case 2:
                return YELLOW;
            case 3:
                return PINK;
            case 4:
                return CYAN;
            default:
                return BLACK;
        }
    }

    @Override
    public String toString() {
        return toPrint;
    }
}

