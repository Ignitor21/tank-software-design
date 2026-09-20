package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.Gdx;
import ru.mipt.bit.platformer.model.Direction;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;

public class KeyboardInput {

    private final Map<Integer, Direction> directionKeys = new LinkedHashMap<>();

    public KeyboardInput() {
        directionKeys.put(UP, Direction.UP);
        directionKeys.put(W, Direction.UP);
        directionKeys.put(LEFT, Direction.LEFT);
        directionKeys.put(A, Direction.LEFT);
        directionKeys.put(DOWN, Direction.DOWN);
        directionKeys.put(S, Direction.DOWN);
        directionKeys.put(RIGHT, Direction.RIGHT);
        directionKeys.put(D, Direction.RIGHT);
    }

    public Optional<Direction> pressedDirection() {
        for (Map.Entry<Integer, Direction> directionKey : directionKeys.entrySet()) {
            if (Gdx.input.isKeyPressed(directionKey.getKey())) {
                return Optional.of(directionKey.getValue());
            }
        }
        return Optional.empty();
    }
}
