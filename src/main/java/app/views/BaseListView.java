package app.views;

import app.database.Database;
import app.model.BaseModel;
import app.model.User;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BaseListView extends VBox {
    protected Database db;
    protected TableView<BaseModel> table;

    public BaseListView(Database database){
        db = database;
    }


    protected Label addHeaders(String headerName){
        this.setPadding(new Insets(20));

        Label heading = new Label();
        heading.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        heading.setText(headerName);
        heading.getStyleClass().add("header");

        return heading;
    }

    protected HBox createCrudButtons(String addText, String editText, String deleteText){
        HBox btnMenu = new HBox();
        btnMenu.setPadding(new Insets(20,0,0,0));
        btnMenu.setSpacing(10);

        Button addButton = new Button(addText);
        Button editButton = new Button(editText);
        Button deleteButton = new Button(deleteText);
        btnMenu.getChildren().addAll(addButton, editButton, deleteButton);

        addButton.setOnAction(actionEvent -> this.handleCreateBtnClick());
        editButton.setOnAction(actionEvent -> this.handleEditBtnClick());
        deleteButton.setOnAction(actionEvent -> this.handleDeleteBtnClick());

        return btnMenu;
    }

    protected void generateTable(){
        this.table = new TableView<>();
        this.table.setEditable(true);
        this.table.getSelectionModel().setCellSelectionEnabled(false); // false = row selection
        this.table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    protected void generateData(String[] columnNames){
        for(String name: columnNames){
            TableColumn<BaseModel, String> colType = new TableColumn<>(name);
            colType.setCellValueFactory(new PropertyValueFactory<>(name));
            this.table.getColumns().add(colType);
        }
    }

    protected void fillTableWithData(){}
    protected void handleCreateBtnClick(){}
    protected void handleEditBtnClick(){}
    protected void handleDeleteBtnClick(){}
}
