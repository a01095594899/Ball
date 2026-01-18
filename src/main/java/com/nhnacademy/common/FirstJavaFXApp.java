package com.nhnacademy.common;

import com.nhnacademy.breakoutgame.BreakoutGame;
import com.nhnacademy.breakoutgame.BreakoutPaddle;
import com.nhnacademy.breakoutgame.PowerUp;
import com.nhnacademy.breakoutgame.PowerUpType;
import com.nhnacademy.breakoutgame.brick.UnbreakableBrick; // (선택) 벽돌 직접 추가 테스트용
import com.nhnacademy.breakoutgame.BreakoutWorld;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FirstJavaFXApp extends Application {

    private static final double WIDTH = 800;
    private static final double HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);

        // ===================== Breakout 세팅 =====================

        // 패들 생성(중심좌표 기준 Box 설계)
        BreakoutPaddle paddle = new BreakoutPaddle(
                new Point(WIDTH / 2, HEIGHT - 40),
                120, 18,
                Color.WHITE,
                0, // speed는 현재 move()에서 안 쓰고 diff*0.1이라 0 줘도 됨
                WIDTH);

        // 월드 생성
        BreakoutGame game = new BreakoutGame();
        BreakoutWorld world = new BreakoutWorld(WIDTH, HEIGHT, paddle, game);

        // 공 1개 생성 + 속도 부여
        Ball ball = new Ball(WIDTH / 2, HEIGHT / 2, 10, Color.WHITE);
        ball.setVelocity(new Vector2D(180, -220));
        world.addBall(ball);

        // (선택) 벽돌/벽 테스트용
        // - 네가 SimpleBrick/MultiHitBrick 등을 이미 만들었으면 여기서 world.addBrick(...)로 추가하면 됨.
        // world.addBrick(new SimpleBrick(...));
        // world.addBrick(new MultiHitBrick(...));

        // ===================== 입력 =====================

        // 마우스 이동으로 패들 목표 X 지정
        scene.setOnMouseMoved(e -> paddle.setTargetX(e.getX()));
        scene.setOnMouseDragged(e -> paddle.setTargetX(e.getX()));

        // 스페이스로 공 리셋(디버그용)
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                ball.setCenter(new Point(WIDTH / 2, HEIGHT / 2));
                ball.setVelocity(new Vector2D(180, -220));
            }
        });

        // ===================== 루프 =====================

        AnimationTimer loop = new AnimationTimer() {
            private long last = 0L;

            @Override
            public void handle(long now) {
                if (last == 0L) {
                    last = now;
                    return;
                }
                double dt = (now - last) / 1_000_000_000.0;
                last = now;

                // 업데이트
                world.update(dt);

                // 렌더 (배경 지우고 다시 그림)
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, WIDTH, HEIGHT);

                world.paint(gc);
            }
        };

        // ===================== 스테이지 =====================

        primaryStage.setTitle("Breakout");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        loop.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
