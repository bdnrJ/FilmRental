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
    private AnchorPane homeAnchorPane;


    @FXML
    private void uzytkownicy(MouseEvent event) throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("adminUzytkownicy.fxml"));
        bp.setCenter(fxml);
    }

    @FXML
    private void filmy() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("adminFilmy.fxml"));
        bp.setCenter(fxml);
    }

    @FXML
    private void nowaTranzakcja() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("adminNowaTranzakcja.fxml"));
        bp.setCenter(fxml);
    }

    @FXML
    private void tranzakcje() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("adminTranzakcje.fxml"));
        bp.setCenter(fxml);
    }

    @FXML
    private void home(){
        bp.setCenter(homeAnchorPane);
    }
    @FXML
    private void dluznicy() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("adminDluznicy.fxml"));
        bp.setCenter(fxml);
    }





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}