package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

public class GameHandler {
    private Controller controller;
    private Game game;
    private int isStarted;
    private int playersNumber;

    private int numberOfClouds;
    private int maxNumberOfTowers;
    private int maxStudentsInEntrance;
    private int numberOfStudentsOnCloud;
    private int numberOfMovements;

    public GameHandler(Player firstPlayer, int playersNumber){
        this.playersNumber = playersNumber;
        this.game = new Game(playersNumber, firstPlayer);
        this.controller = new Controller(this.game, this);
        //TODO: parse expertMode
        parametersSwitch(playersNumber, false);
        isStarted = 0;
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

    /**
     * Add a new player (since the second one) to the game
     * Set isStarted = 1 when the numberOfPlaers required is reached and call startGame
     * @param nickname name chosen by the player
     * @param colorOfTower color chosen by the player
     */
    public void addNewPlayer(String nickname, ColorOfTower colorOfTower){
        Player newPlayer = new Player(nickname, colorOfTower);
        if(checkColorTower(colorOfTower)) {
            game.addPlayer(newPlayer);
            if (game.getOrderOfPlayers().size() == game.getNumberOfPlayers()) {
                setIsStarted(1);
                startGame();
            }
        }else{
            System.out.println("colore già scelto, scegline un altro");
        }

    }

    /**
     * Check if the chosed color is already been taken
     * @param colorOfTower color of the tower to check
     * @return True if the color is still available, false if it is already been taken
     */
    public boolean checkColorTower(ColorOfTower colorOfTower){
        for(Player p : game.getOrderOfPlayers()){
            if(p.getColorOfTowers() == colorOfTower){
                return false;
            }
        }
        return true;
    }

    /**
     *  Start the game:
     * Pick n students from the bag and place them to the players' board
     * Add m towers to each player
     * Start the turn
     */
    public void startGame(){

            for(Player p : game.getOrderOfPlayers()){
                p.getMyBoard().getEntrance().addStudent(game.getBag().pickStudent(maxStudentsInEntrance));
                for(int i = 0; i< maxNumberOfTowers; i++){
                    p.getMyBoard().getTowersOnBoard().addTower();
                }
            }
            controller.getTurnController().startTurn();


    }

    /**
     * Set the value of the game parameters according to the number of players and if it is expert mode
     * @param numberOfPlayers number of the players that are playing the game
     * @param expertMode 1 if the expert mode has been choosed, 0 otherwise
     */
    private void parametersSwitch(int numberOfPlayers, boolean expertMode){

        switch (numberOfPlayers){
            case 2:
                numberOfClouds = 2;
                maxNumberOfTowers = 8;
                maxStudentsInEntrance = 7;
                numberOfStudentsOnCloud = 3;
                numberOfMovements = 3;
                break;
            case 3:
                numberOfClouds = 3;
                maxNumberOfTowers = 6;
                maxStudentsInEntrance = 9;
                numberOfStudentsOnCloud = 4;
                numberOfMovements = 4;
                break;
            case 4:
                numberOfClouds = 4;
                maxNumberOfTowers = 8;
                maxStudentsInEntrance = 7;
                numberOfStudentsOnCloud = 3;
                numberOfMovements = 3;
                break;
        }
    }

    public int getNumberOfStudentsOnCloud() {
        return numberOfStudentsOnCloud;
    }

    public int getNumberOfClouds() {
        return numberOfClouds;
    }

    public int getMaxStudentsInEntrance() {
        return maxStudentsInEntrance;
    }

    public int getNumberOfMovements() {
        return numberOfMovements;
    }

    public int getMaxNumberOfTowers() {
        return maxNumberOfTowers;
    }

    public int getPlayersNumber() {
        return playersNumber;
    }
}
