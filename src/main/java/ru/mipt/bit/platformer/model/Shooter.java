package ru.mipt.bit.platformer.model;

/**
 * Domain input port. Allows game objects to shoot.
 */
public interface Shooter {

    void shoot(GameWorld world);
}
