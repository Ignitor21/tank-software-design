package ru.mipt.bit.platformer.control;

/**
 * Application output port. Reads key states without depending on libGDX.
 */
public interface KeyStateProvider {

    boolean isKeyPressed(int keyCode);

    default boolean isKeyJustPressed(int keyCode) {
        return isKeyPressed(keyCode);
    }
}
