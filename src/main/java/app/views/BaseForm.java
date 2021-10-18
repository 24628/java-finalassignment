package app.views;

import app.database.Database;
import app.helpers.DateParser;
import app.helpers.Session;
import app.helpers.controls.DateTimePicker;
import app.model.Showing;
import app.model.TableHolderRooms;
import app.views.windows.Form_Login;
import app.views.windows.PurchaseTicketWindow;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.List;

public class BaseForm {

    protected Stage stage;
    protected VBox layout;
    protected Button addMovieBtn;
    protected Button logoutBtn;
    protected Button helpBtn;
    protected Button addShowingBtn;
    protected GridPane form;
    protected Database db;
    protected VBox formHolders = new VBox();
    protected GridPane formMenu = new GridPane();
    protected Label warningMessage = new Label();
    protected final TableView<TableHolderRooms> roomOneTableView = generateTable();
    protected final TableView<TableHolderRooms> roomTwoTableView = generateTable();

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

    protected GridPane createRoomGrids() {
        GridPane gridPane = new GridPane();

        gridPane.setAlignment(Pos.CENTER);

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setGridLinesVisible(true);

        String[] columnNames = {"Start", "End", "Title", "Seats", "Price"};

        this.generateData(roomOneTableView, columnNames);
        this.fillTableWithData(roomOneTableView, db.getAllRooms().get(0).getShowingList());

        this.generateData(roomTwoTableView, columnNames);
        this.fillTableWithData(roomTwoTableView, db.getAllRooms().get(1).getShowingList());

        GridPane.setHgrow(roomOneTableView, Priority.ALWAYS);
        GridPane.setHgrow(roomTwoTableView, Priority.ALWAYS);

        gridPane.add(new Label(db.getAllRooms().get(0).getTitle()), 0, 0, 1, 1);
        gridPane.add(roomOneTableView, 0, 1, 1, 1);
        gridPane.add(new Label(db.getAllRooms().get(1).getTitle()), 1, 0, 1, 1);
        gridPane.add(roomTwoTableView, 1, 1, 1, 1);
        return gridPane;
    }

    protected TableView<TableHolderRooms> generateTable() {
        TableView<TableHolderRooms> table = new TableView<TableHolderRooms>();
        table.setEditable(true);
        table.getSelectionModel().setCellSelectionEnabled(false); // false = row selection
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    protected void generateData(TableView<TableHolderRooms> table, String[] columnNames) {
        for (String name : columnNames) {
            TableColumn<TableHolderRooms, String> colType = new TableColumn<>(name);
            colType.setSortable(false);
            colType.setCellValueFactory(new PropertyValueFactory<>(name));
            table.getColumns().add(colType);
        }
    }

    protected void fillTableWithData(TableView<TableHolderRooms> table, List<Showing> showings) {
        table.getItems().clear();

        DecimalFormat df = new DecimalFormat("#.00");

        for (Showing showing : showings) {
            table.getItems().addAll(
                    new TableHolderRooms(
                            DateParser.toString(showing.getStartTime()),
                            DateParser.toString(showing.getStartTime().plusMinutes(showing.getMovie().getDurationInMinutes())),
                            showing.getMovie().getTitle(),
                            String.valueOf(showing.getCurrentSeats()),
                            df.format(showing.getMovie().getPrice())
                    )
            );
        }
    }
}
