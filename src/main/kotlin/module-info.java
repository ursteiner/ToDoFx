open module com.github.ursteiner.todofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires exposed.core;
    requires exposed.jdbc;
    requires java.sql;
    requires kotlinx.coroutines.core;
    requires kotlinx.serialization.json;
    requires org.slf4j;
    requires static org.junit.jupiter.api;

    exports com.github.ursteiner.todofx;
}