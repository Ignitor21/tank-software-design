package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class Level implements CollisionChecker {

    private final int widthInTiles;
    private final int heightInTiles;
    private final Tank playerTank;
    private final List<Tree> trees;

    public Level(int widthInTiles, int heightInTiles, Tank playerTank, Collection<Tree> trees) {
        this.widthInTiles = widthInTiles;
        this.heightInTiles = heightInTiles;
        this.playerTank = playerTank;
        this.trees = new ArrayList<>(trees);
    }

    public void movePlayerTank(Direction direction) {
        playerTank.move(direction, this);
    }

    public void update(float deltaTime) {
        playerTank.update(deltaTime);
    }

    @Override
    public boolean isFree(GridPoint2 coordinates) {
        return isInsideLevel(coordinates) && !isOccupiedByTree(coordinates);
    }

    private boolean isInsideLevel(GridPoint2 coordinates) {
        return coordinates.x >= 0 && coordinates.x < widthInTiles
                && coordinates.y >= 0 && coordinates.y < heightInTiles;
    }

    private boolean isOccupiedByTree(GridPoint2 coordinates) {
        for (Tree tree : trees) {
            if (tree.getCoordinates().equals(coordinates)) {
                return true;
            }
        }
        return false;
    }

    public Tank getPlayerTank() {
        return playerTank;
    }

    public List<Tree> getTrees() {
        return unmodifiableList(trees);
    }
}
