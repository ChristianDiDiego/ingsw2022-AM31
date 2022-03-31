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

    public void startTurn(){
        //aggiunge gli studenti sulle nuvole
        for(Cloud cloud : gameHandler.getGame().getListOfClouds()){
            cloud.addStudents( gameHandler.getGame().getBag().pickStudent(gameHandler.getNumberOfStudentsOnCloud()) );
        }


        //riceve tutte le carte
        //Get the current player and wait a card from him

        //for every card received check if he has that card in the deck
        //checkCardPresence

        //controlla che nessuno giochi carta usata da altri se size del  deck è >1
        //pick the card from the deck and place it in lastUsedCard
        //chiama in caso EndGame se ho finito gli studenti o se il 10 turno
        //Otherwise call game.findPlayerOrder
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

    public boolean checkCardPresence(Player p, Card card){
        return p.getMyDeck().getLeftCards().contains(card);
    }
    /*public boolean checkValidCard(Card card){

    } */


    public void endTurn(){

    }

    public void endGame(){
        gameHandler.setIsStarted(-1);
        //notifyAll per gamehandler che chiama endgame
    }
}


