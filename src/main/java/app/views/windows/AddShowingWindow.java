package app.views.windows;

import app.database.Database;
import app.helpers.DateParser;
import app.helpers.controls.DateTimePicker;
import app.model.Movie;
import app.model.Room;
import app.model.Showing;
import app.model.TableHolderRooms;
import app.views.BaseForm;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class AddShowingWindow extends BaseForm {

    private ComboBox<Room> roomComboBox = new ComboBox<Room>();
    private ComboBox<Movie> movieComboBox = new ComboBox<Movie>();
    private DateTimePicker dateTimePicker = new DateTimePicker();
    private final Button addShowingBtnToRoom = new Button("Purchase");
    private final Button clearBtnForCreatingShowing = new Button("Clear");
    private final Label nOfSeats = new Label();
    private final Label movieEndTime = new Label();
    private final Label moviePrice = new Label();
    private Movie selectedMovie;
    private Room selectedRoom;
    private LocalDateTime currentSelectedLocalDateTime;

    public AddShowingWindow(Database db) {
        super(db);
        Scene mainScene = new Scene(layout);

        this.setHeaderName("Add Showing");
        stage.setWidth(1500);
        stage.setHeight(900);
        stage.setScene(mainScene);


        formHolders.getChildren().addAll(this.formMenu, warningMessage);
        layout.getChildren().addAll(this.createRoomGrids(), formHolders);

        setShowingForm();

        roomComboBox.setConverter(new StringConverter<Room>() {
            @Override
            public String toString(Room object) {
                if(object == null){
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
                if(object == null){
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

        dateTimePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            currentSelectedLocalDateTime = dateTimePicker.getDateTimeValue();
            fillFormCorrectly();
        });
    }

    private void fillFormCorrectly(){
        DecimalFormat df = new DecimalFormat("#.00");
        if(selectedMovie != null){
            moviePrice.setText(df.format(selectedMovie.getPrice()));
        }

        if(selectedMovie != null && currentSelectedLocalDateTime != null){
            movieEndTime.setText(DateParser.toString(currentSelectedLocalDateTime.plusMinutes(selectedMovie.getDurationInMinutes())));
        }

        if(selectedRoom != null){
            nOfSeats.setText(String.valueOf(selectedRoom.getSeats()));
        }
    }

    private void setShowingForm(){
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

            if(selectedMovie == null){
                warningMessage.setText("there is no selected movie");
                return;
            }

            if(currentSelectedLocalDateTime == null){
                warningMessage.setText("there is no selected date time");
                return;
            }

            if(selectedRoom == null){
                warningMessage.setText("there is no selected room");
                return;
            }

            for (Showing showing : selectedRoom.getShowingList()) {
                if (
                        showing.getStartTime().minusMinutes(15).isAfter(dateTimePicker.getDateTimeValue()) &&
                                showing.getStartTime().minusMinutes(selectedMovie.getDurationInMinutes() + 15).isBefore(dateTimePicker.getDateTimeValue())) {
                    warningMessage.setText("there is already a movie planned in this time stamp!");
                    return;
                }
            }


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

        formHolders.getChildren().set(0,this.formMenu);
    }

    private ComboBox<String> generateComboBox(String[] comboBoxItems){
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPrefWidth(400);
        comboBox.getItems().addAll(comboBoxItems);
        comboBox.getSelectionModel().selectFirst();

        return comboBox;
    }
}
