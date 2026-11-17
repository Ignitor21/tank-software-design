package ru.mipt.bit.platformer.model.state;

/**
 * Tank behavior in one state. Movement and reload times are in seconds and must be positive.
 */
public interface TankState {

    float movementDuration(float baseDuration);

    default float shootCooldown(float baseCooldown) {
        return baseCooldown;
    }

    default boolean canShoot() {
        return true;
    }
}
