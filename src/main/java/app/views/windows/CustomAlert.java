package app.views.windows;

import app.database.Database;
import app.helpers.Session;
import app.helpers.controls.CustomSubmitBtn;
import app.views.MainWindow;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class CustomAlert extends MainWindow{
    private Label messageLabel = new Label();

    public CustomAlert(Database database, String message, String type) {
        super(database);
        stage.setWidth(300);
        stage.setHeight(200);

        layout.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toString());
        GridPane form = new GridPane();
        layout.getChildren().set(0, form);
        layout.getChildren().set(1, new VBox());

        Button confirmBtn = new CustomSubmitBtn("Confirm");
        Button cancelBtn = new Button("Cancel");

        form.add(messageLabel, 0, 0, 1, 1);
        form.add(confirmBtn, 0, 1, 1, 1);
        form.add(cancelBtn, 1, 1, 1, 1);

        stage.setTitle(type);
        messageLabel.setText(message);

        cancelBtn.setOnAction(actionEvent -> {

            MainWindow mw = new MainWindow(db);
            mw.getStage().show();

            this.getStage().close();
        });

        confirmBtn.setOnAction(actionEvent -> {
            Platform.exit();
            System.exit(0);
        });
    }

    @Override
    protected VBox createNavBar() {
        return new VBox();
    }
}
