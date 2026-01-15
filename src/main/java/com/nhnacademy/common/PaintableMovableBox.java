package com.nhnacademy.common;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PaintableMovableBox extends MovableBox {
    private Color color;

    public PaintableMovableBox(Point center, double width, double height, Vector2D velocity, Color color) {
        super(center, width, height, velocity);
        this.color = color;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        double x = getCenter().getX() - (getWidth() / 2);
        double y = getCenter().getY() - (getHeight() / 2);

        gc.fillRect(x, y, getWidth(), getHeight());
    }
}