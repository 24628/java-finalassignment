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

    private final NumberTextField numberOfSeatsTextField = new NumberTextField();
    private final TextField ticketNameTextField = new TextField();
    private final Button purchaseBtn = new Button("Purchase");
    private final Button clearBtnForCreatingTickets = new Button("Clear");

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

        this.fillTableWithData(roomOneTableView, db.getAllRooms().get(roomIndex).getShowingList());
    }


}
