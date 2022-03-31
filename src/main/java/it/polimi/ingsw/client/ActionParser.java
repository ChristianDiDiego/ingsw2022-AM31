package it.polimi.ingsw.client;

public class ActionParser {

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
