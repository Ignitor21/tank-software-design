package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class LevelGraphics implements Disposable {

    private final Batch batch;
    private final TiledMap tiledMap;
    private final MapRenderer tiledMapRenderer;
    private final TiledMapTileLayer groundLayer;
    private final List<Renderable> renderables = new ArrayList<>();

    public LevelGraphics(String levelPath) {
        batch = new SpriteBatch();
        tiledMap = new TmxMapLoader().load(levelPath);
        tiledMapRenderer = createSingleLayerMapRenderer(tiledMap, batch);
        groundLayer = getSingleLayer(tiledMap);
    }

    public LevelGraphics add(Renderable renderable) {
        renderables.add(renderable);
        return this;
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
        for (Renderable renderable : renderables) {
            renderable.render(batch);
        }
        batch.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {
        for (Renderable renderable : renderables) {
            renderable.dispose();
        }
        tiledMap.dispose();
        batch.dispose();
    }
}
