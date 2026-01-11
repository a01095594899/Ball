package com.nhnacademy;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameLoopExample extends Application {

    private double ballX = 100;
    private double ballY = 100;
    private double velocityX = 3;
    private double velocityY = 2;
    private static final double BALL_RADIUS = 20;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // 게임 루프 생성
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(); // 게임 상태 업데이트
                render(gc); // 화면 렌더링
            }
        };

        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);

        primaryStage.setTitle("Game Loop Example");
        primaryStage.setScene(scene);
        primaryStage.show();

        gameLoop.start(); // 게임 루프 시작
    }

    // 게임 상태 업데이트 (물리 연산, 충돌 검사 등)
    private void update() {
        ballX += velocityX;
        ballY += velocityY;

        // 벽 충돌 검사
        if (ballX <= BALL_RADIUS || ballX >= 800 - BALL_RADIUS) {
            velocityX = -velocityX;
        }
        if (ballY <= BALL_RADIUS || ballY >= 600 - BALL_RADIUS) {
            velocityY = -velocityY;
        }
    }

    // 화면 렌더링
    private void render(GraphicsContext gc) {
        // 화면 지우기
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 800, 600);

        // 공 그리기
        gc.setFill(Color.BLUE);
        gc.fillOval(ballX - BALL_RADIUS, ballY - BALL_RADIUS,
                BALL_RADIUS * 2, BALL_RADIUS * 2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}