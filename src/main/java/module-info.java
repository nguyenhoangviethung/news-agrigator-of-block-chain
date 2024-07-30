module com.example {
    requires javafx.controls;
    requires javafx.web;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires org.jsoup;
    requires org.controlsfx.controls;
    requires com.google.gson;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens com;
    exports com;
    opens com.gui;
    exports com.gui;
    opens com.crawl.dataFormat;
    exports com.crawl.dataFormat;
}
