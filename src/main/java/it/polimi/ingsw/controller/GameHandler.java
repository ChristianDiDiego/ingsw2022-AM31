package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

public class GameHandler {
    private Controller controller;
    private Game game;
    private int isStarted;
    private int playersNumber;

    public GameHandler(Player firstPlayer, int playersNumber){
        this.playersNumber = playersNumber;
        this.game = new Game(playersNumber, firstPlayer);
        this.controller = new Controller(this.game, this);
        isStarted = 0;
    }

    public Game getGame() {
        return game;
    }

    public Controller getController() {
        return controller;
    }

    public void endGame(){
        //while isStarted != -1  o isFinished != 0 wait
        //comunica al server partita finita
    }

    public void setIsStarted(int i){
        this.isStarted = i;
    }

    public int getIsStarted(){
        return isStarted;
    }

    public void addNewPlayer(String nickname, ColorOfTower colorOfTower){
        Player newPlayer = new Player(nickname, colorOfTower);
        if(checkColorTower(colorOfTower)) {
            game.addPlayer(newPlayer);
            if (game.getOrderOfPlayers().size() == game.getNumberOfPlayers()) {
                setIsStarted(1);
                //notifyall
            }
        }else{
            System.out.println("colore già scelto, scegline un altro");
        }

    }

    public boolean checkColorTower(ColorOfTower colorOfTower){
        for(Player p : game.getOrderOfPlayers()){
            if(p.getColorOfTowers() == colorOfTower){
                return false;
            }
        }
        return true;
    }

    public void startGame(){
        //wait finche isstarted non diventa true
        //TODO: the hardcode values will be replaced with parameters
        if(playersNumber == 3){
            for(Player p : game.getOrderOfPlayers()){
                p.getMyBoard().getEntrance().addStudent(game.getBag().pickStudent(9));
            }
        } else{
            for(Player p : game.getOrderOfPlayers()) {
                p.getMyBoard().getEntrance().addStudent(game.getBag().pickStudent(7));
            }
        }

    }


}
