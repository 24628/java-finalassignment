package app.views.windows;

import app.database.Database;
import app.helpers.Session;
import app.views.BaseForm;
import app.views.partial.UserListView;
import javafx.scene.Scene;

public class MainWindow extends BaseForm {

    public MainWindow(Database db) {
        super(db);
        // --BUTTON EVENTS-- //
        ticketButton.setOnAction(actionEvent -> layout.getChildren().set(1, new UserListView(db,this)));
        userButton.setOnAction(actionEvent -> layout.getChildren().set(1, new UserListView(db,this)));
        dashboardButton.setOnAction(actionEvent -> layout.getChildren().set(1,  new UserListView(db,this)));
        logoutButton.setOnAction(actionEvent -> logoutFromSession());

        // Add the menu and the view. Default view will be the student list view
        layout.getChildren().addAll(new UserListView(db,this));

        // Create the main scene.
        // Scene mainScene = new StyledScene(layout);
        Scene mainScene = new Scene(layout);

        // Let's go!
        stage.setTitle("NoDesk");
        stage.setWidth(1200);
        stage.setHeight(600);
        stage.setScene(mainScene);

        System.out.println(Session.getUser());
    }

    public void setTableView(String option){
        if (option.equalsIgnoreCase("Ticket"))
            layout.getChildren().set(1,  new UserListView(db,this));
        if (option.equalsIgnoreCase("User"))
            layout.getChildren().set(1, new UserListView(db,this));
    }
}
