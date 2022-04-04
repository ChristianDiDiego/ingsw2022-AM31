package it.polimi.ingsw.constants;

//This class contains the parameters that do not change according to the number of players

public class Constants {
    public static final int NUMBEROFKINGDOMS = 5;
    public static final int NUMBEROFISLANDS = 12;
    public static final int NUMBEROFSTUDENTSOFEACHCOLOR = 26;
    public static final int NUMBEROFCARDSINDECK = 10;
    public static final int DININGROOMDESTINATION = 0;
    public static final int MAXSTUDENTSINDINING = 10;
    public static final int MAXPLAYERS = 4;
    public static final int NUMBEROFCHARACTERS = 8;
    public static final int NUMBEROFPLAYABLECHARACTERS = 3;

    private static String ipServer;
    private static int port;


    public static String getIpServer() {
        return ipServer;
    }

    public static void setIpServer(String ipServer) {
        Constants.ipServer = ipServer;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        Constants.port = port;
    }
}
