package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Damageable;
import ru.mipt.bit.platformer.settings.HealthBarSettings;

import static com.badlogic.gdx.math.MathUtils.clamp;

public class HealthBarGraphics implements Renderable {

    private static final float BAR_HEIGHT = 10f;
    private static final float BAR_WIDTH_FACTOR = 0.9f;
    private static final float BAR_GAP = 4f;

    private final GameObjectGraphics<?> delegate;
    private final Damageable damageable;
    private final HealthBarSettings settings;
    private final Texture pixelTexture;
    private final TextureRegion pixel;

    public HealthBarGraphics(GameObjectGraphics<?> delegate, Damageable damageable, HealthBarSettings settings) {
        this.delegate = delegate;
        this.damageable = damageable;
        this.settings = settings;
        this.pixelTexture = createPixelTexture();
        this.pixel = new TextureRegion(pixelTexture);
    }

    @Override
    public void render(Batch batch) {
        delegate.render(batch);
        if (!settings.isVisible()) {
            return;
        }
        Rectangle rectangle = delegate.getRectangle();
        float width = rectangle.width * BAR_WIDTH_FACTOR;
        float x = rectangle.x + (rectangle.width - width) / 2f;
        float y = rectangle.y + rectangle.height + BAR_GAP;
        float healthFraction = clamp((float) damageable.getHealth() / damageable.getMaxHealth(), 0f, 1f);

        batch.setColor(0.15f, 0.15f, 0.15f, 0.9f);
        batch.draw(pixel, x, y, width, BAR_HEIGHT);
        batch.setColor(0.2f, 0.85f, 0.2f, 1f);
        batch.draw(pixel, x, y, width * healthFraction, BAR_HEIGHT);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    @Override
    public void dispose() {
        delegate.dispose();
        pixelTexture.dispose();
    }

    private static Texture createPixelTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
}
