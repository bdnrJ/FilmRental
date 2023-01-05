package com.example.filmrental;

import MappingClasses.Uzytkownik;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class userMojeKontoEditAuthController implements Initializable {

    @FXML
    private Button okBtn;

    @FXML
    private Button okBtn1;

    @FXML
    private PasswordField passField;

    @FXML
    private Text title;

    @FXML
    private Text alert;

    Uzytkownik x;

    Stage pStage;

    Stage thisStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(new Runnable() {
            public void run() {
                getData();
            }
        });
    }

    public void getData(){
        Stage stage = (Stage) title.getScene().getWindow();
        thisStage = stage;
        x = (Uzytkownik) stage.getUserData();
    }

    public void ok() throws IOException {
        if(passField.getText().equals(x.getHaslo())){
            editDane();
        }else{
            alert.setVisible(true);
        }
    }

    public void getParentScene(Stage parentStage){
        pStage = parentStage;
    }

    public void exit(){
        Stage stage1 = (Stage) title.getScene().getWindow();
        stage1.close();
    }

    public void editDane() throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("userMojeKontoEdit.fxml"));
        Parent root = fxmlloader.load();
        root.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(thisStage);
        stage.setTitle("Edycja danych - "+x.getImie()+" "+x.getNazwisko());
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setUserData(x);
        stage.show();

        userMojeKontoEditController controller = fxmlloader.getController();
        controller.outerScene(thisStage);
    }
}
