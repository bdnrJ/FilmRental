package com.example.filmrental;

import MappingClasses.Film;
import MappingClasses.Uzytkownik;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class UserFilmPromptRent implements Initializable {

    @FXML
    private Text stytul;
    @FXML
    private Text film;
    @FXML
    private Text user;
    @FXML
    private ChoiceBox<String> okresWynajmu;
    private String[] opcje = {"7 dni","14 dni","30 dni"};
    @FXML
    private TextField currentDate;

    Film x = new Film();
    Uzytkownik u = new Uzytkownik();
    Date today = new Date();


    public void getData(){
        Stage stage = (Stage) stytul.getScene().getWindow();
        x = (Film) stage.getUserData();

        SimpleStringProperty dataPremiery = new SimpleStringProperty();
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        dataPremiery.setValue(dateFormat.format(x.getDataPremiery()));

        film.setText(x.getTytul()+" ("+dataPremiery.getValue()+")");
    }

    public void initUser(Uzytkownik u){
        System.out.println(u.getNazwisko());
        user.setText(u.getNazwisko());


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        currentDate.setText(dateFormat.format(today));

        okresWynajmu.getItems().addAll(opcje);
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
