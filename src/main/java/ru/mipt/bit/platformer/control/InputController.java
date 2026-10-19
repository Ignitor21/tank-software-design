package ru.mipt.bit.platformer.control;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InputController {

    private final KeyStateProvider keyStateProvider;
    private final Map<Integer, Command> bindings = new LinkedHashMap<>();

    public InputController(KeyStateProvider keyStateProvider) {
        this.keyStateProvider = keyStateProvider;
    }

    public InputController bind(int keyCode, Command command) {
        bindings.put(keyCode, command);
        return this;
    }

    public void handleInput() {
        for (Command command : pressedCommands()) {
            command.execute();
        }
    }

    private List<Command> pressedCommands() {
        List<Command> commands = new ArrayList<>();
        for (Map.Entry<Integer, Command> binding : bindings.entrySet()) {
            if (keyStateProvider.isKeyPressed(binding.getKey())) {
                commands.add(binding.getValue());
            }
        }
        return commands;
    }
}
