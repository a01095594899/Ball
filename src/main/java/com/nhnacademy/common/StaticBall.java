package com.nhnacademy.common;

import javafx.scene.paint.Color;

/**
 * 위치가 고정되어 움직이지 않는 장애물용 공
 */
public class StaticBall extends PaintableBall {

    public StaticBall(Point center, double radius, Color color) {
        super(center, radius, color);
    }
}