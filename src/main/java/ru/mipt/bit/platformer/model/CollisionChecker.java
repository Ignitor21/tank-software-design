package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Domain output port. Checks if a tile is free.
 */
public interface CollisionChecker {

    boolean isFree(GridPoint2 coordinates);
}
