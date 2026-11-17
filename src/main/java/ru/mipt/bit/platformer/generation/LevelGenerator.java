package ru.mipt.bit.platformer.generation;

/**
 * Output port used at game start. Returns a level layout without file format details.
 */
public interface LevelGenerator {

    LevelLayout generate(int widthInTiles, int heightInTiles);
}
