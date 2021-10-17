package app.views.windows;

import app.database.Database;
import app.helpers.Session;
import app.model.BaseModel;
import app.model.Room;
import app.model.Showing;
import app.model.TableHolderRooms;
import app.views.BaseForm;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.text.DecimalFormat;
import java.util.List;

public class MainWindow extends BaseForm {

    public MainWindow(Database db) {
        super(db);
        // --BUTTON EVENTS-- //
//        ticketButton.setOnAction(actionEvent -> layout.getChildren().set(1, new UserListView(db,this)));
//        userButton.setOnAction(actionEvent -> layout.getChildren().set(1, new UserListView(db,this)));
//        dashboardButton.setOnAction(actionEvent -> layout.getChildren().set(1,  new UserListView(db,this)));

        // Add the menu and the view. Default view will be the student list view
//        layout.getChildren().addAll(new UserListView(db,this));

        // Create the main scene.
        // Scene mainScene = new StyledScene(layout);
        Scene mainScene = new Scene(layout);

        // Let's go!
        stage.setTitle("NoDesk");
        this.setHeaderName("Purchase Tickets");
        stage.setWidth(1200);
        stage.setHeight(600);
        stage.setScene(mainScene);

        layout.getChildren().addAll(this.createGrid());
        System.out.println(Session.getUser());
    }


    protected GridPane createGrid() {
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();

        // Position the pane at the center of the screen, both vertically and horizontally and add padding
        gridPane.setAlignment(Pos.CENTER);
//        gridPane.setPadding(new Insets(40, 80, 40, 40));

        // Set gap for row and col
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setGridLinesVisible(true);

        // columnOneConstraints for both column
//        ColumnConstraints columnOneConstraints = new ColumnConstraints(150, 150, Double.MAX_VALUE);
//        columnOneConstraints.setHalignment(HPos.RIGHT);
//        ColumnConstraints columnTwoConstrains = new ColumnConstraints(150,150, Double.MAX_VALUE);
//        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        // Add Column Constraints
//        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);
//        gridPane.setMinWidth(1200);

        List<Room> roomList = db.getAllRooms();
        String[] columnNames = {"Start", "End", "Title", "Seats", "Price"};
        TableView<TableHolderRooms> roomOneTableView = generateTable();
        this.generateData(roomOneTableView, columnNames);
        this.fillTableWithData(roomOneTableView, roomList.get(0).getShowingList());

        TableView<TableHolderRooms> roomTwoTableView = generateTable();
        this.generateData(roomTwoTableView, columnNames);
        this.fillTableWithData(roomTwoTableView, roomList.get(1).getShowingList());

        GridPane.setHgrow(roomOneTableView, Priority.ALWAYS);
        GridPane.setHgrow(roomTwoTableView, Priority.ALWAYS);

        gridPane.add(new Label(roomList.get(0).getTitle()), 0, 0, 1, 1);
        gridPane.add(roomOneTableView, 0, 1, 1, 1);
        gridPane.add(new Label(roomList.get(1).getTitle()), 1, 0, 1, 1);
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
            colType.setCellValueFactory(new PropertyValueFactory<>(name));
            table.getColumns().add(colType);
        }
    }

    protected void fillTableWithData(TableView<TableHolderRooms> table, List<Showing> showings) {

        DecimalFormat df = new DecimalFormat("#.00");

        for (Showing showing : showings) {
            table.getItems().addAll(
                new TableHolderRooms(
                    showing.getStartTime().toString(),
                    showing.getStartTime().plusMinutes(showing.getMovie().getDurationInMinutes()).toString(),
                    showing.getMovie().getTitle(),
                    String.valueOf(showing.getCurrentSeats()),
                    df.format(showing.getMovie().getPrice())
                )
            );
        }
    }

    public void setTableView(String option) {
//        if (option.equalsIgnoreCase("Ticket"))
//            layout.getChildren().set(1,  new UserListView(db,this));
//        if (option.equalsIgnoreCase("User"))
//            layout.getChildren().set(1, new UserListView(db,this));
    }
}
