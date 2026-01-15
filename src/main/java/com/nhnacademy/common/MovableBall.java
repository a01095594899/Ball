package com.nhnacademy.common;

import javafx.scene.paint.Color;

public class MovableBall extends PaintableBall {
    private Vector2D velocity;

    public MovableBall(Point center, double radius) {
        super(center, radius);
        this.velocity = new Vector2D(0, 0);
    }

    public MovableBall(Point center, double radius, Color color) {
        super(center, radius, color);
        this.velocity = new Vector2D(0, 0);
    }

    public MovableBall(Point center, double radius, Color color, Vector2D velocity) {
        super(center, radius, color);
        this.velocity = velocity;
    }

    public Vector2D getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public void move(double dt) { // dt는 delta time (경과 시간, 초 단위)
        if (velocity != null) {
            Vector2D displacement = velocity.multiply(dt);

            this.setCenter(this.getCenter().add(displacement));
        }
    }
}