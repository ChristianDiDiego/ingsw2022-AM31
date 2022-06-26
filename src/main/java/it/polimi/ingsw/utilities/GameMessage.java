package it.polimi.ingsw.utilities;

/**
 * This class contains the messages to be sent to the client
 */
public class GameMessage {
    public static final String cardSelectionMessage = "Play a card of your deck \n writing CARD [Number of the card]";
    public static String studentMovementMessage = "move %d of your students from entrance \n please type MOVEST [COLOR]-[DESTINATION],[COLOR]-[DESTINATION].. \nwhere Color is: the starting letter of the color:\nY for yellow, B for blue...\nDestination is:\n0: Board\n1-12: Archipelagos' id";
    public static String moveMotherNatureMessage = "move mother nature; For this round you can do max %d steps \n please type MOVEMN [STEPSOFMN]\n";
    public static final String chooseCloudMessage = "choose a cloud \n please type CLOUD [NUMBEROFCLOUDTOPICK]\n";
    public static final String characterInstructions = "To select a character push the corresponding image. If the character's action requires some specifications, there will be a menu on the right (you must first select the target in the menu and then click on the character)";
    public static final String specialCommand = "\n\nAt any time you can use the command \"quit\" to quit the game \nand the command \"show deck\" to see your remaining cards\n\n";
}
