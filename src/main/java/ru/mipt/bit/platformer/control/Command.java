package ru.mipt.bit.platformer.control;

/**
 * Application input port. Runs one player or AI action.
 */
public interface Command {

    void execute();
}
