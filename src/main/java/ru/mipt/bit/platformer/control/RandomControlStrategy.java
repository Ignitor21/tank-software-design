package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.CollisionChecker;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;

import java.util.List;
import java.util.Random;

import static java.util.Collections.singletonList;

public class RandomControlStrategy implements ControlStrategy {

    private static final Direction[] DIRECTIONS = Direction.values();

    private final Movable movable;
    private final CollisionChecker collisionChecker;
    private final Random random;

    public RandomControlStrategy(Movable movable, CollisionChecker collisionChecker, Random random) {
        this.movable = movable;
        this.collisionChecker = collisionChecker;
        this.random = random;
    }

    @Override
    public List<Command> nextCommands() {
        Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
        return singletonList(new MoveCommand(movable, direction, collisionChecker));
    }
}
