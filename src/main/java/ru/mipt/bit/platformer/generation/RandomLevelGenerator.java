package ru.mipt.bit.platformer.generation;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomLevelGenerator implements LevelGenerator {

    private final int treeCount;
    private final int tankCount;
    private final Random random;

    public RandomLevelGenerator(int treeCount, Random random) {
        this(treeCount, 0, random);
    }

    public RandomLevelGenerator(int treeCount, int tankCount, Random random) {
        if (treeCount < 0) {
            throw new IllegalArgumentException("Tree count must not be negative: " + treeCount);
        }
        if (tankCount < 0) {
            throw new IllegalArgumentException("Tank count must not be negative: " + tankCount);
        }
        this.treeCount = treeCount;
        this.tankCount = tankCount;
        this.random = random;
    }

    @Override
    public LevelLayout generate(int widthInTiles, int heightInTiles) {
        List<GridPoint2> tiles = allTiles(widthInTiles, heightInTiles);
        if (1 + treeCount + tankCount > tiles.size()) {
            throw new IllegalArgumentException("Level " + widthInTiles + "x" + heightInTiles
                    + " can't fit a player, " + treeCount + " trees and " + tankCount + " tanks");
        }
        Collections.shuffle(tiles, random);
        int treesEnd = 1 + treeCount;
        return new LevelLayout(tiles.get(0), tiles.subList(1, treesEnd),
                tiles.subList(treesEnd, treesEnd + tankCount));
    }

    private static List<GridPoint2> allTiles(int widthInTiles, int heightInTiles) {
        List<GridPoint2> tiles = new ArrayList<>();
        for (int x = 0; x < widthInTiles; x++) {
            for (int y = 0; y < heightInTiles; y++) {
                tiles.add(new GridPoint2(x, y));
            }
        }
        return tiles;
    }
}
