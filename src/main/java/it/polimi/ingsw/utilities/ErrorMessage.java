package it.polimi.ingsw.utilities;

/**
 * this class contains all errors that could be sent to the users
 */
public class ErrorMessage {
    public static final String ModeNotValid = "You inserted a not valid mode, please try again";
    public static final String NumberOfPlayersNotValid = "The number of players is not valid, please insert a number between 2 and 4";
    public static final String DuplicateNickname = "This nickname is already used, please choose another one";
    public static final String ColorNotValid ="This color of towers is already taken, please choose another one";
    public static final String StudentNotPresent = "You do not have enough students of the color you inserted";
    public static final String CardAlreadyTaken = "This card is already taken, please choose another one";
    public static final String CardNotPresent = "This card isn't present in the deck,please choose another one";
    public static final String FullDiningRoom = "Your dining room of the selected color is full, please select another color";
    public static final String EmptyBank = "Sorry, there are not enough money in the bank :(";
    public static final String WrongNumberOfMovements = "You moved the wrong number of students, please move ";
    public static final String DestinationNotValid = "Destination not valid, please type another destination";
    public static final String NotYourTurn = "It's not your turn, please wait";
    public static final String wrongPhase = "It's not the correct phase for this action, the current phase is:";
    public static final String ActionNotValid = "This action is not valid";
    public static final String CloudTaken = "This cloud is alreay taken, please choose another one";
    public static final String TooManySteps = "The card that you played does not allow you to do these steps! You can do max ";
    public static final String characterNotValid = "Character not recognised";
    public static final String AlreadyUsedCharacter = "You already used one character in this turn, so you can't use another one";
    public static final String CharacterNotPresent = "The selected character is not available for this game";
    public static final String NotExpertMode = "Game is not in expert mode, so you can't use any character";
    public static final String Forbidden = "this archipelago is forbidden, so influence will not be calculated";
    public static final String notEnoughCoinsOrWrongAction = "You do not have enough coins for this move or you cannot do this action (check entered values)";
    public static final String restartGame = "Try to restart the game";
}
