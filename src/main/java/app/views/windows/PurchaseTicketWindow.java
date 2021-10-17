package app.views.windows;

import app.database.Database;
import app.helpers.Session;
import app.helpers.controls.NumberTextField;
import app.model.Room;
import app.model.Showing;
import app.model.TableHolderRooms;
import app.model.Ticket;
import app.views.BaseForm;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.controlsfx.control.spreadsheet.Grid;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.List;

public class PurchaseTicketWindow extends BaseForm {

    private VBox formHolders = new VBox();
    private GridPane formMenu = new GridPane();
    private Label warningMessage = new Label();
    private final TableView<TableHolderRooms> roomOneTableView = generateTable();
    private final TableView<TableHolderRooms> roomTwoTableView = generateTable();
    private final NumberTextField numberOfSeatsTextField = new NumberTextField();
    private final TextField ticketNameTextField = new TextField();
    private final Button purchaseBtn = new Button("Purchase");
    private final Button clearBtnForCreatingTickets = new Button("Clear");
    private final List<Room> roomList = db.getAllRooms();

    public PurchaseTicketWindow(Database db) {
        super(db);
        Scene mainScene = new Scene(layout);

        this.setHeaderName("Purchase Tickets");
        stage.setWidth(1500);
        stage.setHeight(900);
        stage.setScene(mainScene);

        formHolders.getChildren().addAll(formMenu, warningMessage);
        layout.getChildren().addAll(this.createRoomGrids(), formHolders);

        roomOneTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println(newSelection.getTitle() + " Room one");
                setUserForm(newSelection, "Room 1", 0, roomOneTableView.getSelectionModel().getSelectedIndex());
            }
        });

        roomTwoTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println(newSelection.getTitle() + " Room two");
                setUserForm(newSelection, "Room 2", 1, roomTwoTableView.getSelectionModel().getSelectedIndex());
            }
        });
    }

    private void setUserForm(TableHolderRooms selectedItem, String roomName, int roomIndex, int showingIndex){
        GridPane formMenu = getBasicGridPane();

        formMenu.add(new Label("Room:"), 0, 0, 1, 1);
        formMenu.add(new Label("Start:"), 0, 1, 1, 1);
        formMenu.add(new Label("End:"), 0, 2, 1, 1);

        formMenu.add(new Label(roomName), 1, 0, 1, 1);
        formMenu.add(new Label(selectedItem.getStart()), 1, 1, 1, 1);
        formMenu.add(new Label(selectedItem.getEnd()), 1, 2, 1, 1);

        formMenu.add(new Label("Movie Title:"), 2, 0, 1, 1);
        formMenu.add(new Label("No of seats:"), 2, 1, 1, 1);
        formMenu.add(new Label("Name:"), 2, 2, 1, 1);

        formMenu.add(new Label(selectedItem.getTitle()), 3, 0, 1, 1);
        formMenu.add(numberOfSeatsTextField, 3, 1, 1, 1);
        formMenu.add(ticketNameTextField, 3, 2, 1, 1);

        formMenu.add(purchaseBtn, 4, 1, 1, 1);
        formMenu.add(clearBtnForCreatingTickets, 4, 2, 1, 1);


        purchaseBtn.setOnAction(actionEvent -> addTicketToShowing(roomIndex, showingIndex, selectedItem.getSeats()));
        clearBtnForCreatingTickets.setOnAction(actionEvent -> {
            numberOfSeatsTextField.setText("");
            ticketNameTextField.setText("");
        });
        formHolders.getChildren().set(0,formMenu);
    }

    private void addTicketToShowing(int roomIndex, int showingIndex, String seats){
        warningMessage.setText("");
        if(numberOfSeatsTextField.getText() == null){
            warningMessage.setText("Fill in the amount of seats!");
            return;
        }

        if(ticketNameTextField.getText() == null){
            warningMessage.setText("Fill in your name");
            return;
        }

        if(Integer.parseInt(seats) <= Integer.parseInt(numberOfSeatsTextField.getText())){
            warningMessage.setText("no seats left!");
            return;
        }


        int amountOfSeats = Integer.parseInt(numberOfSeatsTextField.getText());
        String ticketName = ticketNameTextField.getText();

        Ticket ticket = new Ticket(amountOfSeats, ticketName);
        db.addTicketToShowing(roomIndex, showingIndex, amountOfSeats, ticket);

        this.fillTableWithData(roomOneTableView, roomList.get(roomIndex).getShowingList());
    }

    private GridPane createRoomGrids() {
        GridPane gridPane = new GridPane();

        gridPane.setAlignment(Pos.CENTER);

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setGridLinesVisible(true);

        String[] columnNames = {"Start", "End", "Title", "Seats", "Price"};

        this.generateData(roomOneTableView, columnNames);
        this.fillTableWithData(roomOneTableView, roomList.get(0).getShowingList());

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

    private TableView<TableHolderRooms> generateTable() {
        TableView<TableHolderRooms> table = new TableView<TableHolderRooms>();
        table.setEditable(true);
        table.getSelectionModel().setCellSelectionEnabled(false); // false = row selection
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    private void generateData(TableView<TableHolderRooms> table, String[] columnNames) {
        for (String name : columnNames) {
            TableColumn<TableHolderRooms, String> colType = new TableColumn<>(name);
            colType.setSortable(false);
            colType.setCellValueFactory(new PropertyValueFactory<>(name));
            table.getColumns().add(colType);
        }
    }

    private void fillTableWithData(TableView<TableHolderRooms> table, List<Showing> showings) {
        table.getItems().clear();

        DecimalFormat df = new DecimalFormat("#.00");

        //@todo Fix remove t from date string here!
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
}
