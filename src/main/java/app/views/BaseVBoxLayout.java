package app.views;

import app.database.Database;
import app.helpers.DateParser;
import app.model.Showing;
import app.model.TableHolderRooms;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.text.DecimalFormat;
import java.util.List;

public class BaseVBoxLayout extends VBox {
    protected Database db;
    protected VBox formHolders = new VBox();
    protected GridPane formMenu = new GridPane();
    protected Label warningMessage = new Label();
    protected final TableView<TableHolderRooms> roomOneTableView = generateTable();
    protected final TableView<TableHolderRooms> roomTwoTableView = generateTable();

    public BaseVBoxLayout(Database database) {
        db = database;
    }

    protected GridPane getBasicGridPane() {
        GridPane formMenu = new GridPane();
        formMenu.setAlignment(Pos.TOP_LEFT);
        formMenu.getStyleClass().add("gridpane-padding");

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