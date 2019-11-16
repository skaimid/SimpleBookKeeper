module SimpleBookKeeper.main {
    requires javafx.base;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires poi;
    requires poi.ooxml;
    exports cn.skaimid.SimpleBookKeeper;
    exports cn.skaimid.SimpleBookKeeper.view;
    exports cn.skaimid.SimpleBookKeeper.util;
    exports cn.skaimid.SimpleBookKeeper.model;
}