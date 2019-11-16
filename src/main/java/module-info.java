module SimpleBookKeeper.main {
    requires javafx.base;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires poi;
    requires poi.ooxml;
    exports cn.skaimid.SimpleBookKeeper;
    opens cn.skaimid.SimpleBookKeeper.view to javafx.fxml;
}