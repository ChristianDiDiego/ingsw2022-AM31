package it.polimi.ingsw.client.cli;

/**
 * Enums of the colors to be used in cli
 */
public enum ColorsCli {
    CLEAR("\033[H\033[2J"),
    RESET("\033[0m"); //Color end string, color reset

    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String RED = "\033[0;31m";     // RED

    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PINK = "\033[1;35m";  // PINK
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\u001B[37m"; //WHITE

    private final String toPrint;

    ColorsCli(String toPrint) {
        this.toPrint = toPrint;
    }

    /**
     * returns the color of the corresponding number
     *
     * @param n number of the color typed
     * @return the name of the color
     */
    static String getColorByNumber(int n) {
        return switch (n) {
            case 0 -> GREEN;
            case 1 -> RED;
            case 2 -> YELLOW;
            case 3 -> PINK;
            case 4 -> CYAN;
            default -> BLACK;
        };
    }

    @Override
    public String toString() {
        return toPrint;
    }
}

