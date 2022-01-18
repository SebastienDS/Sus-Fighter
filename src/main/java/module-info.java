module fr.uge.susfighter {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.gluonhq.charm.glisten;
    requires com.google.gson;

    opens fr.uge.susfighter to javafx.fxml;
    exports fr.uge.susfighter;
    exports fr.uge.susfighter.mvc;
    exports fr.uge.susfighter.object;
    opens fr.uge.susfighter.mvc to javafx.fxml, com.google.gson;
    opens fr.uge.susfighter.object to com.google.gson;
}