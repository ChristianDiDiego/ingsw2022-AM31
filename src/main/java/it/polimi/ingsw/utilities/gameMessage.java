package it.polimi.ingsw.utilities;
/*
This class contains the messages to be sent to the client
 */
public class gameMessage {
    public static String notYourTurn = "Is not your turn!";

    public static String cardSelectionMessage = "Play a card of your deck \n writing CARD [Number of the card]";
    public static String studentMovementMessage = "move %d of your students from entrance \n please type MOVEST [COLOR]-[DESTINATION],[COLOR]-[DESTINATION].. \nwhere Color is: the starting letter of the color:\nY for yellow, B for blue...\nDestination is:\n0: Board\n1-12: Archipelagos' id";
    public static String moveMotherNatureMessage = "move mother nature; For this round you can do max %d steps \n please type MOVEMN [STEPSOFMN]\n";
    public static String chooseCloudMessage = "choose a cloud \n please type CLOUD [NUMBEROFCLOUDTOPICK]\n";
}
