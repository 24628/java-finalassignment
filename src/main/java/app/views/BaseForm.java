package app.views;

import app.database.Database;
import app.helpers.Session;
import app.helpers.controls.DateTimePicker;
import app.model.User;
import app.views.windows.Form_Login;
import app.views.windows.MainWindow;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class BaseForm {

    protected User user;
    protected Stage stage;
    protected VBox layout;
    protected Button logoutButton;
    protected Button dashboardButton;
    protected Button userButton;
    protected Button ticketButton;
    protected VBox nav_bar;
    protected GridPane form;
    protected Database db;

    public Stage getStage() {
        return stage;
    }

    public BaseForm(Database database) {
        db = database;
        // create a new Stage (window)
        stage = new Stage();
        nav_bar = this.createNavBar();
        form = this.createGrid();

        // set up the global layout and add nav
        layout = new VBox();
        layout.getChildren().addAll(nav_bar);
    }

    protected VBox createNavBar(){
        VBox container = new VBox();
        VBox header = new VBox();
        HBox nav_bar = new HBox();

        // labels for title and description
        Label title = new Label("NoDesk");
        Label description = new Label("Licensed to: The Garden Group");
        title.setFont(Font.font("Verdana", 30));
        description.setFont(Font.font("Verdana", 20));
        header.setAlignment(Pos.TOP_RIGHT);
        header.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY)));
        header.getChildren().addAll(title, description);

        // buttons
        logoutButton = new Button("Logout");
        dashboardButton = new Button("Dashboard");
        userButton = new Button("Users");
        ticketButton = new Button("Tickets");
        dashboardButton.setMinWidth(296);
        dashboardButton.setMinHeight(40);
        ticketButton.setMinWidth(296);
        ticketButton.setMinHeight(40);
        userButton.setMinWidth(296);
        userButton.setMinHeight(40);
        logoutButton.setMinWidth(296);
        logoutButton.setMinHeight(40);

        // add all children and set alignment to right
        nav_bar.setAlignment(Pos.CENTER);
//        if(Session.isServiceDeskEmployee()) nav_bar.getChildren().addAll(logoutButton, dashboardButton, ticketButton, userButton);
//        else nav_bar.getChildren().addAll(logoutButton, dashboardButton, ticketButton);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(header, nav_bar);
        return container;
    }

    protected GridPane createGrid(){
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();

        // Position the pane at the center of the screen, both vertically and horizontally and add padding
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 80, 40, 40));

        // Set gap for row and col
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // columnOneConstraints for both column
        ColumnConstraints columnOneConstraints = new ColumnConstraints(150, 150, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(150,150, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        // Add Column Constraints
        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);
        return gridPane;
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
        MainWindow mainWindow = new MainWindow(db);
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
