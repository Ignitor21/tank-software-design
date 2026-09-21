package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.Gdx;

public class GdxKeyStateProvider implements KeyStateProvider {

    @Override
    public boolean isKeyPressed(int keyCode) {
        return Gdx.input.isKeyPressed(keyCode);
    }
}
