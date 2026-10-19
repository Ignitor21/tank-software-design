package ru.mipt.bit.platformer.generation;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class LevelLayout {

    private final GridPoint2 playerPosition;
    private final List<GridPoint2> treePositions;

    public LevelLayout(GridPoint2 playerPosition, Collection<GridPoint2> treePositions) {
        this.playerPosition = new GridPoint2(playerPosition);
        this.treePositions = new ArrayList<>();
        for (GridPoint2 treePosition : treePositions) {
            this.treePositions.add(new GridPoint2(treePosition));
        }
    }

    public GridPoint2 getPlayerPosition() {
        return playerPosition;
    }

    public List<GridPoint2> getTreePositions() {
        return unmodifiableList(treePositions);
    }
}
