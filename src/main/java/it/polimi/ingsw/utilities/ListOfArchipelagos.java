package it.polimi.ingsw.utilities;

import it.polimi.ingsw.model.Archipelago;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListOfArchipelagos implements Serializable {
    private List<Archipelago> archipelagos = new ArrayList<>();

    public ListOfArchipelagos(List<Archipelago> archipelagos) {
        this.archipelagos= archipelagos;
    }

    public List<Archipelago> getArchipelagos() {
        return archipelagos;
    }
}
