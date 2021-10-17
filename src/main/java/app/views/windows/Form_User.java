package app.views.windows;

import app.ICallBack;
import app.database.Database;
import app.model.User;
import app.views.BaseForm;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

public class Form_User extends BaseForm {

    private TextField firstName;
    private TextField lastName;
    private ComboBox userType;
    private TextField email;
    private TextField phoneNumber;
    private ComboBox location;


    // comboBox values
    private String[] comboBoxUserTypes = {"Employee", "Service_desk"};
    private String[] comboBoxLocations = {"Amsterdam", "Haarlem", "Knuppeldam", "Headquarters (HQ)"};

    public Form_User(Database db, User user) {
        super(db);
        // set database and helper

        // --CRUD FORM-- //
        this.addUIControls(this.form, user);
        layout.getChildren().addAll(this.form);

        // Create the main scene.
        // Scene mainScene = new StyledScene(layout);
        Scene form_User = new Scene(layout);

        // Let's go!
        stage.setTitle("Users Form");
        stage.setScene(form_User);

        // --NAV BUTTON EVENTS-- //
        ticketButton.setOnAction(actionEvent -> openMainAndClose(actionEvent, "Ticket"));
        userButton.setOnAction(actionEvent -> openMainAndClose(actionEvent, "User"));
        dashboardButton.setOnAction(actionEvent -> openMainAndClose(actionEvent, "Dashboard"));

    }

    protected void addUIControls(GridPane gridPane, User user) {
        // Add Header
        Label headerLabel = new Label("Create User");
        headerLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        Control[] formItems = this.createFormItems();


        // generate form buttons
        Button cancelButton = this.generateFormBtn("CANCEL", 1);
        Button submitButton = this.generateFormBtn("SUBMIT USER", 0);

        // set action to submit button
        submitButton.setOnAction(actionEvent -> this.handleSubmitBtnClick(formItems, user, new ICallBack() {
            @Override
            public void onSucces() {
                System.out.println("data submit succesfull!");
                openMainAndClose(actionEvent, "User"); // open / refresh tableview
            }

            @Override
            public void onError(String err) {
                System.out.println("data submit not succesfull: " + err);
            }
        }));
        // set action to cancel button
        cancelButton.setOnAction(actionEvent -> openMainAndClose(actionEvent,"User"));
    }

    // create empty form
    private Control[] createFormItems(){
        Control[] formItems = {
                firstName = this.generateTextField("First Name: ",1),
                lastName = this.generateTextField("Last Name: ", 2),
                userType = this.generateComboBox("User Type: ", comboBoxUserTypes, 3 ),
                email = this.generateTextField("E-mail: ", 4),
                phoneNumber = this.generateTextField("Phone Number: ", 5),
                location = this.generateComboBox("Location: ", comboBoxLocations, 6 )
        };
        return formItems;
    }

    // Submit button event handle
    private void handleSubmitBtnClick(Control[] formItems, User user, ICallBack callBack) {

    }
}
