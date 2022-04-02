package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.ActionController;

import java.util.Locale;

/**
 * Parse a string and call actionController of that phase or turnController if is card phase
 */
public class ActionParser {
    private ActionController actionController;

    public ActionParser(ActionController actionController){
        this.actionController = actionController;
    }

    public boolean actionSerializer(String message){
        String[] input = message.split(" ");
        String phase = input[0];

        switch(phase.toUpperCase(Locale.ROOT)){
            case "CARD":
            //    actionController.checkActionCard();
                break;
            case "MOVE":
               // actionController.checkActionMoveMN();
                break;
        }
        //just a placeholder to not sign the function in red:
        return true;
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
