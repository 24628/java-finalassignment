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
    private Label headerTitle = new Label();

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

        this.headerTitle.setFont(Font.font("Verdana", 30));
        header.setAlignment(Pos.TOP_LEFT);
        header.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        header.getChildren().addAll(this.headerTitle);

        // buttons
        logoutBtn = new Button("Logout");
        addMovieBtn = new Button("Add Movie");
        addShowingBtn = new Button("Add Showing");
        helpBtn = new Button("Help");

        // add all children and set alignment to right
        nav_bar.setAlignment(Pos.TOP_LEFT);
        nav_bar.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        nav_bar.getChildren().addAll(addMovieBtn, addShowingBtn, helpBtn, logoutBtn);

        logoutBtn.setOnAction(actionEvent -> {
            Session.destroy();
            Form_Login form = new Form_Login(db);
            form.getStage().show();
        });

        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(nav_bar, header);
        return container;
    }

    protected void setHeaderName(String headerName) {
        this.headerTitle.setText(headerName);
    }

    protected GridPane getBasicGridPane(){
        GridPane formMenu = new GridPane();
        formMenu.setAlignment(Pos.CENTER);

        formMenu.setHgap(10);
        formMenu.setVgap(10);
        formMenu.setGridLinesVisible(true);
        return formMenu;
    }
}
