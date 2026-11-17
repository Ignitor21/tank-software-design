package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.state.HealthBasedTankStatePolicy;
import ru.mipt.bit.platformer.model.state.TankState;
import ru.mipt.bit.platformer.model.state.TankStatePolicy;

import java.util.Objects;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.isEqual;

/**
 * Domain class. Uses State and implements the move, shoot and damage ports.
 */
public class Tank implements GameObject, Movable, Shooter, Damageable {

    public static final int DEFAULT_HEALTH = 100;
    public static final int DEFAULT_BULLET_DAMAGE = 25;
    public static final float DEFAULT_SHOOT_COOLDOWN = 0.5f;

    private final float movementDuration;
    private final float bulletSpeed;
    private final float shootCooldown;
    private final int bulletDamage;
    private final TankStatePolicy statePolicy;
    private TankState state;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private float movementProgress;
    private float timeSinceLastShot;
    private Direction direction;
    private final int maxHealth;
    private int health;

    public Tank(GridPoint2 initialCoordinates, Direction initialDirection, float movementDuration) {
        this(initialCoordinates, initialDirection, movementDuration, DEFAULT_HEALTH, DEFAULT_BULLET_DAMAGE,
                movementDuration);
    }

    public Tank(GridPoint2 initialCoordinates, Direction initialDirection, float movementDuration,
                int health, int bulletDamage) {
        this(initialCoordinates, initialDirection, movementDuration, health, bulletDamage, movementDuration);
    }

    public Tank(GridPoint2 initialCoordinates, Direction initialDirection, float movementDuration,
                int health, int bulletDamage, float bulletSpeed) {
        this(initialCoordinates, initialDirection, movementDuration, health, bulletDamage, bulletSpeed,
                new HealthBasedTankStatePolicy());
    }

    public Tank(GridPoint2 initialCoordinates, Direction initialDirection, float movementDuration,
                int health, int bulletDamage, float bulletSpeed, TankStatePolicy statePolicy) {
        if (movementDuration <= 0) {
            throw new IllegalArgumentException("Movement duration must be positive: " + movementDuration);
        }
        if (health <= 0) {
            throw new IllegalArgumentException("Health must be positive: " + health);
        }
        if (bulletDamage < 0) {
            throw new IllegalArgumentException("Bullet damage must not be negative: " + bulletDamage);
        }
        if (bulletSpeed <= 0) {
            throw new IllegalArgumentException("Bullet speed must be positive: " + bulletSpeed);
        }
        this.coordinates = new GridPoint2(initialCoordinates);
        this.destinationCoordinates = new GridPoint2(initialCoordinates);
        this.direction = initialDirection;
        this.movementDuration = movementDuration;
        this.bulletSpeed = bulletSpeed;
        this.shootCooldown = DEFAULT_SHOOT_COOLDOWN;
        this.movementProgress = 1f;
        this.maxHealth = health;
        this.health = health;
        this.bulletDamage = bulletDamage;
        this.statePolicy = Objects.requireNonNull(statePolicy);
        changeState(statePolicy.forHealth(health, maxHealth));
        this.timeSinceLastShot = state.shootCooldown(shootCooldown);
    }

    @Override
    public void move(Direction direction, CollisionChecker collisionChecker) {
        if (!isAlive() || isMoving()) {
            return;
        }
        this.direction = direction;
        GridPoint2 destination = direction.apply(coordinates);
        if (collisionChecker.isFree(destination)) {
            destinationCoordinates.set(destination);
            movementProgress = 0f;
        }
    }

    @Override
    public void shoot(GameWorld world) {
        if (!isAlive() || !state.canShoot() || timeSinceLastShot < state.shootCooldown(shootCooldown)) {
            return;
        }
        timeSinceLastShot = 0f;
        GridPoint2 bulletCoordinates = direction.apply(coordinates);
        Bullet bullet = new Bullet(bulletCoordinates, direction, bulletSpeed, bulletDamage, world, this);
        world.addObject(bullet);
        bullet.resolveSpawnCollision();
    }

    @Override
    public void update(float deltaTime) {
        if (!isAlive()) {
            return;
        }
        movementProgress = clamp(movementProgress + deltaTime / state.movementDuration(movementDuration), 0f, 1f);
        timeSinceLastShot = Math.min(state.shootCooldown(shootCooldown), timeSinceLastShot + deltaTime);
        if (!isMoving()) {
            coordinates.set(destinationCoordinates);
        }
    }

    @Override
    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage must not be negative: " + damage);
        }
        if (damage > 0) {
            health -= damage;
            changeState(statePolicy.forHealth(health, maxHealth));
        }
    }

    public void changeState(TankState state) {
        this.state = Objects.requireNonNull(state);
    }

    @Override
    public boolean occupies(GridPoint2 coordinates) {
        return this.coordinates.equals(coordinates) || destinationCoordinates.equals(coordinates);
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    public boolean isMoving() {
        return !isEqual(movementProgress, 1f);
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    public GridPoint2 getDestinationCoordinates() {
        return destinationCoordinates;
    }

    public float getMovementProgress() {
        return movementProgress;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    public int getBulletDamage() {
        return bulletDamage;
    }
}
