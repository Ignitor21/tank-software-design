package ru.mipt.bit.platformer.generation;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileLevelGenerator implements LevelGenerator {

    private final Path path;

    public FileLevelGenerator(Path path) {
        this.path = path;
    }

    @Override
    public LevelLayout generate(int widthInTiles, int heightInTiles) {
        try {
            return new TextLevelGenerator(Files.readAllLines(path, StandardCharsets.UTF_8))
                    .generate(widthInTiles, heightInTiles);
        } catch (IOException e) {
            throw new UncheckedIOException("Can't read level file " + path, e);
        }
    }
}
