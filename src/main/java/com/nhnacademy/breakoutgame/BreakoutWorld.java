// ✅ BreakoutWorld.java  (createBricks + 생성자에서 호출 + paint에서 bricks 그리기)
// ✅ 수정한 줄마다 // ✅ 표시

package com.nhnacademy.breakoutgame;

import java.util.ArrayList;
import java.util.List;

import com.nhnacademy.Interfacesum.Breakable;
import com.nhnacademy.Interfacesum.Collidable;
import com.nhnacademy.Interfacesum.Paintable;
import com.nhnacademy.breakoutgame.BreakoutGame;
import com.nhnacademy.breakoutgame.BreakoutPaddle;
import com.nhnacademy.breakoutgame.brick.UnbreakableBrick;
import com.nhnacademy.breakoutgame.brick.PowerUpBrick;
import com.nhnacademy.breakoutgame.brick.SimpleBrick; // ✅ 네 SimpleBrick 실제 패키지로 맞춰
import com.nhnacademy.common.Ball;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BreakoutWorld {

    private final List<UnbreakableBrick> walls = new ArrayList<>();
    private final List<Breakable> bricks = new ArrayList<>();
    private final List<Ball> balls = new ArrayList<>();
    private final List<PowerUp> powerUps = new ArrayList<>();

    private final BreakoutPaddle paddle;
    private final BreakoutGame game;

    private final double worldWidth;
    private final double worldHeight;

    public BreakoutWorld(double worldWidth, double worldHeight, BreakoutPaddle paddle, BreakoutGame game) {
        if (paddle == null)
            throw new IllegalArgumentException("paddle cannot be null");
        if (game == null)
            throw new IllegalArgumentException("game cannot be null");
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.paddle = paddle;
        this.game = game;

        game.setPaddle(paddle);
        game.setWorld(this);
        createWalls();
        createBricks(2); // 벽돌 생성 호출(레벨 2)
    }

    public double getWidth() {
        return worldWidth;
    }

    public double getHeight() {
        return worldHeight;
    }

    public void addBall(Ball ball) {
        if (ball != null) {
            balls.add(ball);
            game.setBall(ball);
        }
    }

    public void addBrick(Breakable brick) {
        if (brick != null)
            bricks.add(brick);
    }

    private void tryDropPowerUp(Breakable br) { // 드롭 생성 로직, 파워업 브릭만
        if (!(br instanceof com.nhnacademy.Interfacesum.PowerUpProvider provider)) {
            return;
        }
        if (!provider.shouldDropPowerUp()) {
            return;
        }
        if (br instanceof com.nhnacademy.breakoutgame.brick.SimpleBrick sb) {
            double cx = sb.getX() + sb.getWidth() / 2.0; // 드롭위치 계산
            double cy = sb.getY() + sb.getHeight() / 2.0;

            PowerUpType type = provider.getPowerUpType();
            powerUps.add(new PowerUp(cx, cy, type));
        }

    }

    // 벽 생성
    private void createWalls() {
        walls.clear();
        double t = 20;

        walls.add(new UnbreakableBrick(0, 0, worldWidth, t, Color.GRAY));
        walls.add(new UnbreakableBrick(0, 0, t, worldHeight, Color.GRAY));
        walls.add(new UnbreakableBrick(worldWidth - t, 0, t, worldHeight, Color.GRAY));
    }

    // 벽돌 생성(아이템 브릭 포함)
    public void createBricks(int level) {
        bricks.clear();

        int rows = 5;
        int cols = 10;

        double topMargin = 60;
        double sideMargin = 40;
        double gap = 6;

        double bw = (worldWidth - sideMargin * 2 - gap * (cols - 1)) / cols;
        double bh = 18;

        PowerUpType[] types = PowerUpType.values();

        // 레벨에 따라 아이템 브릭 비율 조정
        double powerUpBrickRate = (level >= 2) ? 0.25 : 0.15; // 15~25프로
        double dropChance = 0.8; // 파워업브릭 깨졌을때 실제 드롭확률

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double x = sideMargin + c * (bw + gap);
                double y = topMargin + r * (bh + gap);

                if (Math.random() < powerUpBrickRate) {
                    PowerUpType type = types[(int) (Math.random() * types.length)];
                    addBrick(new PowerUpBrick(x, y, bw, bh, Color.ORANGE, 10, 1.0, type));
                } else {
                    addBrick(new SimpleBrick(x, y, bw, bh, Color.ORANGE, 10));
                }
            }
        }
    }

    // 업데이트
    public void update(double dt) {
        paddle.move(dt);

        for (Ball b : balls) {
            b.move(dt);
        }
        for (PowerUp p : powerUps) { // 파워업 낙하
            p.move(dt);
        }

        handleCollisions();
        bricks.removeIf(Breakable::isBroken);
        powerUps.removeIf(p -> p.isCollected() || p.getCenter().getY() > worldHeight + 50); // 파워업 수집하거나 화면 밖 나가면 사라짐
    }

    private void handleCollisions() { // 공 어디에 충돌하는지
        for (Ball ball : balls) {

            // 벽
            for (UnbreakableBrick w : walls) {
                if (ball.isColliding(w)) {
                    ((Collidable) ball).handleCollision(w);
                }
            }

            // 패들
            if (ball.isColliding(paddle)) {
                ((Collidable) ball).handleCollision(paddle); // ✅ 최소 동작
            }

            // 벽돌
            for (Breakable br : bricks) {
                if (!(br instanceof Collidable bc))
                    continue;

                if (ball.isColliding(bc)) {
                    ((Collidable) ball).handleCollision(bc);
                    br.hit(1); // 맞으면 데미지 1

                    if (br.isBroken()) {
                        tryDropPowerUp(br);
                    }
                }
            }

        }
        for (PowerUp p : powerUps) { // 파워업 획득, 효과적용
            if (!p.isCollected() && p.isColliding(paddle)) {
                p.applyEffect(game);
            }
        }
    }

    // 벽돌이 실제로 보이게 Paintable 캐스팅으로 그리기
    public void paint(GraphicsContext gc) {
        for (UnbreakableBrick w : walls) {
            w.paint(gc);
        }

        for (Breakable br : bricks) {
            if (br instanceof Paintable p) {
                p.paint(gc);
            }
        }
        for (PowerUp p : powerUps) {
            p.paint(gc);
        }

        paddle.paint(gc);

        for (Ball b : balls) {
            b.paint(gc);
        }
    }
}
