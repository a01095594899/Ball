package com.nhnacademy.breakoutgame;

import com.nhnacademy.common.Ball;
import com.nhnacademy.common.Vector2D;
import com.nhnacademy.breakoutgame.BreakoutGame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class PowerUp extends Ball {

    private PowerUpType type;
    private boolean collected = false;
    private static final double FALL_SPEED = 100;

    public PowerUp(double x, double y, PowerUpType type) {
        super(x, y, 15, type == null ? Color.WHITE : type.getColor());
        if (type == null) {
            throw new IllegalArgumentException();
        }

        this.type = type;
        setVelocity(new Vector2D(0, FALL_SPEED)); // 수직낙하만
    }

    public PowerUpType getType() {
        return type;
    }

    public boolean isCollected() {
        return collected;
    }

    @Override
    public void move(double deltaTime) {
        if (collected)
            return;
        setVelocity(new Vector2D(0, FALL_SPEED));
        super.move(deltaTime);
    }

    @Override
    public void paint(GraphicsContext gc) {
        if (collected)
            return;

        gc.setFill(type.getColor());
        gc.fillOval(getCenter().getX() - getRadius(), getCenter()
                .getY() - getRadius(),
                getRadius() * 2, getRadius() * 2);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(14));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(type.getSymbol(), getCenter().getX(), getCenter().getY() + 5);
    }

    // 파워업 효과 적용
    public void applyEffect(BreakoutGame game) {
        if (collected)
            return;
        if (game == null) {
            throw new IllegalArgumentException();
        }
        collected = true;

        switch (type) {
            case WIDER_PADDLE:
                game.getPaddle().expand(1.5, type.getDuration());
                break;

            case MULTI_BALL:
                game.createMultiBalls();
                break;

            case SLOW_BALL:
                game.slowBalls(type.getDuration());
                break;

            default:
                break;
        }
    }
}
