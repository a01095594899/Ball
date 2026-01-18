package com.nhnacademy.breakoutgame.brick;

import com.nhnacademy.Interfacesum.Breakable;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SimpleBrick extends StaticObject implements Breakable {

    protected boolean broken = false;
    protected int points;

    public SimpleBrick(double x, double y,
            double width, double height,
            Color color, int points) {
        super(x, y, width, height, color);
        this.points = points;
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
    };

    @Override
    public void hit(int damage) {
        broken = true;
    }

    @Override
    public boolean isBroken() {
        return broken;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public void paint(GraphicsContext gc) {
        if (broken) {
            return;
        }

        // 그림자
        gc.setFill(color.darker());
        gc.fillRect(x + 2, y + 2, width, height);

        // 본체
        gc.setFill(color);
        gc.fillRect(x, y, width, height);
    }
}
