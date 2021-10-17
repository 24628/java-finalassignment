package app.views.windows;

import app.database.Database;
import app.helpers.SHA512;
import app.helpers.Session;
import app.model.User;
import app.views.BaseForm;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class Form_Login extends BaseForm {


    // all form items
    private TextField emailField;
    private PasswordField passwordField;


    public Form_Login(Database db) {
        super(db);
        // db conn
        //db = new Database("ProjectNoSQL");

        // --CRUD FORM-- //
        this.addUIControls(this.form);
        layout.getChildren().addAll(this.form);

        // Create the main scene.
        Scene Form_Login = new Scene(layout);

        // Let's go!
        stage.setTitle("Form Login");
        stage.setScene(Form_Login);

    }

    @Override
    protected VBox createNavBar(){
        return new VBox();
    }

    protected void addUIControls(GridPane gridPane) {
        // Add Header
        Label headerLabel = new Label("Login");
        headerLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        emailField = this.generateTextField("email: ", 1);
        passwordField = this.generatePasswordField("password: ", 2);

        Button submitButton = this.generateFormBtn("LOGIN", 0);

        submitButton.setOnAction(actionEvent -> {
                this.handleSubmitBtnClick();
        });
    }

    private void handleSubmitBtnClick() {
        String email = emailField.getText();
        String password = passwordField.getText();

        User loggedInUser = findUser(db.getAllUsers(), email, password);
        if(loggedInUser == null)
            return;

        Session.getInstance(loggedInUser);


    }

    private User findUser(List<User> userList, String email, String password){
        for (User u : userList) {
            if (u.getEmail().equals(email) && u.getPassword().equals(SHA512.encryptThisString(password))) {
                return u;
            }
        }
        return null;
    }

    private void loadNewWindow(User user){
        this.getStage().close();

        Session.getInstance(user);
        MainWindow mw = new MainWindow(db);
        mw.getStage().show();
    }
}
