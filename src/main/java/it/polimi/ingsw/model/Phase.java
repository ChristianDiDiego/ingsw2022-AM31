package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * Enum for the phases of the game
 */
public enum Phase implements Serializable {
    START_GAME,
    CARD_SELECTION,
    MOVE_STUDENTS,
    MOVE_MN,
    CLOUD_SELECTION,
    END_GAME
}
