package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.control.ControlStrategy;
import ru.mipt.bit.platformer.control.GameController;
import ru.mipt.bit.platformer.control.GdxKeyStateProvider;
import ru.mipt.bit.platformer.control.PlayerControls;
import ru.mipt.bit.platformer.control.RandomControlStrategy;
import ru.mipt.bit.platformer.generation.FileLevelGenerator;
import ru.mipt.bit.platformer.generation.LevelGenerator;
import ru.mipt.bit.platformer.generation.LevelLayout;
import ru.mipt.bit.platformer.generation.RandomLevelGenerator;
import ru.mipt.bit.platformer.graphics.BulletGraphics;
import ru.mipt.bit.platformer.graphics.LevelGraphics;
import ru.mipt.bit.platformer.graphics.TankGraphics;
import ru.mipt.bit.platformer.graphics.TreeGraphics;
import ru.mipt.bit.platformer.model.Bullet;
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

public class GameDesktopLauncher implements ApplicationListener {

    private static final float MOVEMENT_SPEED = 0.4f;
    private static final float BULLET_SPEED = 0.1f;
    private static final int TANK_HEALTH = 100;
    private static final int PLAYER_BULLET_DAMAGE = 50;
    private static final int ENEMY_BULLET_DAMAGE = 25;
    private static final String LEVEL_PATH = "level.tmx";
    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";
    private static final String BULLET_TEXTURE = "images/bullet.png";
    private static final int RANDOM_TREE_COUNT = 10;
    private static final int RANDOM_TANK_COUNT = 4;

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

        registerGraphics(groundLayer);

        List<GameObject> objects = new ArrayList<>();
        Tank playerTank = createTank(layout.getPlayerPosition(), PLAYER_BULLET_DAMAGE);
        objects.add(playerTank);
        List<Tank> enemyTanks = new ArrayList<>();
        for (GridPoint2 tankPosition : layout.getTankPositions()) {
            Tank tank = createTank(tankPosition, ENEMY_BULLET_DAMAGE);
            objects.add(tank);
            enemyTanks.add(tank);
        }
        for (GridPoint2 treePosition : layout.getTreePositions()) {
            objects.add(new Tree(treePosition));
        }
        level = new Level(widthInTiles, heightInTiles, objects);
        level.addObserver(levelGraphics);

        List<ControlStrategy> strategies = new ArrayList<>();
        strategies.add(PlayerControls.create(new GdxKeyStateProvider(), playerTank, playerTank, level));
        Random random = new Random();
        for (Tank tank : enemyTanks) {
            strategies.add(new RandomControlStrategy(tank, tank, level, random));
        }
        gameController = new GameController(strategies);
    }

    private void registerGraphics(TiledMapTileLayer groundLayer) {
        TileMovement tankMovement = new TileMovement(groundLayer, Interpolation.smooth);
        TileMovement bulletMovement = new TileMovement(groundLayer, Interpolation.smooth);
        levelGraphics
                .register(Tank.class, tank -> new TankGraphics(TANK_TEXTURE, tank, tankMovement))
                .register(Tree.class, tree -> new TreeGraphics(TREE_TEXTURE, tree, groundLayer))
                .register(Bullet.class, bullet -> new BulletGraphics(BULLET_TEXTURE, bullet, bulletMovement));
    }

    private static Tank createTank(GridPoint2 position, int bulletDamage) {
        return new Tank(position, Direction.RIGHT, MOVEMENT_SPEED, TANK_HEALTH, bulletDamage, BULLET_SPEED);
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
        return new RandomLevelGenerator(RANDOM_TREE_COUNT, RANDOM_TANK_COUNT, new Random());
    }
}
