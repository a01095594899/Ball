module com.nhnacademy.breakoutgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // 외부에서 사용하는 패키지 공개
    exports com.nhnacademy.breakoutgame;
    exports com.nhnacademy.common;
    exports com.nhnacademy.cannongame;

    // JavaFX 런처/컨트롤러 리플렉션 허용
    opens com.nhnacademy.breakoutgame to javafx.graphics, javafx.fxml;
    opens com.nhnacademy.common to javafx.fxml;
    opens com.nhnacademy.cannongame to javafx.fxml;

}