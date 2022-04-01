package it.polimi.ingsw.controller;

import com.sun.org.apache.bcel.internal.Const;
import it.polimi.ingsw.constants.Constants;
import it.polimi.ingsw.model.*;

//TODO: ADDTESTS!!!!!!
public class TurnController {
    private Controller controller;
    private ActionController actionController;
    private GameHandler gameHandler;
    private Game game;

    public TurnController(Controller controller, GameHandler gameHandler, Game game){
        this.controller = controller;
        this.actionController = new ActionController(game, this);
        this.gameHandler = gameHandler;
        this.game = game;

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

//TODO: place a listener for the number of students to end the game?
    /**
     * Start the turn :
     * Add the students to the clouds
     * Call endGame if the number of the students in the bag is finished
     */
    public void startTurn(){
        //Add the students to the clouds
        for(Cloud cloud : game.getListOfClouds()){
            cloud.addStudents( game.getBag().pickStudent(gameHandler.getNumberOfStudentsOnCloud()) );
        }
        if(game.getBag().getNumberOfLeftStudents() == 0){
            endGame();
        }
    }
//TODO: listner of orderOfPlayer instead of calling it directly?
    /**
     * Receive a card and add it to lastUsedCard of the player
     * if the player is the last player find the new player order
     * @param player player that play the card
     * @param power power of the card that the player wants to use
     */
    public void manageReceivedCard(Player player, int power){
            //If the card is in the deck, remove it and place as last used card
            if(checkCardPresence(player, power) ){
                if(checkCardUsage(player, power)){
                    for(Card c : player.getMyDeck().getLeftCards()){
                        if(c.getPower() == power){
                            player.getMyDeck().useCard(c);
                            player.setLastUsedCard(c);

                            if(player == game.getOrderOfPlayers().get(game.getOrderOfPlayers().size()-1)){
                                game.findPlayerOrder();
                            }
                        }
                    }
                }else{
                    System.out.println("Card already played in this turn");
                }
            }else{
                System.out.println("Card not present in the deck!");
            }

    }

    //TODO: addTest
    /**
     * Check that no one else chose the same card before if the size of the player that send it is >1
     * @param p player that is playing the card
     * @param power of the card to be checked
     * @return True if the card has not been used before in the same turn, false otherwise
     */
    public boolean checkCardUsage(Player p, int power){
        if(p.getMyDeck().getLeftCards().size() >1 ){
            for(int i = 0; i< game.getOrderOfPlayers().indexOf(p); i++){
                if(game.getOrderOfPlayers().get(i).getLastUsedCard().getPower() == power){
                    return false;
                }
            }
            return true;
        }else{
            return true;
        }
    }

    //TODO: addTest
    /**
     * Check if a card with a power is present in the player's deck
     * @param p player that played the card
     * @param power power of the card to be checked
     * @return True if the card is in the deck, false otherwise
     */
    public boolean checkCardPresence(Player p, int power){
        for(Card c : p.getMyDeck().getLeftCards()){
            if(c.getPower() == power) return true;
        }
        return false;
    }

    public void endTurn(){

    }

    /**
     * Check if a movement of a student from the entrance of the player's board is allowed
     * @param player that do the movement
     * @param color of the student that the player wants to move
     * @param destination of the student (0 = dining room, [1..12] number of the archipelago
     */
    public void manageStudentMovement(Player player, StudsAndProfsColor color, int destination){
        if(player.getMyBoard().getEntrance().getStudentsByColor(color) == 0){
            System.out.println("You do not have a student of color " + color.toString());
        }else{
            if(destination == 0){
                if(player.getMyBoard().getDiningRoom().getStudentsByColor(color) == Constants.MAXSTUDENTSINDINING){
                    System.out.println("Your dining room of the color " + color.toString() + " is full");
                }else{
                    player.getMyBoard().getEntrance().removeStudent(color);
                    player.getMyBoard().getDiningRoom().addStudent(color);
                }
            }else{
                player.getMyBoard().getEntrance().removeStudent(color);
                for (Archipelago arc : game.getListOfArchipelagos()){
                    for (Island island : arc.getBelongingIslands()){
                        if(island.getIdIsland() == destination){
                            island.addStudent(color);
                        }
                    }
                }
            }
        }
    }

    /**
     * Called when the game is over
     */
    public void endGame(){
        gameHandler.setIsStarted(-1);
        //notifyAll per gamehandler che chiama endgame
    }
}


