package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.GameWorld;
import ru.mipt.bit.platformer.model.Shooter;

public class ShootCommand implements Command {

    private final Shooter shooter;
    private final GameWorld world;

    public ShootCommand(Shooter shooter, GameWorld world) {
        this.shooter = shooter;
        this.world = world;
    }

    @Override
    public void execute() {
        shooter.shoot(world);
    }
}
