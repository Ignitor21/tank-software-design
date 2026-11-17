package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.util.TileMovement;

public class TankGraphics extends GameObjectGraphics<Tank> {

    private final TileMovement tileMovement;

    public TankGraphics(String texturePath, Tank tank, TileMovement tileMovement) {
        super(texturePath, tank);
        this.tileMovement = tileMovement;
    }

    public TankGraphics(String texturePath, Tank tank, TileMovement tileMovement, Color bodyColor) {
        super(createTexture(texturePath, bodyColor), tank);
        this.tileMovement = tileMovement;
    }

    private static Texture createTexture(String texturePath, Color bodyColor) {
        Pixmap pixmap = new Pixmap(Gdx.files.internal(texturePath));
        try {
            pixmap.setBlending(Pixmap.Blending.None);
            Color pixel = new Color();
            float[] hsv = new float[3];
            float[] target = bodyColor.toHsv(new float[3]);
            for (int y = 0; y < pixmap.getHeight(); y++) {
                for (int x = 0; x < pixmap.getWidth(); x++) {
                    Color.rgba8888ToColor(pixel, pixmap.getPixel(x, y));
                    if (pixel.a > 0f && pixel.b > pixel.r && pixel.b > pixel.g) {
                        pixel.toHsv(hsv);
                        pixel.fromHsv(target[0], target[1], Math.min(1f, hsv[2] * 1.3f));
                        pixmap.drawPixel(x, y, Color.rgba8888(pixel));
                    }
                }
            }
            return new Texture(pixmap);
        } finally {
            pixmap.dispose();
        }
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
