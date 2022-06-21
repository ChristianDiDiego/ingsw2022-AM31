package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utilities.ErrorMessage;
import it.polimi.ingsw.utilities.Constants;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Menage the turn changements and the pianification phase
 */
public class TurnController {
    private final Controller controller;
    private final ActionController actionController;
    private final GameHandler gameHandler;
    private final Game game;
    private final PropertyChangeSupport support;
    private boolean isFinished = false;

    public TurnController(Controller controller, GameHandler gameHandler, Game game) {
        this.controller = controller;
        this.actionController = new ActionController(game, this);
        this.gameHandler = gameHandler;
        this.game = game;
        this.support = new PropertyChangeSupport(this);

    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public Controller getController() {
        return controller;
    }

    public ActionController getActionController() {
        return actionController;
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    /**
     * Start the turn :
     * Add the students to the clouds
     * Call endGame if the number of the students in the bag is finished
     */
    public void startTurn() {
        if (game.getBag().getNumberOfLeftStudents() < (gameHandler.getNumberOfStudentsOnCloud() * gameHandler.getNumberOfClouds()) || game.getCurrentPlayer().getMyDeck().getLeftCards().size() == 1) {
            isFinished = true;
        }

        if (game.getListOfPlayer().get(0).getMyDeck().getLeftCards().size() == Constants.NUMBEROFCARDSINDECK) {
            support.firePropertyChange("StartingGame", "", "Game is starting...");
        }

        if (!isFinished) {
            for (Cloud cloud : game.getListOfClouds()) {
                cloud.addStudents(game.getBag().pickStudent(gameHandler.getNumberOfStudentsOnCloud()));
            }
        }

        if (game.isExpertModeOn()) {
            for (Player p : game.getListOfPlayer()) {
                p.setUsedCharacter(null);
            }
        }

        game.nextPhase();
        game.setCurrentPlayer(game.getOrderOfPlayers().get(0));
        support.firePropertyChange("currentPlayerChanged", "CS", game.getCurrentPlayer().getNickname());
    }

    /**
     * Receive a card and add it to lastUsedCard of the player
     * if the player is the last player find the new player order
     *
     * @param player player that play the card
     * @param power  power of the card that the player wants to use
     * @return true if the player can use the card, false otherwise
     */
    public boolean checkActionCard(Player player, int power) {
        if (game.getPhase() == Phase.CARD_SELECTION && player == game.getCurrentPlayer()) {
            //If the card is in the deck, remove it and place as last used card
            if (checkCardPresence(player, power)) {
                if (checkCardUsage(player, power)) {
                    Card cardToUse = null;
                    for (Card c : player.getMyDeck().getLeftCards()) {
                        if (c.getPower() == power) {
                            cardToUse = c;
                            player.setLastUsedCard(c);
                            int index = 0;
                            for (Player p : game.getListOfPlayer()) {
                                if (p.getNickname().equals(player.getNickname())) {
                                    break;
                                }
                                index++;
                            }
                            index = index + 1;
                            if (index == game.getListOfPlayer().size()) {
                                index = 0;
                            }
                            if (game.getListOfPlayer().get(index) == game.getOrderOfPlayers().get(0)) {
                                //if(player == game.getListOfPlayer().get(game.getListOfPlayer().size()-1)){
                                //Send a message to all saying that the card selection phase is finished
                                game.findPlayerOrder();
                                game.nextPhase();
                                System.out.println("Passo alla fase " + game.getPhase());
                            } else {
                                game.calculateNextPlayerPianification();
                                support.firePropertyChange("PhaseChanged", 0, 1);

                            }
                        }
                    }
                    //Outside the for to avoid ConcurrentModificationException
                    player.getMyDeck().useCard(cardToUse);
                    return true;
                } else {
                    System.out.println("Card already played in this turn");
                    support.firePropertyChange("ErrorMessage", player.getNickname(), ErrorMessage.CardAlreadyTaken);
                    return false;
                }
            } else {
                System.out.println("Card not present in the deck!");
                support.firePropertyChange("ErrorMessage", player.getNickname(), ErrorMessage.CardNotPresent);
                return false;
            }

        } else if (game.getPhase() == Phase.CARD_SELECTION || player != game.getCurrentPlayer()) {
            System.out.println("non è il tuo turno!!");
            support.firePropertyChange("ErrorMessage", player.getNickname(), ErrorMessage.NotYourTurn);
            return false;
        } else {
            System.out.println("hai inviato un'azione non valida, riprova");
            support.firePropertyChange("ErrorMessage", player.getNickname(), ErrorMessage.ActionNotValid);
            return false;
        }
    }

    //TODO: addTest

    /**
     * Check that no one else chose the same card before if the size of the player that send it is >1
     *
     * @param p     player that is playing the card
     * @param power of the card to be checked
     * @return True if the card has not been used before in the same turn, false otherwise
     */
    private boolean checkCardUsage(Player p, int power) {
        if (p.getMyDeck().getLeftCards().size() > 1) {
            for (int i = 0; i < game.getOrderOfPlayers().indexOf(p); i++) {
                if (game.getOrderOfPlayers().get(i).getLastUsedCard().getPower() == power) {
                    return false;
                }
            }
        }
        return true;
    }

    //TODO: addTest

    /**
     * Check if a card with a power is present in the player's deck
     *
     * @param p     player that played the card
     * @param power power of the card to be checked
     * @return True if the card is in the deck, false otherwise
     */
    private boolean checkCardPresence(Player p, int power) {
        for (Card c : p.getMyDeck().getLeftCards()) {
            if (c.getPower() == power) return true;
        }
        return false;
    }

    public boolean isFinished() {
        return isFinished;
    }

}


