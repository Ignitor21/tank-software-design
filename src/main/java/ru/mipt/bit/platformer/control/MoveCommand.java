package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;

public class MoveCommand implements Command {

    private final Level level;
    private final Direction direction;

    public MoveCommand(Level level, Direction direction) {
        this.level = level;
        this.direction = direction;
    }

    @Override
    public void execute() {
        level.movePlayerTank(direction);
    }
}
