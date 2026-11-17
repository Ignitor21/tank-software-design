package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.settings.HealthBarSettings;

public class ToggleHealthBarsCommand implements Command {

    private final HealthBarSettings settings;

    public ToggleHealthBarsCommand(HealthBarSettings settings) {
        this.settings = settings;
    }

    @Override
    public void execute() {
        settings.toggle();
    }
}
