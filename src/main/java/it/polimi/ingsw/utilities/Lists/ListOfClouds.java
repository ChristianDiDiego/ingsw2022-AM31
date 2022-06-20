package it.polimi.ingsw.utilities.Lists;

import it.polimi.ingsw.model.Cloud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Serializable list of clouds
 */
public class ListOfClouds implements Serializable {
    private List<Cloud> clouds = new ArrayList<>();

    public ListOfClouds(List<Cloud> clouds) {
        this.clouds = clouds;
    }

    public List<Cloud> getClouds() {
        return clouds;
    }
}
