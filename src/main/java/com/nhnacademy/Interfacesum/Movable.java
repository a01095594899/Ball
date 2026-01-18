package com.nhnacademy.Interfacesum;

import com.nhnacademy.common.Vector2D;

public interface Movable {
    void move(double deltaTime);

    Vector2D getVelocity();

    void setVelocity(Vector2D velocity);

}
