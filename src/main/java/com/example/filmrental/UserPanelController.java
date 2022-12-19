package com.example.filmrental;

import MappingClasses.Film;
import MappingClasses.Uzytkownik;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserPanelController implements Initializable {
    @FXML
    private BorderPane bp;
    @FXML
    private AnchorPane ap;
    @FXML
    private Text username;

    Uzytkownik x = new Uzytkownik();

    @FXML
    private void home(MouseEvent event){

    }

    @FXML
    private void uzytkownicy(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("adminUzytkownicy.fxml"));
        bp.setCenter(fxml);
    }

    @FXML
    private void filmy() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("userFilmy.fxml"));
        bp.setCenter(fxml);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(new Runnable() {
            public void run() {
                getData();
            }
        });
    }
    public void getData(){
        Stage stage = (Stage) bp.getScene().getWindow();
        x = (Uzytkownik) stage.getUserData();
        username.setText(x.getImie());
    }
}
