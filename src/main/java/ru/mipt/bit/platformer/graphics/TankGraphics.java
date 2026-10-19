package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.util.TileMovement;

public class TankGraphics extends GameObjectGraphics<Tank> {

    private final TileMovement tileMovement;

    public TankGraphics(String texturePath, Tank tank, TileMovement tileMovement) {
        super(texturePath, tank);
        this.tileMovement = tileMovement;
    }

    @Override
    protected void moveRectangle(Rectangle rectangle) {
        tileMovement.moveRectangleBetweenTileCenters(rectangle, object.getCoordinates(),
                object.getDestinationCoordinates(), object.getMovementProgress());
    }

    @Override
    protected float getRotation() {
        return object.getDirection().getRotation();
    }
}
