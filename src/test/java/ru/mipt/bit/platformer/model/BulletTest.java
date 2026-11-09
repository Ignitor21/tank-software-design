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

class BulletTest {

    private static final float TANK_SPEED = 0.4f;
    private static final float BULLET_SPEED = 0.1f;

    @Test
    void tankShotSpawnsBulletInFrontOfTank() {
        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, TANK_SPEED);
        Level level = new Level(5, 5, singletonList(tank));

        tank.shoot(level);

        assertEquals(1, bulletsAt(level, new GridPoint2(2, 1)).size());
    }

    @Test
    void bulletFliesInTankDirection() {
        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, TANK_SPEED, Tank.DEFAULT_HEALTH,
                Tank.DEFAULT_BULLET_DAMAGE, BULLET_SPEED);
        Level level = new Level(6, 5, singletonList(tank));
        tank.shoot(level);
        Bullet bullet = bulletsAt(level, new GridPoint2(2, 1)).get(0);

        bullet.update(0f);
        bullet.update(BULLET_SPEED);

        assertEquals(new GridPoint2(3, 1), bullet.getCoordinates());
    }

    @Test
    void bulletLeavesLevelThroughBorder() {
        Tank tank = new Tank(new GridPoint2(4, 1), Direction.RIGHT, TANK_SPEED);
        Level level = new Level(6, 5, singletonList(tank));
        tank.shoot(level);
        Bullet bullet = bulletsAt(level, new GridPoint2(5, 1)).get(0);

        bullet.update(0f);

        assertFalse(bullet.isAlive());
    }

    @Test
    void bulletVanishesOnTreeCollision() {
        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, TANK_SPEED);
        Tree tree = new Tree(new GridPoint2(2, 1));
        Level level = new Level(5, 5, asList(tank, tree));

        tank.shoot(level);

        assertTrue(bulletsAt(level, new GridPoint2(2, 1)).isEmpty());
        assertEquals(singletonList(tree), level.objectsAt(new GridPoint2(2, 1)));
    }

    @Test
    void adjacentTankIsHitImmediately() {
        Tank shooter = new Tank(new GridPoint2(1, 1), Direction.RIGHT, TANK_SPEED, 100, 30);
        Tank target = new Tank(new GridPoint2(2, 1), Direction.LEFT, TANK_SPEED);
        Level level = new Level(5, 5, asList(shooter, target));

        shooter.shoot(level);

        assertEquals(70, target.getHealth());
        assertTrue(bulletsAt(level, new GridPoint2(2, 1)).isEmpty());
    }

    @Test
    void bulletDamagesTankItRunsInto() {
        Tank shooter = new Tank(new GridPoint2(0, 1), Direction.RIGHT, TANK_SPEED, 100, 40, BULLET_SPEED);
        Tank target = new Tank(new GridPoint2(3, 1), Direction.LEFT, TANK_SPEED);
        Level level = new Level(5, 5, asList(shooter, target));
        shooter.shoot(level);
        Bullet bullet = bulletsAt(level, new GridPoint2(1, 1)).get(0);

        bullet.update(0f);
        bullet.update(BULLET_SPEED);
        bullet.update(0f);

        assertEquals(60, target.getHealth());
        assertFalse(bullet.isAlive());
    }

    @Test
    void tanksWithZeroHealthAreRemovedAfterUpdate() {
        Tank shooter = new Tank(new GridPoint2(1, 1), Direction.RIGHT, TANK_SPEED, 100, 100);
        Tank target = new Tank(new GridPoint2(2, 1), Direction.LEFT, TANK_SPEED);
        Level level = new Level(5, 5, asList(shooter, target));

        shooter.shoot(level);
        level.update(0f);

        assertTrue(level.isFree(new GridPoint2(2, 1)));
    }

    @Test
    void bulletsDestroyEachOtherOnCollision() {
        Level level = new Level(5, 1, emptyList());
        Bullet first = new Bullet(new GridPoint2(1, 0), Direction.RIGHT, BULLET_SPEED, 10, level, null);
        Bullet second = new Bullet(new GridPoint2(3, 0), Direction.LEFT, BULLET_SPEED, 10, level, null);
        level.addObject(first);
        level.addObject(second);

        level.update(0f);

        assertFalse(first.isAlive());
        assertFalse(second.isAlive());
    }

    private static List<Bullet> bulletsAt(Level level, GridPoint2 coordinates) {
        return bullets(level.objectsAt(coordinates));
    }

    private static List<Bullet> bullets(List<GameObject> objects) {
        List<Bullet> result = new ArrayList<>();
        for (GameObject object : objects) {
            if (object instanceof Bullet) {
                result.add((Bullet) object);
            }
        }
        return result;
    }
}
