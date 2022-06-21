package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Menage the game status ;
 * starts the game if the number of player is reached and
 * set the value of the game parameters
 */
public class GameHandler implements Serializable {
    transient private Controller controller;
    private final Game game;
    private int isStarted;
    private final int playersNumber;
    private int numberOfClouds;
    private int maxNumberOfTowers;
    private int maxStudentsInEntrance;
    private int numberOfStudentsOnCloud;
    private int numberOfMovements;
    private final PropertyChangeSupport support;


    public GameHandler(Player firstPlayer, int playersNumber, boolean expertMode) {
        this.playersNumber = playersNumber;
        this.game = new Game(playersNumber, firstPlayer, expertMode);
        this.controller = new Controller(this.game, this);
        //TODO: parse expertMode
        parametersSwitch(playersNumber);
        isStarted = 0;
        this.support = new PropertyChangeSupport(this);
    }

    public void setNewController() {
        this.controller = new Controller(this.game, this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public Game getGame() {
        return game;
    }

    public Controller getController() {
        return controller;
    }


    /**
     * Comunicate to the server that the game is over
     */
    public void endGameImmediately(Player winner) {
        System.out.println("sono in endGameImmediately");
        support.firePropertyChange("EndGame", 10, winner.getTeam());
        //comunica al server partita finita
    }

    /*
    Called when a game ends because:
    - the number of the archipelagos is less than 4
    - there are no remaining students in the bag

     */
    public void endGame() {
        Player winner = game.getListOfPlayer().get(0);
        List<Player> listOfWinners = new ArrayList<>();

        for (Player p : game.getListOfPlayer()) {
            if (p.getMyBoard().getTowersOnBoard().getNumberOfTowers() < winner.getMyBoard().getTowersOnBoard().getNumberOfTowers()) {
                winner = p;
            }
        }
        for (Player sameTower : game.getListOfPlayer()) {
            if (sameTower.getMyBoard().getTowersOnBoard().getNumberOfTowers() == winner.getMyBoard().getTowersOnBoard().getNumberOfTowers() && sameTower != winner) {
                listOfWinners.add(sameTower);
            }
        }

        int maxProfs = winner.getMyBoard().getProfessorsTable().getNumberOfProf();

        for (Player p : listOfWinners) {
            if (p.getMyBoard().getProfessorsTable().getNumberOfProf() > maxProfs) {
                winner = p;
                maxProfs = p.getMyBoard().getProfessorsTable().getNumberOfProf();
            }


        }
        game.setPhase(Phase.END_GAME);
        support.firePropertyChange("EndGame", 10, winner.getTeam());
    }

    public void setIsStarted(int i) {
        this.isStarted = i;
    }

    public int getIsStarted() {
        return isStarted;
    }

    /**
     * Add a new player (since the second one) to the game
     * Set isStarted = 1 when the numberOfPlayers required is reached and call startGame
     *
     * @param player to be added
     */
    public void addNewPlayer(Player player) {
        game.addPlayer(player);
        if (game.getOrderOfPlayers().size() == game.getNumberOfPlayers()) {
            setIsStarted(1);
            startGame();
        }
    }


    /**
     * Start the game:
     * Pick n students from the bag and place them to the players' board
     * Add m towers to each player
     * Start the turn
     */
    public void startGame() {

        for (Player p : game.getOrderOfPlayers()) {
            p.getMyBoard().getEntrance().addStudent(game.getBag().pickStudent(maxStudentsInEntrance));
            if (p.getColorOfTowers() != null) {
                for (int i = 0; i < maxNumberOfTowers; i++) {
                    p.getMyBoard().getTowersOnBoard().addTower();
                }
            }
            if (game.isExpertModeOn()) {
                if (game.getCoinFromBank(1)) {
                    p.addCoinsToWallet(1);
                }
            }
        }

        controller.getTurnController().startTurn();


    }

    /**
     * Set the value of the game parameters according to the number of players and if it is expert mode
     *
     * @param numberOfPlayers number of the players that are playing the game
     */
    private void parametersSwitch(int numberOfPlayers) {

        switch (numberOfPlayers) {
            case 2 -> {
                numberOfClouds = 2;
                maxNumberOfTowers = 8;
                maxStudentsInEntrance = 7;
                numberOfStudentsOnCloud = 3;
                numberOfMovements = 3;
            }
            case 3 -> {
                numberOfClouds = 3;
                maxNumberOfTowers = 6;
                maxStudentsInEntrance = 9;
                numberOfStudentsOnCloud = 4;
                numberOfMovements = 4;
            }
            case 4 -> {
                numberOfClouds = 4;
                maxNumberOfTowers = 8;
                maxStudentsInEntrance = 7;
                numberOfStudentsOnCloud = 3;
                numberOfMovements = 3;
            }
        }
    }

    public int getNumberOfStudentsOnCloud() {
        return numberOfStudentsOnCloud;
    }

    public int getNumberOfClouds() {
        return numberOfClouds;
    }

    public int getNumberOfMovements() {
        return numberOfMovements;
    }

    public int getPlayersNumber() {
        return playersNumber;
    }
}