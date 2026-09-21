package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InputControllerTest {

    private final Set<Integer> pressedKeys = new HashSet<>();
    private final List<String> executed = new ArrayList<>();
    private final InputController controller = new InputController(pressedKeys::contains)
            .bind(UP, () -> executed.add("up"))
            .bind(W, () -> executed.add("w"))
            .bind(SPACE, () -> executed.add("space"));

    @Test
    void doesNothingWhenNoKeyIsPressed() {
        controller.handleInput();

        assertEquals(emptyList(), executed);
    }

    @Test
    void executesCommandOfPressedKey() {
        pressedKeys.add(SPACE);

        controller.handleInput();

        assertEquals(Arrays.asList("space"), executed);
    }

    @Test
    void executesAllPressedCommandsInBindingOrder() {
        pressedKeys.addAll(Arrays.asList(SPACE, UP));

        controller.handleInput();

        assertEquals(Arrays.asList("up", "space"), executed);
    }

    @Test
    void executesCommandOncePerHandleInput() {
        pressedKeys.add(W);

        controller.handleInput();
        controller.handleInput();

        assertEquals(Arrays.asList("w", "w"), executed);
    }

    @Test
    void rebindingKeyReplacesCommand() {
        controller.bind(UP, () -> executed.add("replaced"));
        pressedKeys.add(UP);

        controller.handleInput();

        assertEquals(Arrays.asList("replaced"), executed);
    }
}
