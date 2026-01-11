# 8장: 벽돌 깨기 (Breakout)

## 학습 목표

이 장을 완료하면 다음을 할 수 있습니다:
- 완전한 벽돌 깨기 게임을 구현할 수 있습니다
- 게임 상태 관리 시스템을 설계할 수 있습니다
- 점수와 생명 시스템을 구현할 수 있습니다
- 파워업과 특수 효과를 추가할 수 있습니다
- 사용자 입력을 처리하고 게임플레이를 제어할 수 있습니다

## 핵심 개념

### 8.1 게임 구성 요소

**벽돌 깨기 게임의 주요 구성 요소**

이 장에서는 2~7장에서 설계한 클래스와 인터페이스를 최대한 활용하여 완전한 게임을 구현합니다:

1. **패들(Paddle)**: Box 클래스를 확장하여 구현
2. **공(Ball)**: Ball 클래스를 확장하여 구현
3. **벽돌(Brick)**: StaticObject를 확장하고 인터페이스 구현
4. **파워업(PowerUp)**: Ball 클래스를 확장하여 떨어지는 아이템 구현
5. **게임 벽**: UnbreakableBrick으로 게임 공간 정의
6. **특수 효과**: 인터페이스로 정의 (Breakable, MultiHit, Exploding, PowerUpProvider)

**BreakoutGame 클래스 설계**

**필드:**
- `paddle`: 패들 객체
- `ball`: 공 객체 (나중에 여러 개 가능)
- `bricks`: 벽돌 리스트
- `powerUps`: 활성 파워업 리스트
- `gameState`: 현재 게임 상태
- `score`: 현재 점수
- `lives`: 남은 생명
- `level`: 현재 레벨

**상수:**
- `INITIAL_LIVES`: 시작 생명 수 (3)
- `POINTS_PER_BRICK`: 벽돌당 기본 점수 (10)
- `BALL_SPEED_INCREMENT`: 레벨당 속도 증가율 (1.1)

### 8.2 패들(Paddle) 구현

**BreakoutPaddle 클래스 설계**

Box 클래스를 확장하여 플레이어가 조작하는 패들을 구현합니다:

**클래스 선언:**
```java
public class BreakoutPaddle extends Box {
    // Box는 이미 Movable, Collidable, Boundable을 구현
    // 패들 특화 기능만 추가
}
```

**필드 (private):**
- `x`, `y`: 위치 (왼쪽 상단)
- `width`, `height`: 크기
- `speed`: 이동 속도
- `color`: 색상
- `targetX`: 마우스 X 좌표 (목표 위치)

**Box 클래스 상속의 장점:**

1. **이미 구현된 기능:**
   - Movable, Collidable, Boundable 인터페이스
   - 직사각형 충돌 감지
   - 경계 처리
   - 기본 이동 로직

2. **패들 특화 기능 추가:**
   - 파워업 효과 관리 (TimedPowerUp 내부 클래스)
   - 공 반사 각도 계산
   - 끈끈한 패들 기능
   - 레이저 발사 기능

**특수 메서드:**
- `setTargetX(double mouseX)`: 마우스 위치 설정
- `expand(double factor)`: 패들 너비 확장
- `shrink(double factor)`: 패들 너비 축소

**구현 힌트:**
```java
// 부드러운 이동
차이 = targetX - (x + width/2)
dx = 차이 * 0.1  // 느린 추적

// 반사 각도 계산
hitPosition = (ball.x - paddle.x) / paddle.width  // 0.0 ~ 1.0
angle = (hitPosition - 0.5) * π/3  // -60° ~ +60°

// 속도 벡터 설정
ball.dx = speed * sin(angle)
ball.dy = -abs(speed * cos(angle))  // 항상 위로
```

### 8.3 벽돌(Brick) 구현

**인터페이스 기반 벽돌 시스템**

다양한 유형의 벽돌을 인터페이스와 상속으로 구현합니다:

**기본 구조:**
- StaticObject 클래스 상속 (이미 Collidable 구현)
- Breakable 인터페이스 구현 (파괴 가능한 벽돌)
- 특수 효과는 추가 인터페이스로 구현

**벽돌 계층 구조:**
```java
// 깨지지 않는 벽돌 (게임 벽)
public class UnbreakableBrick extends StaticObject {
    // 게임 공간의 경계를 정의
    // WallFactory로 상/좌/우 벽 생성
}

// 기본 벽돌
public class SimpleBrick extends StaticObject implements Breakable {
    // 한 번에 깨지는 일반 벽돌
}

// 다중 타격 벽돌
public class MultiHitBrick extends SimpleBrick implements MultiHit {
    // 여러 번 타격해야 깨지는 벽돌
    // DamageState로 시각적 피드백
}

// 폭발 벽돌
public class ExplodingBrick extends SimpleBrick implements Exploding {
    // 파괴 시 주변 벽돌에 피해
    // ExplosionEffect 생성
}

// 파워업 벽돌
public class PowerUpBrick extends SimpleBrick implements PowerUpProvider {
    // 파괴 시 파워업 드롭
}
```

**필드 (private):**
- `x`, `y`, `width`, `height`: 위치와 크기
- `color`: 현재 색상
- `hitPoints`: 남은 타격 수
- `type`: 벽돌 유형 (BrickType)
- `isDestroyed`: 파괴 여부
- `points`: 파괴 시 획듍 점수

**핵심 메서드 설계:**

1. **paint(GraphicsContext gc)**:
   - 파괴된 벽돌은 그리지 않음
   - 3D 효과: 그림자로 입체감 표현
   - 타격 후 손상 표시 (hitPoints < maxHitPoints)

2. **handleCollision(Collidable other)**:
   - Ball과 충돌 시 `hit()` 호출
   - 파괴 시 특수 효과 발동

3. **hit()** (private):
   - hitPoints 1 감소
   - 0이 되면 isDestroyed = true
   - 특수 효과 체크

**특수 효과 메서드:**
- `triggerExplosion()`: 폭발 효과
- `dropPowerUp()`: 파워업 생성

**구현 힌트:**
```java
// 3D 효과
// 1. 어두운 색 그림자: fillRect(x+2, y+2, width, height)
// 2. 원래 색 본체: fillRect(x, y, width, height)

// 손상 표시
if (hitPoints < type.getMaxHitPoints()) {
    // 가로선으로 균열 표현
    gc.strokeLine(x+5, y+height/2, x+width-5, y+height/2);
}
```

### 8.4 파워업 시스템

**Ball 클래스를 활용한 PowerUp 구현**

파워업은 Ball 클래스를 확장하여 떨어지는 아이템으로 구현합니다:

**PowerUpProvider 인터페이스 활용:**
```java
// PowerUpProvider 인터페이스에 정의된 타입 사용
public enum PowerUpType {
    WIDER_PADDLE("W", 10.0),      // 패들 확장
    MULTI_BALL("M", 0),           // 멀티볼
    EXTRA_LIFE("+1", 0),          // 생명 추가
    LASER("L", 10.0),             // 레이저 발사
    SLOW_BALL("S", 15.0),         // 공 감속
    STICKY_PADDLE("G", 10.0);     // 끈끈한 패들
}
```

**PowerUp 클래스 선언:**
```java
public class PowerUp extends Ball {
    // Ball의 원형 충돌 감지와 물리 엔진 재사용
    // 떨어지는 효과만 추가 구현
    private PowerUpType type;
    
    // 생성자에서 수직 낙하 속도 설정
    // Ball의 draw 메서드 오버라이드로 파워업 아이콘 표시
}
```

**필드 (private):**
- `x`, `y`: 현재 위치
- `radius`: 반지름 (20)
- `dy`: 낙하 속도 (100 pixels/s)
- `type`: 파워업 종류 (PowerType)
- `collected`: 수집 여부

**핵심 메서드 설계:**

1. **paint(GraphicsContext gc)**:
   - 원형 배경 그리기
   - 타입별 색상 설정
   - 심볼 텍스트 중앙 정렬

2. **move(double deltaTime)**:
   - y 좌표만 업데이트
   - 일정한 속도로 낙하
   - 화면 밖으로 나가면 제거

3. **handleCollision(Collidable other)**:
   - Paddle과 충돌 시 collected = true

4. **applyEffect(BreakoutGame game)** (abstract):
   - 각 타입별 효과 적용
   - 하위 클래스에서 구현

**효과 구현 힌트:**
```java
// 파워업 생성 (PowerUpProvider 인터페이스 활용)
if (brick instanceof PowerUpProvider) {
    PowerUpProvider provider = (PowerUpProvider) brick;
    if (provider.shouldDropPowerUp()) {
        createPowerUp(brick, provider.getPowerUpType());
    }
}

// 파워업 효과 적용
private void applyPowerUp(PowerUp powerUp) {
    switch (powerUp.getType()) {
        case WIDER_PADDLE:
            paddle.applyPowerUp(PowerUpType.WIDER_PADDLE, duration);
            break;
        case MULTI_BALL:
            createMultiBalls();
            break;
        // ...
    }
}
```

### 8.5 게임 월드 및 상태 관리

**BreakoutWorld 클래스 설계**

2~7장의 개념을 통합하여 게임 월드를 관리:

```java
public class BreakoutWorld {
    // 게임 벽 (UnbreakableBrick)
    private List<UnbreakableBrick> walls;
    
    // 벽돌들 (Breakable 인터페이스)
    private List<Breakable> bricks;
    
    // 공들 (Ball 확장)
    private List<BreakoutBall> balls;
    
    // 패들 (Box 확장)
    private BreakoutPaddle paddle;
    
    // 파워업들 (Ball 확장)
    private List<PowerUp> powerUps;
    
    // 폭발 효과
    private List<ExplosionEffect> explosions;
}
```

**인터페이스 활용:**
- Collidable로 충돌 처리 통합
- Breakable로 다양한 벽돌 타입 관리
- PowerUpProvider로 파워업 생성 제어
- Exploding으로 폭발 효과 처리

**충돌 처리 통합:**
```java
private void handleCollisions() {
    // 공과 벽 충돌 (UnbreakableBrick)
    for (BreakoutBall ball : balls) {
        for (UnbreakableBrick wall : walls) {
            if (ball.collidesWith(wall)) {
                ball.handleCollision(wall);
            }
        }
    }
    
    // 공과 벽돌 충돌 (Breakable 인터페이스)
    for (BreakoutBall ball : balls) {
        for (Breakable brick : bricks) {
            if (brick instanceof Collidable) {
                Collidable collidable = (Collidable) brick;
                if (ball.collidesWith(collidable)) {
                    // 충돌 처리
                    brick.hit(1);
                    if (brick.isBroken()) {
                        handleBrickDestruction(brick);
                    }
                }
            }
        }
    }
}

// 벽돌 파괴 시 특수 효과 처리
private void handleBrickDestruction(Breakable brick) {
    score += brick.getPoints();
    
    // 폭발 효과
    if (brick instanceof Exploding) {
        handleExplosion((Exploding) brick);
    }
    
    // 파워업 드롭
    if (brick instanceof PowerUpProvider) {
        handlePowerUpDrop((PowerUpProvider) brick);
    }
}
```

**주요 필드 (private):**
- `world`: BreakoutWorld (게임 월드)
- `gameState`: GameState (현재 상태)
- `score`: int (현재 점수)
- `lives`: int (남은 생명)
- `level`: int (현재 레벨)
- `scoreLabel`, `livesLabel`: Label (UI 표시)
- `gameLoop`: AnimationTimer

**핵심 메서드 설계:**

1. **start(Stage stage)**:
   - 화면 구성 (Canvas + UI 패널)
   - 이벤트 핸들러 등록
   - 게임 루프 시작
   - 초기 레벨 로드

2. **updateGame(double deltaTime)**:
   ```java
   // 의사 코드
   if (gameState == GameState.PLAYING) {
       world.update(deltaTime);
       checkBallLost();      // 공 떨어짐 확인
       checkPowerUps();      // 파워업 수집
       checkLevelComplete(); // 벽돌 모두 파괴
       updateUI();           // 점수, 생명 표시
   }
   ```

3. **initializeLevel(int levelNumber)**:
   - 벽돌 행/열 계산
   - 각 위치에 벽돌 생성
   - 타입은 selectBrickType() 호출
   - 월드에 추가

4. **selectBrickType(int row, int col, int level)**:
   - 확률 기반 선택
   - 위쪽 행: 더 단단한 벽돌
   - 레벨 상승: 난이도 증가

**이벤트 처리 메서드:**
- `handleMouseMove(MouseEvent e)`: 패들 이동
- `handleMouseClick(MouseEvent e)`: 공 발사
- `handleKeyPress(KeyEvent e)`: 일시정지, 메뉴

**게임 상태 관리:**
```java
// BreakoutWorld에서 게임 상태 처리
private void checkGameState() {
    // 모든 벽돌이 깨진 경우
    if (bricks.isEmpty()) {
        level++;
        createLevel(level);
        initializeBall();
    }
    
    // 모든 공을 놓친 경우
    if (balls.isEmpty()) {
        lives--;
        if (lives > 0) {
            initializeBall();
        }
    }
}

// 벽돌 생성 시 인터페이스 활용
private Breakable createBrickForLevel(double x, double y, int row, int col, int level) {
    if (level >= 3 && (row + col) % 7 == 0) {
        // Exploding 인터페이스 구현
        return new ExplodingBrick(x, y, width, height, color, points);
    } else if (level >= 2 && row < 2) {
        // MultiHit 인터페이스 구현
        return new MultiHitBrick(x, y, width, height, color, points, hitCount);
    } else if ((row + col) % 5 == 0) {
        // PowerUpProvider 인터페이스 구현
        return new PowerUpBrick(x, y, width, height, color, points, chance);
    } else {
        // Breakable 인터페이스만 구현
        return new SimpleBrick(x, y, width, height, color, points);
    }
}
```

### 8.6 레벨 디자인과 진행

**LevelManager 클래스 설계**

레벨 설정과 진행을 관리하는 클래스:

**LevelConfig 내부 클래스:**
```java
public static class LevelConfig {
    private String name;
    private int rows, cols;
    private double ballSpeedMultiplier;
    private Map<BrickType, Double> brickProbabilities;
    // getter/setter
}
```

**LevelManager 필드:**
- `levels`: List<LevelConfig>
- `currentLevel`: int

**레벨 디자인 예시:**

1. **Level 1 - "시작"**:
   ```java
   rows: 5, cols: 10
   속도: 1.0x
   벽돌: NORMAL(0.9), POWERUP(0.1)
   ```

2. **Level 2 - "도전"**:
   ```java
   rows: 6, cols: 10
   속도: 1.2x
   벽돌: NORMAL(0.6), HARD(0.3), POWERUP(0.1)
   ```

3. **Level 3 - "폭발"**:
   ```java
   rows: 7, cols: 11
   속도: 1.3x
   벽돌: NORMAL(0.5), HARD(0.2), EXPLOSIVE(0.2), POWERUP(0.1)
   ```

**레벨 생성 메서드:**

```java
public LevelConfig createLevel(int levelNumber) {
    // 레벨 번호에 따른 설정 생성
}

public BrickType selectBrickType(Map<BrickType, Double> probabilities) {
    // 확률 기반 타입 선택
}
```

**레벨 진행 시스템:**
1. 모든 벽돌 파괴 시 레벨 완료
2. 다음 레벨로 진행
3. 공 속도 증가
4. 벽돌 배치 복잡도 증가
5. 특수 벽돌 비율 증가

**구현 힌트:**
```java
// 확률 기반 선택 알고리즘
double random = Math.random();
double cumulative = 0;
for (Map.Entry<BrickType, Double> entry : probabilities.entrySet()) {
    cumulative += entry.getValue();
    if (random < cumulative) {
        return entry.getKey();
    }
}
```

**추가 레벨 아이디어:**
- Level 4: 금속 벽돌 추가
- Level 5: 움직이는 벽돌
- Level 6: 보스 전투
- Level 7+: 패턴 기반 배치

## 연습 문제

### 연습 8-1: BreakoutPaddle 클래스 구현

Box 클래스를 확장하여 플레이어가 조작하는 패들을 구현합니다.

#### Step 1: BreakoutPaddle 클래스 작성

`BreakoutPaddle.java` 파일을 생성하고, Box를 상속받아 패들 특화 기능을 구현하세요.

**구현 체크리스트:**
- [ ] `public class BreakoutPaddle extends Box` 클래스 선언
- [ ] `targetX`, `speed` 필드 추가
- [ ] 위치, 크기, 색상, 속도 초기화하는 생성자 구현
- [ ] `setTargetX()` 메서드로 마우스 X 좌표 설정
- [ ] `move()` 메서드 오버라이드하여 부드러운 이동 구현
- [ ] `calculateReflectionAngle()` 메서드로 공 반사 각도 계산
- [ ] `clampPosition()` 메서드로 화면 경계 내 위치 제한

**부드러운 이동 구현:**
```java
@Override
public void move(double deltaTime) {
    double centerX = getX() + getWidth() / 2;
    double diff = targetX - centerX;
    double dx = diff * 0.1;  // 부드러운 추적

    setX(getX() + dx);
    clampPosition();
}
```

**반사 각도 계산:**
```java
public double calculateReflectionAngle(Ball ball) {
    double hitPosition = (ball.getX() - getX()) / getWidth();  // 0.0 ~ 1.0
    double angle = (hitPosition - 0.5) * Math.PI / 3;  // -60° ~ +60°
    return angle;
}
```

#### Step 2: 단위 테스트 작성

| 테스트 | 검증 내용 |
|--------|----------|
| testPaddleCreation | 초기 위치, 크기, 색상 확인 |
| testSetTargetX | targetX 설정 확인 |
| testSmoothMovement | 목표 위치로 부드럽게 이동 |
| testClampPosition | 경계 밖으로 이동 불가 확인 |
| testReflectionAngle | 충돌 위치에 따른 반사 각도 |

```java
@Test
void testReflectionAngle() {
    BreakoutPaddle paddle = new BreakoutPaddle(350, 550, 100, 20, Color.BLUE, 5);

    // 중앙 충돌: 0°
    Ball centerBall = new Ball(400, 540, 10, Color.WHITE);
    assertEquals(0, paddle.calculateReflectionAngle(centerBall), 0.1);

    // 왼쪽 끝 충돌: -60°
    Ball leftBall = new Ball(350, 540, 10, Color.WHITE);
    assertTrue(paddle.calculateReflectionAngle(leftBall) < 0);

    // 오른쪽 끝 충돌: +60°
    Ball rightBall = new Ball(450, 540, 10, Color.WHITE);
    assertTrue(paddle.calculateReflectionAngle(rightBall) > 0);
}
```

#### Step 3: JavaFX 애플리케이션에서 확인

마우스로 패들을 조작하고 공이 반사되는지 확인하세요.

```java
public class PaddleDemo extends Application {
    private BreakoutPaddle paddle;
    private Ball ball;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        paddle = new BreakoutPaddle(350, 550, 100, 20, Color.BLUE, 5);
        ball = new Ball(400, 300, 10, Color.WHITE);
        ball.setVelocity(new Vector2D(200, 200));

        canvas.setOnMouseMoved(e -> paddle.setTargetX(e.getX()));

        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) { lastTime = now; return; }
                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                paddle.move(deltaTime);
                ball.move(deltaTime);

                // 패들과 충돌 시 반사
                if (ball.collidesWith(paddle)) {
                    double angle = paddle.calculateReflectionAngle(ball);
                    double speed = ball.getVelocity().magnitude();
                    ball.setVelocity(new Vector2D(
                        speed * Math.sin(angle),
                        -Math.abs(speed * Math.cos(angle))
                    ));
                }

                draw(gc);
            }
        };
        timer.start();

        stage.setScene(new Scene(new Pane(canvas)));
        stage.show();
    }
}
```

---

### 연습 8-2: Brick 계층 구조 구현

인터페이스 기반으로 다양한 유형의 벽돌을 구현합니다.

#### Step 1: 인터페이스와 벽돌 클래스 작성

**인터페이스 정의:**

```java
// 파괴 가능한 객체
public interface Breakable {
    void hit(int damage);
    boolean isBroken();
    int getPoints();
}

// 다중 타격 객체
public interface MultiHit {
    int getHitPoints();
    int getMaxHitPoints();
}

// 폭발 효과
public interface Exploding {
    double getExplosionRadius();
    int getExplosionDamage();
}

// 파워업 제공
public interface PowerUpProvider {
    boolean shouldDropPowerUp();
    PowerUpType getPowerUpType();
}
```

**구현 체크리스트:**
- [ ] `SimpleBrick` 클래스 (StaticObject 상속, Breakable 구현)
- [ ] `MultiHitBrick` 클래스 (SimpleBrick 상속, MultiHit 구현)
- [ ] `ExplodingBrick` 클래스 (SimpleBrick 상속, Exploding 구현)
- [ ] `PowerUpBrick` 클래스 (SimpleBrick 상속, PowerUpProvider 구현)
- [ ] `UnbreakableBrick` 클래스 (StaticObject 상속, Breakable 미구현)

**SimpleBrick 구현:**
```java
public class SimpleBrick extends StaticObject implements Breakable {
    private boolean destroyed = false;
    private int points;

    @Override
    public void hit(int damage) {
        destroyed = true;
    }

    @Override
    public boolean isBroken() {
        return destroyed;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public void paint(GraphicsContext gc) {
        if (!destroyed) {
            // 3D 효과: 그림자
            gc.setFill(color.darker());
            gc.fillRect(x + 2, y + 2, width, height);
            // 본체
            gc.setFill(color);
            gc.fillRect(x, y, width, height);
        }
    }
}
```

**MultiHitBrick 구현:**
```java
public class MultiHitBrick extends SimpleBrick implements MultiHit {
    private int hitPoints;
    private int maxHitPoints;

    @Override
    public void hit(int damage) {
        hitPoints -= damage;
        if (hitPoints <= 0) {
            super.hit(damage);
        }
    }

    @Override
    public void paint(GraphicsContext gc) {
        super.paint(gc);
        if (!isBroken() && hitPoints < maxHitPoints) {
            // 균열 표시
            gc.setStroke(Color.BLACK);
            gc.strokeLine(x + 5, y + height/2, x + width - 5, y + height/2);
        }
    }
}
```

#### Step 2: 단위 테스트 작성

| 테스트 | 검증 내용 |
|--------|----------|
| testSimpleBrickDestruction | 한 번 타격으로 파괴 |
| testMultiHitBrick | 여러 번 타격 후 파괴 |
| testExplodingBrickRadius | 폭발 범위 확인 |
| testPowerUpBrickDrop | 파워업 드롭 확인 |
| testUnbreakableBrick | 타격해도 파괴 안됨 |

```java
@Test
void testMultiHitBrick() {
    MultiHitBrick brick = new MultiHitBrick(100, 100, 70, 20, Color.RED, 30, 3);

    assertEquals(3, brick.getHitPoints());
    assertFalse(brick.isBroken());

    brick.hit(1);
    assertEquals(2, brick.getHitPoints());
    assertFalse(brick.isBroken());

    brick.hit(1);
    brick.hit(1);
    assertTrue(brick.isBroken());
    assertEquals(30, brick.getPoints());
}

@Test
void testPowerUpBrickDrop() {
    PowerUpBrick brick = new PowerUpBrick(100, 100, 70, 20, Color.GREEN, 10, 1.0);

    assertTrue(brick.shouldDropPowerUp());
    assertNotNull(brick.getPowerUpType());
}
```

---

### 연습 8-3: PowerUp 클래스 구현

Ball 클래스를 확장하여 떨어지는 파워업 아이템을 구현합니다.

#### Step 1: PowerUp 클래스 작성

`PowerUp.java` 파일을 생성하고, Ball을 상속받아 떨어지는 파워업 아이템을 구현하세요.

**구현 체크리스트:**
- [ ] `public class PowerUp extends Ball` 클래스 선언
- [ ] `PowerUpType` enum으로 파워업 종류 정의
- [ ] `type`, `collected` 필드 선언
- [ ] 위치, 타입으로 초기화하고 낙하 속도 설정하는 생성자 구현
- [ ] `paint()` 메서드 오버라이드하여 타입별 색상과 심볼 표시
- [ ] `move()` 메서드 오버라이드하여 수직 낙하만 구현
- [ ] `applyEffect()` 메서드로 효과 적용 (추상 또는 switch)

**PowerUpType enum:**
```java
public enum PowerUpType {
    WIDER_PADDLE("W", Color.BLUE, 10.0),
    MULTI_BALL("M", Color.YELLOW, 0),
    EXTRA_LIFE("+1", Color.GREEN, 0),
    LASER("L", Color.RED, 10.0),
    SLOW_BALL("S", Color.CYAN, 15.0),
    STICKY_PADDLE("G", Color.PURPLE, 10.0);

    private final String symbol;
    private final Color color;
    private final double duration;

    // constructor, getters
}
```

**PowerUp 클래스:**
```java
public class PowerUp extends Ball {
    private PowerUpType type;
    private boolean collected = false;
    private static final double FALL_SPEED = 100;

    public PowerUp(double x, double y, PowerUpType type) {
        super(x, y, 15, type.getColor());
        this.type = type;
        setVelocity(new Vector2D(0, FALL_SPEED));
    }

    @Override
    public void paint(GraphicsContext gc) {
        if (collected) return;

        // 배경 원
        gc.setFill(type.getColor());
        gc.fillOval(getX() - getRadius(), getY() - getRadius(),
                    getRadius() * 2, getRadius() * 2);

        // 심볼
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(14));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(type.getSymbol(), getX(), getY() + 5);
    }

    public void applyEffect(BreakoutGame game) {
        collected = true;
        switch (type) {
            case WIDER_PADDLE:
                game.getPaddle().expand(1.5, type.getDuration());
                break;
            case MULTI_BALL:
                game.createMultiBalls();
                break;
            case EXTRA_LIFE:
                game.addLife();
                break;
            // ...
        }
    }
}
```

#### Step 2: 단위 테스트 작성

| 테스트 | 검증 내용 |
|--------|----------|
| testPowerUpCreation | 타입별 생성 확인 |
| testFallMovement | 수직 낙하만 확인 |
| testCollectOnPaddleCollision | 패들 충돌 시 collected = true |
| testWiderPaddleEffect | 패들 너비 확장 효과 |
| testExtraLifeEffect | 생명 추가 효과 |

```java
@Test
void testPowerUpFall() {
    PowerUp powerUp = new PowerUp(100, 100, PowerUpType.WIDER_PADDLE);

    assertEquals(0, powerUp.getVelocity().getX(), 0.01);
    assertEquals(100, powerUp.getVelocity().getY(), 0.01);

    powerUp.move(1.0);

    assertEquals(100, powerUp.getX(), 0.01);
    assertEquals(200, powerUp.getY(), 0.01);
}

@Test
void testWiderPaddleEffect() {
    BreakoutGame game = new BreakoutGame();
    double originalWidth = game.getPaddle().getWidth();

    PowerUp powerUp = new PowerUp(0, 0, PowerUpType.WIDER_PADDLE);
    powerUp.applyEffect(game);

    assertEquals(originalWidth * 1.5, game.getPaddle().getWidth(), 0.01);
}
```

#### Step 3: JavaFX 애플리케이션에서 확인

```java
public class PowerUpDemo extends Application {
    private List<PowerUp> powerUps = new ArrayList<>();
    private BreakoutPaddle paddle;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        paddle = new BreakoutPaddle(350, 550, 100, 20, Color.BLUE, 5);

        // 다양한 파워업 생성
        powerUps.add(new PowerUp(100, 0, PowerUpType.WIDER_PADDLE));
        powerUps.add(new PowerUp(300, 0, PowerUpType.MULTI_BALL));
        powerUps.add(new PowerUp(500, 0, PowerUpType.EXTRA_LIFE));
        powerUps.add(new PowerUp(700, 0, PowerUpType.SLOW_BALL));

        canvas.setOnMouseMoved(e -> paddle.setTargetX(e.getX()));

        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) { lastTime = now; return; }
                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                paddle.move(deltaTime);

                for (PowerUp p : powerUps) {
                    p.move(deltaTime);
                    if (p.collidesWith(paddle) && !p.isCollected()) {
                        p.collect();
                        System.out.println("Collected: " + p.getType());
                    }
                }

                draw(gc);
            }
        };
        timer.start();

        stage.setScene(new Scene(new Pane(canvas)));
        stage.show();
    }
}
```

---

### 연습 8-4: BreakoutWorld 클래스 구현

2~7장의 개념을 통합하여 게임 월드를 관리하는 클래스를 구현합니다.

#### Step 1: BreakoutWorld 클래스 작성

`BreakoutWorld.java` 파일을 생성하고, 게임 월드를 관리하는 클래스를 구현하세요.

**구현 체크리스트:**
- [ ] `walls`, `bricks`, `balls`, `paddle`, `powerUps`, `explosions` 필드 선언
- [ ] 컬렉션을 초기화하는 생성자 구현
- [ ] `createWalls()` 메서드로 UnbreakableBrick으로 벽 생성
- [ ] `createBricks()` 메서드로 레벨에 따른 벽돌 배치
- [ ] `update()` 메서드로 모든 객체 업데이트
- [ ] `handleCollisions()` 메서드로 충돌 처리 통합
- [ ] `handleBrickDestruction()` 메서드로 파괴 시 특수 효과 처리
- [ ] `paint()` 메서드로 모든 객체 그리기

**BreakoutWorld 클래스:**
```java
public class BreakoutWorld {
    private List<UnbreakableBrick> walls;
    private List<Breakable> bricks;
    private List<Ball> balls;
    private BreakoutPaddle paddle;
    private List<PowerUp> powerUps;
    private List<ExplosionEffect> explosions;

    private int score = 0;

    public void update(double deltaTime) {
        // 패들 업데이트
        paddle.move(deltaTime);

        // 공들 업데이트
        for (Ball ball : balls) {
            ball.move(deltaTime);
        }

        // 파워업들 업데이트
        for (PowerUp powerUp : powerUps) {
            powerUp.move(deltaTime);
        }

        // 폭발 효과 업데이트
        explosions.removeIf(e -> e.isFinished());

        // 충돌 처리
        handleCollisions();

        // 파괴된 벽돌 제거
        bricks.removeIf(Breakable::isBroken);

        // 수집된 파워업 제거
        powerUps.removeIf(PowerUp::isCollected);
    }

    private void handleCollisions() {
        for (Ball ball : balls) {
            // 벽과 충돌
            for (UnbreakableBrick wall : walls) {
                if (ball.collidesWith(wall)) {
                    ball.handleCollision(wall);
                }
            }

            // 패들과 충돌
            if (ball.collidesWith(paddle)) {
                double angle = paddle.calculateReflectionAngle(ball);
                // 반사 처리
            }

            // 벽돌과 충돌
            for (Breakable brick : bricks) {
                if (brick instanceof Collidable) {
                    Collidable collidable = (Collidable) brick;
                    if (ball.collidesWith(collidable)) {
                        ball.handleCollision(collidable);
                        brick.hit(1);
                        if (brick.isBroken()) {
                            handleBrickDestruction(brick);
                        }
                    }
                }
            }
        }

        // 파워업과 패들 충돌
        for (PowerUp powerUp : powerUps) {
            if (powerUp.collidesWith(paddle)) {
                powerUp.collect();
                // 효과 적용
            }
        }
    }

    private void handleBrickDestruction(Breakable brick) {
        score += brick.getPoints();

        if (brick instanceof Exploding) {
            Exploding exploding = (Exploding) brick;
            createExplosion(exploding);
        }

        if (brick instanceof PowerUpProvider) {
            PowerUpProvider provider = (PowerUpProvider) brick;
            if (provider.shouldDropPowerUp()) {
                createPowerUp(brick, provider.getPowerUpType());
            }
        }
    }
}
```

#### Step 2: 단위 테스트 작성

| 테스트 | 검증 내용 |
|--------|----------|
| testWallCreation | 3면 벽 생성 확인 |
| testBrickCreation | 레벨별 벽돌 배치 확인 |
| testBallWallCollision | 벽에서 공 반사 |
| testBallBrickCollision | 벽돌 파괴 및 점수 |
| testExplosionChainReaction | 폭발 연쇄 반응 |
| testPowerUpDropOnDestruction | 파괴 시 파워업 드롭 |

```java
@Test
void testBallBrickCollision() {
    BreakoutWorld world = new BreakoutWorld(800, 600);
    world.createLevel(1);

    int initialBrickCount = world.getBricks().size();
    int initialScore = world.getScore();

    // 공을 벽돌 위치로 이동
    Ball ball = world.getBalls().get(0);
    Breakable brick = world.getBricks().get(0);
    ball.setPosition(((Collidable)brick).getX(), ((Collidable)brick).getY() + 30);
    ball.setVelocity(new Vector2D(0, -200));

    // 시뮬레이션
    for (int i = 0; i < 100; i++) {
        world.update(0.016);
    }

    assertTrue(world.getBricks().size() < initialBrickCount);
    assertTrue(world.getScore() > initialScore);
}
```

#### Step 3: JavaFX 애플리케이션에서 확인

```java
public class BreakoutWorldDemo extends Application {
    private BreakoutWorld world;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        world = new BreakoutWorld(800, 600);
        world.createWalls();
        world.createLevel(1);
        world.createPaddle(350, 550);
        world.createBall(400, 400);

        canvas.setOnMouseMoved(e -> world.getPaddle().setTargetX(e.getX()));
        canvas.setOnMouseClicked(e -> world.launchBall());

        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) { lastTime = now; return; }
                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                world.update(deltaTime);

                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, 800, 600);
                world.paint(gc);

                gc.setFill(Color.WHITE);
                gc.fillText("Score: " + world.getScore(), 10, 20);
            }
        };
        timer.start();

        stage.setScene(new Scene(new Pane(canvas)));
        stage.show();
    }
}
```

---

### 연습 8-5: LevelManager 클래스 구현

레벨 설정과 진행을 관리하는 클래스를 구현합니다.

#### Step 1: LevelManager 클래스 작성

`LevelManager.java` 파일을 생성하고, 레벨 설정과 진행을 관리하는 클래스를 구현하세요.

**구현 체크리스트:**
- [ ] `LevelConfig` 내부 클래스로 레벨 설정 정보 정의
- [ ] `levels`, `currentLevel` 필드 선언
- [ ] `initializeLevels()` 메서드로 기본 레벨들 생성
- [ ] `getLevelConfig()` 메서드로 레벨 번호로 설정 반환
- [ ] `selectBrickType()` 메서드로 확률 기반 타입 선택
- [ ] `nextLevel()` 메서드로 다음 레벨로 진행

**LevelConfig 클래스:**
```java
public static class LevelConfig {
    private String name;
    private int rows;
    private int cols;
    private double ballSpeedMultiplier;
    private Map<Class<? extends Breakable>, Double> brickProbabilities;

    public LevelConfig(String name, int rows, int cols, double speedMult) {
        this.name = name;
        this.rows = rows;
        this.cols = cols;
        this.ballSpeedMultiplier = speedMult;
        this.brickProbabilities = new HashMap<>();
    }

    public void addBrickProbability(Class<? extends Breakable> type, double prob) {
        brickProbabilities.put(type, prob);
    }

    // getters
}
```

**LevelManager 클래스:**
```java
public class LevelManager {
    private List<LevelConfig> levels = new ArrayList<>();
    private int currentLevel = 0;

    public LevelManager() {
        initializeLevels();
    }

    private void initializeLevels() {
        // Level 1 - "시작"
        LevelConfig level1 = new LevelConfig("시작", 5, 10, 1.0);
        level1.addBrickProbability(SimpleBrick.class, 0.9);
        level1.addBrickProbability(PowerUpBrick.class, 0.1);
        levels.add(level1);

        // Level 2 - "도전"
        LevelConfig level2 = new LevelConfig("도전", 6, 10, 1.2);
        level2.addBrickProbability(SimpleBrick.class, 0.6);
        level2.addBrickProbability(MultiHitBrick.class, 0.3);
        level2.addBrickProbability(PowerUpBrick.class, 0.1);
        levels.add(level2);

        // Level 3 - "폭발"
        LevelConfig level3 = new LevelConfig("폭발", 7, 11, 1.3);
        level3.addBrickProbability(SimpleBrick.class, 0.5);
        level3.addBrickProbability(MultiHitBrick.class, 0.2);
        level3.addBrickProbability(ExplodingBrick.class, 0.2);
        level3.addBrickProbability(PowerUpBrick.class, 0.1);
        levels.add(level3);
    }

    public Class<? extends Breakable> selectBrickType(LevelConfig config) {
        double random = Math.random();
        double cumulative = 0;

        for (Map.Entry<Class<? extends Breakable>, Double> entry :
             config.getBrickProbabilities().entrySet()) {
            cumulative += entry.getValue();
            if (random < cumulative) {
                return entry.getKey();
            }
        }

        return SimpleBrick.class;
    }
}
```

#### Step 2: 단위 테스트 작성

| 테스트 | 검증 내용 |
|--------|----------|
| testLevelConfigCreation | 레벨 설정 생성 확인 |
| testBrickProbabilities | 확률 합이 1.0 |
| testSelectBrickType | 확률 기반 선택 분포 |
| testLevelProgression | 레벨 진행 확인 |
| testDifficultyIncrease | 레벨별 난이도 증가 |

```java
@Test
void testBrickTypeDistribution() {
    LevelManager manager = new LevelManager();
    LevelConfig config = manager.getLevelConfig(0);

    Map<Class<?>, Integer> counts = new HashMap<>();
    int iterations = 10000;

    for (int i = 0; i < iterations; i++) {
        Class<?> type = manager.selectBrickType(config);
        counts.merge(type, 1, Integer::sum);
    }

    // SimpleBrick 약 90%, PowerUpBrick 약 10%
    double simpleRatio = counts.getOrDefault(SimpleBrick.class, 0) / (double) iterations;
    double powerUpRatio = counts.getOrDefault(PowerUpBrick.class, 0) / (double) iterations;

    assertEquals(0.9, simpleRatio, 0.05);
    assertEquals(0.1, powerUpRatio, 0.05);
}
```

---

### 연습 8-6: 완전한 BreakoutGame 구현

모든 구성 요소를 통합하여 완전한 벽돌 깨기 게임을 구현합니다.

#### Step 1: BreakoutGame 클래스 작성

`BreakoutGame.java` 파일을 생성하고, 모든 구성 요소를 통합한 완전한 게임을 구현하세요.

**구현 체크리스트:**
- [ ] `world`, `levelManager`, `gameState`, `score`, `lives` 필드 선언
- [ ] `GameState` enum 정의 (MENU, PLAYING, PAUSED, GAME_OVER, LEVEL_COMPLETE)
- [ ] `start()` 메서드로 JavaFX Application 시작
- [ ] `initializeGame()` 메서드로 초기 상태 설정
- [ ] `updateGame()` 메서드로 게임 루프 처리
- [ ] `handleInput()` 메서드로 마우스/키보드 입력 처리
- [ ] `checkGameConditions()` 메서드로 승리/패배 조건 확인
- [ ] `renderUI()` 메서드로 점수, 생명, 상태 표시

**GameState enum:**
```java
public enum GameState {
    MENU,
    PLAYING,
    PAUSED,
    GAME_OVER,
    LEVEL_COMPLETE
}
```

**BreakoutGame 클래스:**
```java
public class BreakoutGame extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int INITIAL_LIVES = 3;

    private BreakoutWorld world;
    private LevelManager levelManager;
    private GameState gameState = GameState.MENU;
    private int score = 0;
    private int lives = INITIAL_LIVES;
    private int level = 1;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        levelManager = new LevelManager();
        initializeGame();

        // 이벤트 핸들러
        canvas.setOnMouseMoved(this::handleMouseMove);
        canvas.setOnMouseClicked(this::handleMouseClick);

        Scene scene = new Scene(new Pane(canvas));
        scene.setOnKeyPressed(this::handleKeyPress);

        AnimationTimer gameLoop = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) { lastTime = now; return; }
                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                updateGame(deltaTime);
                render(gc);
            }
        };
        gameLoop.start();

        stage.setTitle("Breakout Game");
        stage.setScene(scene);
        stage.show();
    }

    private void initializeGame() {
        world = new BreakoutWorld(WIDTH, HEIGHT);
        world.createWalls();
        loadLevel(level);
        lives = INITIAL_LIVES;
        score = 0;
        gameState = GameState.MENU;
    }

    private void loadLevel(int levelNumber) {
        LevelConfig config = levelManager.getLevelConfig(levelNumber - 1);
        world.createLevel(config);
        world.createPaddle(WIDTH / 2 - 50, HEIGHT - 50);
        world.createBall(WIDTH / 2, HEIGHT - 100);
    }

    private void updateGame(double deltaTime) {
        if (gameState != GameState.PLAYING) return;

        world.update(deltaTime);
        score = world.getScore();

        checkGameConditions();
    }

    private void checkGameConditions() {
        // 공을 놓친 경우
        if (world.getBalls().isEmpty()) {
            lives--;
            if (lives <= 0) {
                gameState = GameState.GAME_OVER;
            } else {
                world.createBall(WIDTH / 2, HEIGHT - 100);
            }
        }

        // 모든 벽돌 파괴
        if (world.getBricks().isEmpty()) {
            level++;
            if (level > levelManager.getLevelCount()) {
                gameState = GameState.GAME_OVER;  // 승리
            } else {
                gameState = GameState.LEVEL_COMPLETE;
            }
        }
    }

    private void handleKeyPress(KeyEvent e) {
        switch (e.getCode()) {
            case SPACE:
                if (gameState == GameState.MENU) {
                    gameState = GameState.PLAYING;
                } else if (gameState == GameState.LEVEL_COMPLETE) {
                    loadLevel(level);
                    gameState = GameState.PLAYING;
                }
                break;
            case P:
                if (gameState == GameState.PLAYING) {
                    gameState = GameState.PAUSED;
                } else if (gameState == GameState.PAUSED) {
                    gameState = GameState.PLAYING;
                }
                break;
            case R:
                if (gameState == GameState.GAME_OVER) {
                    level = 1;
                    initializeGame();
                }
                break;
        }
    }

    private void render(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        world.paint(gc);
        renderUI(gc);
    }

    private void renderUI(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(16));
        gc.fillText("Score: " + score, 10, 20);
        gc.fillText("Lives: " + lives, WIDTH - 80, 20);
        gc.fillText("Level: " + level, WIDTH / 2 - 30, 20);

        if (gameState == GameState.MENU) {
            gc.setFont(Font.font(32));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText("BREAKOUT", WIDTH / 2, HEIGHT / 2 - 50);
            gc.setFont(Font.font(18));
            gc.fillText("Press SPACE to Start", WIDTH / 2, HEIGHT / 2 + 20);
        } else if (gameState == GameState.PAUSED) {
            gc.setFont(Font.font(32));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText("PAUSED", WIDTH / 2, HEIGHT / 2);
        } else if (gameState == GameState.GAME_OVER) {
            gc.setFont(Font.font(32));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText("GAME OVER", WIDTH / 2, HEIGHT / 2 - 20);
            gc.setFont(Font.font(18));
            gc.fillText("Final Score: " + score, WIDTH / 2, HEIGHT / 2 + 20);
            gc.fillText("Press R to Restart", WIDTH / 2, HEIGHT / 2 + 50);
        }
    }
}
```

#### Step 2: 통합 테스트 작성

| 테스트 | 검증 내용 |
|--------|----------|
| testGameStateTransitions | 상태 전환 확인 |
| testLoseLife | 공 놓침 시 생명 감소 |
| testGameOver | 생명 0이면 게임 오버 |
| testLevelComplete | 모든 벽돌 파괴 시 레벨 완료 |
| testScoreAccumulation | 점수 누적 확인 |

```java
@Test
void testGameStateTransitions() {
    BreakoutGame game = new BreakoutGame();

    assertEquals(GameState.MENU, game.getGameState());

    game.startGame();
    assertEquals(GameState.PLAYING, game.getGameState());

    game.pauseGame();
    assertEquals(GameState.PAUSED, game.getGameState());

    game.resumeGame();
    assertEquals(GameState.PLAYING, game.getGameState());
}

@Test
void testLoseLife() {
    BreakoutGame game = new BreakoutGame();
    game.startGame();

    assertEquals(3, game.getLives());

    game.loseLife();
    assertEquals(2, game.getLives());
    assertEquals(GameState.PLAYING, game.getGameState());

    game.loseLife();
    game.loseLife();
    assertEquals(0, game.getLives());
    assertEquals(GameState.GAME_OVER, game.getGameState());
}
```

#### Step 3: 완전한 게임 실행

```java
public class Main {
    public static void main(String[] args) {
        Application.launch(BreakoutGame.class, args);
    }
}
```

**게임 조작법:**
- 마우스: 패들 이동
- 클릭: 공 발사
- SPACE: 게임 시작 / 다음 레벨
- P: 일시정지
- R: 재시작 (게임 오버 시)

---

### 연습 8-7: 고급 기능 추가 (선택)

게임을 더 재미있게 만드는 추가 기능을 구현합니다.

#### Step 1: 추가 기능 선택 및 구현

다음 기능 중 2개 이상을 선택하여 구현하세요.

**A. 콤보 시스템:**
```java
public class ComboSystem {
    private int comboCount = 0;
    private long lastHitTime = 0;
    private static final long COMBO_TIMEOUT = 2000; // 2초

    public int addHit() {
        long now = System.currentTimeMillis();
        if (now - lastHitTime > COMBO_TIMEOUT) {
            comboCount = 0;
        }
        comboCount++;
        lastHitTime = now;
        return comboCount;
    }

    public int getMultiplier() {
        if (comboCount >= 10) return 4;
        if (comboCount >= 5) return 2;
        return 1;
    }
}
```

**B. 특수 공:**
```java
public class FireBall extends Ball {
    private boolean piercing = true;  // 관통

    @Override
    public void handleCollision(Collidable other) {
        if (other instanceof Breakable) {
            ((Breakable) other).hit(2);  // 2배 데미지
            if (!piercing) {
                super.handleCollision(other);
            }
        } else {
            super.handleCollision(other);
        }
    }
}
```

**C. 보스 벽돌:**
```java
public class BossBrick extends MultiHitBrick implements PowerUpProvider {
    private int phase = 1;
    private List<Ball> projectiles = new ArrayList<>();

    @Override
    public void hit(int damage) {
        super.hit(damage);
        if (getHitPoints() < getMaxHitPoints() / 2 && phase == 1) {
            phase = 2;
            // 패턴 변경
        }
    }

    public void attack(BreakoutWorld world) {
        if (Math.random() < 0.01) {
            // 탄막 발사
            projectiles.add(new Ball(getX() + getWidth()/2, getY() + getHeight(), 5, Color.RED));
        }
    }
}
```

**D. 리더보드:**
```java
public class Leaderboard {
    private static final String FILE = "highscores.txt";
    private List<ScoreEntry> scores = new ArrayList<>();

    public void addScore(String name, int score) {
        scores.add(new ScoreEntry(name, score, LocalDateTime.now()));
        scores.sort((a, b) -> b.score - a.score);
        if (scores.size() > 10) {
            scores = scores.subList(0, 10);
        }
        save();
    }

    public List<ScoreEntry> getTopScores() {
        return Collections.unmodifiableList(scores);
    }
}
```

#### Step 2: 단위 테스트 작성

선택한 기능에 대한 테스트를 작성하세요.

```java
@Test
void testComboMultiplier() {
    ComboSystem combo = new ComboSystem();

    assertEquals(1, combo.getMultiplier());

    for (int i = 0; i < 5; i++) combo.addHit();
    assertEquals(2, combo.getMultiplier());

    for (int i = 0; i < 5; i++) combo.addHit();
    assertEquals(4, combo.getMultiplier());
}

@Test
void testFireBallPiercing() {
    FireBall fireBall = new FireBall(100, 100, 10, Color.ORANGE);
    SimpleBrick brick1 = new SimpleBrick(100, 80, 70, 20, Color.RED, 10);
    SimpleBrick brick2 = new SimpleBrick(100, 60, 70, 20, Color.RED, 10);

    fireBall.handleCollision(brick1);
    fireBall.handleCollision(brick2);

    // 관통하므로 두 벽돌 모두 데미지
    assertTrue(brick1.isBroken());
    assertTrue(brick2.isBroken());
}
```

#### Step 3: 통합 및 플레이 테스트

추가한 기능이 게임에 잘 통합되었는지 직접 플레이하며 확인하세요.

**체크리스트:**
- [ ] 콤보 시스템: 연속 타격 시 배율 증가 동작 확인
- [ ] 특수 공: 파워업 획득 시 변환 동작 확인
- [ ] 보스 벽돌: 여러 페이즈 동작 확인
- [ ] 리더보드: 점수 저장/불러오기 동작 확인

## JUnit 테스트 예제

```java
public class BreakoutTest {
    
    @Test
    public void testPaddleBallCollision() {
        Paddle paddle = new Paddle(400, 550);
        Ball ball = new Ball(450, 540, 10);
        ball.setDy(100); // 아래로 이동
        
        assertTrue(paddle.isColliding(ball));
        
        paddle.handleCollision(ball);
        
        // 공이 위로 반사됨
        assertTrue(ball.getDy() < 0);
    }
    
    @Test
    public void testBrickDestruction() {
        Brick brick = new Brick(100, 100, 70, 20, BrickType.NORMAL);
        Ball ball = new Ball(135, 110, 10);
        
        assertFalse(brick.isDestroyed());
        
        brick.handleCollision(ball);
        
        assertTrue(brick.isDestroyed());
        assertEquals(10, brick.getPoints());
    }
    
    @Test
    public void testPowerUpEffect() {
        BreakoutGame game = new BreakoutGame();
        Paddle paddle = game.getPaddle();
        double originalWidth = paddle.getWidth();
        
        PowerUp powerUp = new PowerUp(0, 0, PowerType.EXPAND_PADDLE);
        powerUp.applyEffect(game);
        
        assertEquals(originalWidth * 1.5, paddle.getWidth(), 0.001);
    }
    
    @Test
    public void testGameStateTransitions() {
        BreakoutGame game = new BreakoutGame();
        
        assertEquals(GameState.MENU, game.getGameState());
        
        game.startGame();
        assertEquals(GameState.PLAYING, game.getGameState());
        
        game.pauseGame();
        assertEquals(GameState.PAUSED, game.getGameState());
        
        // 모든 생명을 잃으면
        for (int i = 0; i < 3; i++) {
            game.loseLife();
        }
        assertEquals(GameState.GAME_OVER, game.getGameState());
    }
}
```

## 자가 평가 문제

1. **인터페이스 기반 설계의 장점은?**
   - 코드 재사용성 향상
   - 유연한 확장성
   - 관심사 분리
   - 테스트 용이성

2. **2~7장의 클래스를 활용한 예시는?**
   - Ball → BreakoutBall, PowerUp
   - Box → BreakoutPaddle
   - StaticObject → 모든 벽돌 클래스
   - 인터페이스 → Breakable, MultiHit, Exploding

3. **UnbreakableBrick의 역할은?**
   - 게임 공간의 경계 정의
   - 공이 튀겨나가는 벽 역할
   - StaticObject 상속으로 충돌 처리
   - WallFactory로 쉽게 생성

4. **특수 효과 인터페이스의 이점은?**
   - 다중 인터페이스 구현 가능
   - 효과를 독립적으로 관리
   - instanceof로 타입 확인
   - 새로운 효과 추가 용이

## 자주 하는 실수와 해결 방법

### 1. 프레임 의존적 물리
```java
// 잘못된 코드 - FPS에 따라 속도가 달라짐
ball.setY(ball.getY() + 5);

// 올바른 코드 - 델타 타임 사용
ball.setY(ball.getY() + velocity * deltaTime);
```

### 2. 동시 수정 오류
```java
// 잘못된 코드 - 순회 중 리스트 수정
for (Brick brick : bricks) {
    if (brick.isDestroyed()) {
        bricks.remove(brick); // ConcurrentModificationException!
    }
}

// 올바른 코드 - Iterator 사용 또는 별도 리스트
bricks.removeIf(Brick::isDestroyed);
```

### 3. 파워업 효과 누적
```java
// 문제 - 파워업이 영구적으로 적용됨
paddle.expand(1.5);

// 해결 - 시간 제한과 원래 크기 저장
class TimedEffect {
    double duration;
    double originalValue;
    
    void update(double deltaTime) {
        duration -= deltaTime;
        if (duration <= 0) {
            restore();
        }
    }
}
```

## 다음 장 미리보기

9장에서는 외부 효과를 추가합니다:
- 중력 시뮬레이션
- 바람 효과
- 마찰력
- 반발 계수

## 추가 학습 자료

- [Game Programming Patterns](https://gameprogrammingpatterns.com/)
- [Breakout Game Tutorial](https://developer.mozilla.org/en-US/docs/Games/Tutorials/2D_Breakout_game_pure_JavaScript)
- [JavaFX Game Development](https://github.com/AlmasB/FXGLGames)
- [Interface-Based Design Patterns](https://www.baeldung.com/java-interface-based-design)
- [SOLID Principles in Game Development](https://www.gamedev.net/tutorials/programming/general-and-gameplay-programming/solid-principles-in-game-development-r5101/)

## 학습 체크포인트

- [ ] 2~7장의 기본 클래스(Ball, Box, StaticObject)를 활용했습니다
- [ ] 인터페이스(Breakable, MultiHit, Exploding, PowerUpProvider)를 구현했습니다
- [ ] UnbreakableBrick으로 게임 공간을 정의했습니다
- [ ] 상속과 인터페이스로 재사용 가능한 구조를 만들었습니다
- [ ] 완전한 벽돌 깨기 게임을 구현했습니다
- [ ] 게임 상태를 관리할 수 있습니다
- [ ] 파워업 시스템을 구현했습니다