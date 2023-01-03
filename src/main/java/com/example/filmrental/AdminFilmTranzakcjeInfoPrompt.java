package com.example.filmrental;

import MappingClasses.Film;
import MappingClasses.Item;
import MappingClasses.Tranzakcje;
import MappingClasses.Uzytkownik;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.List;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class AdminFilmTranzakcjeInfoPrompt implements Initializable {

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
    private Text tranzakcjaDataTranzakcji;

    @FXML
    private Text tranzakcjaDataZwrotu;

    @FXML
    private Text tranzakcjaId;

    @FXML
    private Text tranzakcjaKoszt;

    @FXML
    private Text tranzakcjaProbyKontatku;

    @FXML
    private Text tranzakcjaZwrocono;

    @FXML
    private Text userId;

    @FXML
    private Text userImie;

    @FXML
    private Text userNazwisko;

    @FXML
    private Text userNrtel;

    @FXML
    private Button zwroconeBtn;



    private Tranzakcje tranzakcja;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(new Runnable() {
            public void run() {
                getTranzakcjaData();
            }
        });
    }

    public void getTranzakcjaData(){
        Stage stage = (Stage) userId.getScene().getWindow();
        tranzakcja = (Tranzakcje) stage.getUserData();
        tranzakcjaKoszt.setText(tranzakcja.getKoszt()+"$");
        tranzakcjaProbyKontatku.setText(String.valueOf(tranzakcja.getProbyKontaktu()));
        tranzakcjaId.setText(String.valueOf(tranzakcja.getId()));
        tranzakcjaZwrocono.setText("NIE");
        if(tranzakcja.isDone()) {
            tranzakcjaZwrocono.setText("TAK");
            zwroconeBtn.setVisible(false);
        }


        String pattern = "yyyy-MM-dd";
        DateFormat df = new SimpleDateFormat(pattern);
        Date date = tranzakcja.getData_tranzakcji();
        String dataString = df.format(date);
        tranzakcjaDataTranzakcji.setText(dataString);
        date = tranzakcja.getData_zwrotu();
        dataString = df.format(date);
        tranzakcjaDataZwrotu.setText(dataString);


        getUserData(tranzakcja.getId_uzytkownika());
        getFilmyData(tranzakcja.getId());
    }

    public void getUserData(int idUzytkownika){
        Configuration config = new Configuration().configure();

        config.addAnnotatedClass(MappingClasses.Uzytkownik.class);

        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(config.getProperties());
        SessionFactory factory = config.buildSessionFactory(builder.build());
        Session session = factory.openSession();

        Uzytkownik uzytkownik = (Uzytkownik) session.get(Uzytkownik.class, idUzytkownika);
        session.close();

        userId.setText(String.valueOf(uzytkownik.getId()));
        userImie.setText(uzytkownik.getImie());
        userNazwisko.setText(uzytkownik.getNazwisko());
        userNrtel.setText(uzytkownik.getNr_tel());
    }

    public void getFilmyData(int idTranzakcji){
        System.out.println(idTranzakcji);
        Configuration config = new Configuration().configure();

        config.addAnnotatedClass(MappingClasses.Item.class);
        config.addAnnotatedClass(MappingClasses.Tranzakcje.class);
        config.addAnnotatedClass(MappingClasses.Film.class);

        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(config.getProperties());
        SessionFactory factory = config.buildSessionFactory(builder.build());
        Session session = factory.openSession();

        Tranzakcje tranzakcja = session.get(Tranzakcje.class, idTranzakcji);
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

    public void setZwrocone(){
        tranzakcja.setDone(true);
        tranzakcjaZwrocono.setText("TAK");
        zwroconeBtn.setVisible(false);
    }

    public void probyKontaktuplus(){
        int x = Integer.parseInt(tranzakcjaProbyKontatku.getText());
        x++;
        tranzakcjaProbyKontatku.setText(String.valueOf(x));
    }

    public void exit(){
        Stage stage = (Stage) userNrtel.getScene().getWindow();
        stage.close();
    }

    public void updateTranzakcja(){
        Configuration config = new Configuration().configure();

        config.addAnnotatedClass(MappingClasses.Tranzakcje.class);
        config.addAnnotatedClass(MappingClasses.Item.class);


        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(config.getProperties());
        SessionFactory factory = config.buildSessionFactory(builder.build());
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        tranzakcja.setProbyKontaktu(Integer.parseInt(tranzakcjaProbyKontatku.getText()));


        session.update(tranzakcja);
        transaction.commit();
        System.out.println(tranzakcja);
        session.close();

        exit();
    }

    @FXML
    public void deleteTranzakcja() throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("adminTranzakcjePromptDelete.fxml"));
        Parent root = (Parent) fxmlloader.load();
        root.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        Stage stage = new Stage();
        stage.setTitle("Usun Tranzakcje");
        stage.setUserData(tranzakcja);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();

        stage.setOnHidden(new EventHandler<WindowEvent>(){
            public void handle(WindowEvent we) {
                closeOrNotclose();
            }
        });
    }

    public void closeOrNotclose(){
        Configuration config = new Configuration().configure();

        config.addAnnotatedClass(MappingClasses.Tranzakcje.class);
        config.addAnnotatedClass(MappingClasses.Item.class);


        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(config.getProperties());
        SessionFactory factory = config.buildSessionFactory(builder.build());
        Session session = factory.openSession();

        Tranzakcje x =(Tranzakcje) session.find(Tranzakcje.class, tranzakcja.getId());
        session.close();
        if(x== null) exit();
    }

}
