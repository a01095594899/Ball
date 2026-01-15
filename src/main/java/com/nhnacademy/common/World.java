package com.nhnacademy.common;

import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import javafx.scene.canvas.GraphicsContext;

public class World {
    private final double width;
    private final double height;
    private final List<Ball> balls;

    public World(double width, double height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException();
        }
        this.width = width;
        this.height = height;
        this.balls = new ArrayList<>();
    }

    public void add(Ball ball) {
        if (ball == null) {
            throw new IllegalArgumentException();
        }
        // 공이 월드 경계 안에 있는지 체크
        if (!isInBounds(ball)) {
            throw new IllegalArgumentException("공이 월드 경계를 벗어났습니다.");
        }
        balls.add(ball);
    }

    public void remove(Ball ball) {
        if (!balls.contains(ball)) {
            throw new NoSuchElementException("제거하려는 공이 월드 내에 존재하지 않습니다.");
        }
        balls.remove(ball);
    }

    public void clear() {
        balls.clear();
    }

    public int getBallCount() {
        return balls.size();
    }

    // 방어적 복사
    public List<Ball> getBalls() {
        return new ArrayList<>(balls);
    }

    public void update(double deltaTime) {
        for (Ball ball : getBalls()) {
            if (ball instanceof MovableBall movableBall) {
                movableBall.move(deltaTime);
            }
        }
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    // 모든 공 그리기
    public void draw(GraphicsContext gc) {
        if (gc == null)
            return;

        // 배경 지우개
        gc.clearRect(0, 0, width, height);
        for (Ball ball : balls) {
            if (ball instanceof PaintableBall) {
                ((PaintableBall) ball).draw(gc);
            }
        }
    }

    public boolean removeBallAt(double x, double y) {
        for (int i = balls.size() - 1; i >= 0; i--) {
            Ball ball = balls.get(i);

            double dx = x - ball.getCenter().getX();
            double dy = y - ball.getCenter().getY();
            double distanceSquared = (dx * dx) + (dy * dy);
            if (distanceSquared <= Math.pow(ball.getRadius(), 2)) {
                balls.remove(i);
                return true;
            }
        }
        return false;
    }

    // private 경계 체크 메서드
    private boolean isInBounds(Ball ball) {
        return (ball.getCenter().getX() - ball.getRadius() >= 0) &&
                (ball.getCenter().getX() + ball.getRadius() <= width) &&
                (ball.getCenter().getY() - ball.getRadius() >= 0) &&
                (ball.getCenter().getY() + ball.getRadius() <= height);
    }
}