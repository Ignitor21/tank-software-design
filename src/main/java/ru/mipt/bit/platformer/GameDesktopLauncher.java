package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.control.KeyboardInput;
import ru.mipt.bit.platformer.graphics.LevelGraphics;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import static java.util.Collections.singletonList;

public class GameDesktopLauncher implements ApplicationListener {

    private static final float MOVEMENT_SPEED = 0.4f;
    private static final String LEVEL_PATH = "level.tmx";

    private LevelGraphics levelGraphics;
    private Level level;
    private KeyboardInput keyboardInput;

    @Override
    public void create() {
        levelGraphics = new LevelGraphics(LEVEL_PATH);
        Tank playerTank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, MOVEMENT_SPEED);
        Tree treeObstacle = new Tree(new GridPoint2(1, 3));
        level = new Level(levelGraphics.getWidthInTiles(), levelGraphics.getHeightInTiles(),
                playerTank, singletonList(treeObstacle));
        keyboardInput = new KeyboardInput();
    }

    @Override
    public void render() {
        keyboardInput.pressedDirection().ifPresent(level::movePlayerTank);
        level.update(Gdx.graphics.getDeltaTime());

        levelGraphics.render(level);
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
