package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;

import static com.badlogic.gdx.Input.Keys.valueOf;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerControlsTest {

    @ParameterizedTest
    @CsvSource({
            "Up, UP", "W, UP",
            "Left, LEFT", "A, LEFT",
            "Down, DOWN", "S, DOWN",
            "Right, RIGHT", "D, RIGHT"
    })
    void keyMovesTankInDirection(String keyName, Direction expected) {
        int keyCode = valueOf(keyName);
        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, 0.4f);
        Level level = new Level(10, 8, singletonList(tank));
        KeyboardControlStrategy strategy = PlayerControls.create(key -> key == keyCode, tank, level);

        new GameController(singletonList(strategy)).executeCommands();

        assertEquals(expected, tank.getDirection());
        assertEquals(expected.apply(new GridPoint2(1, 1)), tank.getDestinationCoordinates());
    }
}
