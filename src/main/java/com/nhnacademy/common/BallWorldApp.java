package com.nhnacademy.common;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class BallWorldApp extends Application {
    private World world;
    private Canvas canvas;
    private GraphicsContext gc;
    private Random random = new Random();

    @Override
    public void start(Stage stage) {
        this.world = new World(800, 600);
        this.canvas = new Canvas(800, 600);
        this.gc = canvas.getGraphicsContext2D();
        // 초기 공 5개 생성
        createRandomBalls(10);
        // 마우스 클릭 이벤트 설정
        canvas.setOnMouseClicked(event -> handleMouseClick(event));
        // Scene, stage 구성
        // 초기 화면 그리기 draw 메서드
        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);
        // C 입력시 모든 공 clear
        scene.setOnKeyPressed(event -> handleKeyPressed(event));

        // Stage 설정
        stage.setTitle("Ball World App");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        draw();
    }

    public void createRandomBalls(int count) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            double radius = 10 + random.nextDouble() * 40; // 반지름은 10 ~ 50 랜덤 생성
            double x = radius + random.nextDouble() * (world.getWidth() - 2 * radius);
            double y = radius + random.nextDouble() * (world.getHeight() - 2 * radius);

            Color color = Color.rgb( // 색상 랜덤
                    random.nextInt(256),
                    random.nextInt(256),
                    random.nextInt(256));
            // 4. PaintableBall 생성
            try {
                PaintableBall newBall = new PaintableBall(x, y, radius, color);
                world.add(newBall);
            } catch (IllegalArgumentException e) {
                i--;
            }
        }
    }

    private void handleMouseClick(javafx.scene.input.MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        // [우클릭 처리]
        if (event.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
            world.removeBallAt(x, y);
        }
        // [좌클릭 처리]
        else if (event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
            // 변수 선언을 try 바로 직전 혹은 안에서 수행합니다.
            double radius = 10 + random.nextDouble() * 40;
            Color color = randomColor();

            try {
                PaintableBall newBall = new PaintableBall(x, y, radius, color);
                world.add(newBall);
            } catch (IllegalArgumentException e) {
                // 벽 근처 에러는 로그로 처리하고 넘깁니다.
                System.out.println("벽 근처에는 공을 만들 수 없습니다.");
            }
        }

        // [공통] 마지막에 화면 갱신 및 개수 업데이트
        draw();
    }

    private void handleKeyPressed(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == javafx.scene.input.KeyCode.C) {
            world.clear();
            draw();
        }
    }

    private Color randomColor() {
        return Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble());
    }

    public void setWorld(World world) {
        this.world = world;
    }

    private void draw() {
        // World의 draw 메서드 호출
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        world.draw(gc);
        gc.setFill(Color.BLACK);
        gc.setFont(new javafx.scene.text.Font("Arial", 30)); // 폰트 지정 + 글자 크기
        gc.fillText("공 개수: " + world.getBallCount(), 20, 30);
    }

    public static void main(String[] args) {
        launch(args);
    }
}