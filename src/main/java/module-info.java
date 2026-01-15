module com.nhnacademy {
    requires javafx.graphics;
    requires javafx.controls;

    // Application 클래스가 있는 패키지를 외부(JavaFX)에서 접근 가능하게 공개
    exports com.nhnacademy.common;

    // JavaFX 런처가 리플렉션으로 Application을 만들 수 있게 열어줌
    opens com.nhnacademy.common to javafx.graphics;
}
