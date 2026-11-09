package ru.mipt.bit.platformer.control;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class KeyboardControlStrategy implements ControlStrategy {

    private final KeyStateProvider keyStateProvider;
    private final Map<Integer, Command> pressedBindings = new LinkedHashMap<>();
    private final Map<Integer, Command> justPressedBindings = new LinkedHashMap<>();

    public KeyboardControlStrategy(KeyStateProvider keyStateProvider) {
        this.keyStateProvider = keyStateProvider;
    }

    public KeyboardControlStrategy bind(int keyCode, Command command) {
        pressedBindings.put(keyCode, command);
        return this;
    }

    public KeyboardControlStrategy bindJustPressed(int keyCode, Command command) {
        justPressedBindings.put(keyCode, command);
        return this;
    }

    @Override
    public List<Command> nextCommands() {
        List<Command> commands = new ArrayList<>();
        for (Map.Entry<Integer, Command> binding : pressedBindings.entrySet()) {
            if (keyStateProvider.isKeyPressed(binding.getKey())) {
                commands.add(binding.getValue());
            }
        }
        for (Map.Entry<Integer, Command> binding : justPressedBindings.entrySet()) {
            if (keyStateProvider.isKeyJustPressed(binding.getKey())) {
                commands.add(binding.getValue());
            }
        }
        return commands;
    }
}
