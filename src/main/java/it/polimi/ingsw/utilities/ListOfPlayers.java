package it.polimi.ingsw.utilities;

import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListOfPlayers implements Serializable {
    private List<Player> players = new ArrayList<>();

    public ListOfPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
