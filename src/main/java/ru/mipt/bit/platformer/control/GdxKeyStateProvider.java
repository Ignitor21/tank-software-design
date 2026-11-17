package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.Gdx;

/**
 * Keyboard adapter. Reads keys from libGDX for the KeyStateProvider port.
 */
public class GdxKeyStateProvider implements KeyStateProvider {

    @Override
    public boolean isKeyPressed(int keyCode) {
        return Gdx.input.isKeyPressed(keyCode);
    }

    @Override
    public boolean isKeyJustPressed(int keyCode) {
        return Gdx.input.isKeyJustPressed(keyCode);
    }
}
