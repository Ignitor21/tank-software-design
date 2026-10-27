package ru.mipt.bit.platformer.generation;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomLevelGeneratorTest {

    @RepeatedTest(20)
    void placesPlayerAndTreesOnDistinctTilesInsideLevel() {
        LevelLayout layout = new RandomLevelGenerator(15, new Random()).generate(10, 8);

        Set<GridPoint2> occupied = new HashSet<>(layout.getTreePositions());
        assertEquals(15, occupied.size());
        assertFalse(occupied.contains(layout.getPlayerPosition()));
        occupied.add(layout.getPlayerPosition());
        for (GridPoint2 position : occupied) {
            assertTrue(position.x >= 0 && position.x < 10 && position.y >= 0 && position.y < 8);
        }
    }

    @Test
    void sameSeedGivesSameLayout() {
        LevelLayout first = new RandomLevelGenerator(5, new Random(42)).generate(10, 8);
        LevelLayout second = new RandomLevelGenerator(5, new Random(42)).generate(10, 8);

        assertEquals(first.getPlayerPosition(), second.getPlayerPosition());
        assertEquals(first.getTreePositions(), second.getTreePositions());
    }

    @Test
    void differentSeedsGiveDifferentLayouts() {
        LevelLayout first = new RandomLevelGenerator(5, new Random(1)).generate(10, 8);
        LevelLayout second = new RandomLevelGenerator(5, new Random(2)).generate(10, 8);

        assertNotEquals(first.getTreePositions(), second.getTreePositions());
    }

    @Test
    void canFillWholeLevel() {
        LevelLayout layout = new RandomLevelGenerator(5, new Random()).generate(3, 2);

        assertEquals(5, layout.getTreePositions().size());
    }

    @Test
    void failsWhenTreesDoNotFit() {
        assertThrows(IllegalArgumentException.class,
                () -> new RandomLevelGenerator(6, new Random()).generate(3, 2));
    }

    @RepeatedTest(20)
    void placesPlayerTreesAndTanksOnDistinctTilesInsideLevel() {
        LevelLayout layout = new RandomLevelGenerator(10, 5, new Random()).generate(10, 8);

        Set<GridPoint2> occupied = new HashSet<>(layout.getTreePositions());
        occupied.addAll(layout.getTankPositions());
        occupied.add(layout.getPlayerPosition());
        assertEquals(10, layout.getTreePositions().size());
        assertEquals(5, layout.getTankPositions().size());
        assertEquals(16, occupied.size());
        for (GridPoint2 position : occupied) {
            assertTrue(position.x >= 0 && position.x < 10 && position.y >= 0 && position.y < 8);
        }
    }

    @Test
    void generatesNoTanksByDefault() {
        LevelLayout layout = new RandomLevelGenerator(5, new Random()).generate(10, 8);

        assertTrue(layout.getTankPositions().isEmpty());
    }

    @Test
    void sameSeedGivesSameTankPositions() {
        LevelLayout first = new RandomLevelGenerator(5, 3, new Random(42)).generate(10, 8);
        LevelLayout second = new RandomLevelGenerator(5, 3, new Random(42)).generate(10, 8);

        assertEquals(first.getTankPositions(), second.getTankPositions());
    }

    @Test
    void canFillWholeLevelWithTreesAndTanks() {
        LevelLayout layout = new RandomLevelGenerator(3, 2, new Random()).generate(3, 2);

        assertEquals(3, layout.getTreePositions().size());
        assertEquals(2, layout.getTankPositions().size());
    }

    @Test
    void failsWhenTanksDoNotFit() {
        assertThrows(IllegalArgumentException.class,
                () -> new RandomLevelGenerator(3, 3, new Random()).generate(3, 2));
    }

    @Test
    void rejectsNegativeTankCount() {
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelGenerator(0, -1, new Random()));
    }

    @Test
    void rejectsNegativeTreeCount() {
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelGenerator(-1, new Random()));
    }
}
