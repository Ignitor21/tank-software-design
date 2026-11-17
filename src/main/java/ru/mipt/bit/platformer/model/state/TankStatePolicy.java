package ru.mipt.bit.platformer.model.state;

/**
 * Domain rule. Chooses a state for a new tank and after it takes damage.
 */
public interface TankStatePolicy {

    TankState forHealth(int health, int maxHealth);
}
