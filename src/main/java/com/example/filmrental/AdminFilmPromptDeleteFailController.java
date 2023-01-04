package com.example.filmrental;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminFilmPromptDeleteFailController implements Initializable {


    @FXML
    private Text sukcesja;

    Stage outer;
    public void outerScene(Stage stage){
        outer = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void close(){
        Stage stage25 = (Stage) sukcesja.getScene().getWindow();
        outer.close();
        stage25.close();
    }
}
