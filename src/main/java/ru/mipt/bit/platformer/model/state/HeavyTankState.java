package ru.mipt.bit.platformer.model.state;

public final class HeavyTankState implements TankState {

    @Override
    public float movementDuration(float baseDuration) {
        return baseDuration * 3f;
    }

    @Override
    public boolean canShoot() {
        return false;
    }
}
