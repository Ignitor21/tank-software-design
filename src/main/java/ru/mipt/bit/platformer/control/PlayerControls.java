package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.CollisionChecker;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Movable;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;

public final class PlayerControls {

    private PlayerControls() {
    }

    public static InputController create(KeyStateProvider keyStateProvider, Movable player,
                                         CollisionChecker collisionChecker) {
        return new InputController(keyStateProvider)
                .bind(UP, new MoveCommand(player, Direction.UP, collisionChecker))
                .bind(W, new MoveCommand(player, Direction.UP, collisionChecker))
                .bind(LEFT, new MoveCommand(player, Direction.LEFT, collisionChecker))
                .bind(A, new MoveCommand(player, Direction.LEFT, collisionChecker))
                .bind(DOWN, new MoveCommand(player, Direction.DOWN, collisionChecker))
                .bind(S, new MoveCommand(player, Direction.DOWN, collisionChecker))
                .bind(RIGHT, new MoveCommand(player, Direction.RIGHT, collisionChecker))
                .bind(D, new MoveCommand(player, Direction.RIGHT, collisionChecker));
    }
}
