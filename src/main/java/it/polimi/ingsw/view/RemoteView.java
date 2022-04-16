package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.model.Archipelago;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.board.Board;
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
//public class RemoteView extends Observable<MessageForParser> implements Observer<Game>, PropertyChangeListener {
public class RemoteView implements PropertyChangeListener{
    private SocketClientConnection clientConnection;
    private Player player;
    private Game currentGame;

    public RemoteView(Player player, SocketClientConnection c, Game currentGame) {
        this.player = player;
        this.clientConnection = c;
        this.currentGame = currentGame;
        //c.addObserver(new MessageReceiver());
        // c.asyncSend("Your opponent is: " + opponent);

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("playerOrderChanged")){
            if(player.getNickname().equals(evt.getNewValue())){
                System.out.println("I'm notified and is my turn");
                showMessage(evt.getNewValue() + " is your turn!");
            }else{
                System.out.println("I'm notified");
                showMessage("is the turn of " + evt.getNewValue());
            }
        }
        if(evt.getPropertyName().equals("MNmove") || evt.getPropertyName().equals("ArchUnified")){
            showMessage(currentGame.getListOfArchipelagos());

        }else if(evt.getPropertyName().equals("PhaseChanged")){
            if(currentGame.getCurrentPlayer() == player){
                switch (currentGame.getPhase()){
                    case CARD_SELECTION ->
                            showMessage("choose a card\n");
                    case MOVE_STUDENTS ->
                            showMessage("move your students\n");
                    case MOVE_MN ->
                            showMessage("move mother nature\n");
                    case CLOUD_SELECTION ->
                            showMessage("choose a cloud\n");
                }
            }
        }else if(evt.getPropertyName().equals("UsedCard")){
            if(currentGame.getCurrentPlayer() == player){
                showMessage(player.getMyDeck());
            }
            for(Player p : currentGame.getOrderOfPlayers()){
                showMessage(p.getLastUsedCard());
            }
        }else if (evt.getPropertyName().equals("RemovedStudentFromEntrance")){
            for(Archipelago a : currentGame.getListOfArchipelagos()){
                showMessage(a);
            }
            for(Player p : currentGame.getListOfPlayer()) {
                showMessage(p.getMyBoard());
            }
        }else if(evt.getPropertyName().equals("ChangedProfessor")){
            for(Player p : currentGame.getListOfPlayer()) {
                showMessage(p.getMyBoard());
            }
        }else if(evt.getPropertyName().equals("MessageForParser")){

        }
    }

    /**
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
    */

    protected void showMessage(Object message) {
        clientConnection.asyncSend(message);
    }




}
