package com.nhnacademy.breakoutgame;

import javafx.scene.paint.Color;

public enum PowerUpType {

    WIDER_PADDLE("W", Color.BLUE, 10.0),
    SLOW_BALL("S", Color.CYAN, 15.0),
    MULTI_BALL("M", Color.YELLOW, 0.0); // 즉시 발동

    private final String symbol;
    private final Color color;
    private final double duration;

    PowerUpType(String symbol, Color color, double duration) {
        this.symbol = symbol;
        this.color = color;
        this.duration = duration;
    }

    public String getSymbol() {
        return symbol;
    }

    public Color getColor() {
        return color;
    }

    public double getDuration() {
        return duration;
    }
}
