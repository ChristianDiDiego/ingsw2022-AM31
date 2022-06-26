package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ActionParser;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.expertMode.Characters;
import it.polimi.ingsw.server.SocketClientConnection;
import it.polimi.ingsw.utilities.*;
import it.polimi.ingsw.utilities.Lists.ListOfArchipelagos;
import it.polimi.ingsw.utilities.Lists.ListOfBoards;
import it.polimi.ingsw.utilities.Lists.ListOfClouds;
import it.polimi.ingsw.utilities.Lists.ListOfPlayers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class contains the methods to listen the changes from the model
 * and send a message to the client who is associated with it
 */
public class RemoteView implements PropertyChangeListener {
    private final SocketClientConnection clientConnection;
    private final Player player;
    private final Game currentGame;
    private final ActionParser actionParser;
    final Object lock;

    public RemoteView(Player player, SocketClientConnection c, Game currentGame, ActionParser actionParser) {
        this.player = player;
        this.clientConnection = c;
        this.currentGame = currentGame;
        this.actionParser = actionParser;
        this.lock = new Object();
    }

    public Player getPlayer() {
        return this.player;
    }

    /**
     * Sent a message to the associated client
     *
     * @param message to be sent
     */
    protected synchronized void showMessage(Object message) {
        clientConnection.asyncSend(message);
    }

    @Override
    public synchronized void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case EventName.EndGame:
                endGame(evt.getNewValue());
                break;
            case EventName.CurrentPlayerChanged:
                currentPlayerChanged(evt);
                break;
            case EventName.MNmove:
                sendArchipelagos();
                break;
            case EventName.PhaseChanged:
                phaseChanged();
                break;
            case EventName.UsedCard:
                usedCard();
                break;
            case EventName.RemovedStudentFromEntrance:
                sendArchipelagos();
                sendBoards();
                break;
            case EventName.ChangedProfessor:
                sendBoards();
                break;
            case EventName.MessageForParser:
                sendMessageToParser(evt);
                break;
            case EventName.ChangedCloudStatus:
                sendClouds();
                break;
            case EventName.playedCharacter:
                playedCharacter(evt);
                break;
            case EventName.ErrorMessage:
                sendErrorMessage(evt);
                break;
            case EventName.StartingGame:
                startingGame(evt);
                break;
            case EventName.DeckRequired:
                if (player.getNickname().equals(evt.getNewValue())) {
                    sendDeck();
                }
                break;
            case EventName.AvailableCharactersRequired:
                sendAvailableCharacters(evt);
                break;
        }
    }

    /**
     * Sends win or lost messages and closes connections
     *
     * @param winnerTeam team that won the game
     */
    private void endGame(Object winnerTeam) {
        try {
            TimeUnit.MICROSECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        synchronized (lock) {
            if (winnerTeam.equals(player.getTeam())) {
                showMessage("The game has ended \n YOU WON");
                clientConnection.setPlayerQuitted(true);
                try {
                    TimeUnit.MICROSECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                clientConnection.closeOnlyThis();

            } else {
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

    /**
     * Sends updates of the status of the game
     *
     * @param evt
     */
    private void currentPlayerChanged(PropertyChangeEvent evt) {
        try {
            TimeUnit.MICROSECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        synchronized (lock) {
            Object lock2 = new Object();
            synchronized (lock2) {
                showMessage(GameMessage.specialCommand);
                sendBoards();
                sendArchipelagos();
                sendClouds();

                if (currentGame.getCurrentPlayer() == player) {
                    sendDeck();
                    //sends characters
                    if (currentGame.isExpertModeOn()) {
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

                        for (Characters c : currentGame.getCharactersPlayable()) {
                            showMessage("Character: " + c.getId() + " \n Description: " + c.getDescriptionOfPower() + "\n Price: " + c.getPrice() + " \n\n");
                        }
                        try {
                            TimeUnit.MICROSECONDS.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
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
            synchronized (lock2) {
                Object internalLock = new Object();
                if (player.getNickname().equals(evt.getNewValue())) {
                    ListOfPlayers players = new ListOfPlayers(currentGame.getListOfPlayer());
                    synchronized (internalLock) {
                        showMessage(players);
                        try {
                            TimeUnit.MICROSECONDS.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    synchronized (internalLock) {
                        System.out.println(evt.getNewValue() + " is your turn!");
                        showMessage(evt.getNewValue() + " is your turn!");
                        try {
                            TimeUnit.MICROSECONDS.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (evt.getOldValue().equals("CS")) {
                        synchronized (internalLock) {
                            showMessage(GameMessage.cardSelectionMessage);
                            try {
                                TimeUnit.MICROSECONDS.sleep(500);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                } else {
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
    }

    /**
     * Sends updated boards
     */
    private void sendBoards() {
        try {
            TimeUnit.MICROSECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
    }

    /**
     * Sends updated list of archipelagos
     */
    private void sendArchipelagos() {
        ListOfArchipelagos archipelagos = new ListOfArchipelagos(currentGame.getListOfArchipelagos());
        synchronized (lock) {
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

    /**
     * Sends updated clouds
     */
    private void sendClouds() {
        ListOfClouds clouds = new ListOfClouds(currentGame.getListOfClouds());
        synchronized (lock) {
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

    /**
     * Sends updated personal deck
     */
    private void sendDeck() {
        synchronized (lock) {
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

    /**
     * Sends coins still available
     */
    private void sendCoins() {
        synchronized (lock) {
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            showMessage(player.getWallet());
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Sends messages to explain the current phase
     */
    private void phaseChanged() {
        synchronized (lock) {
            if (currentGame.isExpertModeOn()) {
                sendCoins();
            }
            System.out.println("I should send phase " + currentGame.getPhase());
            if (currentGame.getCurrentPlayer().getNickname().equals(player.getNickname())) {
                switch (currentGame.getPhase()) {
                    case CARD_SELECTION -> {/* cardSelectionPhase */ }
                    case MOVE_STUDENTS ->
                            showMessage(String.format(GameMessage.studentMovementMessage, actionParser.getActionController().getTurnController().getGameHandler().getNumberOfMovements()));
                    case MOVE_MN -> moveMnPhase();
                    case CLOUD_SELECTION -> cloudSelectionPhase();
                }
            }
        }
    }

    /**
     * Sends updated boards and archipelagos
     * and says how many steps a player can do in the current turn
     */
    private void moveMnPhase() {
        try {
            TimeUnit.MICROSECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Object lock = new Object();
        synchronized (lock) {
            sendBoards();
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sendArchipelagos();
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        synchronized (lock) {
            showMessage(String.format(GameMessage.moveMotherNatureMessage, player.getLastUsedCard().getMaxSteps() + ((player.getUsedCharacter() == null) ? 0 : player.getUsedCharacter().getBonusSteps())));
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Sends clouds still available and the cloudSelectionMessage
     */
    private void cloudSelectionPhase() {
        sendClouds();
        synchronized (lock) {
            showMessage(GameMessage.chooseCloudMessage);
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Sends updated deck when a card is chosen
     */
    private void usedCard() {
        synchronized (lock) {
            if (currentGame.getCurrentPlayer() == player) {
                sendDeck();
            }
            sendListOfPlayers();
        }
    }

    /**
     * Sends the list of players in order to update last used cards
     */
    private void sendListOfPlayers() {
        try {
            TimeUnit.MICROSECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ListOfPlayers players = new ListOfPlayers(currentGame.getListOfPlayer());
        synchronized (lock) {
            showMessage(players);
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Sends a message about the character played
     *
     * @param evt event
     */
    private void playedCharacter(PropertyChangeEvent evt) {
        try {
            TimeUnit.MICROSECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        synchronized (lock) {
            showMessage("Played character: " + evt.getNewValue());
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        synchronized (lock) {
            switch (evt.getNewValue().toString()) {
                case "CHARACTER1", "CHARACTER3" -> sendArchipelagos();
                case "CHARACTER7" -> sendBoards();
            }
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Resends situation after a match has been restored (for persistence)
     */
    public void resendSituation() {
        try {
            TimeUnit.MICROSECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //Simulate a change event in the player and phase to re-send all
        String oldValue = "";
        if (currentGame.getPhase() == Phase.CARD_SELECTION) {
            oldValue = "CS";
        }
        PropertyChangeEvent event = new PropertyChangeEvent("", "currentPlayerChanged", oldValue, currentGame.getCurrentPlayer().getNickname());
        currentPlayerChanged(event);
        phaseChanged();
    }

    /**
     * sends error messages
     * @param evt
     */
    private void sendErrorMessage(PropertyChangeEvent evt){
        try {
            TimeUnit.MICROSECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        synchronized (lock) {
            if (evt.getOldValue().equals(player.getNickname())) {
                showMessage(evt.getNewValue());
                try {
                    TimeUnit.MICROSECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * send to each player his color of towers and team
     * @param evt
     */
    private void startingGame(PropertyChangeEvent evt){
        try {
            TimeUnit.MICROSECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        synchronized (lock) {
            showMessage(evt.getNewValue());
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (currentGame.getNumberOfPlayers() == 4) {
                String teammate = new String();
                String color = new String();
                for (Player p : currentGame.getListOfPlayer()) {
                    if (p.getTeam() == player.getTeam() && p != player) {
                        teammate = p.getNickname();
                        if (p.getColorOfTowers() != null) {
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
    }

    /**
     * just in expert mode sends the available characters
     * @param evt
     */
    private void sendAvailableCharacters(PropertyChangeEvent evt){
        try {
            TimeUnit.MICROSECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (player.getNickname().equals(evt.getNewValue())) {
            for (Characters c : currentGame.getCharactersPlayable()) {
                showMessage("Character: " + c.getId() + "\n Description: " + c.getDescriptionOfPower() + "\n Price:" + c.getPrice() + "\n\n");
                try {
                    TimeUnit.MICROSECONDS.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void sendMessageToParser(PropertyChangeEvent evt){
        synchronized (this) {
            actionParser.actionSerializer(player.getNickname(), (String) evt.getNewValue());
        }
    }

}