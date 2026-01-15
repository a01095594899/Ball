package com.nhnacademy.common;

public class Point {
    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;

    }

    public Point(Point other) {
        x = other.getX();
        y = other.getY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distanceTo(Point other) { // 다른놈과 나와의 거리
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }

    public Point add(Vector2D vector) { // 벡터의 덧셈 메서드 추가
        return new Point(this.x + vector.getX(), this.y + vector.getY());
    }

    public Vector2D subtract(Point other) { // 벡터의 뺄셈 메서드 추가
        return new Vector2D(this.x - other.x, this.y - other.y);
    }
}