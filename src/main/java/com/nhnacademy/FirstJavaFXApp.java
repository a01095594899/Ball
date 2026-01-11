package com.nhnacademy;

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

        // 원 그리기
        gc.setFill(Color.RED);
        gc.fillOval(400 - 50, 300 - 50, 100, 100); // 중앙에 반지름 50인 원

        // Scene 생성
        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);

        // Stage 설정
        primaryStage.setTitle("My First JavaFX Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}