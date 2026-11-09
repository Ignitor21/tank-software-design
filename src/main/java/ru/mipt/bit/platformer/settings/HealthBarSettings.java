package ru.mipt.bit.platformer.settings;

public class HealthBarSettings {

    private boolean visible;

    public boolean isVisible() {
        return visible;
    }

    public void toggle() {
        visible = !visible;
    }
}
