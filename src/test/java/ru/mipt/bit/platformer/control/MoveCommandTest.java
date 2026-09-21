package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveCommandTest {

    @Test
    void movesPlayerTankInGivenDirection() {
        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, 0.4f);
        Level level = new Level(10, 8, tank, emptyList());

        new MoveCommand(level, Direction.DOWN).execute();

        assertEquals(Direction.DOWN, tank.getDirection());
        assertEquals(new GridPoint2(1, 0), tank.getDestinationCoordinates());
    }
}
