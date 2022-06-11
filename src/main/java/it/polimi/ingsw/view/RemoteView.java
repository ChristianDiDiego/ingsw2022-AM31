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
    Object lock;

    public RemoteView(Player player, SocketClientConnection c, Game currentGame, ActionParser actionParser) {
        this.player = player;
        this.clientConnection = c;
        this.currentGame = currentGame;
        this.actionParser = actionParser;
        this.lock = new Object();
    }

    protected synchronized void showMessage(Object message) {
        clientConnection.asyncSend(message);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("EndGame")){
            endGame(evt.getNewValue().toString());


        }else if(evt.getPropertyName().equals("currentPlayerChanged")){
            currentPlayerChanged(evt);

        }else if(evt.getPropertyName().equals("MNmove") || evt.getPropertyName().equals("ArchUnified")){
            mnMovedArcUnified();
        } else if(evt.getPropertyName().equals("PhaseChanged")){
            sendListOfPlayers();
            phaseChanged();
        }else if(evt.getPropertyName().equals("UsedCard")){
            usedCard();
        }else if (evt.getPropertyName().equals("RemovedStudentFromEntrance")){
            sendArchipelagos();
            sendBoards();
        }else if(evt.getPropertyName().equals("ChangedProfessor")){
            synchronized (lock){
               sendBoards();
            }
        }else if(evt.getPropertyName().equals("MessageForParser")){
            synchronized (this){
                //System.out.println("Action to send to parser ");
                actionParser.actionSerializer(player.getNickname(),(String)evt.getNewValue());
            }
        }else if(evt.getPropertyName().equals("ChangedCloudStatus")){
            synchronized (lock){
               sendClouds();
            }
        }else if(evt.getPropertyName().equals("playedCharacter")){
            playedCharacter(evt);

            synchronized (lock){
                switch (evt.getNewValue().toString()){
                    case "CHARACTER1":
                        sendArchipelagos();
                        break;
                    case "CHARACTER3":
                        sendArchipelagos();
                        break;
                    case "CHARACTER7":
                       sendBoards();
                        break;
                }
                try {
                    TimeUnit.MICROSECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }else if(evt.getPropertyName().equals("ErrorMessage")){
            synchronized (lock){
                if(evt.getOldValue().equals(player.getNickname())) {
                    showMessage(evt.getNewValue());
                    try {
                        TimeUnit.MICROSECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }else if(evt.getPropertyName().equals("StartingGame")) {
            synchronized (lock) {
                showMessage(evt.getNewValue());
                try {
                    TimeUnit.MICROSECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(currentGame.getNumberOfPlayers() == 4) {
                    String teammate = new String();
                    String color = new String();
                    for(Player p : currentGame.getListOfPlayer()){
                        if(p.getTeam() == player.getTeam() && p != player){
                            teammate = p.getNickname();
                            if(p.getColorOfTowers() != null) {
                                color = p.getColorOfTowers().toString();
                            } else {
                                color = player.getColorOfTowers().toString();
                            }
                        }
                    }
                    showMessage("Your teammate is " + teammate + "\n" + "Your color of towers is " + color);
                    try {
                        TimeUnit.MICROSECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }else if(evt.getPropertyName().equals("DeckRequired")){
            System.out.println("im notified");
            if(player.getNickname().equals(evt.getNewValue())){
                sendDeck();
            }
        }else if(evt.getPropertyName().equals("AvailableCharactersRequired")){
            if(player.getNickname().equals(evt.getNewValue())){
                for(Characters c : currentGame.getCharactersPlayable()){
                    showMessage("Character: " + c.getId() + "\n Description: " + c.getDescriptionOfPower() + "\n Price:" + c.getPrice() + "\n\n");
                    try {
                        TimeUnit.MICROSECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }



        /*else if(evt.getPropertyName().equals("EndGame")){
            if(evt.getNewValue().equals(player.getTeam())){
                showMessage("The game has ended \n YOU WON");
            }else{
                showMessage("The game has ended \n YOU LOST");
            }
        }
        */
    }
    private void endGame(String newValue){
        synchronized (this){
            if(newValue.equals(player.getTeam())){
                System.out.println("sono nella view");
                showMessage("The game has ended \n YOU WON");
                clientConnection.setPlayerQuitted(true);
                try {
                    TimeUnit.MICROSECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                clientConnection.closeOnlyThis();

            }else{
                System.out.println("sono nella view");
                showMessage("The game has ended \n YOU LOST");
                clientConnection.setPlayerQuitted(true);
                try {
                    TimeUnit.MICROSECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                clientConnection.closeOnlyThis();

            }
        }
    }

    private void currentPlayerChanged(PropertyChangeEvent evt){
        Object lock2 = new Object();
        synchronized (lock2){
            sendBoards();

            sendArchipelagos();

            sendClouds();

            if(currentGame.getCurrentPlayer() == player){
                sendDeck();

                System.out.println("exmode: " + currentGame.isExpertModeOn());
                if(currentGame.isExpertModeOn()){
                    synchronized (lock){
                        try {
                            TimeUnit.MICROSECONDS.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        showMessage("Playable characters: \n");

                        try {
                            TimeUnit.MICROSECONDS.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        for(Characters c : currentGame.getCharactersPlayable()){
                            showMessage("Character: " + c.getId() + " \n Description: " + c.getDescriptionOfPower() + "\n Price: " + c.getPrice() + " \n\n");
                        }
                        try {
                            TimeUnit.MICROSECONDS.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    synchronized (lock){
                        try {
                            TimeUnit.MICROSECONDS.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        showMessage("Available coins: " + currentGame.getCurrentPlayer().getWallet());
                        try {
                            TimeUnit.MICROSECONDS.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        synchronized (lock2){
            Object internalLock = new Object();
            if(player.getNickname().equals(evt.getNewValue())){
                ListOfPlayers players = new ListOfPlayers(currentGame.getListOfPlayer());
                synchronized (internalLock){
                    showMessage(players);
                    try {
                        TimeUnit.MICROSECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                synchronized (internalLock){
                    System.out.println(evt.getNewValue() + " is your turn!");
                    showMessage(evt.getNewValue() + " is your turn!");
                    try {
                        TimeUnit.MICROSECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                if(evt.getOldValue().equals("CS")){
                    synchronized (internalLock){
                        showMessage(GameMessage.cardSelectionMessage);
                        try {
                            TimeUnit.MICROSECONDS.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }else{

                try {
                    TimeUnit.MICROSECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("is the turn of " + evt.getNewValue());
                showMessage("is the turn of " + evt.getNewValue());
                try {
                    TimeUnit.MICROSECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void sendBoards(){
        List<Board> boards = new ArrayList<>();
        for(Player p : currentGame.getListOfPlayer()) {
            boards.add(p.getMyBoard());
        }
        ListOfBoards boards1 = new ListOfBoards(boards);
        synchronized (lock){
            showMessage(boards1);
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendArchipelagos(){
        ListOfArchipelagos archipelagos = new ListOfArchipelagos(currentGame.getListOfArchipelagos());
        synchronized (lock){
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            showMessage(archipelagos);
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendClouds(){
        ListOfClouds clouds = new ListOfClouds(currentGame.getListOfClouds());
        synchronized (lock){
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            showMessage(clouds);
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendDeck(){
        synchronized (lock){
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            showMessage(player.getMyDeck());
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


    }

    private void mnMovedArcUnified(){
        ListOfArchipelagos archipelagos = new ListOfArchipelagos(currentGame.getListOfArchipelagos());
        showMessage(archipelagos);
    }

    private void phaseChanged(){
        synchronized (this){
            System.out.println("I should send phase " + currentGame.getPhase());
            if(currentGame.getCurrentPlayer().getNickname().equals(player.getNickname())){
                switch (currentGame.getPhase()){
                    case CARD_SELECTION ->{/* cardSelectionPhase */ }
                    case MOVE_STUDENTS -> showMessage(String.format(GameMessage.studentMovementMessage, actionParser.getActionController().getTurnController().getGameHandler().getNumberOfMovements()));
                    case MOVE_MN -> {moveMnPhase();}
                    case CLOUD_SELECTION -> {cloudSelectionPhase();}
                }
            }
        }
    }

    private void moveMnPhase(){
        Object lock = new Object();
        synchronized (lock) {
            sendBoards();
            sendArchipelagos();
        }
        synchronized (lock) {
            showMessage(String.format(GameMessage.moveMotherNatureMessage, player.getLastUsedCard().getMaxSteps() + ((player.getUsedCharacter() == null)? 0: player.getUsedCharacter().getBonusSteps())));
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void cloudSelectionPhase(){
        sendClouds();
        synchronized (lock){
            showMessage(GameMessage.chooseCloudMessage);
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void usedCard(){
        synchronized (lock){
            if (currentGame.getCurrentPlayer() == player) {
               sendDeck();
            }

            sendListOfPlayers();
        }
    }

    private void sendListOfPlayers(){
        ListOfPlayers players = new ListOfPlayers(currentGame.getListOfPlayer());
        synchronized (lock){
            showMessage(players);
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void playedCharacter(PropertyChangeEvent evt){
        synchronized (lock) {
            showMessage("Played character: " + evt.getNewValue());
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void resendSituation(){
        //Simulate a change event in the player and phase to re-send all
        String oldValue = "";
        if(currentGame.getPhase() == Phase.CARD_SELECTION){
            oldValue = "CS";
        }
        PropertyChangeEvent event = new PropertyChangeEvent("", "currentPlayerChanged", oldValue, currentGame.getCurrentPlayer().getNickname());
        currentPlayerChanged(event);
        phaseChanged();
    }

    public Player getPlayer(){
        return this.player;
    }

      /*
                            cardSelectionPhase:
                            ListOfPlayers players = new ListOfPlayers(currentGame.getListOfPlayer());
                            try {
                                TimeUnit.MICROSECONDS.sleep(500);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            synchronized (lock){
                                showMessage(players);
                            }

                             */


}