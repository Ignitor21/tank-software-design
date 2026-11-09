package ru.mipt.bit.platformer.model;

public interface Damageable {

    void takeDamage(int damage);

    int getHealth();

    int getMaxHealth();
}
