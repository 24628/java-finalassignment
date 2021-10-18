package app;

import app.database.Database;
import app.views.windows.AddMovieWindow;
import app.views.windows.AddShowingWindow;
import app.views.windows.PurchaseTicketWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // rendering the text more crisply
        System.setProperty("prism.lcdtext", "false");

        // We should open a login window, but that is outside of scope for this lesson.
        // So we go straight to the main window of the application


        Database db = new Database();

//        Form_Login form = new Form_Login(db);
//        form.getStage().show();
//
//        PurchaseTicketWindow mw = new PurchaseTicketWindow(db);
//        mw.getStage().show();
//
//        AddMovieWindow mw = new AddMovieWindow(db);
//        mw.getStage().show();

        AddShowingWindow mw = new AddShowingWindow(db);
        mw.getStage().show();

    }

    public static void main(String[] args) {
        //Connection = new Connection();
        launch();
    }
}