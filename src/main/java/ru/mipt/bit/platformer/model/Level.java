package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Level implements CollisionChecker {

    private final int widthInTiles;
    private final int heightInTiles;
    private final List<GameObject> objects;

    public Level(int widthInTiles, int heightInTiles, Collection<? extends GameObject> objects) {
        this.widthInTiles = widthInTiles;
        this.heightInTiles = heightInTiles;
        this.objects = new ArrayList<>(objects);
    }

    public void update(float deltaTime) {
        for (GameObject object : objects) {
            object.update(deltaTime);
        }
    }

    @Override
    public boolean isFree(GridPoint2 coordinates) {
        return isInsideLevel(coordinates) && !isOccupied(coordinates);
    }

    private boolean isInsideLevel(GridPoint2 coordinates) {
        return coordinates.x >= 0 && coordinates.x < widthInTiles
                && coordinates.y >= 0 && coordinates.y < heightInTiles;
    }

    private boolean isOccupied(GridPoint2 coordinates) {
        for (GameObject object : objects) {
            if (object.occupies(coordinates)) {
                return true;
            }
        }
        return false;
    }
}
