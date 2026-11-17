package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.GameWorld;
import ru.mipt.bit.platformer.model.Level;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertSame;

class ShootCommandTest {

    @Test
    void asksShooterToShootInGivenWorld() {
        Level level = new Level(1, 1, emptyList());
        GameWorld[] usedWorld = new GameWorld[1];

        new ShootCommand(world -> usedWorld[0] = world, level).execute();

        assertSame(level, usedWorld[0]);
    }
}
