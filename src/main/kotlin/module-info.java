module com.github.ursteiner.todofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires exposed.core;
    requires exposed.jdbc;
    requires java.sql;
    requires kotlinx.coroutines.core;
    requires kotlinx.serialization.json;
    requires org.slf4j;

    opens com.github.ursteiner.todofx to javafx.fxml, javafx.controls, kotlinx.serialization.json, org.slf4j, exposed.core, exposed.jdbc;
    opens com.github.ursteiner.todofx.controller to javafx.fxml, javafx.controls;
    opens com.github.ursteiner.todofx.model to javafx.base;
    exports com.github.ursteiner.todofx;
}