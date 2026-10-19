package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelTest {

    private static final float SPEED = 0.4f;

    private final Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, SPEED);
    private final Level level = new Level(10, 8, asList(tank, new Tree(new GridPoint2(1, 3))));

    @Test
    void tileInsideLevelWithoutObjectsIsFree() {
        assertTrue(level.isFree(new GridPoint2(0, 0)));
        assertTrue(level.isFree(new GridPoint2(9, 7)));
    }

    @Test
    void tileWithTreeIsNotFree() {
        assertFalse(level.isFree(new GridPoint2(1, 3)));
    }

    @Test
    void tileWithTankIsNotFree() {
        assertFalse(level.isFree(new GridPoint2(1, 1)));
    }

    @Test
    void tilesOutsideLevelAreNotFree() {
        assertFalse(level.isFree(new GridPoint2(-1, 0)));
        assertFalse(level.isFree(new GridPoint2(0, -1)));
        assertFalse(level.isFree(new GridPoint2(10, 0)));
        assertFalse(level.isFree(new GridPoint2(0, 8)));
    }

    @Test
    void anyGameObjectCanBlockTile() {
        GridPoint2 blocked = new GridPoint2(5, 5);
        Level customLevel = new Level(10, 8, singletonList(blocked::equals));

        assertFalse(customLevel.isFree(blocked));
        assertTrue(customLevel.isFree(new GridPoint2(5, 6)));
    }

    @Test
    void treeBlocksTank() {
        tank.move(Direction.UP, level);
        level.update(SPEED);

        tank.move(Direction.UP, level);

        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 2), tank.getCoordinates());
    }

    @Test
    void levelBorderBlocksTank() {
        Tank cornerTank = new Tank(new GridPoint2(0, 0), Direction.RIGHT, SPEED);
        Level cornerLevel = new Level(10, 8, singletonList(cornerTank));

        cornerTank.move(Direction.LEFT, cornerLevel);

        assertFalse(cornerTank.isMoving());
        assertEquals(Direction.LEFT, cornerTank.getDirection());
    }

    @Test
    void updateAdvancesAllObjects() {
        List<Float> updates = new ArrayList<>();
        GameObject first = new GameObject() {
            @Override
            public boolean occupies(GridPoint2 coordinates) {
                return false;
            }

            @Override
            public void update(float deltaTime) {
                updates.add(deltaTime);
            }
        };
        Level levelWithObjects = new Level(10, 8, asList(first, tank));
        tank.move(Direction.UP, levelWithObjects);

        levelWithObjects.update(SPEED / 2);

        assertEquals(singletonList(SPEED / 2), updates);
        assertEquals(0.5f, tank.getMovementProgress(), 1e-6f);
    }

    @Test
    void objectsAreCopied() {
        List<GameObject> objects = new ArrayList<>();
        objects.add(new Tree(new GridPoint2(2, 2)));
        Level levelWithTree = new Level(10, 8, objects);

        objects.clear();

        assertFalse(levelWithTree.isFree(new GridPoint2(2, 2)));
        assertTrue(new Level(10, 8, emptyList()).isFree(new GridPoint2(2, 2)));
    }
}
