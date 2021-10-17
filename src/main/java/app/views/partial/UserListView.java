package app.views.partial;

import app.database.Database;
import app.model.User;
import app.views.BaseListView;
import app.views.windows.MainWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class UserListView extends BaseListView {

    private MainWindow mainWindow;

    public UserListView(Database database, MainWindow mainWindow) {
        super(database);
        this.mainWindow = mainWindow;

        this.generateTable();

        this.fillTableWithData();

        Label heading = this.addHeaders("Users");

        TextField filterTable = new TextField();
        filterTable.setMaxWidth(200);
        filterTable.setPromptText("Enter something...");

        String[] columnNames = {"firstName", "lastName", "userType", "email", "phoneNumber", "location"};
        this.generateData(columnNames);

        HBox menu = this.createCrudButtons("add User", "edit User", "Delete User");

        getChildren().addAll(heading, filterTable, table, menu);
    }

    protected void fillTableWithData() {
        ObservableList<User> tableList = FXCollections.observableArrayList();
//        for (Document doc : db.findAll("users")) {
//            tableList.add(new User(
//                    doc.get("firstName").toString(),
//                    doc.get("lastName").toString(),
//                    doc.get("type").toString(),
//                    doc.get("email").toString(),
//                    doc.get("phonenumber").toString(),
//                    doc.get("location").toString()
//            ));
//        }
//
//        for (BaseModel item : tableList) {
//            table.getItems().add(item);
//        }
    }

}

