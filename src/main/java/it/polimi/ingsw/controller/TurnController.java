package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

public class TurnController {
    private Controller controller;
    private ActionController actionController;
    private GameHandler gameHandler;
    private Game game;

    public TurnController(Controller controller, GameHandler gameHandler, Game game){
        this.controller = controller;
        this.actionController = new ActionController(game);
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

    /**
     * Start the turn :
     * Add the students to the clouds
     * Receive all the cards: for every card received check if he has that card in the deck calling checkCardPresence
     * Check that no one else chose the same card before if the size of the player that send it is >1 calling ...
     * Pick the card from the deck and place it in lastUsedCard
     * Call endGame if the number of the students in the bag is finished
     * Otherwise call findPlayerOrder to order the players according to the card that they used
     */
    public void startTurn(){
        //aggiunge gli studenti sulle nuvole
        for(Cloud cloud : gameHandler.getGame().getListOfClouds()){
            cloud.addStudents( gameHandler.getGame().getBag().pickStudent(gameHandler.getNumberOfStudentsOnCloud()) );
        }
    }

    public void checkReceivedCard(Player player, int power){
        if(game.getPhase()== Phase.CARD_SELECTION && player == game.getCurrentPlayer()){
            //check che l'azione sia valida, in caso modifico il model

        }else if(game.getPhase()== Phase.CARD_SELECTION && player != game.getCurrentPlayer()){
            System.out.println("non è il tuo turno!!");
        }else if(game.getPhase()!= Phase.CARD_SELECTION && player == game.getCurrentPlayer()){
            System.out.println("hai inviato un'azione non valida, riprova");
        }
    }

    /**
     * Check if a card is present in the player's deck
     * @param p player that played the card
     * @param card card to be checked
     * @return True if the card is in the deck, false otherwise
     */
    public boolean checkCardPresence(Player p, Card card){
        return p.getMyDeck().getLeftCards().contains(card);
    }
    /*public boolean checkValidCard(Card card){

    } */


    public void endTurn(){

    }

    /**
     * Called when the game is over
     */
    public void endGame(){
        gameHandler.setIsStarted(-1);
        //notifyAll per gamehandler che chiama endgame
    }
}


