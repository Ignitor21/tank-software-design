package ru.mipt.bit.platformer.model.state;

public final class HealthBasedTankStatePolicy implements TankStatePolicy {

    private static final TankState LIGHT = new LightTankState();
    private static final TankState MEDIUM = new MediumTankState();
    private static final TankState HEAVY = new HeavyTankState();

    @Override
    public TankState forHealth(int health, int maxHealth) {
        if ((long) health * 100 > (long) maxHealth * 70) {
            return LIGHT;
        }
        if ((long) health * 100 >= (long) maxHealth * 15) {
            return MEDIUM;
        }
        return HEAVY;
    }
}
