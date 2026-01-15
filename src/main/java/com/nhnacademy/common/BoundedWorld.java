package com.nhnacademy.common;

import java.util.List;

public class BoundedWorld extends MovableWorld {

    // 1. 생성자: super 호출 시 타입을 명시하지 않고 값만 반환 함
    public BoundedWorld(double width, double height) {
        super((int) width, (int) height);
    }

    // 2. add 오버라이드: 공이 들어올 때 바운더리를 알려줌
    @Override
    public void add(Ball ball) {
        super.add(ball);
        if (ball instanceof BoundedBall boundedBall) {
            // world에서 알아서 세팅하게 함
            boundedBall.setBounds(0, 0, getWidth(), getHeight());
        }
    }

    // 3. update 오버라이드: 이동 -> 벽 충돌 -> 공 충돌 순서
    @Override
    public void update(double deltaTime) {
        // [순서 1] 모든 공 이동 (MovableWorld 기능 상속)
        super.update(deltaTime);

        // [순서 2] 공 간의 충돌 검사 및 반환 (이중 루프 사용)
        checkBallCollisions();
    }

    private void checkBallCollisions() {
        List<Ball> balls = getBalls();

        // 이중 루프로 모든 공의 조합을 확인합니다 (i번 공과 j번 공)
        for (int i = 0; i < balls.size(); i++) {
            for (int j = i + 1; j < balls.size(); j++) {
                Ball ballA = balls.get(i);
                Ball ballB = balls.get(j);

                // 두 공이 서로 부딪혔는지 확인 (Ball 클래스의 isColliding 활용)
                if (BallCollision.areColliding(ballA, ballB)) {
                    resolveCollision(ballA, ballB);
                }
            }
        }
    }

    // 공끼리 부딪혔을 때의 반응 (간단하게 속도를 교환하거나 반전)
    private void resolveCollision(Ball a, Ball b) {
        BallCollision.resolveElasticCollision(a, b);
    }
}