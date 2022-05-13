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
            if(evt.getNewValue().equals(player.getTeam())){
                showMessage("The game has ended \n YOU WON");
            }else{
                showMessage("The game has ended \n YOU LOST");
            }

        }else if(evt.getPropertyName().equals("currentPlayerChanged")){
            Object lock2 = new Object();
            synchronized (lock2){
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



                if(currentGame.getCurrentPlayer() == player){
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
                                showMessage("Character: " + c.getId() + "\n Description: " + c.getDescriptionOfPower() + "\n Price:" + c.getPrice() + "\n\n");
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
                            showMessage(gameMessage.cardSelectionMessage);
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
        }else if(evt.getPropertyName().equals("MNmove") || evt.getPropertyName().equals("ArchUnified")){
            ListOfArchipelagos archipelagos = new ListOfArchipelagos(currentGame.getListOfArchipelagos());
            showMessage(archipelagos);
        } else if(evt.getPropertyName().equals("PhaseChanged")){
            synchronized (this){
                if(currentGame.getCurrentPlayer() == player){
                    switch (currentGame.getPhase()){
                        case CARD_SELECTION ->{
                            /*
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

                            //showMessage(gameMessage.cardSelectionMessage);
                        case MOVE_STUDENTS ->
                                showMessage(String.format(gameMessage.studentMovementMessage, actionParser.getActionController().getTurnController().getGameHandler().getNumberOfMovements()));
                        case MOVE_MN -> {
                            Object lock = new Object();
                            Object lock2 = new Object();
                            synchronized (lock2) {
                                List<Board> boards = new ArrayList<>();
                                for (Player p : currentGame.getListOfPlayer()) {
                                    boards.add(p.getMyBoard());
                                }
                                ListOfBoards boards1 = new ListOfBoards(boards);
                                synchronized (lock) {
                                    showMessage(boards1);
                                    try {
                                        TimeUnit.MICROSECONDS.sleep(500);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }


                                ListOfArchipelagos archipelagos = new ListOfArchipelagos(currentGame.getListOfArchipelagos());
                                synchronized (lock) {
                                    showMessage(archipelagos);
                                    try {
                                        TimeUnit.MICROSECONDS.sleep(500);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                            try {
                                TimeUnit.MICROSECONDS.sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            synchronized (lock) {
                                showMessage(String.format(gameMessage.moveMotherNatureMessage, player.getLastUsedCard().getMaxSteps() + ((player.getUsedCharacter() == null)? 0: player.getUsedCharacter().getBonusSteps())));
                                try {
                                    TimeUnit.MICROSECONDS.sleep(500);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }case CLOUD_SELECTION -> {
                            ListOfClouds clouds = new ListOfClouds(currentGame.getListOfClouds());
                            synchronized (lock){
                                showMessage(clouds);
                                try {
                                    TimeUnit.MICROSECONDS.sleep(500);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            try {
                                TimeUnit.MICROSECONDS.sleep(500);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            synchronized (lock){
                                showMessage(gameMessage.chooseCloudMessage);
                                try {
                                    TimeUnit.MICROSECONDS.sleep(500);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
            }
        }else if(evt.getPropertyName().equals("UsedCard")){
            synchronized (lock){
                if (currentGame.getCurrentPlayer() == player) {
                    showMessage(player.getMyDeck());
                    try {
                        TimeUnit.MICROSECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

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
        }else if (evt.getPropertyName().equals("RemovedStudentFromEntrance")){
            synchronized (lock) {
                ListOfArchipelagos archipelagos = new ListOfArchipelagos(currentGame.getListOfArchipelagos());
                showMessage(archipelagos);
                try {
                    TimeUnit.MICROSECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            synchronized (lock){

                List<Board> boards = new ArrayList<>();
                for (Player p : currentGame.getListOfPlayer()) {
                    boards.add(p.getMyBoard());
                }
                ListOfBoards boards1 = new ListOfBoards(boards);
                showMessage(boards1);
                try {
                    TimeUnit.MICROSECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }else if(evt.getPropertyName().equals("ChangedProfessor")){
            synchronized (lock){
                List<Board> boards = new ArrayList<>();
                for (Player p : currentGame.getListOfPlayer()) {
                    boards.add(p.getMyBoard());
                }
                ListOfBoards boards1 = new ListOfBoards(boards);
                showMessage(boards1);
                try {
                    TimeUnit.MICROSECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }else if(evt.getPropertyName().equals("MessageForParser")){
            synchronized (this){
                //System.out.println("Action to send to parser ");
                actionParser.actionSerializer(player.getNickname(),(String)evt.getNewValue());
            }
        }else if(evt.getPropertyName().equals("ChangedCloudStatus")){
            synchronized (lock){
                ListOfClouds clouds = new ListOfClouds(currentGame.getListOfClouds());
                showMessage(clouds);
                try {
                    TimeUnit.MICROSECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }else if(evt.getPropertyName().equals("playedCharacter")){
            synchronized (lock) {
                showMessage("Played character: " + evt.getNewValue());
                try {
                    TimeUnit.MICROSECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            synchronized (lock){
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
                showMessage(player.getMyDeck());
                try {
                    TimeUnit.MICROSECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
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

}