package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Level implements GameWorld {

    private final int widthInTiles;
    private final int heightInTiles;
    private final List<GameObject> objects;
    private final List<LevelObserver> observers = new ArrayList<>();

    public Level(int widthInTiles, int heightInTiles, Collection<? extends GameObject> objects) {
        this.widthInTiles = widthInTiles;
        this.heightInTiles = heightInTiles;
        this.objects = new ArrayList<>(objects);
    }

    public void addObserver(LevelObserver observer) {
        observers.add(observer);
        for (GameObject object : objects) {
            observer.onObjectAdded(object);
        }
    }

    public void removeObserver(LevelObserver observer) {
        observers.remove(observer);
    }

    public void update(float deltaTime) {
        for (GameObject object : new ArrayList<>(objects)) {
            if (objects.contains(object)) {
                object.update(deltaTime);
            }
        }
        removeDeadObjects();
    }

    @Override
    public boolean isFree(GridPoint2 coordinates) {
        return isInside(coordinates) && objectsAt(coordinates).isEmpty();
    }

    @Override
    public boolean isInside(GridPoint2 coordinates) {
        return coordinates.x >= 0 && coordinates.x < widthInTiles
                && coordinates.y >= 0 && coordinates.y < heightInTiles;
    }

    @Override
    public List<GameObject> objectsAt(GridPoint2 coordinates) {
        List<GameObject> result = new ArrayList<>();
        for (GameObject object : objects) {
            if (object.occupies(coordinates)) {
                result.add(object);
            }
        }
        return result;
    }

    @Override
    public void addObject(GameObject object) {
        objects.add(object);
        for (LevelObserver observer : observers) {
            observer.onObjectAdded(object);
        }
    }

    @Override
    public boolean removeObject(GameObject object) {
        boolean removed = objects.remove(object);
        if (removed) {
            for (LevelObserver observer : observers) {
                observer.onObjectRemoved(object);
            }
        }
        return removed;
    }

    private void removeDeadObjects() {
        for (Iterator<GameObject> iterator = objects.iterator(); iterator.hasNext(); ) {
            GameObject object = iterator.next();
            if (!object.isAlive()) {
                iterator.remove();
                for (LevelObserver observer : observers) {
                    observer.onObjectRemoved(object);
                }
            }
        }
    }
}
