package com.nhnacademy.common;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.Random;

public class MovableWorldApp extends Application {

    private World world;
    private Canvas canvas;
    private GraphicsContext gc;
    private Label fpsLabel;
    private Random random = new Random();

    // FPS 계산용 변수
    private long lastUpdate = 0;
    private int frameCount = 0;
    private long lastFpsTime = 0;

    @Override
    public void start(Stage stage) {
        // MovableWorld 생성 (800x600)
        world = new World(800, 600);
        // Canvas 생성 및 GraphicsContext 획득
        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
        // FPS 라벨 생성
        // 초기 공 10개 생성 (createMovingBalls 메서드)
        // AnimationTimer 생성 및 시작
        // Scene, Stage 구성
        fpsLabel = new Label("FPS: 0");
        fpsLabel.setTextFill(Color.WHITE);
        fpsLabel.setLayoutX(20);
        fpsLabel.setLayoutY(20);
        createMovingBalls(10);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    lastFpsTime = now;
                    return;
                }
                double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;
                frameCount++;
                if (now - lastFpsTime >= 1_000_000_000L) {
                    fpsLabel.setText("FPS: " + frameCount);
                    frameCount = 0;
                    lastFpsTime = now;
                }
                world.update(deltaTime);
                render();
            }
        };
        timer.start();
        Pane root = new Pane(canvas, fpsLabel); // 캔버스와 라벨을 동시에 담음
        Scene scene = new Scene(root, 800, 600, Color.BLACK); // 배경색 검정
        stage.setTitle("Movable World Simulation");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void createMovingBalls(int count) {
        // count개의 랜덤 위치, 크기, 색상, 속도의 MovableBall 생성
        // 랜덤 위치: 가장자리에서 반지름만큼 떨어진 곳
        // 랜덤 크기: 10~30 픽셀
        // 랜덤 색상: RGB 각각 0~1
        // 랜덤 속도: -100~100 pixels/second
        for (int i = 0; i < count; i++) {
            double r = 10 + random.nextDouble() * 20;
            double x = r + random.nextDouble() * (800 - 2 * r);
            double y = r + random.nextDouble() * (600 - 2 * r);

            Vector2D velocity = new Vector2D(
                    -100 + random.nextDouble() * 200,
                    -100 + random.nextDouble() * 200);
            BoundedBall ball = new BoundedBall(new Point(x, y), r, randomColor(), velocity);
            ball.setBounds(0, 0, 800, 600);
            world.add(ball);
        }
    }

    private void render() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 800, 600);
        if (world != null) {
            for (Ball ball : world.getBalls()) {
                if (ball instanceof PaintableBall paintableBall) {
                    paintableBall.draw(gc);
                }
            }
        }
    }

    private Color randomColor() {
        return Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble());
    }

    public static void main(String[] args) {
        launch(args);
    }
}