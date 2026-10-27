package ru.mipt.bit.platformer.generation;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

public class LevelLayout {

    private final GridPoint2 playerPosition;
    private final List<GridPoint2> treePositions;
    private final List<GridPoint2> tankPositions;

    public LevelLayout(GridPoint2 playerPosition, Collection<GridPoint2> treePositions) {
        this(playerPosition, treePositions, emptyList());
    }

    public LevelLayout(GridPoint2 playerPosition, Collection<GridPoint2> treePositions,
                       Collection<GridPoint2> tankPositions) {
        this.playerPosition = new GridPoint2(playerPosition);
        this.treePositions = copyOf(treePositions);
        this.tankPositions = copyOf(tankPositions);
    }

    public GridPoint2 getPlayerPosition() {
        return playerPosition;
    }

    public List<GridPoint2> getTreePositions() {
        return unmodifiableList(treePositions);
    }

    public List<GridPoint2> getTankPositions() {
        return unmodifiableList(tankPositions);
    }

    private static List<GridPoint2> copyOf(Collection<GridPoint2> positions) {
        List<GridPoint2> copy = new ArrayList<>();
        for (GridPoint2 position : positions) {
            copy.add(new GridPoint2(position));
        }
        return copy;
    }
}
