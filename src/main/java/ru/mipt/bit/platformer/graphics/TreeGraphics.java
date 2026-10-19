package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tree;

import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class TreeGraphics extends GameObjectGraphics<Tree> {

    private static final float NO_ROTATION = 0f;

    private final TiledMapTileLayer groundLayer;

    public TreeGraphics(String texturePath, Tree tree, TiledMapTileLayer groundLayer) {
        super(texturePath, tree);
        this.groundLayer = groundLayer;
    }

    @Override
    protected void moveRectangle(Rectangle rectangle) {
        moveRectangleAtTileCenter(groundLayer, rectangle, object.getCoordinates());
    }

    @Override
    protected float getRotation() {
        return NO_ROTATION;
    }
}
