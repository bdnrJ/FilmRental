package com.example.filmrental;

import MappingClasses.Film;
import MappingClasses.Uzytkownik;
import jakarta.persistence.PersistenceException;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class AdminUserPromptEditController implements Initializable {

    @FXML
    private PasswordField haslo;

    @FXML
    private Label hasloAlert;

    @FXML
    private TextField imie;

    @FXML
    private Label imieAlert;

    @FXML
    private TextField nazwisko;

    @FXML
    private Label nazwiskoAlert;

    @FXML
    private TextField nr_tel;

    @FXML
    private Label nr_telAlert;

    @FXML
    private Label succesAlert;

    @FXML
    private Label usedNrAlert;

    Uzytkownik user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(new Runnable() {
            public void run() {
                getData();
            }
        });
    }

    public void onChange(){
        nr_telAlert.setVisible(false);
        nazwiskoAlert.setVisible(false);
        imieAlert.setVisible(false);
        hasloAlert.setVisible(false);

        //Sprawdzanie czy inputy sa git
        if (!isTelNumberOk(nr_tel.getText())) nr_telAlert.setVisible(true);
        if (!isNazwiskoOk(nazwisko.getText())) nazwiskoAlert.setVisible(true);
        if (!isImieOk(imie.getText())) imieAlert.setVisible(true);
        if (!isHasloOk(haslo.getText())) hasloAlert.setVisible(true);

        if(isImieOk(imie.getText()) && isNazwiskoOk(nazwisko.getText()) && isTelNumberOk(nr_tel.getText()) && isHasloOk(haslo.getText())) {
            Configuration config = new Configuration().configure();
            config.addAnnotatedClass(MappingClasses.Uzytkownik.class);
            StandardServiceRegistryBuilder builder =
                    new StandardServiceRegistryBuilder().applySettings(config.getProperties());

            SessionFactory factory = config.buildSessionFactory(builder.build());
            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();

            user.setHaslo(haslo.getText());
            user.setNr_tel(nr_tel.getText());
            user.setImie(imie.getText());
            user.setNazwisko(nazwisko.getText());

            try {
                session.update(user);
                transaction.commit();
                session.close();
                exit();
            } catch (PersistenceException e) {
                System.out.println(e);
                usedNrAlert.setVisible(true);
                session.close();
            }

        }
    }

    public void exit(){
        Stage stage1 = (Stage) imieAlert.getScene().getWindow();
        stage1.close();
    }

    public void getData(){
        Stage stage = (Stage) imieAlert.getScene().getWindow();
        user = (Uzytkownik) stage.getUserData();

        imie.setText(user.getImie());
        nazwisko.setText(user.getNazwisko());
        nr_tel.setText(user.getNr_tel());
        haslo.setText(user.getHaslo());
    }

    //regex
    private boolean isTelNumberOk(String s){
        return s.matches("\\d{9}");
    }
    private boolean isImieOk(String s){
        return s.matches("\\w{2,24}[a-z]");
    }
    private boolean isNazwiskoOk(String s){
        return s.matches("\\w{2,24}[a-z]");
    }
    private boolean isHasloOk(String s){
        return s.matches("(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }
}
