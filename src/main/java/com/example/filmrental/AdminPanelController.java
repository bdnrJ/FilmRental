package com.example.filmrental;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminPanelController implements Initializable {

    @FXML
    private BorderPane bp;
    @FXML
    private AnchorPane ap;

    @FXML
    private void home(MouseEvent event){

    }

    @FXML
    private void uzytkownicy(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("Uzytkownicy.fxml"));
        bp.setCenter(fxml);
    }

    @FXML
    private void filmy() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("filmy.fxml"));
        bp.setCenter(fxml);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}