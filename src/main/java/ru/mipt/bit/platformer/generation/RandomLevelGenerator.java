package ru.mipt.bit.platformer.generation;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomLevelGenerator implements LevelGenerator {

    private final int treeCount;
    private final Random random;

    public RandomLevelGenerator(int treeCount, Random random) {
        if (treeCount < 0) {
            throw new IllegalArgumentException("Tree count must not be negative: " + treeCount);
        }
        this.treeCount = treeCount;
        this.random = random;
    }

    @Override
    public LevelLayout generate(int widthInTiles, int heightInTiles) {
        List<GridPoint2> tiles = allTiles(widthInTiles, heightInTiles);
        if (treeCount + 1 > tiles.size()) {
            throw new IllegalArgumentException("Level " + widthInTiles + "x" + heightInTiles
                    + " can't fit a player and " + treeCount + " trees");
        }
        Collections.shuffle(tiles, random);
        return new LevelLayout(tiles.get(0), tiles.subList(1, treeCount + 1));
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
