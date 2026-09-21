package ru.mipt.bit.platformer.control;

/**
 * An action triggered by user input. New actions (e.g. shooting) are added
 * as new implementations and bound to keys in {@link InputController}.
 */
public interface Command {

    void execute();
}
