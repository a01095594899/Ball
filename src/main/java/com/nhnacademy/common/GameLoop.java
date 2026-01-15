package com.nhnacademy.common;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

public class GameLoop extends AnimationTimer {
    private long lastUpdate = 0;
    private final World world;
    private final GraphicsContext gc;

    public GameLoop(World world, GraphicsContext gc) {
        this.world = world;
        this.gc = gc;
    }

    @Override
    public void handle(long now) {
        if (lastUpdate == 0) {
            lastUpdate = now;
            return;
        }

        double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
        lastUpdate = now;

        update(deltaTime);
        render();
    }

    private void update(double deltaTime) {
        for (Ball ball : world.getBalls()) {
            if (ball instanceof MovableBall) {
                ((MovableBall) ball).move(deltaTime);
                // checkWallCollision
                // checkWallCollision((MovableBall) ball);
            }
        }
    }

    private void render() {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        for (Ball ball : world.getBalls()) {
            if (ball instanceof PaintableBall) {
                ((PaintableBall) ball).draw(gc);
            }
        }
    }
}