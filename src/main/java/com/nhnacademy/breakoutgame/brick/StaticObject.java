package com.nhnacademy.breakoutgame.brick;

import com.nhnacademy.Interfacesum.Bounds;
import com.nhnacademy.Interfacesum.Collidable;
import com.nhnacademy.Interfacesum.Paintable;
import com.nhnacademy.common.CollisionAction;
import com.nhnacademy.common.RectangleBounds;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Breakout의 "고정 오브젝트" 공통 부모.
 * - Movable 없음 (절대 움직이지 않음)
 * - Paintable: 화면에 그릴 수 있음
 * - Collidable/Boundable: 충돌 대상 + 경계 제공
 */
public abstract class StaticObject implements Paintable, Collidable {

    // 벽돌/벽 공통 좌표계: 좌상단(x, y) + (width, height)
    protected double x;
    protected double y;
    protected double width;
    protected double height;

    protected Color color;

    // 충돌 시 행동은 기본 BOUNCE (월드/볼이 이를 해석)
    protected CollisionAction collisionAction = CollisionAction.BOUNCE;

    protected StaticObject(double x, double y, double width, double height, Color color) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width/height must be positive");
        }
        if (color == null) {
            throw new IllegalArgumentException("color cannot be null");
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    /* ===================== Paintable ===================== */

    /**
     * 기본 그리기: 단색 사각형.
     * 벽돌(예: SimpleBrick)에서 3D 효과/균열 등으로 오버라이드 권장.
     */
    @Override
    public void paint(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillRect(x, y, width, height);
    }

    /* ===================== Boundable (via Collidable) ===================== */

    @Override
    public Bounds getBounds() {
        return new RectangleBounds(x, y, width, height);
    }

    /* ===================== Collidable ===================== */

    /**
     * StaticObject는 "충돌 대상" 역할이 주이며,
     * 실제 충돌 반응(예: hit 처리)은 벽돌 쪽(handleCollision or World)에서 처리하도록 비워둠.
     */
    @Override
    public void handleCollision(Collidable other) {
        // no-op (기본은 아무것도 하지 않음)
        // 예: Ball이 Brick과 충돌하면 World가 brick.hit(1) 호출하는 구조가 가장 깔끔함
    }

    @Override
    public CollisionAction getCollisionAction() {
        return collisionAction;
    }

    @Override
    public void setCollisionAction(CollisionAction action) {
        if (action == null) {
            throw new IllegalArgumentException("collisionAction cannot be null");
        }
        this.collisionAction = action;
    }

    /* ===================== Getters ===================== */

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Color getColor() {
        return color;
    }
}
