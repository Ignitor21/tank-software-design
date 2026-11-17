package ru.mipt.bit.platformer.control;

import java.util.List;

/**
 * Application output port. Returns the next commands from the keyboard or AI.
 */
public interface ControlStrategy {

    List<Command> nextCommands();
}
