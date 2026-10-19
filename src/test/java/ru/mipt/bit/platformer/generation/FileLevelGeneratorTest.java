package ru.mipt.bit.platformer.generation;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileLevelGeneratorTest {

    @TempDir
    Path tempDir;

    @Test
    void readsLevelFromFile() throws IOException {
        Path file = tempDir.resolve("level.txt");
        Files.write(file, asList("T__", "_X_"), StandardCharsets.UTF_8);

        LevelLayout layout = new FileLevelGenerator(file).generate(10, 8);

        assertEquals(new GridPoint2(1, 0), layout.getPlayerPosition());
        assertEquals(asList(new GridPoint2(0, 1)), layout.getTreePositions());
    }

    @Test
    void readsBundledExampleLevel() {
        LevelLayout layout = new FileLevelGenerator(Paths.get("levels/example.txt")).generate(10, 8);

        assertEquals(new GridPoint2(5, 1), layout.getPlayerPosition());
        assertEquals(15, layout.getTreePositions().size());
    }

    @Test
    void failsOnMissingFile() {
        assertThrows(UncheckedIOException.class,
                () -> new FileLevelGenerator(tempDir.resolve("missing.txt")).generate(10, 8));
    }
}
