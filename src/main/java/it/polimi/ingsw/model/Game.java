package it.polimi.ingsw.model;

import it.polimi.ingsw.model.expertMode.*;
import it.polimi.ingsw.utilities.Constants;
import it.polimi.ingsw.utilities.EventName;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.*;

/**
 * Model of the game, menage the actions that require an overview of all the game (e.g. how to assign the professors)
 * and the chagement of the current player / current phase
 */
public class Game implements Serializable {
    private final List<Player> listOfPlayers;
    private final List<Archipelago> listOfArchipelagos;
    private final List<Cloud> listOfClouds;
    private final List<Player> orderOfPlayers;
    private final Bag bag;
    private final int numberOfPlayers;
    private Player currentPlayer;
    private Phase phase;
    private int bank;
    private Characters[] charactersPlayable;
    private final boolean expertModeOn;
    private final PropertyChangeSupport support;

    public Game(int numberOfPlayers, Player player, boolean expertModeOn) {
        this.support = new PropertyChangeSupport(this);
        this.listOfPlayers = new ArrayList<>();
        this.listOfArchipelagos = new ArrayList<>();
        this.listOfClouds = new ArrayList<>();
        this.orderOfPlayers = new ArrayList<>();
        this.numberOfPlayers = numberOfPlayers;
        listOfPlayers.add(player);
        orderOfPlayers.add(player);
        this.bag = new Bag();
        this.currentPlayer = player;
        phase = Phase.START_GAME;
        this.expertModeOn = expertModeOn;

        for (int i = Constants.IDSTARTINGARCMN; i <= Constants.NUMBEROFISLANDS; i++) {
            Archipelago arc = new Archipelago(i);
            listOfArchipelagos.add(arc);
        }
        for (int i = 0; i < numberOfPlayers; i++) {
            Cloud cloud = new Cloud(i);
            listOfClouds.add(cloud);
        }

        initializeArchipelagos();

        if (expertModeOn) {
            initializeExpertMode();
        }
    }

    /**
     * put a random student on each island except for the one on which is mother nature
     * and the opposite one
     */
    public void initializeArchipelagos() {
        int[] studentsForIslands = new int[Constants.NUMBEROFKINGDOMS];
        //Place randomly two students of each color on the islands (one student per island)
        //except on the island where MN is present and on the opposite one
        for (int i = 0; i < Constants.NUMBEROFKINGDOMS; i++) {
            studentsForIslands[i] = 2;
        }
        Random random = new Random();

        for (Archipelago a : this.listOfArchipelagos) {
            for (Island i : a.getBelongingIslands()) {
                if (a.getIdArchipelago() != Constants.IDSTARTINGARCMN && a.getIdArchipelago() != Constants.IDSTARTINGOPPOSITEARC) {
                    int value = random.nextInt(Constants.NUMBEROFKINGDOMS);
                    while (studentsForIslands[value] <= 0) {
                        value = random.nextInt(Constants.NUMBEROFKINGDOMS);
                    }
                    i.addStudent(StudsAndProfsColor.values()[value]);

                    studentsForIslands[value]--;
                }
            }
        }
    }

    /**
     * if expert mode is on, randomly choose 3 special character to be used during game
     */
    public void initializeExpertMode() {
        Random random = new Random();
        this.bank = 20;
        //Extract 3 characters that will be available for the game
        charactersPlayable = new Characters[Constants.NUMBEROFPLAYABLECHARACTERS];
        int i = 0;
        while (i < Constants.NUMBEROFPLAYABLECHARACTERS) {
            //Randomly generate a number between 1 and 8 (random last value is exclusive)
            int value = random.nextInt(1, Constants.NUMBEROFCHARACTERS + 1);
            boolean found = false;
            for (Characters c : charactersPlayable) {
                if (c != null) {
                    if (c.getId() == value) {
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                switch (value) {
                    case 1 -> charactersPlayable[i] = new Character1(this);
                    case 2 -> charactersPlayable[i] = new Character2(this);
                    case 3 -> charactersPlayable[i] = new Character3(this);
                    case 4 -> charactersPlayable[i] = new Character4(this);
                    case 5 -> charactersPlayable[i] = new Character5(this);
                    case 6 -> charactersPlayable[i] = new Character6(this);
                    case 7 -> charactersPlayable[i] = new Character7(this);
                    case 8 -> charactersPlayable[i] = new Character8(this);
                }
                i++;
            }
        }

    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
        for (Cloud c : getListOfClouds()) {
            c.addPropertyChangeListener(pcl);
        }
        for (Player p : getListOfPlayer()) {
            p.getMyDeck().addPropertyChangeListener(pcl);
            p.getMyBoard().getEntrance().addPropertyChangeListener(pcl);
        }
    }


    /**
     * Add a new player to the list of players
     *
     * @param player to be added to the game
     */
    public void addPlayer(Player player) {
        listOfPlayers.add(player);
        orderOfPlayers.add(player);
    }

    public List<Player> getListOfPlayer() {
        return this.listOfPlayers;
    }

    public List<Cloud> getListOfClouds() {
        return this.listOfClouds;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void setCurrentPlayer(Player p) {
        this.currentPlayer = p;
    }

    /**
     * For each player, according to the power of the card used, calculate his order in the next turn
     */
    public void findPlayerOrder() {
        this.currentPlayer = orderOfPlayers.get(1);
        orderOfPlayers.sort((o1, o2) -> {
            if (o1.getLastUsedCard() == null && o2.getLastUsedCard() == null) {
                return 0;
            } else {
                return o1.getLastUsedCard().compareTo(o2.getLastUsedCard());
            }
        });
        this.currentPlayer = orderOfPlayers.get(0);

        support.firePropertyChange(EventName.CurrentPlayerChanged, "aaaa", currentPlayer.getNickname());

    }

    public List<Player> getOrderOfPlayers() {
        return orderOfPlayers;
    }

    /**
     * Move Mother Nature from the current archipelago to another one
     *
     * @param steps number of steps that MN needs to do
     */
    public void moveMotherNature(int steps) {
        int index = 0;
        for (Archipelago arci : listOfArchipelagos) {
            if (arci.getIsMNPresent()) {
                index = listOfArchipelagos.indexOf(arci);
                arci.changeMNPresence();
                break;
            }
        }
        index += steps;
        if (index >= listOfArchipelagos.size()) {
            index = index % listOfArchipelagos.size();
        }
        for (Archipelago arci : listOfArchipelagos) {
            if (listOfArchipelagos.indexOf(arci) == index) {
                arci.changeMNPresence();
                break;
            }
        }
    }

    public List<Archipelago> getListOfArchipelagos() {
        return listOfArchipelagos;
    }

    /**
     * Calculate who is the current player according to the ordered list of players for the action phase
     */
    public void calculateNextPlayerAction() {
        int index = 0;
        for (Player p : orderOfPlayers) {
            if (p.getNickname().equals(currentPlayer.getNickname())) {
                break;
            }
            index++;
        }
        index = index + 1;
        currentPlayer = orderOfPlayers.get(index);
        support.firePropertyChange(EventName.CurrentPlayerChanged, "", currentPlayer.getNickname());
    }

    /**
     * Calculate who is the current player according to the  list of players for the pianification phase
     */
    public void calculateNextPlayerPianification() {
        int index = 0;
        for (Player p : listOfPlayers) {
            if (p.getNickname().equals(currentPlayer.getNickname())) {
                break;
            }
            index++;
        }
        index = index + 1;
        if (index == listOfPlayers.size()) {
            index = 0;
        }
        currentPlayer = listOfPlayers.get(index);
        support.firePropertyChange(EventName.PhaseChanged, "", Phase.CARD_SELECTION);
        support.firePropertyChange(EventName.CurrentPlayerChanged, "CS", currentPlayer.getNickname());
    }

    /**
     * Check if a professor needs to be assigned to a player and do it
     *
     * @param color of the professor to check
     */
    public void assignProfessor(StudsAndProfsColor color) {
        if (currentPlayer.getUsedCharacter() != null && currentPlayer.getUsedCharacter().getId() == CharactersEnum.CHARACTER8.ordinal()) {
            Character8 character8 = new Character8(this);
            character8.assignProfessor(color);
        } else {
            Player player = currentPlayer;
            int max = 0;
            for (Player p : listOfPlayers) {
                if (p != currentPlayer) {
                    if (p.getMyBoard().getDiningRoom().getStudentsByColor(color) >= max) {
                        player = p;
                        max = p.getMyBoard().getDiningRoom().getStudentsByColor(color);
                    }
                }
            }
            if (currentPlayer.getMyBoard().getDiningRoom().getStudentsByColor(color) == player.getMyBoard().getDiningRoom().getStudentsByColor(color)) {
                player.getMyBoard().getProfessorsTable().removeProfessor(color);
                currentPlayer.getMyBoard().getProfessorsTable().removeProfessor(color);
            }
            if (currentPlayer.getMyBoard().getDiningRoom().getStudentsByColor(color) > player.getMyBoard().getDiningRoom().getStudentsByColor(color)) {
                player.getMyBoard().getProfessorsTable().removeProfessor(color);
                currentPlayer.getMyBoard().getProfessorsTable().addProfessor(color);
            }
        }
        support.firePropertyChange("ChangeProfessor", 0, 1);
        //TODO: check usage

    }

    /**
     * Unify two archipelagos adding the islands of the archToBeUnified to the one of the actualArchipelago
     *
     * @param actualArchipelago Archipelago where MN is present
     * @param archToBeUnified   Archipelago to be unified to the actual one
     */
    public void unifyArchipelagos(Archipelago actualArchipelago, Archipelago archToBeUnified) {
        for (Island island : archToBeUnified.getBelongingIslands()) {
            actualArchipelago.addIsland(island);
        }
        if (archToBeUnified.getIsForbidden()) {
            actualArchipelago.setIsForbidden(true);
        }
        listOfArchipelagos.remove(archToBeUnified);

        support.firePropertyChange(EventName.ArchUnified, 0, 1);
    }

    public Phase getPhase() {
        return phase;
    }

    /**
     * Decide the nextPhase of the match
     * every time that the phase of a player change send a message to everyone
     * saying what should he do
     */
    public void nextPhase() {
        switch (phase) {
            case START_GAME:
                phase = Phase.CARD_SELECTION;
                break;
            case CARD_SELECTION:
                phase = Phase.MOVE_STUDENTS;
                break;
            case MOVE_STUDENTS:
                phase = Phase.MOVE_MN;
                break;
            case MOVE_MN:
                phase = Phase.CLOUD_SELECTION;
                break;
            case CLOUD_SELECTION:
                if (getCurrentPlayer() == orderOfPlayers.get(orderOfPlayers.size() - 1)) {
                    phase = Phase.CARD_SELECTION;
                    support.firePropertyChange(EventName.IsLastPlayerOfTurn, currentPlayer, orderOfPlayers.get(0));
                    currentPlayer = orderOfPlayers.get(0);
                } else {
                    calculateNextPlayerAction();
                    phase = Phase.MOVE_STUDENTS;
                }
                break;
        }
        support.firePropertyChange(EventName.PhaseChanged, 0, 1);
    }

    public Bag getBag() {
        return bag;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * if there are enough coins in bank, removes the number of coin from it, else return false
     *
     * @param coins to be removed from the bank
     * @return true if it has correctly removed coins, else false
     */
    public boolean getCoinFromBank(int coins) {
        if (bank >= coins) {
            bank -= coins;
            return true;
        } else {
            return false;
        }
    }

    public int getBank() {
        return bank;
    }

    /**
     * Add an amount of coins to the bank of the game
     *
     * @param coins to be added
     */
    public void addCoinInBank(int coins) {
        bank += coins;
    }


    public Characters[] getCharactersPlayable() {
        return charactersPlayable;
    }

    //Only for testing purposes:
    public void setCharacterPlayable(Characters c) {
        charactersPlayable[0] = c;
    }

    /**
     * @return true if expert mode is on, else false
     */
    public boolean isExpertModeOn() {
        return expertModeOn;
    }

    public Archipelago getArchipelagoById(int idArchipelago) {
        for (Archipelago a : listOfArchipelagos) {
            if (a.getIdArchipelago() == idArchipelago) {
                return a;
            }
        }
        return null;
    }

    /**
     * sets the current phase
     *
     * @param phase current phase
     */
    public void setPhase(Phase phase) {
        this.phase = phase;
    }
}