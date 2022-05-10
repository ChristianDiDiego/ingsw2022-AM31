package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Archipelago;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Player;

import java.util.List;

/**
 * Interface for the view
 */
public interface View {
    public void printLogo();

    void printMyDeck(Deck deck);
    void printLastUsedCards(List<Player> players);
    void printBoards(List<Player> players);

    void printArchipelagos(List<Archipelago> archipelagos);
    void printClouds(List<Cloud> clouds);
}
