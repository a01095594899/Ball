package com.nhnacademy.common;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Box extends PaintableBall {
    private double width;
    private double height;

    @Override
    public void paint(GraphicsContext gc) {
        gc.setFill(getColor());
        gc.fillOval(getCenter().getX() - getRadius(), getCenter().getY() - getRadius(), getRadius() * 2,
                getRadius() * 2);

    }

    public Box(Point center, double width, double height, Color color) {
        super(center, Math.sqrt(width * width + height * height) / 2, color);

        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("너비와 높이는 0보다 커야 합니다.");
        }
        this.width = width;
        this.height = height;
    }

    public Box(Point center, double width, double height) {
        super(center, Math.sqrt(width * width + height * height) / 2, Color.BLACK);
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(getColor());
        double x = getCenter().getX() - (width / 2);
        double y = getCenter().getY() - (height / 2);

        gc.fillRect(x, y, width, height);
    }
}