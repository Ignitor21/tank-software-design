package ru.mipt.bit.platformer.generation;

public interface LevelGenerator {

    LevelLayout generate(int widthInTiles, int heightInTiles);
}
