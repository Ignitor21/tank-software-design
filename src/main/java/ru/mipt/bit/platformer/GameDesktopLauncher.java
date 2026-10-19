package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.control.GdxKeyStateProvider;
import ru.mipt.bit.platformer.control.InputController;
import ru.mipt.bit.platformer.control.PlayerControls;
import ru.mipt.bit.platformer.graphics.LevelGraphics;
import ru.mipt.bit.platformer.graphics.TankGraphics;
import ru.mipt.bit.platformer.graphics.TreeGraphics;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import static java.util.Arrays.asList;

public class GameDesktopLauncher implements ApplicationListener {

    private static final float MOVEMENT_SPEED = 0.4f;
    private static final String LEVEL_PATH = "level.tmx";
    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";

    private LevelGraphics levelGraphics;
    private Level level;
    private InputController inputController;

    @Override
    public void create() {
        levelGraphics = new LevelGraphics(LEVEL_PATH);
        TiledMapTileLayer groundLayer = levelGraphics.getGroundLayer();

        Tank playerTank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, MOVEMENT_SPEED);
        Tree treeObstacle = new Tree(new GridPoint2(1, 3));
        level = new Level(levelGraphics.getWidthInTiles(), levelGraphics.getHeightInTiles(),
                asList(playerTank, treeObstacle));

        levelGraphics
                .add(new TankGraphics(TANK_TEXTURE, playerTank, new TileMovement(groundLayer, Interpolation.smooth)))
                .add(new TreeGraphics(TREE_TEXTURE, treeObstacle, groundLayer));

        inputController = PlayerControls.create(new GdxKeyStateProvider(), playerTank, level);
    }

    @Override
    public void render() {
        inputController.handleInput();
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
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
