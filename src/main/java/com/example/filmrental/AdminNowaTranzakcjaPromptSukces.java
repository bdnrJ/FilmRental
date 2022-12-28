package com.example.filmrental;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AdminNowaTranzakcjaPromptSukces {

    @FXML
    private Text sukcesja;

    public void close(){
        Stage stage1 = (Stage) sukcesja.getScene().getWindow();
        stage1.close();
    }
}
