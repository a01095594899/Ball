package com.nhnacademy.common;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PaintableBall extends Ball {
    static final Color DEFAULT_COLOR = Color.RED;
    private Color color;

    public PaintableBall(Point center, double radius, Color color) {
        super(center, radius, color);
        if (color == null) {
            throw new IllegalArgumentException("색은 null로 지정할수 없습니다.");
        }
        this.color = color;
    }

    public PaintableBall(Point center, double radius) {
        this(center, radius, DEFAULT_COLOR);
    }

    public PaintableBall(double x, double y, double radius, Color color) {
        this(new Point(x, y), radius, color);
    }

    public PaintableBall(double x, double y, double radius) {
        this(new Point(x, y), radius);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (color == null) {
            throw new IllegalArgumentException("변경 색상이 null입니다.");
        }
        this.color = color;
    }

    public void draw(GraphicsContext gc) {
        if (gc == null) {
            throw new IllegalArgumentException("GraphicsContext가 null입니다.");
        }
        // 공의 왼쪽 상단 좌표 계산
        Point center = getCenter();
        double leftX = center.getX() - getRadius();
        double topY = center.getY() - getRadius();
        double diameter = getRadius() * 2;

        // 공 채우기
        gc.setFill(this.color);
        gc.fillOval(leftX, topY, diameter, diameter);

        gc.setStroke(Color.BLACK);
        gc.strokeOval(leftX, topY, diameter, diameter);
    }
}