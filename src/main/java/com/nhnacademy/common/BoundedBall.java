package com.nhnacademy.common;

import javafx.scene.paint.Color;

public class BoundedBall extends MovableBall {
    private double minX;
    private double minY;
    private double maxX;
    private double maxY;

    public BoundedBall(Point center, double radius, Color color, Vector2D velocity) {
        super(center, radius, color, velocity);

        this.minX = Double.MIN_VALUE;
        this.minY = Double.MIN_VALUE;
        this.maxX = Double.MAX_VALUE;
        this.maxY = Double.MAX_VALUE;
    }

    public void setBounds(double worldMinX, double worldMinY, double worldMaxX, double worldMaxY) {
        double r = getRadius();

        this.minX = worldMinX + r;
        this.minY = worldMinY + r;
        this.maxX = worldMaxX - r;
        this.maxY = worldMaxY - r;
    }

    @Override
    public void move(double deltaTime) {
        super.move(deltaTime);
        Point currentPos = getCenter();
        Vector2D currentVelo = getVelocity();
        double nextX = currentPos.getX();
        double nextY = currentPos.getY();
        double nextVX = currentVelo.getX();
        double nextVY = currentVelo.getY();
        // x 축 벽 충돌 검사
        if (nextX < minX) {
            nextX = minX;
            nextVX = -nextVX;
        } else if (nextX > maxX) {
            nextX = maxX;
            nextVX = -nextVX;
        }

        // Y 축 벽 충돌 검사
        if (nextY < minY) {
            nextY = minY;
            nextVY = -nextVY;
        } else if (nextY > maxY) {
            nextY = maxY;
            nextVY = -nextVY;
        }
        setCenter(new Point(nextX, nextY));
        setVelocity(new Vector2D(nextVX, nextVY));
    }
}