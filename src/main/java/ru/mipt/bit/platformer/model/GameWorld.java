package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

/**
 * Domain output port. Finds objects on a tile and adds or removes game objects.
 */
public interface GameWorld extends CollisionChecker {

    boolean isInside(GridPoint2 coordinates);

    List<GameObject> objectsAt(GridPoint2 coordinates);

    void addObject(GameObject object);

    boolean removeObject(GameObject object);
}
