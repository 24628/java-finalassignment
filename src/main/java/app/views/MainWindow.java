package app.views;

import app.database.Database;
import app.helpers.DateParser;
import app.helpers.Session;
import app.model.Showing;
import app.model.TableHolderRooms;
import app.views.windows.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class MainWindow {

    protected Stage stage;
    protected VBox layout;
    protected Button addMovieBtn;
    protected Button logoutBtn;
    protected Button purchaseBtn;
    protected Button addShowingBtn;
    protected GridPane form;
    protected Database db;
    protected final TableView<TableHolderRooms> roomOneTableView = generateTable();
    protected final TableView<TableHolderRooms> roomTwoTableView = generateTable();
    protected Scene mainScene;

    private Label headerTitle = new Label("Purchase a Ticket");

    public Stage getStage() {
        return stage;
    }

    public MainWindow(Database database) {
        db = database;
        stage = new Stage();
        layout = new VBox();
        Scene mainScene = new Scene(layout);

        stage.setTitle("Java Assignment Final!");
        stage.setWidth(1600);
        stage.setHeight(800);
        stage.setScene(mainScene);

        layout.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toString());
        layout.getChildren().addAll(this.createNavBar(), new PurchaseTicketWindow(db));

        stage.setOnCloseRequest((EventHandler<WindowEvent>) event -> {
            if(Session.getUser() != null) {
                CustomAlert alert = new CustomAlert(db, "Close window", "INFO");
                alert.getStage().show();
                this.getStage().close();
            } else {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    protected VBox createNavBar() {
        VBox container = new VBox();
        VBox header = new VBox();
        HBox nav_bar = new HBox();

        // labels for title and description

        headerTitle.setFont(Font.font("Verdana", 30));
        header.setAlignment(Pos.TOP_LEFT);
        header.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        header.getChildren().addAll(this.headerTitle);

        // buttons
        logoutBtn = new Button("Logout");
        addMovieBtn = new Button("Add Movie");
        addShowingBtn = new Button("Add Showing");
        purchaseBtn = new Button("Purchase Ticket");

        logoutBtn.getStyleClass().add("navbar-btn");
        addMovieBtn.getStyleClass().add("navbar-btn");
        addShowingBtn.getStyleClass().add("navbar-btn");
        purchaseBtn.getStyleClass().add("navbar-btn");

        // add all children and set alignment to right
        nav_bar.setAlignment(Pos.TOP_LEFT);
        nav_bar.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        if (Session.isAdmin())
            nav_bar.getChildren().addAll(addMovieBtn, addShowingBtn, purchaseBtn, logoutBtn);
        else
            nav_bar.getChildren().addAll(purchaseBtn, logoutBtn);

        logoutBtn.setOnAction(actionEvent -> {
            Session.destroy();
            this.getStage().close();
            Form_Login form = new Form_Login(db);
            form.getStage().show();
        });

        addMovieBtn.setOnAction(actionEvent -> {
            layout.getChildren().set(1, new AddMovieWindow(db));
            headerTitle.setText("Add Movie");
        });
        addShowingBtn.setOnAction(actionEvent -> {
            layout.getChildren().set(1, new AddShowingWindow(db));
            headerTitle.setText("Add Showing");
        });
        purchaseBtn.setOnAction(actionEvent -> {
            layout.getChildren().set(1, new PurchaseTicketWindow(db));
            headerTitle.setText("Purchase a Ticket");
        });

        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(nav_bar, header);
        return container;
    }

    protected void setHeaderName(String headerName) {
        this.headerTitle.setText(headerName);
    }

    protected GridPane getBasicGridPane() {
        GridPane formMenu = new GridPane();
        formMenu.setAlignment(Pos.CENTER);

        formMenu.setHgap(10);
        formMenu.setVgap(10);
        return formMenu;
    }

    protected GridPane createRoomGrids() {
        GridPane gridPane = new GridPane();

        gridPane.setAlignment(Pos.CENTER);

        gridPane.setHgap(10);
        gridPane.setVgap(10);

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
