package com.nhnacademy.common;

import com.nhnacademy.Interfacesum.Bounds; // ✅
import com.nhnacademy.Interfacesum.Boundable; // ✅
import com.nhnacademy.Interfacesum.Paintable;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Box extends PaintableBall implements Paintable, Boundable { // ✅ Boundable 구현 추가

    private double width;
    private double height;

    @Override
    public void paint(GraphicsContext gc) {
        // ✅ 원(oval)로 그리던 것 제거하고, 사각형으로 그리기 통일
        gc.setFill(getColor());
        double x = getCenter().getX() - (width / 2.0);
        double y = getCenter().getY() - (height / 2.0);
        gc.fillRect(x, y, width, height);
    }

    public Box(Point center, double width, double height, Color color) {
        super(center, Math.sqrt(width * width + height * height) / 2.0, color);

        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("너비와 높이는 0보다 커야 합니다.");
        }
        this.width = width;
        this.height = height;
    }

    public Box(Point center, double width, double height) {
        super(center, Math.sqrt(width * width + height * height) / 2.0, Color.BLACK);
        if (width <= 0 || height <= 0) { // ✅ 유효성 체크 추가
            throw new IllegalArgumentException("너비와 높이는 0보다 커야 합니다.");
        }
        this.width = width;
        this.height = height;
    }

    public void setWidth(double width) {
        if (width <= 0) {
            throw new IllegalArgumentException("width must be positive");
        }
        this.width = width;
    }

    public void setHeight(double height) { // ✅ (선택) 높이 setter 추가
        if (height <= 0) {
            throw new IllegalArgumentException("height must be positive");
        }
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public void draw(GraphicsContext gc) { // ✅ 기존 draw는 paint로 통일했지만, 남겨도 동작하도록 동일 구현
        paint(gc);
    }

    @Override
    public Bounds getBounds() { // ✅ 핵심: center 기반 Bounds 제공
        double left = getCenter().getX() - width / 2.0;
        double top = getCenter().getY() - height / 2.0;
        return new RectangleBounds(left, top, width, height);
    }
}
