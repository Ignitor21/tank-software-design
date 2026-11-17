package ru.mipt.bit.platformer.model;

/**
 * Domain output port. Reports added and removed objects without depending on graphics.
 */
public interface LevelObserver {

    void onObjectAdded(GameObject object);

    void onObjectRemoved(GameObject object);
}
