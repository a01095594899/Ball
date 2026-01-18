package com.nhnacademy.breakoutgame.brick;

import javafx.scene.paint.Color;

import java.util.Random;

import com.nhnacademy.Interfacesum.PowerUpProvider;
import com.nhnacademy.breakoutgame.PowerUpType;

public class PowerUpBrick extends SimpleBrick implements PowerUpProvider {

    private final double dropChance;
    private final PowerUpType powerUpType;
    private static final Random random = new Random();

    public PowerUpBrick(double x, double y,
            double width, double height,
            Color color, int points,
            double dropChance,
            PowerUpType powerUpType) {
        super(x, y, width, height, color, points);

        if (dropChance < 0 || dropChance > 1) {
            throw new IllegalArgumentException("dropChance must be 0~1");
        }

        this.dropChance = dropChance;
        this.powerUpType = powerUpType;
    }

    @Override
    public boolean shouldDropPowerUp() {
        return random.nextDouble() < dropChance;
    }

    @Override
    public PowerUpType getPowerUpType() {
        return powerUpType;
    }
}
