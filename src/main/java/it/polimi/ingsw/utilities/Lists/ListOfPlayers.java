package it.polimi.ingsw.utilities.Lists;

import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
Serializable list of players
 */
public class ListOfPlayers implements Serializable {
    private List<Player> players = new ArrayList<>();

    public ListOfPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
