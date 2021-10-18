package app.views.windows;

import app.database.Database;
import app.helpers.controls.CustomSubmitBtn;
import app.helpers.controls.NumberTextField;
import app.model.Movie;
import app.views.BaseVBoxLayout;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.text.DecimalFormat;
import java.util.List;

public class AddMovieWindow extends BaseVBoxLayout {

    private VBox formHolders = new VBox();
    private GridPane formMenu = new GridPane();
    private Label warningMessage = new Label();
    private final NumberTextField minutesDurationField = new NumberTextField();
    private final TextField moviePrice = new TextField();
    private final TextField movieTitle = new TextField();
    private final Button createMovieBtn = new CustomSubmitBtn("Create Movie");
    private final Button clearFormBtn = new Button("Clear");
    TableView<Movie> tableView = generateTableForMovie();

    public AddMovieWindow(Database database) {
        super(database);

        System.out.println("ELLO!");

        setMovieForm();
        formHolders.getChildren().addAll(this.formMenu, warningMessage);
        this.getChildren().addAll(this.createMovieTableView(), formHolders);
    }

    private void setMovieForm() {
        this.formMenu = getBasicGridPane();

        this.formMenu.add(new Label("Minutes"), 0, 0, 1, 1);
        this.formMenu.add(new Label("Price"), 0, 1, 1, 1);
        this.formMenu.add(new Label("Title"), 0, 2, 1, 1);

        this.formMenu.add(minutesDurationField, 1, 0, 1, 1);
        this.formMenu.add(moviePrice, 1, 1, 1, 1);
        this.formMenu.add(movieTitle, 1, 2, 1, 1);

        this.formMenu.add(createMovieBtn, 2, 1, 1, 1);
        this.formMenu.add(clearFormBtn, 2, 2, 1, 1);


        createMovieBtn.setOnAction(actionEvent -> {
            warningMessage.setText("");
            if (minutesDurationField.getText() == null) {
                warningMessage.setText("Fill in the duration");
                return;
            }

            if (moviePrice.getText() == null) {
                warningMessage.setText("Fill in the price");
                return;
            }

            if (movieTitle.getText() == null) {
                warningMessage.setText("Fill in the movie Title");
                return;
            }

            double price;
            try {
                price = Double.parseDouble(moviePrice.getText());

            } catch (NumberFormatException e) {
                price = 12.00;
            }
            int min = Integer.parseInt(minutesDurationField.getText());
            String title = movieTitle.getText();
            db.addMovieToList(new Movie(min, title, price));

            this.fillTableWithMovieData(this.tableView, db.getAllMovies());
        });

        clearFormBtn.setOnAction(actionEvent -> {
            minutesDurationField.setText("");
            moviePrice.setText("");
            movieTitle.setText("");
        });
    }

    private TableView<Movie> createMovieTableView() {
        String[] columnNames = {"durationInMinutes", "Price", "Title"};

        this.generateDataForMovie(this.tableView, columnNames);
        this.fillTableWithMovieData(this.tableView, db.getAllMovies());
        return this.tableView;
    }

    protected TableView<Movie> generateTableForMovie() {
        TableView<Movie> table = new TableView<Movie>();
        table.setEditable(true);
        table.getSelectionModel().setCellSelectionEnabled(false); // false = row selection
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    private void generateDataForMovie(TableView<Movie> table, String[] columnNames) {
        for (String name : columnNames) {
            TableColumn<Movie, String> colType = new TableColumn<>(name);
            colType.setSortable(false);
            colType.setCellValueFactory(new PropertyValueFactory<>(name));
            table.getColumns().add(colType);
        }
    }

    private void fillTableWithMovieData(TableView<Movie> table, List<Movie> movies) {
        table.getItems().clear();

        DecimalFormat df = new DecimalFormat("#.00");

        for (Movie movie : movies) {
            table.getItems().addAll(movie);
        }
    }
}
