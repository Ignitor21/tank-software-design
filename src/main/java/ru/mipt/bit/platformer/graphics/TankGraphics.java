package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.util.TileMovement;

public class TankGraphics extends GameObjectGraphics<Tank> {

    private final TileMovement tileMovement;

    public TankGraphics(String texturePath, TileMovement tileMovement) {
        super(texturePath);
        this.tileMovement = tileMovement;
    }

    @Override
    protected void moveRectangle(Rectangle rectangle, Tank tank) {
        tileMovement.moveRectangleBetweenTileCenters(rectangle, tank.getCoordinates(),
                tank.getDestinationCoordinates(), tank.getMovementProgress());
    }

    @Override
    protected float getRotation(Tank tank) {
        return tank.getDirection().getRotation();
    }
}
