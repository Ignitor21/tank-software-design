package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.CollisionChecker;
import ru.mipt.bit.platformer.model.Direction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class MoveCommandTest {

    @Test
    void movesMovableInGivenDirectionUsingCollisionChecker() {
        CollisionChecker collisionChecker = coordinates -> true;
        Direction[] movedDirection = new Direction[1];
        CollisionChecker[] usedChecker = new CollisionChecker[1];

        new MoveCommand((direction, checker) -> {
            movedDirection[0] = direction;
            usedChecker[0] = checker;
        }, Direction.DOWN, collisionChecker).execute();

        assertEquals(Direction.DOWN, movedDirection[0]);
        assertSame(collisionChecker, usedChecker[0]);
    }
}
