package ru.mipt.bit.platformer.model;

/**
 * Domain input port. Allows taking damage and reading health.
 */
public interface Damageable {

    void takeDamage(int damage);

    int getHealth();

    int getMaxHealth();
}
