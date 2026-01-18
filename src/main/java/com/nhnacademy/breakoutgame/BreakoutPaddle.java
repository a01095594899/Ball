package com.nhnacademy.breakoutgame;

import com.nhnacademy.common.Ball;
import com.nhnacademy.common.Box;
import com.nhnacademy.common.Point;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// 플레이어가 조작하는 패들 구현
public class BreakoutPaddle extends Box {

    private double targetX;
    private final double speed;
    private double worldWidth;

    // --- PowerUp 상태 ---
    private boolean laserEnabled = false;
    private boolean stickyEnabled = false;

    private double laserTimeLeft = 0.0;
    private double stickyTimeLeft = 0.0;

    private double expandTimeLeft = 0.0;
    private final double originalWidth;

    public BreakoutPaddle(Point center, double width, double height, Color color, double speed, double worldWidth) {
        super(center, width, height, color);
        this.targetX = center.getX();
        this.speed = speed;
        this.worldWidth = worldWidth;
        this.originalWidth = width;
    }

    public BreakoutPaddle(double x, double y, double width, double height, Color color, double speed) {
        super(new Point(x, y), width, height, color);
        this.targetX = x;
        this.speed = speed;
        this.worldWidth = 0;
        this.originalWidth = width;
    }

    // 마우스 입력
    public void setTargetX(double targetX) {
        this.targetX = targetX;
    }

    public void setWorldWidth(double worldWidth) {
        this.worldWidth = worldWidth;
    }

    // 반사각도 계산
    public double calculateReflectionAngle(Ball ball) {
        double paddleCenterX = getCenter().getX();
        double halfWidth = getWidth() / 2.0;

        // 충돌 지점이 패들 중심에서 얼마나 떨어졌는지 비율
        double hitRatio = (ball.getCenter().getX() - paddleCenterX) / halfWidth;

        // 기존 너 로직 유지
        return (hitRatio - 0.5) * Math.PI / 3;
    }

    // 화면 경계 내 위치 제한
    public void clampPosition() {
        double halfWidth = getWidth() / 2.0;
        double x = getCenter().getX();

        if (x - halfWidth < 0) {
            x = halfWidth;
        } else if (x + halfWidth > worldWidth) {
            x = worldWidth - halfWidth;
        }
        setCenter(new Point(x, getCenter().getY()));
    }

    // ===================== PowerUp API =====================

    public void expand(double factor, double durationSeconds) {
        if (factor <= 0)
            return;

        setWidth(originalWidth * factor); // Box.setWidth 필요
        expandTimeLeft = Math.max(0.0, durationSeconds);
        clampPosition();
    }

    public void enableLaser(double durationSeconds) {
        laserEnabled = true;
        laserTimeLeft = Math.max(0.0, durationSeconds);
    }

    public void enableSticky(double durationSeconds) {
        stickyEnabled = true;
        stickyTimeLeft = Math.max(0.0, durationSeconds);
    }

    public boolean isLaserEnabled() {
        return laserEnabled;
    }

    public boolean isStickyEnabled() {
        return stickyEnabled;
    }

    // 이동
    @Override
    public void move(double deltaTime) {
        double centerX = getCenter().getX();
        double diff = targetX - centerX;

        // 기존 너 로직 유지(부드러운 추적)
        double dx = diff * 0.1;

        setCenter(new Point(centerX + dx, getCenter().getY()));

        // PowerUp 타이머 갱신/원복
        updatePowerUpTimers(deltaTime);

        clampPosition();
    }

    @Override
    public void paint(GraphicsContext gc) {
        gc.setFill(getColor());

        double x = getCenter().getX() - getWidth() / 2.0;
        double y = getCenter().getY() - getHeight() / 2.0;

        gc.fillRect(x, y, getWidth(), getHeight());
    }

    private void updatePowerUpTimers(double deltaTime) {
        if (expandTimeLeft > 0) {
            expandTimeLeft -= deltaTime;
            if (expandTimeLeft <= 0) {
                setWidth(originalWidth);
                expandTimeLeft = 0;
            }
        }

        if (laserTimeLeft > 0) {
            laserTimeLeft -= deltaTime;
            if (laserTimeLeft <= 0) {
                laserEnabled = false;
                laserTimeLeft = 0;
            }
        }

        if (stickyTimeLeft > 0) {
            stickyTimeLeft -= deltaTime;
            if (stickyTimeLeft <= 0) {
                stickyEnabled = false;
                stickyTimeLeft = 0;
            }
        }
    }
}
