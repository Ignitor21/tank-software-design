package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TreeTest {

    @Test
    void keepsItsOwnCopyOfCoordinates() {
        GridPoint2 coordinates = new GridPoint2(1, 3);
        Tree tree = new Tree(coordinates);

        coordinates.set(0, 0);

        assertEquals(new GridPoint2(1, 3), tree.getCoordinates());
    }
}
