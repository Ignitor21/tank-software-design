package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public abstract class GameObjectGraphics<T> implements Renderable {

    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;
    protected final T object;

    protected GameObjectGraphics(String texturePath, T object) {
        this.texture = new Texture(texturePath);
        this.graphics = new TextureRegion(texture);
        this.rectangle = createBoundingRectangle(graphics);
        this.object = object;
    }

    @Override
    public void render(Batch batch) {
        moveRectangle(rectangle);
        drawTextureRegionUnscaled(batch, graphics, rectangle, getRotation());
    }

    protected abstract void moveRectangle(Rectangle rectangle);

    protected abstract float getRotation();

    @Override
    public void dispose() {
        texture.dispose();
    }
}
