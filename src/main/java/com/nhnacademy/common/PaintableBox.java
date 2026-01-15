package com.nhnacademy.common;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PaintableBox extends Box {
    private Color color;

    public PaintableBox(Point center, double width, double height, Color color) {
        super(center, width, height);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillRect(getCenter().getX() - (getWidth() / 2),
                getCenter().getY() - (getHeight() / 2),
                getWidth(), getHeight());
    }
}