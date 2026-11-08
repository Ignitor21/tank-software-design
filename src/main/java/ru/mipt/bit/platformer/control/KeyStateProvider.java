package ru.mipt.bit.platformer.control;

public interface KeyStateProvider {

    boolean isKeyPressed(int keyCode);

    default boolean isKeyJustPressed(int keyCode) {
        return isKeyPressed(keyCode);
    }
}
