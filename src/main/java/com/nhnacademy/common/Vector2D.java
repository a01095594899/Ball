package com.nhnacademy.common;

public class Vector2D {
    private final double x;
    private final double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    public Vector2D multiply(double scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    // public Vector2D angle() {
    // return Math.toDegrees(Math.atan2(getY(), getX()));
    // }

    public double getMagnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2D normalize() {
        double mag = getMagnitude();
        if (mag == 0) {
            return new Vector2D(0, 0);
        }
        return new Vector2D(x / mag, y / mag);
    }

    public double dot(Vector2D other) {
        return (this.x * other.x) + (this.y * other.y);
    }
}