package com.example.filmrental;

import MappingClasses.Uzytkownik;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLIntegrityConstraintViolationException;

public class RejestracjaController {
    @FXML
    private Label imieAlert;
    @FXML
    private Label nazwiskoAlert;
    @FXML
    private Label nr_telAlert;
    @FXML
    private Label hasloAlert;
    @FXML
    private Label succesAlert;

    @FXML
    private Label usedNrAlert;

    @FXML
    private TextField imie;
    @FXML
    private TextField nazwisko;

    @FXML
    private TextField nr_tel;

    @FXML
    private TextField haslo;

    @FXML
    public void onRegister(){
        //Tworzenie nowego uzytkownika z inputow
        Uzytkownik newUser = new Uzytkownik();
        newUser.setImie(imie.getText());
        newUser.setNazwisko(nazwisko.getText());
        newUser.setNr_tel(nr_tel.getText());
        newUser.setHaslo(haslo.getText());


        //na start beda falsami (przydatne po bledzie bo za pierwszym razem i tak sa)
        nr_telAlert.setVisible(false);
        nazwiskoAlert.setVisible(false);
        imieAlert.setVisible(false);
        hasloAlert.setVisible(false);

        //Sprawdzanie czy inputy sa git
        if(!isTelNumberOk(newUser.getNr_tel())) {
            System.out.println("chujowy numer");
            nr_telAlert.setVisible(true);
        }
        if(!isNazwiskoOk(newUser.getNazwisko())){
            System.out.println("zle imie");
            nazwiskoAlert.setVisible(true);
        }
        if(!isImieOk(newUser.getImie())){
            System.out.println("zle nazwisko");
            imieAlert.setVisible(true);
        }
        if(!isHasloOk(newUser.getHaslo())){
            System.out.println("zle haslo");
            hasloAlert.setVisible(true);
        }


        boolean clickable = true;
        //jesli wszyskite dane sa git jako tako to dodajemy do bazy danych
        if(isImieOk(newUser.getImie()) && isNazwiskoOk(newUser.getNazwisko()) && isTelNumberOk(newUser.getNr_tel()) && isHasloOk(newUser.getHaslo()) && clickable) {
            boolean x = true;
            Configuration config = new Configuration().configure();

            config.addAnnotatedClass(MappingClasses.Uzytkownik.class);

            StandardServiceRegistryBuilder builder =
                    new StandardServiceRegistryBuilder().applySettings(config.getProperties());

            SessionFactory factory = config.buildSessionFactory(builder.build());

            Session session = factory.openSession();
            Transaction transaction = session.beginTransaction();

            try {
                session.persist(newUser);
                transaction.commit();
                session.close();
            }catch(PersistenceException e){
                System.out.println(e);
                usedNrAlert.setVisible(true);
                x=false;
                session.close();
            }

            if(x){
                clickable = false;
                usedNrAlert.setVisible(false);
                succesAlert.setVisible(true);
                PauseTransition delay = new PauseTransition(Duration.seconds(1));
                delay.play();
                delay.setOnFinished( event -> close());
            }

        }

    }

    public void close() {

        Stage stage1 = (Stage) imieAlert.getScene().getWindow();
        stage1.close();
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
