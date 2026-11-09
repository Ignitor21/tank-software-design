package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.isEqual;

public class Bullet implements GameObject {

    private final GameWorld world;
    private final GameObject owner;
    private final Direction direction;
    private final float movementSpeed;
    private final int damage;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private float movementProgress;
    private boolean alive = true;

    public Bullet(GridPoint2 initialCoordinates, Direction direction, float movementSpeed, int damage,
                  GameWorld world, GameObject owner) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage must not be negative: " + damage);
        }
        this.coordinates = new GridPoint2(initialCoordinates);
        this.destinationCoordinates = new GridPoint2(initialCoordinates);
        this.direction = direction;
        this.movementSpeed = movementSpeed;
        this.damage = damage;
        this.world = world;
        this.owner = owner;
        this.movementProgress = 1f;
    }

    public void resolveSpawnCollision() {
        if (!world.isInside(coordinates)) {
            destroy();
            return;
        }
        if (hasBlockerAt(coordinates)) {
            collideAt(coordinates);
        }
    }

    @Override
    public void update(float deltaTime) {
        if (!alive) {
            return;
        }
        if (isMoving()) {
            movementProgress = clamp(movementProgress + deltaTime / movementSpeed, 0f, 1f);
            if (!isMoving()) {
                coordinates.set(destinationCoordinates);
                if (hasBlockerAt(coordinates)) {
                    collideAt(coordinates);
                }
            }
            return;
        }
        startNextStep();
    }

    @Override
    public boolean occupies(GridPoint2 coordinates) {
        return alive && (this.coordinates.equals(coordinates) || destinationCoordinates.equals(coordinates));
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    private void startNextStep() {
        GridPoint2 next = direction.apply(coordinates);
        if (!world.isInside(next)) {
            destroy();
            return;
        }
        if (hasBlockerAt(next)) {
            collideAt(next);
            return;
        }
        destinationCoordinates.set(next);
        movementProgress = 0f;
    }

    private boolean hasBlockerAt(GridPoint2 coordinates) {
        for (GameObject object : world.objectsAt(coordinates)) {
            if (object != this && object != owner) {
                return true;
            }
        }
        return false;
    }

    private void collideAt(GridPoint2 coordinates) {
        if (!alive) {
            return;
        }
        for (GameObject object : world.objectsAt(coordinates)) {
            if (object == this || object == owner) {
                continue;
            }
            if (object instanceof Bullet) {
                ((Bullet) object).destroy();
            } else if (object instanceof Damageable) {
                ((Damageable) object).takeDamage(damage);
            }
        }
        destroy();
    }

    private void destroy() {
        if (!alive) {
            return;
        }
        alive = false;
        world.removeObject(this);
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

    public int getDamage() {
        return damage;
    }
}
