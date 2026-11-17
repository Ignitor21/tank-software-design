package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameWorld;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.Shooter;
import ru.mipt.bit.platformer.settings.HealthBarSettings;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.L;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;

public final class PlayerControls {

    private PlayerControls() {
    }

    public static KeyboardControlStrategy create(KeyStateProvider keyStateProvider, Movable player,
                                                   Shooter shooter, GameWorld world,
                                                   HealthBarSettings healthBarSettings) {
        return new KeyboardControlStrategy(keyStateProvider)
                .bind(UP, new MoveCommand(player, Direction.UP, world))
                .bind(W, new MoveCommand(player, Direction.UP, world))
                .bind(LEFT, new MoveCommand(player, Direction.LEFT, world))
                .bind(A, new MoveCommand(player, Direction.LEFT, world))
                .bind(DOWN, new MoveCommand(player, Direction.DOWN, world))
                .bind(S, new MoveCommand(player, Direction.DOWN, world))
                .bind(RIGHT, new MoveCommand(player, Direction.RIGHT, world))
                .bind(D, new MoveCommand(player, Direction.RIGHT, world))
                .bind(SPACE, new ShootCommand(shooter, world))
                .bindJustPressed(L, new ToggleHealthBarsCommand(healthBarSettings));
    }
}
