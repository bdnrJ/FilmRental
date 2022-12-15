package com.example.filmrental;

import MappingClasses.Film;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class PromptDeleteFilmController {

    @FXML
    private Text id;
    @FXML
    private Text tytul;
    @FXML
    private Button delete;
    @FXML
    private Button exit;

    Film x = new Film();



    public void initialize(){

        Platform.runLater(new Runnable() {
            public void run() {
                getData();
            }
        });
    }



    public void getData(){
        Stage stage = (Stage) id.getScene().getWindow();
        x = (Film) stage.getUserData();
        id.setText(String.valueOf(x.getId()));
        tytul.setText(x.getTytul());
    }

    public void usunFilm(){
        Configuration config = new Configuration().configure();

        config.addAnnotatedClass(MappingClasses.Film.class);

        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(config.getProperties());

        SessionFactory factory = config.buildSessionFactory(builder.build());

        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(x);
        transaction.commit();
        session.close();

        exit();

    }

    public void exit(){
            Stage stage1 = (Stage) id.getScene().getWindow();
            stage1.close();
    }
}
