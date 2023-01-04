package com.example.filmrental;

import MappingClasses.Film;
import MappingClasses.Item;
import MappingClasses.Tranzakcje;
import MappingClasses.Uzytkownik;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;


public class UserFilmTranzakcjeInfoPrompt implements Initializable {
    @FXML
    private Text film1;

    @FXML
    private Text film2;

    @FXML
    private Text film3;

    @FXML
    private Text film4;

    @FXML
    private Text film5;

    @FXML
    private Text tranzakcjaProbyKontatku;

    @FXML
    private Text tranzakcjaZwrocono;

    Tranzakcje x;
    Uzytkownik u;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(new Runnable() {
            public void run() {
                getData();
            }
        });
    }

    public void getData(){
        Stage stage = (Stage) film1.getScene().getWindow();
        x = (Tranzakcje) stage.getUserData();

        System.out.println(x.getId());
        Configuration config = new Configuration().configure();

        config.addAnnotatedClass(MappingClasses.Item.class);
        config.addAnnotatedClass(MappingClasses.Tranzakcje.class);
        config.addAnnotatedClass(MappingClasses.Film.class);

        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(config.getProperties());
        SessionFactory factory = config.buildSessionFactory(builder.build());
        Session session = factory.openSession();

        Tranzakcje tranzakcja = session.get(Tranzakcje.class, x.getId());
        System.out.println(tranzakcja);

        Set<Item> items =  tranzakcja.getItems();
        ArrayList<Film> filmy = new ArrayList<>();

        for(Item temp : items){
            Film x = session.get(Film.class, temp.getId_film());
            filmy.add(x);
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy");

        for(int i=0; i<filmy.size(); i++){
            String g = dateFormat.format(filmy.get(i).getDataPremiery());
            ktoryFilm(i).setVisible(true);
            ktoryFilm(i).setText(filmy.get(i).getTytul()+" ("+g+")");
        }

        tranzakcjaProbyKontatku.setText(String.valueOf(x.getProbyKontaktu()));
        tranzakcjaZwrocono.setText("NIE");
        if(x.isDone()) {
            tranzakcjaZwrocono.setText("TAK");
        }

    }

    public void getUser(Uzytkownik x){
        u = x;
    }

    public Text ktoryFilm(int arraySize){
        switch (arraySize){
            case 0:{
                return film1;
            }
            case 1: {
                return film2;
            }
            case 2: {
                return film3;
            }
            case 3: {
                return film4;
            }
            case 4: {
                return film5;
            }
        }
        return film1;
    }

    public void exit(){
        Stage stage = (Stage) film1.getScene().getWindow();
        stage.close();
    }

}
