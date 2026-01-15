package com.nhnacademy.common;

public class MovableWorld extends World {

    public MovableWorld(int width, int height) {
        super(width, height);
    }

    /*
     *
     * 
     * @param deltaTime 이전 프레임으로부터 경과된 시간(초 단위)
     */
    public void update(double deltaTime) {
        for (Ball ball : getBalls()) {
            if (ball instanceof MovableBall movableBall) {
                movableBall.move(deltaTime);
            }
        }
    }
}