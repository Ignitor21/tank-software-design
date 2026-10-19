package ru.mipt.bit.platformer.generation;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TextLevelGeneratorTest {

    private static final List<String> EXAMPLE = asList(
            "___T__T___",
            "__TT__TTTT",
            "_________T",
            "TTTT__T__T",
            "_____X____",
            "__________"
    );

    @Test
    void parsesExampleLevel() {
        LevelLayout layout = new TextLevelGenerator(EXAMPLE).generate(10, 8);

        assertEquals(new GridPoint2(5, 1), layout.getPlayerPosition());
        assertEquals(15, layout.getTreePositions().size());
    }

    @Test
    void firstLineIsTopRow() {
        LevelLayout layout = new TextLevelGenerator(asList("T_", "_X")).generate(2, 2);

        assertEquals(new GridPoint2(1, 0), layout.getPlayerPosition());
        assertEquals(asList(new GridPoint2(0, 1)), layout.getTreePositions());
    }

    @Test
    void readsAllTrees() {
        LevelLayout layout = new TextLevelGenerator(asList("TT_", "X_T")).generate(3, 2);

        assertEquals(new HashSet<>(asList(new GridPoint2(0, 1), new GridPoint2(1, 1), new GridPoint2(2, 0))),
                new HashSet<>(layout.getTreePositions()));
    }

    @Test
    void ignoresTrailingBlankLines() {
        LevelLayout layout = new TextLevelGenerator(asList("_X", "", "  ")).generate(2, 1);

        assertEquals(new GridPoint2(1, 0), layout.getPlayerPosition());
    }

    @Test
    void levelMayBeSmallerThanField() {
        LevelLayout layout = new TextLevelGenerator(asList("X")).generate(10, 8);

        assertEquals(new GridPoint2(0, 0), layout.getPlayerPosition());
    }

    @Test
    void rejectsLevelWithoutPlayer() {
        assertThrows(IllegalArgumentException.class,
                () -> new TextLevelGenerator(asList("T_", "__")).generate(2, 2));
    }

    @Test
    void rejectsLevelWithSeveralPlayers() {
        assertThrows(IllegalArgumentException.class,
                () -> new TextLevelGenerator(asList("X_", "_X")).generate(2, 2));
    }

    @Test
    void rejectsUnknownCell() {
        assertThrows(IllegalArgumentException.class,
                () -> new TextLevelGenerator(asList("X?")).generate(2, 1));
    }

    @Test
    void rejectsRowsOfDifferentLength() {
        assertThrows(IllegalArgumentException.class,
                () -> new TextLevelGenerator(asList("X__", "__")).generate(3, 2));
    }

    @Test
    void rejectsEmptyLevel() {
        assertThrows(IllegalArgumentException.class,
                () -> new TextLevelGenerator(emptyList()).generate(2, 2));
    }

    @Test
    void rejectsLevelLargerThanField() {
        assertThrows(IllegalArgumentException.class,
                () -> new TextLevelGenerator(EXAMPLE).generate(9, 8));
        assertThrows(IllegalArgumentException.class,
                () -> new TextLevelGenerator(EXAMPLE).generate(10, 5));
    }
}
