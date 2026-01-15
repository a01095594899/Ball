package com.nhnacademy.common;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FirstJavaFXApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 캔버스 생성
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // 배경
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 800, 600);

        // // 공 생성
        // PaintableBall ball1 = new PaintableBall(200, 200, 60, Color.RED);
        // PaintableBall ball2 = new PaintableBall(280, 200, 50, Color.BLUE);
        // PaintableBall ball3 = new PaintableBall(500, 200, 40, Color.GREEN);
        // // 공 그리기
        // ball1.draw(gc);
        // ball2.draw(gc);
        // ball3.draw(gc);
        // // 충돌 상태 텍스트 표시
        // gc.setFill(Color.WHITE);
        // gc.fillText("ball1 vs ball2: " + ball1.isColliding(ball2), 50, 400);
        // gc.fillText("ball1 vs ball3: " + ball1.isColliding(ball3), 50, 420);
        // gc.fillText("ball2 vs ball3: " + ball1.isColliding(ball3), 50, 440);

        // World 생성 및 공 추가
        World world = new MovableWorld(800, 600);
        GameLoop gameLoop = new GameLoop(world, gc);
        world.add(new MovableBall(new Point(100, 100), 10, Color.BLUE, new Vector2D(50, 50)));
        // Scene 생성
        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);

        // Stage 설정
        primaryStage.setTitle("My First JavaFX Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        // gameLoop 실행
        gameLoop.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}