package com.nhnacademy.common;

public class MovableBox extends Box {
    private Vector2D velocity;

    public MovableBox(Point center, double width, double height, Vector2D velocity) {
        super(center, width, height);
        this.velocity = velocity;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public void move(double dt) {
        double newX = getCenter().getX() + velocity.getX() * dt;
        double newY = getCenter().getY() + velocity.getY() * dt;
        setCenter(new Point(newX, newY));
    }
}