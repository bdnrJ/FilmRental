package com.example.filmrental;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;

public class AdminFilmImageTooBigController {

    @FXML
    private Text sukcesja;

    @FXML
    void close() {
        Stage stage = (Stage) sukcesja.getScene().getWindow();
        stage.close();
    }


}
