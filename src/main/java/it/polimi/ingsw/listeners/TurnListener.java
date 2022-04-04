package it.polimi.ingsw.listeners;

import it.polimi.ingsw.controller.GameHandler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TurnListener implements PropertyChangeListener {
    private GameHandler gameHandler;
    public TurnListener(GameHandler gameHandler){
        this.gameHandler = gameHandler;
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("listOfPlayers")){
            if(Integer.parseInt((String) evt.getNewValue()) == gameHandler.getPlayersNumber()){
                gameHandler.startGame();
            }
        }else if (evt.getPropertyName().equals("lastPlayerTurn")){
            if(gameHandler.getGame().getCurrentPlayer().getMyDeck().getLeftCards().size() == 0){
                gameHandler.endGame();
            }else {
                gameHandler.getController().getTurnController().startTurn();
            }
        }
    }
}
