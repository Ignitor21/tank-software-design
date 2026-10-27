package ru.mipt.bit.platformer.generation;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelLayoutTest {

    @Test
    void hasNoTanksWhenOnlyTreesAreGiven() {
        LevelLayout layout = new LevelLayout(new GridPoint2(0, 0), singletonList(new GridPoint2(1, 1)));

        assertTrue(layout.getTankPositions().isEmpty());
    }

    @Test
    void tankPositionsAreCopied() {
        GridPoint2 tankPosition = new GridPoint2(2, 2);
        List<GridPoint2> tankPositions = new ArrayList<>(singletonList(tankPosition));
        LevelLayout layout = new LevelLayout(new GridPoint2(0, 0), new ArrayList<>(), tankPositions);

        tankPositions.clear();
        tankPosition.set(5, 5);

        assertEquals(singletonList(new GridPoint2(2, 2)), layout.getTankPositions());
    }

    @Test
    void tankPositionsAreUnmodifiable() {
        LevelLayout layout = new LevelLayout(new GridPoint2(0, 0), new ArrayList<>(),
                singletonList(new GridPoint2(2, 2)));

        assertThrows(UnsupportedOperationException.class, () -> layout.getTankPositions().clear());
    }
}
