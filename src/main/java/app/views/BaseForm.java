package app.views;

import app.database.Database;
import app.helpers.Session;
import app.helpers.controls.DateTimePicker;
import app.views.windows.Form_Login;
import app.views.windows.PurchaseTicketWindow;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class BaseForm {

    protected Stage stage;
    protected VBox layout;
    protected Button addMovieBtn;
    protected Button logoutBtn;
    protected Button helpBtn;
    protected Button addShowingBtn;
    protected GridPane form;
    protected Database db;
    private String headerName = "Purchase Tickets";

    public Stage getStage() {
        return stage;
    }

    public BaseForm(Database database) {
        db = database;
        stage = new Stage();
        layout = new VBox();
        layout.getChildren().addAll(this.createNavBar());
    }

    protected VBox createNavBar(){
        VBox container = new VBox();
        VBox header = new VBox();
        HBox nav_bar = new HBox();

        // labels for title and description
        Label title = new Label(headerName);
        title.setFont(Font.font("Verdana", 30));
        header.setAlignment(Pos.TOP_LEFT);
        header.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        header.getChildren().addAll(title);

        // buttons
        logoutBtn = new Button("Logout");
        addMovieBtn = new Button("Add Movie");
        addShowingBtn = new Button("Add Showing");
        helpBtn = new Button("Help");

        // add all children and set alignment to right
        nav_bar.setAlignment(Pos.TOP_LEFT);
        nav_bar.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        nav_bar.getChildren().addAll(addMovieBtn, helpBtn, logoutBtn, addShowingBtn);

        logoutBtn.setOnAction(actionEvent -> logoutFromSession());
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(nav_bar, header);
        return container;
    }

    protected void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    protected DateTimePicker generateDateTimePicker(String title, int placement){
        Label label = new Label(title);
        this.form.add(label, 0,placement);

        DateTimePicker date = new DateTimePicker();
        date.setPrefHeight(20);
        date.setPrefWidth(400);
        this.form.add(date, 1, placement);

        return date;
    }
    protected ComboBox<String> generateComboBox(String title, String[] comboBoxItems, int placement){
        Label label = new Label(title);
        this.form.add(label, 0, placement);

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPrefWidth(400);
        comboBox.getItems().addAll(comboBoxItems);
        this.form.add(comboBox, 1, placement);

        return comboBox;
    }
    protected TextField generateTextField(String title, int placement){
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
    protected PasswordField generatePasswordField(String title, int placement){
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
    protected Button generateFormBtn(String btnTitle, int placement){
        Button btn = new Button(btnTitle);
        btn.setPrefHeight(40);
        btn.setDefaultButton(true);
        btn.setPrefWidth(100);
        this.form.add(btn, placement, 10, 2, 1);
        GridPane.setMargin(btn, new Insets(20, 0,20,0));

        return btn;
    }

    // --Open main window and close this one
    protected void openMainAndClose(ActionEvent actionEvent, String option){
        PurchaseTicketWindow mainWindow = new PurchaseTicketWindow(db);
        mainWindow.setTableView(option);
        mainWindow.getStage().show();

        // close this window
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }

    protected void logoutFromSession(){
        this.getStage().close();
        Session.destroy();

        Form_Login form = new Form_Login(db);
        form.getStage().show();
    }
}
