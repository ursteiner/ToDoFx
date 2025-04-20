module com.github.ursteiner.todofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires exposed.core;
    requires java.sql;

    opens com.github.ursteiner.todofx to javafx.fxml;
    opens com.github.ursteiner.todofx.controller to javafx.fxml;
    opens com.github.ursteiner.todofx.model to javafx.base;
    exports com.github.ursteiner.todofx;
}