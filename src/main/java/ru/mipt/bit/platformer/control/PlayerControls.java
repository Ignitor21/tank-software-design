package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;

/**
 * The default key layout for the player. New controls are added here as one more
 * {@code bind(...)} call, e.g. {@code .bind(SPACE, new ShootCommand(level))}.
 */
public final class PlayerControls {

    private PlayerControls() {
    }

    public static InputController create(KeyStateProvider keyStateProvider, Level level) {
        return new InputController(keyStateProvider)
                .bind(UP, new MoveCommand(level, Direction.UP))
                .bind(W, new MoveCommand(level, Direction.UP))
                .bind(LEFT, new MoveCommand(level, Direction.LEFT))
                .bind(A, new MoveCommand(level, Direction.LEFT))
                .bind(DOWN, new MoveCommand(level, Direction.DOWN))
                .bind(S, new MoveCommand(level, Direction.DOWN))
                .bind(RIGHT, new MoveCommand(level, Direction.RIGHT))
                .bind(D, new MoveCommand(level, Direction.RIGHT));
    }
}
