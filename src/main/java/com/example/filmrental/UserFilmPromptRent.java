package com.example.filmrental;

import MappingClasses.Film;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class UserFilmPromptRent implements Initializable {

    @FXML
    private Text stytul;
    @FXML
    private Text film;


    Film x = new Film();

    public void getData(){
        Stage stage = (Stage) stytul.getScene().getWindow();
        x = (Film) stage.getUserData();
        SimpleStringProperty property = new SimpleStringProperty();
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        property.setValue(dateFormat.format(x.getDataPremiery()));
        film.setText(x.getTytul()+" ("+property.getValue()+")");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(new Runnable() {
            public void run() {
                getData();
            }
        });
    }

    public void exit(){
        Stage stage1 = (Stage) stytul.getScene().getWindow();
        stage1.close();
    }
}
