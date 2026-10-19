package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.isEqual;

public class Tank implements GameObject, Movable {

    private final float movementSpeed;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private float movementProgress;
    private Direction direction;

    public Tank(GridPoint2 initialCoordinates, Direction initialDirection, float movementSpeed) {
        this.coordinates = new GridPoint2(initialCoordinates);
        this.destinationCoordinates = new GridPoint2(initialCoordinates);
        this.direction = initialDirection;
        this.movementSpeed = movementSpeed;
        this.movementProgress = 1f;
    }

    @Override
    public void move(Direction direction, CollisionChecker collisionChecker) {
        if (isMoving()) {
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
    public void update(float deltaTime) {
        movementProgress = clamp(movementProgress + deltaTime / movementSpeed, 0f, 1f);
        if (!isMoving()) {
            coordinates.set(destinationCoordinates);
        }
    }

    @Override
    public boolean occupies(GridPoint2 coordinates) {
        return this.coordinates.equals(coordinates) || destinationCoordinates.equals(coordinates);
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
}
