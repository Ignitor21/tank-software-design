package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TreeTest {

    @Test
    void keepsItsOwnCopyOfCoordinates() {
        GridPoint2 coordinates = new GridPoint2(1, 3);
        Tree tree = new Tree(coordinates);

        coordinates.set(0, 0);

        assertEquals(new GridPoint2(1, 3), tree.getCoordinates());
    }

    @Test
    void occupiesOnlyItsTile() {
        Tree tree = new Tree(new GridPoint2(1, 3));

        assertTrue(tree.occupies(new GridPoint2(1, 3)));
        assertFalse(tree.occupies(new GridPoint2(1, 2)));
    }
}
