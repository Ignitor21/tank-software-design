package ru.mipt.bit.platformer.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameController {

    private final List<ControlStrategy> strategies;

    public GameController(Collection<? extends ControlStrategy> strategies) {
        this.strategies = new ArrayList<>(strategies);
    }

    public void executeCommands() {
        for (ControlStrategy strategy : strategies) {
            for (Command command : strategy.nextCommands()) {
                command.execute();
            }
        }
    }
}
