package app.helpers.controls;

import javafx.scene.control.Button;

public class CustomSubmitBtn extends Button {

    public CustomSubmitBtn(String text){
        this.setText(text);
        this.setPrefHeight(30);
        this.setDefaultButton(true);
        this.setPrefWidth(120);
    }
}
