package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tree;

import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class TreeGraphics extends GameObjectGraphics<Tree> {

    private static final float NO_ROTATION = 0f;

    private final TiledMapTileLayer groundLayer;

    public TreeGraphics(String texturePath, TiledMapTileLayer groundLayer) {
        super(texturePath);
        this.groundLayer = groundLayer;
    }

    @Override
    protected void moveRectangle(Rectangle rectangle, Tree tree) {
        moveRectangleAtTileCenter(groundLayer, rectangle, tree.getCoordinates());
    }

    @Override
    protected float getRotation(Tree tree) {
        return NO_ROTATION;
    }
}
