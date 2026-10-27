package ru.mipt.bit.platformer.control;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class KeyboardControlStrategy implements ControlStrategy {

    private final KeyStateProvider keyStateProvider;
    private final Map<Integer, Command> bindings = new LinkedHashMap<>();

    public KeyboardControlStrategy(KeyStateProvider keyStateProvider) {
        this.keyStateProvider = keyStateProvider;
    }

    public KeyboardControlStrategy bind(int keyCode, Command command) {
        bindings.put(keyCode, command);
        return this;
    }

    @Override
    public List<Command> nextCommands() {
        List<Command> commands = new ArrayList<>();
        for (Map.Entry<Integer, Command> binding : bindings.entrySet()) {
            if (keyStateProvider.isKeyPressed(binding.getKey())) {
                commands.add(binding.getValue());
            }
        }
        return commands;
    }
}
