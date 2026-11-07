package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.LevelObserver;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class LevelGraphics implements Disposable, LevelObserver {

    private final Batch batch;
    private final TiledMap tiledMap;
    private final MapRenderer tiledMapRenderer;
    private final TiledMapTileLayer groundLayer;
    private final Map<GameObject, Renderable> renderables = new LinkedHashMap<>();
    private final Map<Class<?>, Function<GameObject, Renderable>> graphicsFactories = new LinkedHashMap<>();

    public LevelGraphics(String levelPath) {
        batch = new SpriteBatch();
        tiledMap = new TmxMapLoader().load(levelPath);
        tiledMapRenderer = createSingleLayerMapRenderer(tiledMap, batch);
        groundLayer = getSingleLayer(tiledMap);
    }

    public <T extends GameObject> LevelGraphics register(Class<T> type, Function<T, Renderable> factory) {
        graphicsFactories.put(type, object -> factory.apply(type.cast(object)));
        return this;
    }

    @Override
    public void onObjectAdded(GameObject object) {
        renderables.put(object, createRenderable(object));
    }

    @Override
    public void onObjectRemoved(GameObject object) {
        Renderable renderable = renderables.remove(object);
        if (renderable != null) {
            renderable.dispose();
        }
    }

    public TiledMapTileLayer getGroundLayer() {
        return groundLayer;
    }

    public int getWidthInTiles() {
        return groundLayer.getWidth();
    }

    public int getHeightInTiles() {
        return groundLayer.getHeight();
    }

    public void render() {
        clearScreen();

        tiledMapRenderer.render();

        batch.begin();
        for (Renderable renderable : renderables.values()) {
            renderable.render(batch);
        }
        batch.end();
    }

    private Renderable createRenderable(GameObject object) {
        Function<GameObject, Renderable> factory = graphicsFactories.get(object.getClass());
        if (factory == null) {
            throw new IllegalStateException("No graphics registered for " + object.getClass().getName());
        }
        return factory.apply(object);
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {
        for (Renderable renderable : renderables.values()) {
            renderable.dispose();
        }
        renderables.clear();
        tiledMap.dispose();
        batch.dispose();
    }
}
