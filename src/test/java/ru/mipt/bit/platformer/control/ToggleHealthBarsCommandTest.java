package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.settings.HealthBarSettings;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToggleHealthBarsCommandTest {

    @Test
    void togglesHealthBarVisibility() {
        HealthBarSettings settings = new HealthBarSettings();
        ToggleHealthBarsCommand command = new ToggleHealthBarsCommand(settings);

        command.execute();
        assertTrue(settings.isVisible());

        command.execute();
        assertFalse(settings.isVisible());
    }
}
