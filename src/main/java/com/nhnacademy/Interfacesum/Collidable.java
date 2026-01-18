package com.nhnacademy.Interfacesum;

import com.nhnacademy.common.CollisionAction;

//충돌 행동 담당
// Boundable을 확장
public interface Collidable extends Boundable {
    void handleCollision(Collidable other);

    CollisionAction getCollisionAction();

    void setCollisionAction(CollisionAction action);
}
