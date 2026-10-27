package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.CollisionChecker;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomControlStrategyTest {

    private static final float SPEED = 0.4f;

    @Test
    void givesOneMoveCommandPerCall() {
        List<Direction> moves = new ArrayList<>();
        RandomControlStrategy strategy = new RandomControlStrategy(
                (direction, checker) -> moves.add(direction), coordinates -> true, new Random(0));

        List<Command> commands = strategy.nextCommands();

        assertEquals(1, commands.size());
        assertTrue(moves.isEmpty());
        commands.get(0).execute();
        assertEquals(1, moves.size());
    }

    @Test
    void passesCollisionCheckerToMovable() {
        CollisionChecker collisionChecker = coordinates -> true;
        CollisionChecker[] usedChecker = new CollisionChecker[1];
        RandomControlStrategy strategy = new RandomControlStrategy(
                (direction, checker) -> usedChecker[0] = checker, collisionChecker, new Random(0));

        strategy.nextCommands().get(0).execute();

        assertSame(collisionChecker, usedChecker[0]);
    }

    @Test
    void eventuallyChoosesEveryDirection() {
        Set<Direction> chosen = EnumSet.noneOf(Direction.class);
        RandomControlStrategy strategy = new RandomControlStrategy(
                (direction, checker) -> chosen.add(direction), coordinates -> true, new Random(7));

        for (int i = 0; i < 200; i++) {
            strategy.nextCommands().get(0).execute();
        }

        assertEquals(EnumSet.allOf(Direction.class), chosen);
    }

    @Test
    void sameSeedGivesSameDirections() {
        assertEquals(directions(new Random(42)), directions(new Random(42)));
    }

    @Test
    void tankDrivenRandomlyStaysInsideLevelAndAvoidsObstacles() {
        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, SPEED);
        Tree tree = new Tree(new GridPoint2(1, 0));
        Level level = new Level(3, 3, asList(tank, tree));
        GameController controller = new GameController(
                singletonList(new RandomControlStrategy(tank, level, new Random(3))));

        for (int i = 0; i < 500; i++) {
            controller.executeCommands();
            level.update(SPEED / 2);
            GridPoint2 position = tank.getDestinationCoordinates();
            assertTrue(position.x >= 0 && position.x < 3 && position.y >= 0 && position.y < 3);
            assertFalse(position.equals(tree.getCoordinates()));
        }
    }

    private static List<Direction> directions(Random random) {
        List<Direction> directions = new ArrayList<>();
        RandomControlStrategy strategy = new RandomControlStrategy(
                (direction, checker) -> directions.add(direction), coordinates -> true, random);
        for (int i = 0; i < 20; i++) {
            strategy.nextCommands().get(0).execute();
        }
        return directions;
    }
}
