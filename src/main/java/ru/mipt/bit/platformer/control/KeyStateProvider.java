package ru.mipt.bit.platformer.control;

/**
 * Source of keyboard state; key codes are {@link com.badlogic.gdx.Input.Keys} constants.
 */
public interface KeyStateProvider {

    boolean isKeyPressed(int keyCode);
}
