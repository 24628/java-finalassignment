module com.example.projectnosqllol {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens app to javafx.fxml;
    opens app.model;
    exports app;
}