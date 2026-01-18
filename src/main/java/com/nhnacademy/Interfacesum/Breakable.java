package com.nhnacademy.Interfacesum;

// 파괴 가능한 객체
public interface Breakable {
    void hit(int damage);

    boolean isBroken();

    int getPoints();

}
