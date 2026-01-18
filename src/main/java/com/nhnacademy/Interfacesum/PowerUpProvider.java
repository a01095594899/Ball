package com.nhnacademy.Interfacesum;

import com.nhnacademy.breakoutgame.PowerUpType;

// 파워업 제공
public interface PowerUpProvider {
    boolean shouldDropPowerUp();

    PowerUpType getPowerUpType();
}
