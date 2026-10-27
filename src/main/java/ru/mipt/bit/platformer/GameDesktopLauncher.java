package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.control.GameController;
import ru.mipt.bit.platformer.control.GdxKeyStateProvider;
import ru.mipt.bit.platformer.control.PlayerControls;
import ru.mipt.bit.platformer.generation.FileLevelGenerator;
import ru.mipt.bit.platformer.generation.LevelGenerator;
import ru.mipt.bit.platformer.generation.LevelLayout;
import ru.mipt.bit.platformer.generation.RandomLevelGenerator;
import ru.mipt.bit.platformer.graphics.LevelGraphics;
import ru.mipt.bit.platformer.graphics.TankGraphics;
import ru.mipt.bit.platformer.graphics.TreeGraphics;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Collections.singletonList;

public class GameDesktopLauncher implements ApplicationListener {

    private static final float MOVEMENT_SPEED = 0.4f;
    private static final String LEVEL_PATH = "level.tmx";
    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";
    private static final int RANDOM_TREE_COUNT = 10;

    private final LevelGenerator levelGenerator;
    private LevelGraphics levelGraphics;
    private Level level;
    private GameController gameController;

    public GameDesktopLauncher(LevelGenerator levelGenerator) {
        this.levelGenerator = levelGenerator;
    }

    @Override
    public void create() {
        levelGraphics = new LevelGraphics(LEVEL_PATH);
        TiledMapTileLayer groundLayer = levelGraphics.getGroundLayer();
        int widthInTiles = levelGraphics.getWidthInTiles();
        int heightInTiles = levelGraphics.getHeightInTiles();
        LevelLayout layout = levelGenerator.generate(widthInTiles, heightInTiles);

        List<GameObject> objects = new ArrayList<>();
        Tank playerTank = new Tank(layout.getPlayerPosition(), Direction.RIGHT, MOVEMENT_SPEED);
        objects.add(playerTank);
        levelGraphics.add(new TankGraphics(TANK_TEXTURE, playerTank, new TileMovement(groundLayer, Interpolation.smooth)));
        for (GridPoint2 treePosition : layout.getTreePositions()) {
            Tree tree = new Tree(treePosition);
            objects.add(tree);
            levelGraphics.add(new TreeGraphics(TREE_TEXTURE, tree, groundLayer));
        }
        level = new Level(widthInTiles, heightInTiles, objects);

        gameController = new GameController(singletonList(
                PlayerControls.create(new GdxKeyStateProvider(), playerTank, level)));
    }

    @Override
    public void render() {
        gameController.executeCommands();
        level.update(Gdx.graphics.getDeltaTime());

        levelGraphics.render();
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        levelGraphics.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(createLevelGenerator(args)), config);
    }

    private static LevelGenerator createLevelGenerator(String[] args) {
        if (args.length > 0) {
            return new FileLevelGenerator(Paths.get(args[0]));
        }
        return new RandomLevelGenerator(RANDOM_TREE_COUNT, new Random());
    }
}
