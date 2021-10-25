package app.views.windows;

import app.database.Database;
import app.helpers.DateParser;
import app.helpers.controls.CustomSubmitBtn;
import app.helpers.controls.DateTimePicker;
import app.model.Movie;
import app.model.Room;
import app.model.Showing;
import app.views.BaseVBoxLayout;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AddShowingWindow extends BaseVBoxLayout {

    private ComboBox<Room> roomComboBox = new ComboBox<Room>();
    private ComboBox<Movie> movieComboBox = new ComboBox<Movie>();
    private DateTimePicker dateTimePicker = new DateTimePicker();
    private final Button addShowingBtnToRoom = new CustomSubmitBtn("Add Showing");
    private final Button clearBtnForCreatingShowing = new Button("Clear");
    private final Button exportShowingsBtn = new CustomSubmitBtn("Export Showings");
    private final Label nOfSeats = new Label();
    private final Label movieEndTime = new Label();
    private final Label moviePrice = new Label();
    private Movie selectedMovie;
    private Room selectedRoom;
    private LocalDateTime currentSelectedLocalDateTime;

    public AddShowingWindow(Database database) {
        super(database);

        formHolders.getChildren().addAll(this.formMenu, warningMessage);
        this.getChildren().addAll(exportShowingsBtn, this.createRoomGrids(), formHolders);

        setShowingForm();

        roomComboBox.setConverter(new StringConverter<Room>() {
            @Override
            public String toString(Room object) {
                if (object == null) {
                    return "Select a room!";
                }
                return object.getTitle();
            }

            @Override
            public Room fromString(String string) {
                return null;
            }
        });

        movieComboBox.setConverter(new StringConverter<Movie>() {
            @Override
            public String toString(Movie object) {
                if (object == null) {
                    return "Select a movie!";
                }
                return object.getTitle();
            }

            @Override
            public Movie fromString(String string) {
                return null;
            }
        });

        roomComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println(newVal);
            selectedRoom = newVal;
            fillFormCorrectly();
        });

        movieComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println(newVal);
            selectedMovie = newVal;
            fillFormCorrectly();
        });

        //TO initialize
        dateTimePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("i dod update not?");
            currentSelectedLocalDateTime = dateTimePicker.getDateTimeValue();
            fillFormCorrectly();
        });

        //IF there is a change on typing we need to still force update the forms to show correct time stamp!
        dateTimePicker.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> {
            System.out.println("i dod update not?");
            currentSelectedLocalDateTime = dateTimePicker.getDateTimeValue();
            fillFormCorrectly();
        });

        exportShowingsBtn.setOnAction(actionEvent -> {
            try {
                Writer writer = null;
                try {
                    File file = new File("Showings.csv");
                    writer = new BufferedWriter(new FileWriter(file));
                    for (Showing s : db.getAllRooms().get(0).getShowingList()) {

                        String text = DateParser.toString(s.getStartTime()) + ","
                                + DateParser.toString(s.getStartTime().plusMinutes(s.getMovie().getDurationInMinutes())) + ","
                                + "Room 1," +
                                s.getMovie().getTitle() + "," +
                                s.getCurrentSeats() + "," +
                                s.getMovie().getPrice() + "," +"\n";

                        writer.write(text);
                    }

                    for (Showing s : db.getAllRooms().get(1).getShowingList()) {

                        String text = DateParser.toString(s.getStartTime()) + ","
                                + DateParser.toString(s.getStartTime().plusMinutes(s.getMovie().getDurationInMinutes())) + ","
                                + "Room 2," +
                                s.getMovie().getTitle() + "," +
                                s.getCurrentSeats() + "," +
                                s.getMovie().getPrice() + "," +"\n";

                        writer.write(text);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                finally {

                    writer.flush();
                    writer.close();
                }
            } catch (Exception ignored){}
        });
    }

    private void fillFormCorrectly() {
        DecimalFormat df = new DecimalFormat("#.00");
        if (selectedMovie != null) {
            moviePrice.setText(df.format(selectedMovie.getPrice()));
        }

        if (selectedMovie != null && currentSelectedLocalDateTime != null) {
            movieEndTime.setText(DateParser.toString(currentSelectedLocalDateTime.plusMinutes(selectedMovie.getDurationInMinutes())));
        }

        if (selectedRoom != null) {
            nOfSeats.setText(String.valueOf(selectedRoom.getSeats()));
        }
    }

    private void setShowingForm() {
        roomComboBox.setItems(FXCollections.observableArrayList(db.getAllRooms()));
        movieComboBox.setItems(FXCollections.observableArrayList(db.getAllMovies()));

        this.formMenu = getBasicGridPane();

        this.formMenu.add(new Label("Movie Title:"), 0, 0, 1, 1);
        this.formMenu.add(new Label("Room:"), 0, 1, 1, 1);
        this.formMenu.add(new Label("No of Seats:"), 0, 2, 1, 1);

        this.formMenu.add(movieComboBox, 1, 0, 1, 1);
        this.formMenu.add(roomComboBox, 1, 1, 1, 1);
        this.formMenu.add(nOfSeats, 1, 2, 1, 1);

        this.formMenu.add(new Label("Start:"), 2, 0, 1, 1);
        this.formMenu.add(new Label("End:"), 2, 1, 1, 1);
        this.formMenu.add(new Label("Price:"), 2, 2, 1, 1);

        this.formMenu.add(dateTimePicker, 3, 0, 1, 1);
        this.formMenu.add(movieEndTime, 3, 1, 1, 1);
        this.formMenu.add(moviePrice, 3, 2, 1, 1);

        this.formMenu.add(addShowingBtnToRoom, 4, 1, 1, 1);
        this.formMenu.add(clearBtnForCreatingShowing, 4, 2, 1, 1);


        addShowingBtnToRoom.setOnAction(actionEvent -> {

            if (selectedMovie == null) {
                warningMessage.setText("there is no selected movie");
                return;
            }

            if (currentSelectedLocalDateTime == null) {
                warningMessage.setText("there is no selected date time");
                return;
            }

            if (selectedRoom == null) {
                warningMessage.setText("there is no selected room");
                return;
            }

            for (Showing showing : selectedRoom.getShowingList()) {
                System.out.println(showing.getStartTime().minusMinutes(15).isAfter(dateTimePicker.getDateTimeValue()));
                System.out.println(showing.getStartTime().plusMinutes(selectedMovie.getDurationInMinutes() + 15).isBefore(dateTimePicker.getDateTimeValue()));
                if (
                        showing.getStartTime().minusMinutes(15).isBefore(dateTimePicker.getDateTimeValue()) &&
                                showing.getStartTime().plusMinutes(selectedMovie.getDurationInMinutes() + 15).isAfter(dateTimePicker.getDateTimeValue())) {
                    warningMessage.setText("there is already a movie planned in this time stamp!");
                    return;
                }
            }

            selectedRoom.addShowingList(new Showing(currentSelectedLocalDateTime, selectedMovie, selectedRoom.getSeats()));
            if (selectedRoom.getTitle().contains("1"))
                this.fillTableWithData(roomOneTableView, selectedRoom.getShowingList());
            else
                this.fillTableWithData(roomTwoTableView, selectedRoom.getShowingList());

            System.out.println("IT WORKS!");
        });

        clearBtnForCreatingShowing.setOnAction(actionEvent -> {
            roomComboBox.setItems(null);
            movieComboBox.setItems(null);
            roomComboBox.setItems(FXCollections.observableArrayList(db.getAllRooms()));
            movieComboBox.setItems(FXCollections.observableArrayList(db.getAllMovies()));
            dateTimePicker.setValue(null);
            movieEndTime.setText("");
            moviePrice.setText("");
            nOfSeats.setText("");
        });

        formHolders.getChildren().set(0, this.formMenu);
    }

}
