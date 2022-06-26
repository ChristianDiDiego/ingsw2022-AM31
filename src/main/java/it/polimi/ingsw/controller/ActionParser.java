package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.utilities.ErrorMessage;
import it.polimi.ingsw.utilities.EventName;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Locale;

/**
 * Parse a string and call actionController of that phase or turnController if is card phase
 * Message structure:
 * CARD CARDPOWER
 * MOVEST [COLOR][DESTINATION],[COLOR][DESTINATION].. (R-0,B-2,R-3)
 * MOVEMN STEPSOFMN
 * CLOUD NOFCLOUDTOPICK
 * CHARACTER [IDCHARACTER] [IDARCHIPELAGO]/ FOR CHARACTER7 : ([COLOR TO ADD],[COLOR TO ADD],[COLOR TO REMOVE],[COLOR TO REMOVE]
 */

public class ActionParser {
    private final ActionController actionController;

    private final PropertyChangeSupport support;

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public ActionParser(ActionController actionController) {
        this.actionController = actionController;
        this.support = new PropertyChangeSupport(this);
    }

    /**
     * Serialize the message arrived from the player recognising him and sending the action to the proper method
     *
     * @param nickname nick of the player that sent the action
     * @param message  received from the player
     * @return 1 if action valid, 0 if some error occurs
     */
    public synchronized boolean actionSerializer(String nickname, String message) {
        //Every proper action message is composed of phase+action, so if
        //a message does not contain at least a space is not fine
        if (!message.contains(" ") || message.split(" ").length <= 1) {
            if (message.equalsIgnoreCase("SHOWDECK")) {
                System.out.println("I received deck request");
                support.firePropertyChange(EventName.DeckRequired, "", nickname);
                System.out.println("Fire sent");
                return true;
            } else if (message.equalsIgnoreCase("SHOWAVAILABLECHARACTERS")) {
                if (getActionController().getGame().isExpertModeOn()) {
                    support.firePropertyChange(EventName.AvailableCharactersRequired, "", nickname);
                    return true;
                }
            }
            System.out.println(ErrorMessage.ActionNotValid);
            support.firePropertyChange("ErrorMessage", nickname, ErrorMessage.ActionNotValid);
            System.out.println("ERRORE INVIATO");
            return false;
        } else {
            String[] input = message.split(" ");
            String phase = input[0];
            Player player = recognisePlayer(nickname);
            System.out.println("input size:" + input.length);
            if (player != null) {
                return switch (phase.toUpperCase(Locale.ROOT)) {
                    case "CARD" -> playCard(input[1], nickname, player);
                    case "MOVEST" -> moveStudents(input[1], nickname, player);
                    case "MOVEMN" -> moveMotherNature(input[1], nickname, player);
                    case "CLOUD" -> chooseCloud(input[1], nickname, player);
                    case "CHARACTER" -> playCharacter(input, nickname, player);
                    default -> reportError(nickname);
                };
            } else {
                System.out.println("Player not recognised");
                return false;
            }
        }
    }

    /**
     * Return the player in the game with the nickname nickname
     */
    private Player recognisePlayer(String nickname) {
        for (Player player : actionController.getGame().getOrderOfPlayers()) {
            if (player.getNickname().equals(nickname)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Convert a char to the corresponding StudsAndProfColor enum
     */
    private StudsAndProfsColor charToColorEnum(char color) {
        return switch (Character.toUpperCase(color)) {
            case 'G' -> StudsAndProfsColor.GREEN;
            case 'R' -> StudsAndProfsColor.RED;
            case 'Y' -> StudsAndProfsColor.YELLOW;
            case 'P' -> StudsAndProfsColor.PINK;
            case 'B' -> StudsAndProfsColor.BLUE;
            default -> null;
        };
    }

    private static Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * If the power of the card received is not null, send the power to the controller
     * return false if the cardPower is null
     * @param input received power of a card
     * @param nickname
     * @param player
     * @return result of checkActionPower if power is not null, else false
     */
    private boolean playCard(String input, String nickname, Player player) {
        Integer cardPower = tryParse(input);
        if (cardPower == null) {
            System.out.println(ErrorMessage.ActionNotValid);
            support.firePropertyChange("ErrorMessage", nickname, ErrorMessage.ActionNotValid);
            return false;
        }
        System.out.println("Card " + cardPower + " received");
        return actionController.getTurnController().checkActionCard(player, cardPower);
    }

    /**
     * Get the color of the students to move from the entrance and their destination
     * using the string received
     * @param input is the action to do in string format
     * @param nickname
     * @param player
     * @return false if the message is not formatted well or if the destination is not valid
     */
    private boolean moveStudents(String input, String nickname, Player player) {
        if (!input.contains(",")) {
            System.out.println(ErrorMessage.ActionNotValid);
            support.firePropertyChange("ErrorMessage", nickname, ErrorMessage.ActionNotValid);
            return false;
        }
        String[] colorDestination = input.split(",");
        StudsAndProfsColor[] colors = new StudsAndProfsColor[colorDestination.length];
        int[] destinations = new int[colorDestination.length];
        for (int i = 0; i < colorDestination.length; i++) {
            if (!colorDestination[i].contains("-") || colorDestination[i].split("-").length <= 1 || colorDestination[i].split("-")[0].isEmpty() || colorDestination[i].split("-")[1].isEmpty()) {
                System.out.println(ErrorMessage.ActionNotValid);
                support.firePropertyChange("ErrorMessage", nickname, ErrorMessage.ActionNotValid);
                return false;
            }
            colors[i] = charToColorEnum(colorDestination[i].split("-")[0].charAt(0));
            Integer tempDestination = tryParse(colorDestination[i].split("-")[1]);
            if (tempDestination == null || colors[i] == null) {
                System.out.println(ErrorMessage.ActionNotValid);
                support.firePropertyChange("ErrorMessage", nickname, ErrorMessage.ActionNotValid);
                return false;
            }
            destinations[i] = tempDestination;
        }
        return actionController.checkActionMoveStudent(player, colors, destinations);
    }

    /**
     * Get the steps of mother nature that the user wants to do and send them to the controller
     * @param input number of steps
     * @param nickname
     * @param player
     * @return result of checkActionMoveMn if input is not null, else false
     */
    private boolean moveMotherNature(String input, String nickname, Player player) {
        Integer mnSteps = tryParse(input);
        if (mnSteps == null) {
            System.out.println(ErrorMessage.ActionNotValid);
            support.firePropertyChange("ErrorMessage", nickname, ErrorMessage.ActionNotValid);
            return false;
        }
        return actionController.checkActionMoveMN(player, mnSteps);
    }


    /**
     * Get the number of the cloud that the user wants to choose and send it to the controller
     * @param input id of cloud
     * @param nickname
     * @param player
     * @return result of checkActionCloud if id is not null, else false
     */
    private boolean chooseCloud(String input, String nickname, Player player) {
        Integer nOfCloud = tryParse(input);
        if (nOfCloud == null) {
            System.out.println(ErrorMessage.ActionNotValid);
            support.firePropertyChange("ErrorMessage", nickname, ErrorMessage.ActionNotValid);
            return false;
        }
        return actionController.checkActionCloud(player, nOfCloud);
    }

    /**
     * Get the character that the user wants to use and send it to the controller
     * @param input id of character
     * @param nickname
     * @param player
     * @return result of checkActionCharacter if id is not null, else false
     */
    private boolean playCharacter(String[] input, String nickname, Player player) {
        Integer idOfCharacter = tryParse(input[1]);
        if (idOfCharacter == null) {
            System.out.println(ErrorMessage.ActionNotValid);
            support.firePropertyChange("ErrorMessage", nickname, ErrorMessage.ActionNotValid);
            return false;
        }
        String action = "";
        if (input.length >= 3) {
            action = input[2];
        }
        return actionController.checkActionCharacter(player, idOfCharacter, action);
    }

    /**
     * Called if an action is not recognised by the parser
     * @param nickname
     * @return false to report error
     */
    private boolean reportError(String nickname) {
        support.firePropertyChange("ErrorMessage", nickname, ErrorMessage.ActionNotValid);
        System.out.println("Action not recognised");
        return false;
    }

    public ActionController getActionController() {
        return actionController;
    }
}
