package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.List;

public interface GameWorld extends CollisionChecker {

    boolean isInside(GridPoint2 coordinates);

    List<GameObject> objectsAt(GridPoint2 coordinates);

    void addObject(GameObject object);

    boolean removeObject(GameObject object);
}
