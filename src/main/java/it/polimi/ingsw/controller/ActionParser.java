package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.ActionController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.StudsAndProfsColor;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.utilities.MessageForParser;
import it.polimi.ingsw.view.RemoteView;

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

public class ActionParser implements Observer<MessageForParser> {
    private ActionController actionController;

    public ActionParser(ActionController actionController){
        this.actionController = actionController;
    }

    /**
     * Serialize the message arrived from the player recognising him and sending the action to the proper method
     * @param nickname nick of the player that sent the action
     * @param message received from the player
     * @return 1 if action valid, 0 if some error occurs
     */
    public boolean actionSerializer(String nickname, String message){
        String[] input = message.split(" ");
        String phase = input[0];
        Player player = recognisePlayer(nickname);
        if( player != null){
            switch(phase.toUpperCase(Locale.ROOT)){
                case "CARD":
                    int cardPower = Integer.parseInt(input[1]);
                    return actionController.getTurnController().checkActionCard(player, cardPower);

                case "MOVEST":
                    String[] colorDestination = input[1].split(",");
                    StudsAndProfsColor[] colors = new StudsAndProfsColor[colorDestination.length];
                    int[] destinations = new int[colorDestination.length];
                    for(int i = 0; i< colorDestination.length; i++){

                        colors[i] = charToColorEnum(colorDestination[i].split("-")[0].charAt(0));
                        destinations[i] = Integer.parseInt(colorDestination[i].split("-")[1]);
                    }
                     return actionController.checkActionMoveStudent(player, colors, destinations);

                case "MOVEMN":
                    int mnSteps = Integer.parseInt(input[1]);
                    return actionController.checkActionMoveMN(player, mnSteps);

                case "CLOUD":
                    int nOfCloud = Integer.parseInt(input[1]);
                    return actionController.checkActionCloud(player, nOfCloud);

                case "CHARACTER":
                    int idOfCharacter = Integer.parseInt(input[1]);
                    String action = input[2];
                    //TODO: set in action controller to check
                    return actionController.checkActionCharacter(player, idOfCharacter, action);


                default:
                    System.out.println("Action not recognised");
                    return false;

            }
        }else{
            System.out.println("Player not recognised");
            return false;
        }


    }

    private Player recognisePlayer(String nickname){
        for(Player player :actionController.getGame().getOrderOfPlayers()){
            if(player.getNickname().equals(nickname)) {
                return player;
            }
        }
        return null;
    }

    private StudsAndProfsColor charToColorEnum(char color){
        switch (Character.toUpperCase(color)){
            case 'R':
                return StudsAndProfsColor.RED;
            case 'G':
                return StudsAndProfsColor.GREEN;
            case 'Y':
                return StudsAndProfsColor.YELLOW;
            case 'P':
                return StudsAndProfsColor.PINK;
            case 'B':
                return StudsAndProfsColor.BLUE;
           
        }
        return null;
    }

    @Override
    public void update(MessageForParser message) {

    }
    /**
     * riceve tutti i messaggi della CLI
     * converte la stringa in FASE : AZIONE
     * fa switch case FASE=
     * per ogni caso chiama actionController.checkAction<FASE>(player, azione)
     *
     * se la fase è SCELTACARTE chiamo checkAction nel turn controller
     *
     * se la fase è muovi Studenti chiamo il metodo studentMoveParsing che riceve la stringa
     * e la trasforma in due array: il primo contiene il colore (enum/int), il secondo contiene
     * l'indice di isola/board dove va messo
     *
     * se la fase è una delle altre l'azione inviata è un intero ricavato dalla stringa
     *

     *
     *
     */

}
