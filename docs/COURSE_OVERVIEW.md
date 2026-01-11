# Java 게임 프로그래밍 과정 개요

## 과정 소개

이 과정은 Java와 JavaFX를 사용하여 게임 개발을 배우면서 객체 지향 프로그래밍의 핵심 개념을 마스터하는 것을 목표로 합니다. 단순한 Ball 클래스부터 시작하여 완전한 대포 게임과 벽돌 깨기 게임을 구현하는 과정을 통해 프로그래밍 실력을 체계적으로 향상시킬 수 있습니다.

## 학습 경로 다이어그램

```
1장: 소개
    ↓
2장: Ball World (Ball → PaintableBall)
    ↓
3장: Movable World (MovableBall)
    ↓
4장: Bounded World (BoundedBall)
    ↓
5장: 추상 데이터 타입
    ↓
6장: 새로운 객체 (Box) - 상속의 한계
    ↓
7장: Simple World - 인터페이스 도입
    ├→ 8장: 벽돌 깨기 게임
    └→ 9장: 외부 효과 (물리)
            ↓
       10장: 대포 게임
```

## 주요 학습 목표

### 프로그래밍 기초 (1-3장)
- Java 개발 환경 설정
- 클래스와 객체의 이해
- 상속을 통한 기능 확장
- JavaFX 그래픽 프로그래밍
- JUnit 테스트 작성

### 고급 개념 (4-6장)
- 충돌 감지와 물리 시뮬레이션
- 추상 클래스와 템플릿 메서드 패턴
- 상속의 한계와 문제점 인식
- 코드 재사용성과 유지보수성

### 설계 패턴 (7-10장)
- 인터페이스를 통한 유연한 설계
- 다형성과 의존성 주입
- 게임 상태 관리
- 완전한 게임 구현

## 각 장별 핵심 내용

### 1장: 소개 (Introduction)
- 개발 환경 설정
- JavaFX 기본 구조
- 첫 프로그램 작성
- JUnit 테스트 기초

**Java 학습 포인트:**
- 클래스와 객체의 기본 개념
- `public`, `private` 접근 제어자
- 생성자(Constructor) 작성법
- getter/setter 메서드 패턴

### 2장: 볼 월드 (Ball World)
- Ball 클래스 구현
- PaintableBall로 확장
- Canvas 렌더링
- 예외 처리

**Java 학습 포인트:**
- `extends` 키워드를 통한 상속
- `super()` 생성자 호출
- 메서드 오버라이딩 (`@Override`)
- 예외 처리 (`throw`, `throws`, `try-catch`)
- `final` 키워드 (불변 필드)

### 3장: 움직이는 월드 (Movable World)
- MovableBall 구현
- 시간 기반 애니메이션
- 벡터와 속도
- AnimationTimer 사용

**Java 학습 포인트:**
- 상속 계층 구조 (Ball → PaintableBall → MovableBall)
- 익명 내부 클래스 (Anonymous Inner Class)
- 람다 표현식 기초
- `this` 키워드 활용

### 4장: 경계가 있는 월드 (Bounded World)
- BoundedBall 구현
- 충돌 감지
- 반사 메커니즘
- 탄성 충돌

**Java 학습 포인트:**
- 깊은 상속 계층의 이해
- `protected` 접근 제어자
- 조건문과 논리 연산자 활용
- 메서드 체이닝

### 5장: 추상 데이터 타입 (Abstract Data Types)
- 추상 클래스 설계
- Bounds와 Vector 구현
- 템플릿 메서드 패턴
- 코드 재사용성

**Java 학습 포인트:**
- `abstract` 클래스와 메서드
- 템플릿 메서드 패턴 (Template Method Pattern)
- `record` 클래스 (Java 16+, 불변 데이터 객체)
- `static` 팩토리 메서드

### 6장: 새로운 객체들 (New Objects)
- Box 클래스 추가
- 상속의 한계 경험
- 클래스 폭발 문제
- 인터페이스의 필요성

**Java 학습 포인트:**
- 다중 상속 불가의 이해
- 코드 중복 문제 인식
- 상속 vs 구성(Composition) 비교
- 리팩토링의 필요성 인식

### 7장: 단순한 월드 (Simple World)
- 인터페이스 설계
- Paintable, Movable, Collidable
- 다형성 활용
- 유연한 객체 조합

**Java 학습 포인트:**
- `interface` 키워드와 구현 (`implements`)
- 다중 인터페이스 구현
- 다형성 (Polymorphism)과 동적 바인딩
- `instanceof` 연산자와 패턴 매칭 (Java 16+)
- `default` 메서드 (인터페이스 기본 구현)

### 8장: 벽돌 깨기 (Breakout)
- 완전한 게임 구현
- 게임 상태 관리
- 파워업 시스템
- 레벨 디자인

**Java 학습 포인트:**
- `enum` (열거형) 활용
- 컬렉션 프레임워크 (`List`, `ArrayList`, `Iterator`)
- 스트림 API (`filter`, `forEach`, `removeIf`)
- 상태 패턴 (State Pattern)

### 9장: 외부 효과 (External Effects)
- 중력 구현
- 바람과 마찰
- 반발 계수
- 물리 엔진 설계

**Java 학습 포인트:**
- 전략 패턴 (Strategy Pattern)
- 함수형 인터페이스 (`@FunctionalInterface`)
- 람다 표현식 심화
- 의존성 주입 (Dependency Injection) 기초

### 10장: 대포 게임 (Cannon Game)
- 포물선 운동
- 사용자 입력 처리
- 다양한 포탄과 목표물
- 게임 완성

**Java 학습 포인트:**
- 팩토리 패턴 (Factory Pattern)
- 옵저버 패턴 (Observer Pattern)
- 이벤트 기반 프로그래밍
- `Optional` 클래스 활용
- 종합 설계 및 리팩토링

## 학습 방법 가이드

### 효과적인 학습 전략

1. **순차적 학습**
   - 각 장은 이전 장의 내용을 기반으로 합니다
   - 건너뛰지 말고 순서대로 학습하세요

2. **실습 중심**
   - 모든 Lab 과제를 직접 구현해보세요
   - 코드를 작성하고 실행하며 이해하세요

3. **테스트 주도**
   - 각 기능에 대한 JUnit 테스트를 작성하세요
   - TDD(Test-Driven Development) 연습

4. **점진적 개선**
   - 처음부터 완벽할 필요는 없습니다
   - 작동하는 코드를 먼저 작성하고 개선하세요

### 일일 학습 계획 예시

**Day 1-2**: 1장 + 2장 기초
- 환경 설정
- Ball 클래스 구현
- 첫 JavaFX 프로그램

**Day 3-4**: 2장 완료 + 3장 시작
- PaintableBall 구현
- 움직임 추가
- 애니메이션 기초

**Day 5-6**: 3장 완료 + 4장
- 속도와 가속도
- 충돌 감지
- 물리 시뮬레이션

**Day 7-8**: 5장 + 6장
- 추상 클래스
- Box 추가
- 상속의 문제점

**Day 9-11**: 7장
- 인터페이스 설계
- 코드 리팩토링
- 다형성 활용

**Day 12-15**: 8장 또는 9-10장
- 게임 프로젝트 선택
- 완전한 게임 구현

## 평가 기준

### 각 장별 완료 기준

**기초 (Pass)**
- 모든 Lab 과제 구현
- 기본 테스트 통과
- 핵심 개념 이해

**우수 (Good)**
- 추가 기능 구현
- 코드 품질 개선
- 문서화 완료

**탁월 (Excellent)**
- 창의적 확장
- 성능 최적화
- 새로운 기능 제안

### 최종 프로젝트 평가

1. **기능성 (40%)**
   - 모든 요구사항 구현
   - 버그 없는 실행
   - 사용자 경험

2. **코드 품질 (30%)**
   - 객체 지향 원칙 준수
   - 가독성과 유지보수성
   - 적절한 주석

3. **창의성 (20%)**
   - 독창적인 기능
   - 시각적 개선
   - 게임플레이 혁신

4. **문서화 (10%)**
   - README 작성
   - 코드 문서화
   - 사용 설명서

## 추가 도전 과제

### 중급 과제
- 사운드 효과 추가
- 파티클 시스템 구현
- 고득점 저장 기능
- 설정 메뉴 구현

### 고급 과제
- 네트워크 멀티플레이어
- 레벨 에디터 제작
- AI 상대 구현
- 3D 그래픽 전환

### 연구 과제
- 다른 게임 엔진과 비교
- 성능 프로파일링
- 디자인 패턴 적용
- 모바일 포팅

## 자주 묻는 질문 (FAQ)

**Q: 프로그래밍 경험이 없어도 가능한가요?**
A: Java 기초 문법은 알고 있어야 합니다. 객체 지향 개념은 과정 중에 배웁니다.

**Q: JavaFX 대신 다른 라이브러리를 사용할 수 있나요?**
A: 가능하지만, 과정의 예제들은 JavaFX 기준으로 작성되었습니다.

**Q: 얼마나 시간이 걸리나요?**
A: 하루 2-3시간씩 학습하면 2-3주 정도 소요됩니다.

**Q: 6장까지만 하고 7장부터 인터페이스를 배우는 이유는?**
A: 상속만으로는 해결할 수 없는 문제를 직접 경험한 후 인터페이스의 필요성을 이해하기 위함입니다.

## 학습 자원

### 필수 도구
- JDK 21 이상
- IntelliJ IDEA (Community Edition)
- JavaFX SDK
- Git (버전 관리)

### 참고 서적
- "Effective Java" - Joshua Bloch
- "Clean Code" - Robert C. Martin
- "Game Programming Patterns" - Robert Nystrom

### 온라인 자료
- [JavaFX 공식 문서](https://openjfx.io/)
- [Oracle Java 튜토리얼](https://docs.oracle.com/javase/tutorial/)
- [Baeldung Java 가이드](https://www.baeldung.com/)

## 마치며

이 과정을 통해 단순히 게임을 만드는 것을 넘어서, 좋은 소프트웨어를 설계하고 구현하는 능력을 기를 수 있습니다. 각 장에서 배운 개념들은 게임 개발뿐만 아니라 모든 종류의 소프트웨어 개발에 적용할 수 있는 중요한 원칙들입니다.

화이팅! 🎮🚀