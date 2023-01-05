package com.example.filmrental;

import MappingClasses.Uzytkownik;
import jakarta.persistence.PersistenceException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.net.URL;
import java.util.ResourceBundle;

public class userMojeKontoEditController implements Initializable {

    @FXML
    private PasswordField haslo;

    @FXML
    private Label hasloAlert;

    @FXML
    private Label nochangeAlert;

    @FXML
    private TextField nr_tel;

    @FXML
    private Label nr_telAlert;

    @FXML
    private Label usedNrAlert;

    Uzytkownik user;

    Stage originalStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(new Runnable() {
            public void run() {
                getData();
            }
        });
    }

    public void exit(){
        Stage stage1 = (Stage) usedNrAlert.getScene().getWindow();
        stage1.close();
        originalStage.close();
    }

    public void getData(){
        Stage stage = (Stage) usedNrAlert.getScene().getWindow();
        user = (Uzytkownik) stage.getUserData();
    }

    public void onChange(){
        nr_telAlert.setVisible(false);
        hasloAlert.setVisible(false);
        nochangeAlert.setVisible(false);

        if(!nr_tel.getText().equals("")) {
            if (!isTelNumberOk(nr_tel.getText())) nr_telAlert.setVisible(true);
        }

        if(!haslo.getText().equals("")) {
            if (!isHasloOk(haslo.getText())) hasloAlert.setVisible(true);
        }

        if((isHasloOk(haslo.getText()) || haslo.getText().equals("")) && (isTelNumberOk(nr_tel.getText()) || nr_tel.getText().equals(""))) {
            if (haslo.getText().equals("") && nr_tel.getText().equals("")) {
                nochangeAlert.setVisible(true);
            } else {
                Configuration config = new Configuration().configure();

                config.addAnnotatedClass(MappingClasses.Uzytkownik.class);

                StandardServiceRegistryBuilder builder =
                        new StandardServiceRegistryBuilder().applySettings(config.getProperties());

                SessionFactory factory = config.buildSessionFactory(builder.build());

                Session session = factory.openSession();
                Transaction transaction = session.beginTransaction();

                if (!nr_tel.getText().equals("")) user.setNr_tel(nr_tel.getText());
                if (!haslo.getText().equals("")) user.setHaslo(haslo.getText());

                try {
                    System.out.println(user);
                    session.update(user);
                    transaction.commit();
                    session.close();
                    exit();
                    originalStage.close();
                } catch (PersistenceException e) {
                    System.out.println(e);
                    usedNrAlert.setVisible(true);
                    session.close();
                }
            }
        }

    }

    private boolean isTelNumberOk(String s){
        return s.matches("\\d{9}");
    }

    private boolean isHasloOk(String s){
        return s.matches("(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }

    public void outerScene(Stage stage){
        originalStage = stage;
    }
}
