package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameControllerTest {

    private static final float SPEED = 0.4f;

    @Test
    void executesCommandsOfAllStrategiesInOrder() {
        List<String> executed = new ArrayList<>();
        ControlStrategy first = () -> asList(() -> executed.add("a"), () -> executed.add("b"));
        ControlStrategy second = () -> singletonList(() -> executed.add("c"));

        new GameController(asList(first, second)).executeCommands();

        assertEquals(asList("a", "b", "c"), executed);
    }

    @Test
    void doesNothingWithoutStrategies() {
        new GameController(emptyList()).executeCommands();
    }

    @Test
    void asksStrategiesForNewCommandsEachTime() {
        int[] calls = new int[1];
        ControlStrategy strategy = () -> {
            calls[0]++;
            return emptyList();
        };
        GameController controller = new GameController(singletonList(strategy));

        controller.executeCommands();
        controller.executeCommands();

        assertEquals(2, calls[0]);
    }

    @Test
    void strategiesAreCopied() {
        List<ControlStrategy> strategies = new ArrayList<>();
        List<String> executed = new ArrayList<>();
        strategies.add(() -> singletonList(() -> executed.add("a")));
        GameController controller = new GameController(strategies);

        strategies.clear();
        controller.executeCommands();

        assertEquals(singletonList("a"), executed);
    }

    @Test
    void secondTankCannotEnterTileTakenByFirstInSameFrame() {
        Tank first = new Tank(new GridPoint2(0, 0), Direction.RIGHT, SPEED);
        Tank second = new Tank(new GridPoint2(2, 0), Direction.LEFT, SPEED);
        Level level = new Level(3, 1, asList(first, second));
        GameController controller = new GameController(asList(
                () -> singletonList(new MoveCommand(first, Direction.RIGHT, level)),
                () -> singletonList(new MoveCommand(second, Direction.LEFT, level))));

        controller.executeCommands();

        assertTrue(first.isMoving());
        assertFalse(second.isMoving());
        assertEquals(new GridPoint2(1, 0), first.getDestinationCoordinates());
        assertEquals(new GridPoint2(2, 0), second.getDestinationCoordinates());
    }
}
