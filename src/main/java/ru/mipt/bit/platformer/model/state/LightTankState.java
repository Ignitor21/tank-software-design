package ru.mipt.bit.platformer.model.state;

public final class LightTankState implements TankState {

    @Override
    public float movementDuration(float baseDuration) {
        return baseDuration;
    }
}
