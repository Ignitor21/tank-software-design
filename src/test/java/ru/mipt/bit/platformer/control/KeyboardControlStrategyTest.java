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

class KeyboardControlStrategyTest {

    private final Set<Integer> pressedKeys = new HashSet<>();
    private final List<String> executed = new ArrayList<>();
    private final KeyboardControlStrategy strategy = new KeyboardControlStrategy(pressedKeys::contains)
            .bind(UP, () -> executed.add("up"))
            .bind(W, () -> executed.add("w"))
            .bind(SPACE, () -> executed.add("space"));

    @Test
    void givesNoCommandsWhenNoKeyIsPressed() {
        assertEquals(emptyList(), strategy.nextCommands());
    }

    @Test
    void givesCommandOfPressedKey() {
        pressedKeys.add(SPACE);

        executeNextCommands();

        assertEquals(Arrays.asList("space"), executed);
    }

    @Test
    void givesAllPressedCommandsInBindingOrder() {
        pressedKeys.addAll(Arrays.asList(SPACE, UP));

        executeNextCommands();

        assertEquals(Arrays.asList("up", "space"), executed);
    }

    @Test
    void givesCommandOncePerCall() {
        pressedKeys.add(W);

        executeNextCommands();
        executeNextCommands();

        assertEquals(Arrays.asList("w", "w"), executed);
    }

    @Test
    void doesNotExecuteCommandsItself() {
        pressedKeys.add(UP);

        strategy.nextCommands();

        assertEquals(emptyList(), executed);
    }

    @Test
    void rebindingKeyReplacesCommand() {
        strategy.bind(UP, () -> executed.add("replaced"));
        pressedKeys.add(UP);

        executeNextCommands();

        assertEquals(Arrays.asList("replaced"), executed);
    }

    @Test
    void justPressedBindingFiresOnlyWhileKeyIsJustPressed() {
        Set<Integer> pressedKeys = new HashSet<>();
        Set<Integer> justPressedKeys = new HashSet<>();
        KeyStateProvider provider = new KeyStateProvider() {
            @Override
            public boolean isKeyPressed(int keyCode) {
                return pressedKeys.contains(keyCode);
            }

            @Override
            public boolean isKeyJustPressed(int keyCode) {
                return justPressedKeys.contains(keyCode);
            }
        };
        List<String> executed = new ArrayList<>();
        KeyboardControlStrategy justPressedStrategy = new KeyboardControlStrategy(provider)
                .bindJustPressed(SPACE, () -> executed.add("toggle"));

        justPressedKeys.add(SPACE);
        executeNextCommands(justPressedStrategy);
        justPressedKeys.clear();
        executeNextCommands(justPressedStrategy);

        assertEquals(Arrays.asList("toggle"), executed);
    }

    @Test
    void pressedBindingDoesNotReuseJustPressedState() {
        Set<Integer> pressedKeys = new HashSet<>();
        KeyStateProvider provider = new KeyStateProvider() {
            @Override
            public boolean isKeyPressed(int keyCode) {
                return pressedKeys.contains(keyCode);
            }

            @Override
            public boolean isKeyJustPressed(int keyCode) {
                return true;
            }
        };
        List<String> executed = new ArrayList<>();
        KeyboardControlStrategy pressedStrategy = new KeyboardControlStrategy(provider)
                .bind(UP, () -> executed.add("up"));

        executeNextCommands(pressedStrategy);

        assertEquals(emptyList(), executed);
    }

    private void executeNextCommands() {
        executeNextCommands(strategy);
    }

    private void executeNextCommands(KeyboardControlStrategy keyboardStrategy) {
        for (Command command : keyboardStrategy.nextCommands()) {
            command.execute();
        }
    }
}
