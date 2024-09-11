module org.example.checkers {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.checkers to javafx.fxml;
    exports org.example.checkers;
}