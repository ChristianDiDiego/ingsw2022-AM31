package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.SocketClientConnection;
import it.polimi.ingsw.utilities.MessageForParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

//Observer: osserva qualcosa e quando c'è una notify
// dell'oggetto osservato lancia l'update

//Observable: un metodo della classe lancerà una notify del
//tipo detto nell'observable
public class RemoteView extends Observable<MessageForParser> implements Observer<Game>, PropertyChangeListener {

    private SocketClientConnection clientConnection;
    private Player player;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("currentPlayerChangement")){
            showMessage(evt.getNewValue() + " is your turn!");
        }
    }

    private class MessageReceiver extends Observable<MessageForParser> implements Observer<String>{

        @Override
        public void update(String message) {
            // riceve il messaggio da socketClientConnection (input del client) e lo manda al parser
            System.out.println("Received: " + message);
            try{
                MessageForParser m = new MessageForParser(player, message);
                notify(m);
            }catch(IllegalArgumentException | ArrayIndexOutOfBoundsException e){
                clientConnection.asyncSend("Error!");
            }
        }
    }

    public RemoteView(Player player, SocketClientConnection c) {
        this.player = player;
        this.clientConnection = c;
        c.addObserver(new MessageReceiver());
       // c.asyncSend("Your opponent is: " + opponent);

    }

    protected void showMessage(Object message) {
        clientConnection.asyncSend(message);
    }

    @Override
    /**
     * questo riceve l'intero game clonato dal model
     * in ogni caso manda il messaggio con la board aggiornata alla socketClietConnection
     * chiamando asynchSend
     */
    public void update(Game game){
        showMessage(game);
        //TODO: mettere che stampa ogni singola cosa senza passare tutto il game

        if(game.getCurrentPlayer() == player){
            switch (game.getPhase()){
                case CARD_SELECTION ->
                    showMessage("Choose a card\n");
                case MOVE_STUDENTS ->
                    showMessage("move your students\n");
                case MOVE_MN ->
                    showMessage("move mother nature\n");
                case CLOUD_SELECTION ->
                    showMessage("Choose a cloud\n");

            }

        }

    }


}
