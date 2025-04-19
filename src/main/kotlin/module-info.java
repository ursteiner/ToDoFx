module com.github.ursteiner.todofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens com.github.ursteiner.todofx to javafx.fxml;
    exports com.github.ursteiner.todofx;
}