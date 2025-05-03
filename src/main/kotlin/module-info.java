module com.github.ursteiner.todofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires exposed.core;
    requires java.sql;
    requires kotlinx.coroutines.core;
    requires kotlinx.serialization.json;
    requires org.slf4j;

    opens com.github.ursteiner.todofx to javafx.fxml, kotlinx.serialization.json, org.slf4j;
    opens com.github.ursteiner.todofx.controller to javafx.fxml;
    opens com.github.ursteiner.todofx.model to javafx.base;
    exports com.github.ursteiner.todofx;
}