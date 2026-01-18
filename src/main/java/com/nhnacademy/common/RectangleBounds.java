package com.nhnacademy.common;

import com.nhnacademy.Interfacesum.Bounds;

public class RectangleBounds implements Bounds {

    private final double x;
    private final double y;
    private final double width;
    private final double height;

    public RectangleBounds(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean intersects(Bounds other) {
        if (!(other instanceof RectangleBounds o)) {
            return false;
        }
        return x < o.x + o.width &&
                x + width > o.x &&
                y < o.y + o.height &&
                y + height > o.y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
