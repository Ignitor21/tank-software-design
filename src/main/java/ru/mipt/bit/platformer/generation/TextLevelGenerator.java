package ru.mipt.bit.platformer.generation;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;

public class TextLevelGenerator implements LevelGenerator {

    private static final char TREE = 'T';
    private static final char PLAYER = 'X';
    private static final char EMPTY = '_';

    private final List<String> rows;

    public TextLevelGenerator(List<String> lines) {
        this.rows = withoutTrailingBlankLines(lines);
    }

    @Override
    public LevelLayout generate(int widthInTiles, int heightInTiles) {
        validateSize(widthInTiles, heightInTiles);

        GridPoint2 playerPosition = null;
        List<GridPoint2> treePositions = new ArrayList<>();
        for (int row = 0; row < rows.size(); row++) {
            String line = rows.get(row);
            int y = rows.size() - 1 - row;
            for (int x = 0; x < line.length(); x++) {
                char cell = line.charAt(x);
                GridPoint2 position = new GridPoint2(x, y);
                if (cell == TREE) {
                    treePositions.add(position);
                } else if (cell == PLAYER) {
                    if (playerPosition != null) {
                        throw new IllegalArgumentException("Level has more than one player position");
                    }
                    playerPosition = position;
                } else if (cell != EMPTY) {
                    throw new IllegalArgumentException("Unknown cell '" + cell + "' at row " + (row + 1)
                            + ", column " + (x + 1));
                }
            }
        }
        if (playerPosition == null) {
            throw new IllegalArgumentException("Level has no player position");
        }
        return new LevelLayout(playerPosition, treePositions);
    }

    private void validateSize(int widthInTiles, int heightInTiles) {
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("Level is empty");
        }
        int width = rows.get(0).length();
        for (int row = 0; row < rows.size(); row++) {
            if (rows.get(row).length() != width) {
                throw new IllegalArgumentException("Row " + (row + 1) + " has length " + rows.get(row).length()
                        + ", expected " + width);
            }
        }
        if (width > widthInTiles || rows.size() > heightInTiles) {
            throw new IllegalArgumentException("Level " + width + "x" + rows.size()
                    + " doesn't fit into " + widthInTiles + "x" + heightInTiles);
        }
    }

    private static List<String> withoutTrailingBlankLines(List<String> lines) {
        List<String> result = new ArrayList<>(lines);
        while (!result.isEmpty() && result.get(result.size() - 1).trim().isEmpty()) {
            result.remove(result.size() - 1);
        }
        return result;
    }
}
