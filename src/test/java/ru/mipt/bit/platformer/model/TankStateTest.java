package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.mipt.bit.platformer.model.state.TankState;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankStateTest {

    private static final float MOVEMENT_DURATION = 0.4f;
    private static final CollisionChecker FREE = position -> true;

    @ParameterizedTest
    @CsvSource({"100, 1", "71, 1", "70, 2", "16, 2", "15, 2", "14, 3", "1, 3"})
    void movementDurationDependsOnRemainingHealth(int health, int durationMultiplier) {
        Tank tank = tankWithHealth(health);
        tank.move(Direction.RIGHT, FREE);

        tank.update(MOVEMENT_DURATION);

        assertEquals(1f / durationMultiplier, tank.getMovementProgress(), 1e-6f);
        tank.update(MOVEMENT_DURATION * (durationMultiplier - 1));
        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(2, 1), tank.getCoordinates());
    }

    @ParameterizedTest
    @CsvSource({"100, true", "71, true", "70, true", "15, true", "14, false", "1, false", "0, false"})
    void shootingDependsOnRemainingHealth(int health, boolean canShoot) {
        Tank tank = tankWithHealth(health);
        Level level = new Level(10, 8, singletonList(tank));

        tank.shoot(level);

        assertEquals(canShoot ? 1 : 0, level.objectsAt(new GridPoint2(2, 1)).size());
    }

    @ParameterizedTest
    @CsvSource({"80, 57, 1", "80, 56, 2", "80, 12, 2", "80, 11, 3",
            "7, 5, 1", "7, 4, 2", "7, 2, 2", "7, 1, 3"})
    void thresholdsAreRelativeToInitialHealth(int maxHealth, int health, int durationMultiplier) {
        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, MOVEMENT_DURATION, maxHealth, 25);
        tank.takeDamage(maxHealth - health);
        tank.move(Direction.RIGHT, FREE);

        tank.update(MOVEMENT_DURATION);

        assertEquals(1f / durationMultiplier, tank.getMovementProgress(), 1e-6f);
    }

    @Test
    void damageChangesSpeedDuringMovementWithoutResettingProgress() {
        Tank tank = tankWithHealth(100);
        tank.move(Direction.RIGHT, FREE);
        tank.update(MOVEMENT_DURATION / 2f);

        tank.takeDamage(30);
        tank.update(MOVEMENT_DURATION / 2f);

        assertEquals(0.75f, tank.getMovementProgress(), 1e-6f);
        tank.takeDamage(56);
        tank.update(MOVEMENT_DURATION * 0.75f);
        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(2, 1), tank.getCoordinates());
    }

    @Test
    void heavyDamageDisablesShootingEvenAfterCooldown() {
        Tank tank = tankWithHealth(100);
        Level level = new Level(10, 8, singletonList(tank));
        tank.takeDamage(86);
        tank.update(Tank.DEFAULT_SHOOT_COOLDOWN * 10);

        tank.shoot(level);

        assertTrue(level.isFree(new GridPoint2(2, 1)));
    }

    @Test
    void newStateCanDoubleMovementAndFiringRate() {
        Tank tank = tankWithHealth(100);
        Level level = new Level(10, 8, singletonList(tank));
        tank.changeState(boostedState());
        tank.move(Direction.UP, FREE);

        tank.shoot(level);
        removeBullet(level, new GridPoint2(1, 2));
        tank.update(MOVEMENT_DURATION / 2f);

        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 2), tank.getCoordinates());
        tank.shoot(level);
        assertTrue(level.isFree(new GridPoint2(1, 3)));

        tank.update(Tank.DEFAULT_SHOOT_COOLDOWN / 2f - MOVEMENT_DURATION / 2f);
        tank.shoot(level);
        assertEquals(1, level.objectsAt(new GridPoint2(1, 3)).size());
    }

    @Test
    void slowerFiringStateCanFinishLongerCooldown() {
        Tank tank = tankWithHealth(100);
        Level level = new Level(10, 8, singletonList(tank));
        tank.changeState(new TankState() {
            @Override
            public float movementDuration(float baseDuration) {
                return baseDuration;
            }

            @Override
            public float shootCooldown(float baseCooldown) {
                return baseCooldown * 2f;
            }
        });
        tank.update(Tank.DEFAULT_SHOOT_COOLDOWN);
        tank.shoot(level);
        removeBullet(level, new GridPoint2(2, 1));
        tank.update(Tank.DEFAULT_SHOOT_COOLDOWN);
        tank.shoot(level);
        assertTrue(level.isFree(new GridPoint2(2, 1)));

        tank.update(Tank.DEFAULT_SHOOT_COOLDOWN);
        tank.shoot(level);

        assertEquals(1, level.objectsAt(new GridPoint2(2, 1)).size());
    }

    @Test
    void healthPolicyCanBeReplacedWithoutChangingTank() {
        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, MOVEMENT_DURATION,
                100, 25, 0.1f, (health, maxHealth) -> boostedState());
        tank.takeDamage(90);
        tank.move(Direction.RIGHT, FREE);

        tank.update(MOVEMENT_DURATION / 2f);

        assertFalse(tank.isMoving());
        Level level = new Level(10, 8, singletonList(tank));
        tank.shoot(level);
        assertEquals(1, level.objectsAt(new GridPoint2(3, 1)).size());
    }

    @Test
    void bulletDamageChangesTargetsMovementAndShootingBehavior() {
        Tank shooter = new Tank(new GridPoint2(1, 1), Direction.RIGHT, MOVEMENT_DURATION, 100, 86);
        Tank target = new Tank(new GridPoint2(2, 1), Direction.RIGHT, MOVEMENT_DURATION);
        Level level = new Level(10, 8, asList(shooter, target));

        shooter.shoot(level);
        target.move(Direction.UP, level);
        target.update(MOVEMENT_DURATION);
        target.shoot(level);

        assertEquals(14, target.getHealth());
        assertEquals(1f / 3f, target.getMovementProgress(), 1e-6f);
        assertEquals(singletonList(target), level.objectsAt(new GridPoint2(2, 2)));
    }

    @Test
    void zeroDamageDoesNotCancelAnExplicitState() {
        Tank tank = tankWithHealth(100);
        tank.changeState(boostedState());

        tank.takeDamage(0);
        tank.move(Direction.RIGHT, FREE);
        tank.update(MOVEMENT_DURATION / 2f);

        assertFalse(tank.isMoving());
    }

    @Test
    void deadTankCannotBeRevivedByChangingState() {
        Tank tank = tankWithHealth(0);
        tank.changeState(boostedState());
        Level level = new Level(10, 8, singletonList(tank));

        tank.move(Direction.RIGHT, FREE);
        tank.shoot(level);
        tank.update(10f);

        assertFalse(tank.isAlive());
        assertFalse(tank.isMoving());
        assertTrue(level.isFree(new GridPoint2(2, 1)));
    }

    @Test
    void stateCannotBeNull() {
        Tank tank = tankWithHealth(100);

        assertThrows(NullPointerException.class, () -> tank.changeState(null));
    }

    private static Tank tankWithHealth(int health) {
        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, MOVEMENT_DURATION);
        tank.takeDamage(100 - health);
        return tank;
    }

    private static TankState boostedState() {
        return new TankState() {
            @Override
            public float movementDuration(float baseDuration) {
                return baseDuration / 2f;
            }

            @Override
            public float shootCooldown(float baseCooldown) {
                return baseCooldown / 2f;
            }
        };
    }

    private static void removeBullet(Level level, GridPoint2 coordinates) {
        for (GameObject object : level.objectsAt(coordinates)) {
            if (object instanceof Bullet) {
                level.removeObject(object);
            }
        }
    }
}
