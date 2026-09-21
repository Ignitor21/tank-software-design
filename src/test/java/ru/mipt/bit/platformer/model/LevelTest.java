package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelTest {

    private static final float SPEED = 0.4f;

    private final Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, SPEED);
    private final Level level = new Level(10, 8, tank, singletonList(new Tree(new GridPoint2(1, 3))));

    @Test
    void tileInsideLevelWithoutTreeIsFree() {
        assertTrue(level.isFree(new GridPoint2(0, 0)));
        assertTrue(level.isFree(new GridPoint2(9, 7)));
    }

    @Test
    void tileWithTreeIsNotFree() {
        assertFalse(level.isFree(new GridPoint2(1, 3)));
    }

    @Test
    void tilesOutsideLevelAreNotFree() {
        assertFalse(level.isFree(new GridPoint2(-1, 0)));
        assertFalse(level.isFree(new GridPoint2(0, -1)));
        assertFalse(level.isFree(new GridPoint2(10, 0)));
        assertFalse(level.isFree(new GridPoint2(0, 8)));
    }

    @Test
    void movesPlayerTank() {
        level.movePlayerTank(Direction.UP);

        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
    }

    @Test
    void treeBlocksPlayerTank() {
        level.movePlayerTank(Direction.UP);
        level.update(SPEED);

        level.movePlayerTank(Direction.UP);

        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 2), tank.getCoordinates());
    }

    @Test
    void levelBorderBlocksPlayerTank() {
        Tank cornerTank = new Tank(new GridPoint2(0, 0), Direction.RIGHT, SPEED);
        Level cornerLevel = new Level(10, 8, cornerTank, new ArrayList<>());

        cornerLevel.movePlayerTank(Direction.LEFT);

        assertFalse(cornerTank.isMoving());
        assertEquals(Direction.LEFT, cornerTank.getDirection());
    }

    @Test
    void updateAdvancesPlayerTank() {
        level.movePlayerTank(Direction.UP);

        level.update(SPEED / 2);

        assertEquals(0.5f, tank.getMovementProgress(), 1e-6f);
    }

    @Test
    void treesAreCopiedAndReadOnly() {
        List<Tree> trees = new ArrayList<>();
        trees.add(new Tree(new GridPoint2(2, 2)));
        Level levelWithTrees = new Level(10, 8, tank, trees);

        trees.clear();

        assertEquals(1, levelWithTrees.getTrees().size());
        assertThrows(UnsupportedOperationException.class, () -> levelWithTrees.getTrees().clear());
    }
}
