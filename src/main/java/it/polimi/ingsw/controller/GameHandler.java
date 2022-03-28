package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.ColorOfTower;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

public class GameHandler {
    private Controller controller;
    private Game game;
    private boolean isStarted;
    private int playersNumber;

    public GameHandler(Player firstPlayer, int playersNumber){
        this.playersNumber = playersNumber;
        this.game = new Game(playersNumber, firstPlayer);
        this.controller = new Controller(this.game, this);
        isStarted = false;
    }

    public Game getGame() {
        return game;
    }

    public Controller getController() {
        return controller;
    }

    public void endGame(){

    }

    public boolean getIsStarted(){
        return isStarted;
    }

    public void addNewPlayer(String nickname, ColorOfTower colorOfTower){
        //se game.getorderofplayer.size = game.getnumberplayer mette isstarted a true
    }
    //questa chiama checkColorTower

    public boolean checkColorTower(){
        return true;
    }

    public void startGame(){
    }

}
