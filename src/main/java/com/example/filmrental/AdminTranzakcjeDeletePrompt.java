package com.example.filmrental;

import MappingClasses.Tranzakcje;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminTranzakcjeDeletePrompt implements Initializable {

    @FXML
    private Button delete;

    Tranzakcje tranzakcja;

    public void exit(){
        Stage stage = (Stage) delete.getScene().getWindow();
        stage.close();
    }

    public void usunTranzakcje(){
        Configuration config = new Configuration().configure();

        config.addAnnotatedClass(MappingClasses.Tranzakcje.class);
        config.addAnnotatedClass(MappingClasses.Item.class);


        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(config.getProperties());
        SessionFactory factory = config.buildSessionFactory(builder.build());
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        session.delete(tranzakcja);
        transaction.commit();
        session.close();

        exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(new Runnable() {
            public void run() {
                getTranzakcjaData();
            }
        });
    }

    public void getTranzakcjaData(){
        Stage stage = (Stage) delete.getScene().getWindow();
        tranzakcja = (Tranzakcje) stage.getUserData();
    }
}
