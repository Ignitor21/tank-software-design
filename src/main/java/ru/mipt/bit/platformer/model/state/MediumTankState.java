package ru.mipt.bit.platformer.model.state;

public final class MediumTankState implements TankState {

    @Override
    public float movementDuration(float baseDuration) {
        return baseDuration * 2f;
    }
}
