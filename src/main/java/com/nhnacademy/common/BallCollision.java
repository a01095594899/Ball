package com.nhnacademy.common;

public class BallCollision {

    // 1. 충돌 여부 확인
    public static boolean areColliding(Ball ball1, Ball ball2) {
        return ball1.getCenter().distanceTo(ball2.getCenter()) < (ball1.getRadius() + ball2.getRadius());
    }

    // 2. 탄성 충돌 처리 및 겹침 분리 통합
    public static void resolveElasticCollision(Ball b1, Ball b2) {
        double dx = b2.getCenter().getX() - b1.getCenter().getX();
        double dy = b2.getCenter().getY() - b1.getCenter().getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance == 0)
            return;

        // [추가] 먼저 겹친 공들을 물리적으로 분리 (고정체 로직)
        separateBalls(b1, b2, distance);

        // 속도 변화 로직 (둘 다 MovableBall일 때만 계산)
        if (b1 instanceof MovableBall ball1 && b2 instanceof MovableBall ball2) {
            double nx = dx / distance;
            double ny = dy / distance;

            double rvx = ball1.getVelocity().getX() - ball2.getVelocity().getX();
            double rvy = ball1.getVelocity().getY() - ball2.getVelocity().getY();

            double velAlongNormal = rvx * nx + rvy * ny;
            if (velAlongNormal > 0)
                return;

            double impulse = -2 * velAlongNormal / 2;
            double impulseX = impulse * nx;
            double impulseY = impulse * ny;

            ball1.setVelocity(new Vector2D(
                    ball1.getVelocity().getX() + impulseX,
                    ball1.getVelocity().getY() + impulseY));
            ball2.setVelocity(new Vector2D(
                    ball2.getVelocity().getX() - impulseX,
                    ball2.getVelocity().getY() - impulseY));
        }
    }

    // 3. 고정체 대응 겹침 분리 로직
    public static void separateBalls(Ball b1, Ball b2, double distance) {
        double overlap = (b1.getRadius() + b2.getRadius()) - distance;
        if (overlap <= 0) {
            return;
        }

        double dx = b2.getCenter().getX() - b1.getCenter().getX();
        double dy = b2.getCenter().getY() - b1.getCenter().getY();
        double nx = dx / distance;
        double ny = dy / distance;

        // 고정체 여부 확인 (StaticBall은 위치가 변하지 않음)
        boolean is1Static = (b1 instanceof StaticBall) || !(b1 instanceof MovableBall);
        boolean is2Static = (b2 instanceof StaticBall) || !(b2 instanceof MovableBall);

        if (is1Static && is2Static)
            return; // 둘 다 고정체면 무시

        if (is1Static) {
            // b1이 벽이면 b2만 100% 밀어냄
            MovableBall mb2 = (MovableBall) b2;
            mb2.setCenter(new Point(mb2.getCenter().getX() + overlap * nx, mb2.getCenter().getY() + overlap * ny));
        } else if (is2Static) {
            // b2가 벽이면 b1만 100% 밀어냄
            MovableBall mb1 = (MovableBall) b1;
            mb1.setCenter(new Point(mb1.getCenter().getX() - overlap * nx, mb1.getCenter().getY() - overlap * ny));
        } else {
            // 둘 다 이동체면 반반(50%)씩 밀어냄
            MovableBall mb1 = (MovableBall) b1;
            MovableBall mb2 = (MovableBall) b2;
            double moveX = (overlap / 2) * nx;
            double moveY = (overlap / 2) * ny;

            mb1.setCenter(new Point(mb1.getCenter().getX() - moveX, mb1.getCenter().getY() - moveY));
            mb2.setCenter(new Point(mb2.getCenter().getX() + moveX, mb2.getCenter().getY() + moveY));
        }
    }
}