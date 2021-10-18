package app.views.windows;

import app.database.Database;
import app.helpers.controls.CustomSubmitBtn;
import app.helpers.controls.NumberTextField;
import app.model.TableHolderRooms;
import app.model.Ticket;
import app.views.BaseVBoxLayout;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class PurchaseTicketWindow extends BaseVBoxLayout {

    private final NumberTextField numberOfSeatsTextField = new NumberTextField();
    private final TextField ticketNameTextField = new TextField();
    private final Button purchaseBtn = new CustomSubmitBtn("Purchase");
    private final Button clearBtnForCreatingTickets = new Button("Clear");

    public PurchaseTicketWindow(Database db) {
        super(db);
        formHolders.getChildren().addAll(formMenu, warningMessage);
        this.getChildren().addAll(this.createRoomGrids(), formHolders);

        roomOneTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println(newSelection.getTitle() + " Room one");
                setUserForm(newSelection, "Room 1", 0, roomOneTableView.getSelectionModel().getSelectedIndex(), roomOneTableView);
            }
        });

        roomTwoTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println(newSelection.getTitle() + " Room two");
                setUserForm(newSelection, "Room 2", 1, roomTwoTableView.getSelectionModel().getSelectedIndex(), roomTwoTableView);
            }
        });
    }

    private void setUserForm(TableHolderRooms selectedItem, String roomName, int roomIndex, int showingIndex, TableView tableView) {
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


        purchaseBtn.setOnAction(actionEvent -> addTicketToShowing(roomIndex, showingIndex, selectedItem.getSeats(), tableView));
        clearBtnForCreatingTickets.setOnAction(actionEvent -> {
            numberOfSeatsTextField.setText("");
            ticketNameTextField.setText("");
        });
        formHolders.getChildren().set(0, formMenu);
    }

    private void addTicketToShowing(int roomIndex, int showingIndex, String seats, TableView tableView) {
        warningMessage.setText("");
        if (numberOfSeatsTextField.getText() == null) {
            warningMessage.setText("Fill in the amount of seats!");
            return;
        }

        if (ticketNameTextField.getText() == null) {
            warningMessage.setText("Fill in your name");
            return;
        }

        if (Integer.parseInt(seats) <= Integer.parseInt(numberOfSeatsTextField.getText())) {
            warningMessage.setText("no seats left!");
            return;
        }


        int amountOfSeats = Integer.parseInt(numberOfSeatsTextField.getText());
        String ticketName = ticketNameTextField.getText();

        Ticket ticket = new Ticket(amountOfSeats, ticketName);
        db.addTicketToShowing(roomIndex, showingIndex, amountOfSeats, ticket);

        this.fillTableWithData(tableView, db.getAllRooms().get(roomIndex).getShowingList());
    }


}
