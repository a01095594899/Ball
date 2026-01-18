package com.nhnacademy.breakoutgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.nhnacademy.Interfacesum.Breakable;
import com.nhnacademy.common.Ball;
import com.nhnacademy.common.Point;
import com.nhnacademy.common.Vector2D;

import javafx.scene.paint.Color;

public class BreakoutGame {
    private static final int INITIAL_LIVES = 3;
    private static final double POINTS_PER_BRICK = 10;
    private static final double BALL_SPEED_INCREMENT = 1.1;
    private BreakoutPaddle paddle;
    private Ball ball;
    private List<Breakable> bricks;
    private List<PowerUp> powerUps = new ArrayList<>();
    private double gameState;
    private double score;
    private double lives;
    private double level;
    private BreakoutWorld world;
    private final Random random = new Random();

    public void setWorld(BreakoutWorld world) {
        this.world = world;
    }

    public BreakoutPaddle getPaddle() {
        return paddle;
    }

    public void setPaddle(BreakoutPaddle paddle) {
        this.paddle = paddle;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public void createMultiBalls() {
        if (ball == null || world == null)
            return;

        Vector2D v = ball.getVelocity();
        if (v == null)
            return;

        Point c = ball.getCenter();

        Ball b1 = new Ball(new Point(c.getX(), c.getY()), ball.getRadius(), Color.PINK);
        b1.setVelocity(new Vector2D(v.getX() * 0.9, -v.getY()));

        Ball b2 = new Ball(new Point(c.getX(), c.getY()), ball.getRadius(), Color.PINK);
        b2.setVelocity(new Vector2D(-v.getX() * 0.9, v.getY()));
        world.addBall(b2);
        world.addBall(b1);
    }

    public void slowBalls(double durationSeconds) {
        if (ball == null)
            return;

        Vector2D v = ball.getVelocity();
        if (v == null)
            return;
        ball.setVelocity(new Vector2D(v.getX() * 0.6, v.getY() * 0.6));
    }
}
