package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankTest {

    private static final float SPEED = 0.4f;
    private static final CollisionChecker ALWAYS_FREE = coordinates -> true;
    private static final CollisionChecker ALWAYS_BLOCKED = coordinates -> false;

    private final Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, SPEED);

    @Test
    void initiallyStandsStill() {
        assertFalse(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
        assertEquals(Direction.RIGHT, tank.getDirection());
        assertEquals(1f, tank.getMovementProgress());
    }

    @Test
    void startsMovingToFreeNeighbour() {
        tank.move(Direction.UP, ALWAYS_FREE);

        assertTrue(tank.isMoving());
        assertEquals(Direction.UP, tank.getDirection());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getMovementProgress());
    }

    @Test
    void onlyTurnsWhenDestinationIsBlocked() {
        tank.move(Direction.UP, ALWAYS_BLOCKED);

        assertFalse(tank.isMoving());
        assertEquals(Direction.UP, tank.getDirection());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
    }

    @Test
    void asksCheckerAboutNeighbourTile() {
        GridPoint2[] checked = new GridPoint2[1];
        tank.move(Direction.LEFT, coordinates -> {
            checked[0] = coordinates;
            return true;
        });

        assertEquals(new GridPoint2(0, 1), checked[0]);
    }

    @Test
    void movementIsNotInterruptible() {
        tank.move(Direction.UP, ALWAYS_FREE);
        tank.update(SPEED / 2);

        tank.move(Direction.RIGHT, ALWAYS_FREE);

        assertEquals(Direction.UP, tank.getDirection());
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        assertEquals(0.5f, tank.getMovementProgress(), 1e-6f);
    }

    @Test
    void progressAdvancesProportionallyToSpeed() {
        tank.move(Direction.UP, ALWAYS_FREE);

        tank.update(SPEED / 4);

        assertEquals(0.25f, tank.getMovementProgress(), 1e-6f);
        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
    }

    @Test
    void arrivesAtDestinationAfterFullStep() {
        tank.move(Direction.UP, ALWAYS_FREE);

        tank.update(SPEED * 3);

        assertFalse(tank.isMoving());
        assertEquals(1f, tank.getMovementProgress());
        assertEquals(new GridPoint2(1, 2), tank.getCoordinates());
    }

    @Test
    void canMoveAgainAfterArrival() {
        tank.move(Direction.UP, ALWAYS_FREE);
        tank.update(SPEED);

        tank.move(Direction.RIGHT, ALWAYS_FREE);

        assertTrue(tank.isMoving());
        assertEquals(new GridPoint2(2, 2), tank.getDestinationCoordinates());
    }
}
