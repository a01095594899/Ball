package com.nhnacademy.common;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball implements Paintable {
    private Point center;
    private double radius;

    @Override
    public void paint(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.fillOval(getCenter().getX() - getRadius(), getCenter().getY() - getRadius(), getRadius(), getRadius() * 2);
    }

    public Ball(Point center, double radius) { // 필드
        if (center == null) {
            throw new IllegalArgumentException("중심점은 0일수 없습니다.");
        }
        if (radius <= 0) {
            throw new IllegalArgumentException("반지름은 0보다 커야 합니다");
        }
        this.center = center;
        this.radius = radius;
    }

    public Ball(double x, double y, double radius) {
        this(new Point(x, y), radius);
    }

    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    public void moveTo(Point newCenter) {
        if (newCenter == null) {
            throw new IllegalArgumentException("null 이 올수 없습니다.");
        }
        this.center = newCenter;
    }

    public boolean contains(Point p) {
        return this.center.distanceTo(p) <= this.radius;
    }

    public boolean contains(double x, double y) {
        return contains(new Point(x, y));
    }

    public double getArea() {
        return Math.PI * Math.pow((getRadius()), 2);
    }

    // 이름을 isColliding other == null -> false
    public boolean isColliding(Ball other) {
        if (other == null) {
            return false;
        }

        // 자기 자신과 비교하는 경우 true;
        if (this == other) {
            return true;
        }

        double distance = center.distanceTo(other.getCenter());
        double sumOfRadii = this.getRadius() + other.getRadius();

        // 겹치거나 닿았으면 true, 아니면 false
        return distance <= sumOfRadii;
    }

    public void setCenter(Point center) {
        if (center == null) {
            throw new IllegalArgumentException("위치는 Null일수 없습니다.");
        }
        this.center = center;
    }
}