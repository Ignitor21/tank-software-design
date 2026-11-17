package ru.mipt.bit.platformer.model;

/**
 * Domain input port. Allows game objects to move.
 */
public interface Movable {

    void move(Direction direction, CollisionChecker collisionChecker);
}
