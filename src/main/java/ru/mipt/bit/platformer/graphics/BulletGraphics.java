package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.util.TileMovement;

public class BulletGraphics extends GameObjectGraphics<Bullet> {

    private final TileMovement tileMovement;

    public BulletGraphics(String texturePath, Bullet bullet, TileMovement tileMovement) {
        super(texturePath, bullet);
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
