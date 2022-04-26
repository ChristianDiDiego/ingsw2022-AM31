package it.polimi.ingsw.model.expertMode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharactersEnumTest {

    @Test
    void values() {
        assertEquals(8, CharactersEnum.values().length);
    }

    @Test
    void valueOf() {
        assertEquals("CHARACTER1", CharactersEnum.valueOf("CHARACTER1").toString());
    }
}