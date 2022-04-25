package it.polimi.ingsw.view;

import it.polimi.ingsw.client.cli.Cli;
import it.polimi.ingsw.controller.ActionParser;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.server.SocketClientConnection;
import it.polimi.ingsw.utilities.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/*
Remote view: listen the changements from the model and send a message to the client who is associated with it
 */
public class RemoteView implements PropertyChangeListener{
    private final SocketClientConnection clientConnection;
    private final Player player;
    private final Game currentGame;
    private final ActionParser actionParser;

    public RemoteView(Player player, SocketClientConnection c, Game currentGame, ActionParser actionParser) {
        this.player = player;
        this.clientConnection = c;
        this.currentGame = currentGame;
        this.actionParser = actionParser;
        //c.addObserver(new MessageReceiver());
        //c.asyncSend("Your opponent is: " + opponent);

    }

    protected synchronized void showMessage(Object message) {
        clientConnection.asyncSend(message);
    }

    /**
     public void eventPerformed(EventObject evt){
     System.out.println("Fired: " + ((Integer)evt.getSource()).toString());
     showMessage(evt);
     }
     */

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("currentPlayerChanged")){
            Object lock = new Object();
            synchronized (this){
                List<Board> boards = new ArrayList<>();
                for(Player p : currentGame.getListOfPlayer()) {
                    boards.add(p.getMyBoard());
                }
                ListOfBoards boards1 = new ListOfBoards(boards);
                synchronized (lock){
                    showMessage(boards1);
                }

                ListOfArchipelagos archipelagos = new ListOfArchipelagos(currentGame.getListOfArchipelagos());
                synchronized (lock){
                    showMessage(archipelagos);
                }

                ListOfClouds clouds = new ListOfClouds(currentGame.getListOfClouds());
                synchronized (lock){
                    showMessage(clouds);
                }

                ListOfPlayers players = new ListOfPlayers(currentGame.getListOfPlayer());
                synchronized (lock){
                    showMessage(players);
                }

                if(currentGame.getCurrentPlayer() == player){
                    synchronized (lock){
                        showMessage(player.getMyDeck());
                    }
                }
            }
            synchronized (this){
                if(player.getNickname().equals(evt.getNewValue())){
                    showMessage(evt.getNewValue() + " is your turn!");
                }else{
                    showMessage("is the turn of " + evt.getNewValue());
                }
            }
        }else if(evt.getPropertyName().equals("MNmove") || evt.getPropertyName().equals("ArchUnified")){
            ListOfArchipelagos archipelagos = new ListOfArchipelagos(currentGame.getListOfArchipelagos());
            showMessage(archipelagos);
        } else if(evt.getPropertyName().equals("PhaseChanged")){
            synchronized (this){
                if(currentGame.getCurrentPlayer() == player){
                    switch (currentGame.getPhase()){
                        case CARD_SELECTION -> {
                            System.out.println("card selection");
                            showMessage(gameMessage.cardSelectionMessage);
                        }case MOVE_STUDENTS ->
                                showMessage(gameMessage.studentMovementMessage);
                        case MOVE_MN ->
                                showMessage(gameMessage.moveMotherNatureMessage);
                        case CLOUD_SELECTION ->
                                showMessage(gameMessage.chooseCloudMessage);
                    }
                }
            }
        }else if(evt.getPropertyName().equals("UsedCard")){
            synchronized (this){
                if (currentGame.getCurrentPlayer() == player) {
                    showMessage(player.getMyDeck());
                }

                ListOfPlayers players = new ListOfPlayers(currentGame.getListOfPlayer());
                showMessage(players);
            }
        }else if (evt.getPropertyName().equals("RemovedStudentFromEntrance")){
            synchronized (this){
                ListOfArchipelagos archipelagos = new ListOfArchipelagos(currentGame.getListOfArchipelagos());
                showMessage(archipelagos);

                List<Board> boards = new ArrayList<>();
                for (Player p : currentGame.getListOfPlayer()) {
                    boards.add(p.getMyBoard());
                }
                ListOfBoards boards1 = new ListOfBoards(boards);
                showMessage(boards1);
            }
        }else if(evt.getPropertyName().equals("ChangedProfessor")){
            synchronized (this){
                List<Board> boards = new ArrayList<>();
                for (Player p : currentGame.getListOfPlayer()) {
                    boards.add(p.getMyBoard());
                }
                ListOfBoards boards1 = new ListOfBoards(boards);
                showMessage(boards1);
            }
        }else if(evt.getPropertyName().equals("MessageForParser")){
            synchronized (this){
                //System.out.println("Action to send to parser ");
                actionParser.actionSerializer(player.getNickname(),(String)evt.getNewValue());
            }
        }else if(evt.getPropertyName().equals("ChangedCloudStatus")){
            synchronized (this){
                ListOfClouds clouds = new ListOfClouds(currentGame.getListOfClouds());
                showMessage(clouds);
            }
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
}