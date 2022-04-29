package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ActionParser;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.expertMode.Characters;
import it.polimi.ingsw.server.SocketClientConnection;
import it.polimi.ingsw.utilities.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
            Object lock2 = new Object();
            synchronized (lock2){
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
                    try {
                        TimeUnit.MICROSECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    showMessage(archipelagos);
                }

                ListOfClouds clouds = new ListOfClouds(currentGame.getListOfClouds());
                synchronized (lock){
                    try {
                        TimeUnit.MICROSECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    showMessage(clouds);
                }

                ListOfPlayers players = new ListOfPlayers(currentGame.getListOfPlayer());
                synchronized (lock){
                    try {
                        TimeUnit.MICROSECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    showMessage(players);
                }

                if(currentGame.getCurrentPlayer() == player){
                    synchronized (lock){
                        try {
                            TimeUnit.MICROSECONDS.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        showMessage(player.getMyDeck());
                    }
                    System.out.println("exmode: " + currentGame.isExpertModeOn());
                    if(currentGame.isExpertModeOn()){
                        synchronized (lock){
                            try {
                                TimeUnit.MICROSECONDS.sleep(500);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            showMessage("Playable characters: \n");
                            for(Characters c : currentGame.getCharactersPlayable()){
                                showMessage("Character: " + c.getId() + "\n Description: " + c.getDescriptionOfPower() + "\n Price:" + c.getPrice() + "\n\n");
                            }
                        }
                        synchronized (lock){
                            showMessage("Available coins: " + currentGame.getCurrentPlayer().getWallet());
                        }
                    }
                }
            }
            synchronized (lock2){
                if(player.getNickname().equals(evt.getNewValue())){
                    try {
                        TimeUnit.MICROSECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(evt.getNewValue() + " is your turn!");
                    showMessage(evt.getNewValue() + " is your turn!");
                }else{
                    try {
                        TimeUnit.MICROSECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("is the turn of " + evt.getNewValue());
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
        }else if(evt.getPropertyName().equals("playedCharacter")){
            synchronized (this){
                showMessage("Played character: " + evt.getNewValue());
                switch (evt.getNewValue().toString()){
                    case "CHARACTER1":
                        ListOfArchipelagos archipelagos = new ListOfArchipelagos(currentGame.getListOfArchipelagos());
                        showMessage(archipelagos);
                        break;
                    case "CHARACTER3":
                        ListOfArchipelagos archipelagoss = new ListOfArchipelagos(currentGame.getListOfArchipelagos());
                        showMessage(archipelagoss);
                        break;
                    case "CHARACTER7":
                        List<Board> boards = new ArrayList<>();
                        for (Player p : currentGame.getListOfPlayer()) {
                            boards.add(p.getMyBoard());
                        }
                        ListOfBoards boards1 = new ListOfBoards(boards);
                        showMessage(boards1);
                        break;
                }
            }
        }else if(evt.getPropertyName().equals("ErrorMessage")){
            synchronized (this){
                showMessage(evt.getNewValue());
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