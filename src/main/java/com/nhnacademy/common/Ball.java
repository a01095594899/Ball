package com.nhnacademy.common;

import com.nhnacademy.Interfacesum.Boundable;
import com.nhnacademy.Interfacesum.Bounds;
import com.nhnacademy.Interfacesum.Collidable;
import com.nhnacademy.Interfacesum.Movable;
import com.nhnacademy.Interfacesum.Paintable;
import com.nhnacademy.common.CollisionAction; // ⚠️ CollisionAction 위치가 다르면 너 패키지로 변경
import com.nhnacademy.common.RectangleBounds; // ⚠️ RectangleBounds 위치가 다르면 너 패키지로 변경

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball implements Paintable, Movable, Boundable, Collidable {

    private Point center;
    private final double radius;
    private Vector2D velocity;
    private final Color color;

    // 충돌 행동(기본: 튕김)
    private CollisionAction collisionAction = CollisionAction.BOUNCE;

    public Ball(Point center, double radius, Color color) {
        if (center == null) {
            throw new IllegalArgumentException("중심점은 null일 수 없습니다.");
        }
        if (radius <= 0) {
            throw new IllegalArgumentException("반지름은 0보다 커야 합니다.");
        }
        if (color == null) {
            throw new IllegalArgumentException("색상은 null일 수 없습니다.");
        }
        this.center = center;
        this.radius = radius;
        this.color = color;
    }

    public Ball(double x, double y, double radius) {
        this(new Point(x, y), radius, Color.WHITE);
    }

    public Ball(double x, double y, double radius, Color color) {
        this(new Point(x, y), radius, color);
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        if (center == null) {
            throw new IllegalArgumentException("위치는 null일 수 없습니다.");
        }
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public void paint(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillOval(center.getX() - radius, center.getY() - radius, radius * 2, radius * 2);
    }

    @Override
    public void move(double deltaTime) {
        if (velocity == null)
            return;

        double nextX = center.getX() + velocity.getX() * deltaTime;
        double nextY = center.getY() + velocity.getY() * deltaTime;
        this.center = new Point(nextX, nextY);
    }

    @Override
    public Vector2D getVelocity() {
        return velocity;
    }

    @Override
    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    @Override
    public Bounds getBounds() {
        double left = center.getX() - radius;
        double top = center.getY() - radius;
        return new RectangleBounds(left, top, radius * 2, radius * 2);
    }

    /**
     * 사각형 Bounds 기준 반사 처리(벽/패들/벽돌 전용으로 충분히 동작).
     */
    @Override
    public void handleCollision(Collidable other) {
        if (other == null)
            return; // ✅ == 로 수정
        if (velocity == null)
            return;

        Bounds a = this.getBounds();
        Bounds b = other.getBounds();
        if (a == null || b == null)
            return;

        if (!(a instanceof RectangleBounds ra) || !(b instanceof RectangleBounds rb)) {
            // Bounds 타입이 다르면 기본 Y 반사 정도로 처리
            setVelocity(new Vector2D(velocity.getX(), -velocity.getY()));
            return;
        }

        // 겹친 양(침투량) 계산: 더 덜 겹친 축을 반사축으로 선택
        double overlapLeft = (ra.getX() + ra.getWidth()) - rb.getX();
        double overlapRight = (rb.getX() + rb.getWidth()) - ra.getX();
        double overlapTop = (ra.getY() + ra.getHeight()) - rb.getY();
        double overlapBottom = (rb.getY() + rb.getHeight()) - ra.getY();

        double minX = Math.min(overlapLeft, overlapRight);
        double minY = Math.min(overlapTop, overlapBottom);

        // 축 반사
        if (minX < minY) {
            setVelocity(new Vector2D(-velocity.getX(), velocity.getY()));
        } else {
            setVelocity(new Vector2D(velocity.getX(), -velocity.getY()));
        }
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

    // 기존 메서드 필요하면 유지 (지금은 Bounds 기반 isColliding을 쓰므로 없어도 됨)
    public boolean contains(Point p) {
        return this.center.distanceTo(p) <= this.radius;
    }

    public boolean contains(double x, double y) {
        return contains(new Point(x, y));
    }

    public double getArea() {
        return Math.PI * Math.pow(radius, 2);
    }
}
