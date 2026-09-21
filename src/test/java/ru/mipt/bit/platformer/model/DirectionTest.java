package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectionTest {

    private final GridPoint2 origin = new GridPoint2(5, 5);

    @Test
    void appliesDirectionVector() {
        assertEquals(new GridPoint2(5, 6), Direction.UP.apply(origin));
        assertEquals(new GridPoint2(4, 5), Direction.LEFT.apply(origin));
        assertEquals(new GridPoint2(5, 4), Direction.DOWN.apply(origin));
        assertEquals(new GridPoint2(6, 5), Direction.RIGHT.apply(origin));
    }

    @Test
    void applyDoesNotModifyArgument() {
        Direction.UP.apply(origin);

        assertEquals(new GridPoint2(5, 5), origin);
    }

    @Test
    void storesRotation() {
        assertEquals(90f, Direction.UP.getRotation());
        assertEquals(-180f, Direction.LEFT.getRotation());
        assertEquals(-90f, Direction.DOWN.getRotation());
        assertEquals(0f, Direction.RIGHT.getRotation());
    }
}
