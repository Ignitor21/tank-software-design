package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;

/**
 * Graphics interface. Not a domain port: it uses Batch from libGDX.
 */
public interface Renderable extends Disposable {

    void render(Batch batch);
}
