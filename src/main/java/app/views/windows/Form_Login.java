package app.views.windows;

import app.database.Database;
import app.helpers.SHA512;
import app.helpers.Session;
import app.model.User;
import app.views.MainWindow;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class Form_Login extends MainWindow {


    // all form items
    private TextField emailField;
    private PasswordField passwordField;
    private Label errorMessage = new Label("");


    public Form_Login(Database db) {
        super(db);
        // db conn
        //db = new Database("ProjectNoSQL");

        // --CRUD FORM-- //
        this.form = getBasicGridPane();
        this.addUIControls(this.form);
        layout.getChildren().set(0, this.form);
        layout.getChildren().set(1, new VBox());

        // Let's go!
        stage.setTitle("Form Login");

        errorMessage.setFont(Font.font("Verdana", 30));
    }

    @Override
    protected VBox createNavBar() {
        return new VBox();
    }

    protected void addUIControls(GridPane gridPane) {
        // Add Header
        Label headerLabel = new Label("Login");
        headerLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

        emailField = this.generateTextField("email: ", 1);
        passwordField = this.generatePasswordField("password: ", 2);

        Button submitButton = this.generateFormBtn("LOGIN", 0);

        gridPane.add(errorMessage, 0, 8, 2, 1);

        submitButton.setOnAction(actionEvent -> {
            this.handleSubmitBtnClick();
        });
    }

    private void handleSubmitBtnClick() {
        String email = emailField.getText();
        String password = passwordField.getText();

        User loggedInUser = findUser(db.getAllUsers(), email, password);
        if (loggedInUser == null) {
            errorMessage.setText("These items do not match the credentials!");
            return;
        }
        Session.getInstance(loggedInUser);

        MainWindow mw = new MainWindow(db);
        mw.getStage().show();

        this.getStage().close();
    }

    private User findUser(List<User> userList, String email, String password) {
        for (User u : userList) {
            if (u.getEmail().equals(email) && u.getPassword().equals(SHA512.encryptThisString(password))) {
                return u;
            }
        }
        return null;
    }

    private TextField generateTextField(String title, int placement) {
        Label label = new Label(title);
        this.form.add(label, 0, placement);

        // Add description Field
        TextField field = new TextField();
        field.setPrefHeight(20);
        field.setPrefWidth(400);
        field.setMaxWidth(400);
        this.form.add(field, 1, placement);

        return field;
    }

    private PasswordField generatePasswordField(String title, int placement) {
        Label label = new Label(title);
        this.form.add(label, 0, placement);

        // Add description Field
        PasswordField field = new PasswordField();
        field.setPrefHeight(20);
        field.setPrefWidth(400);
        field.setMaxWidth(400);
        this.form.add(field, 1, placement);

        return field;
    }

    private Button generateFormBtn(String btnTitle, int placement) {
        Button btn = new Button(btnTitle);
        btn.setPrefHeight(40);
        btn.setDefaultButton(true);
        btn.setPrefWidth(100);
        this.form.add(btn, placement, 5, 2, 1);
        GridPane.setMargin(btn, new Insets(20, 0, 20, 0));

        return btn;
    }
}
