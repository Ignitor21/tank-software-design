package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.CollisionChecker;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameWorld;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.Shooter;

import java.util.List;
import java.util.Random;

import static java.util.Collections.singletonList;

public class RandomControlStrategy implements ControlStrategy {

    private static final Direction[] DIRECTIONS = Direction.values();
    private static final int DEFAULT_SHOOTING_PERCENT = 5;

    private final Movable movable;
    private final CollisionChecker collisionChecker;
    private final Shooter shooter;
    private final GameWorld world;
    private final int shootingPercent;
    private final Random random;

    public RandomControlStrategy(Movable movable, CollisionChecker collisionChecker, Random random) {
        this(movable, collisionChecker, null, null, 0, random);
    }

    public RandomControlStrategy(Movable movable, Shooter shooter, GameWorld world, Random random) {
        this(movable, world, shooter, world, DEFAULT_SHOOTING_PERCENT, random);
    }

    public RandomControlStrategy(Movable movable, CollisionChecker collisionChecker, Shooter shooter,
                                 GameWorld world, int shootingPercent, Random random) {
        if (shootingPercent < 0 || shootingPercent > 100) {
            throw new IllegalArgumentException("Shooting percent must be between 0 and 100: " + shootingPercent);
        }
        this.movable = movable;
        this.collisionChecker = collisionChecker;
        this.shooter = shooter;
        this.world = world;
        this.shootingPercent = shootingPercent;
        this.random = random;
    }

    @Override
    public List<Command> nextCommands() {
        if (shooter != null && random.nextInt(100) < shootingPercent) {
            return singletonList(new ShootCommand(shooter, world));
        }
        Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
        return singletonList(new MoveCommand(movable, direction, collisionChecker));
    }
}
