package app.views.windows;

import app.database.Database;
import app.model.Ticket;
import app.views.BaseForm;
import com.mongodb.client.model.Filters;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Form_Ticket extends BaseForm {

    // db
    private Database db;

    // all form items
    private DatePicker reported;
    private TextField incident;
    private ComboBox type;
    private ComboBox user;
    private ComboBox priority;
    private DatePicker deadline;
    private TextField description;

    // cmb values
    private String[] comboBoxTypes = {"Choice1", "choice2", "choice3"};
    private String[] comboBoxUserNames = {"Bram", "Koen", "Noor"};
    private String[] comboBoxPriorityNames = {"LOW", "MEDIUM", "HIGH"};

    public Form_Ticket(Ticket ticket) {
        // db conn
        //db = new Database("ProjectNoSQL");
        db = new Database("noSql");

        // --CRUD FORM-- //
        this.addUIControls(this.form, ticket);

        // Add the menu and the view. Default view will be the student list view
        layout.getChildren().addAll(this.form);

        // Create the main scene.
        Scene form_Ticket = new Scene(layout);

        // Let's go!
        stage.setTitle("Form Ticket");
        stage.setScene(form_Ticket);
    }

    protected void addUIControls(GridPane gridPane, Ticket ticket) {
        // Add Header
        Label headerLabel = new Label("Create Ticket");
        headerLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        Control[] formItems;
        if (ticket == null)
            formItems = this.createFormItems();
        else
            formItems = this.createFormItems(ticket);


        Button cancelButton = this.generateFormBtn("CANCEL", 1);
        Button submitButton = this.generateFormBtn("SUBMIT TICKET", 0);

        submitButton.setOnAction(actionEvent -> this.handleSubmitBtnClick(formItems, ticket));
    }

    private Control[] createFormItems(){
        Control[] formItems = {
                reported = this.generateDatePicker("Date/time reported: ",1),
                incident = this.generateTextField("Subject of incident:: ", 2),
                type = this.generateComboBox("Type of incident:", comboBoxTypes, 3),
                user = this.generateComboBox("Reported by user:", comboBoxUserNames, 4),
                priority = this.generateComboBox("Priority", comboBoxPriorityNames,5),
                deadline = this.generateDatePicker("Deadline/follow up: ", 6),
                description = this.generateTextField("Description: ", 7),
        };
        return formItems;
    }

    private Control[] createFormItems(Ticket ticket){
        reported = this.generateDatePicker("Date/time reported: ", 1);
        reported.setValue(ticket.getReported().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        incident = this.generateTextField("Subject of incident: ", 2);
        incident.setText(ticket.getIncident());

        type = this.generateComboBox("Type of incident:", comboBoxTypes, 3);
        type.getSelectionModel().select(getCMBIndex((ComboBox<String>) type, ticket.getType()));

        user = this.generateComboBox("Reported by user:", comboBoxUserNames, 4);
        user.getSelectionModel().select(getCMBIndex((ComboBox<String>) user, ticket.getUser_id()));

        priority = this.generateComboBox("Priority", comboBoxPriorityNames, 5);
        priority.getSelectionModel().select(getCMBIndex((ComboBox<String>) priority, ticket.getPriority()));

        deadline = this.generateDatePicker("Deadline/follow up: ", 6);
        deadline.setValue(ticket.getDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        description = this.generateTextField("Description: ", 7);
        description.setText(ticket.getDescription());

        Control[] formItems = { reported, incident, type, user, priority, deadline, description};
        return formItems;
    }

    protected void handleSubmitBtnClick(Control[] formItems, Ticket ticket){
        System.out.println("Handle Submit!");
        List<String> data = new ArrayList<String>();

        for (Control item : formItems) {
            if(item instanceof TextField){
                final TextField parsedTextField = (TextField) item;
                data.add(parsedTextField.getText());
            }
            if(item instanceof ComboBox){
                final ComboBox parsedComboBox = (ComboBox) item;
                data.add(parsedComboBox.getValue().toString());
            }
            if(item instanceof DatePicker){
                final DatePicker parsedDatePicker = (DatePicker) item;
                data.add(parsedDatePicker.getValue().toString());
            }
        }

        if (ticket == null)
            db.insertOne(generateDocument(data), "Tickets");
        else{
            Bson filter = Filters.eq("incident", ticket.getIncident());
            db.replaceOne(filter, generateDocument(data), "Tickets");
        }
    }



    
    // helper methods
    private Document generateDocument(List<String> data){
        // new document and all column names
        Document document = new Document();
        String[] columnNames = {"Reported", "incident", "type", "user_id", "priority", "deadline", "description"};

        // create document
        for (int i = 0; i < data.size(); i++) {
            document.append(columnNames[i], data.get(i));
        }

        // return document
        return document;
    }

    private int getCMBIndex(ComboBox<String> box, String value){
        int index = 0;
        for (String s:box.getItems()) {
            if (s.equalsIgnoreCase(value))
                break;
            index++;
        }
        return index;
    }
}
