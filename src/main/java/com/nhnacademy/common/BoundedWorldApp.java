package com.nhnacademy.common;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.Random;

public class BoundedWorldApp extends Application {
    private BoundedWorld world;
    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) {
        // 1. 월드와 캔버스 생성 (800x600)
        world = new BoundedWorld(800, 600);
        canvas = new Canvas(800, 600);

        // 2. 랜덤한 공 10개 추가
        Random random = new Random();
        final double width = world.getWidth();
        final double height = world.getHeight();
        for (int i = 0; i < 10; i++) {
            // BoundedBall 생성 시 속도(Vector2D)까지 한 번에 넣는 생성자를 사용합니다.
            double radius = 15 + random.nextInt(15);
            Color color = Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble());
            Vector2D velocity = new Vector2D(-100 + random.nextInt(200), -100 + random.nextInt(200));
            double x = radius + random.nextDouble() * (width - 2 * radius);
            double y = radius + random.nextDouble() * (height - 2 * radius);
            BoundedBall ball = new BoundedBall(
                    new Point(x, y),
                    radius,
                    color,
                    velocity // 생성자 인자에 velocity가 포함되어 있는지 확인하세요!
            );

            // 월드에 추가 (BoundedWorld.add 내부에서 setBounds가 자동으로 호출됨)
            world.add(ball);
        }

        // 3. 게임 루프 설정
        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }
                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                // 데이터 업데이트 (이동 + 벽 충돌 + 공 충돌)
                world.update(deltaTime);

                // 화면 렌더링
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.setFill(Color.WHITE); // 배경색 흰색
                gc.fillRect(0, 0, width, height);

                // 월드의 모든 공 그리기 (world.draw 메서드 활용)
                world.draw(gc);
            }
        };
        timer.start();

        // 4. 레이아웃 및 장면 설정
        Pane root = new Pane(canvas);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Bounded World Demo - Collision Active!");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // 창 크기 조절 불가 (경계 고정)
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}