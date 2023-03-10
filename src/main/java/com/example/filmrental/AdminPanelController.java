package com.example.filmrental;

import MappingClasses.Film;
import MappingClasses.Tranzakcje;
import MappingClasses.Uzytkownik;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AdminPanelController implements Initializable {

    @FXML
    private BorderPane bp;
    @FXML
    private AnchorPane homeAnchorPane;

    @FXML
    private Text dluznicyCounter;
    @FXML
    private Text filmyCounter;
    @FXML
    private Text tranzakcjeCounter;
    @FXML
    private Text uzytkownicyCounter;
    @FXML
    private Text tranzakcjeWToku;


    @FXML
    private void uzytkownicy() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("adminUzytkownicy.fxml"));
        bp.setCenter(fxml);
    }

    @FXML
    private void filmy() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("adminFilmy.fxml"));
        bp.setCenter(fxml);
    }

    @FXML
    private void nowaTranzakcja() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("adminNowaTranzakcja.fxml"));
        bp.setCenter(fxml);
    }

    @FXML
    private void tranzakcje() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("adminTranzakcje.fxml"));
        bp.setCenter(fxml);
    }
    @FXML
    private void home(){
        bp.setCenter(homeAnchorPane);
        getInfoAndDisplay();
    }
    @FXML
    private void dluznicy() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("adminDluznicy.fxml"));
        bp.setCenter(fxml);
    }

    @FXML
    public void rejestracja() throws IOException{
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("rejestracja.fxml"));
        Parent root = fxmlloader.load();
        root.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(bp.getScene().getWindow());
        stage.setTitle("Tworzenie uzytkownika");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void login() throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = (Parent) fxmlloader.load();
        root.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        Stage stage = new Stage();
        stage.setTitle("login");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();

        Stage stage1 = (Stage) bp.getScene().getWindow();
        stage1.close();
    }

    public void exit(){
        Platform.exit();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getInfoAndDisplay();
    }

    public void getInfoAndDisplay(){
        Configuration config = new Configuration().configure();

        config.addAnnotatedClass(MappingClasses.Tranzakcje.class);
        config.addAnnotatedClass(MappingClasses.Item.class);
        config.addAnnotatedClass(MappingClasses.Uzytkownik.class);
        config.addAnnotatedClass(MappingClasses.Film.class);

        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(config.getProperties());
        SessionFactory factory = config.buildSessionFactory(builder.build());
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        Query filmyQuery = session.createQuery("select count(*) from Film");
        Query uzytkownicyQuery = session.createQuery("select count(*) from Uzytkownik");
        Query tranzakcjeQuery = session.createQuery("select count(*) from Tranzakcje");
        Query tranzakcjeQueryWToku = session.createQuery("select count(*) from Tranzakcje where isDone = 0");

        Long filmyCount = (Long) filmyQuery.uniqueResult();
        Long uzytkownicyCount = (Long) uzytkownicyQuery.uniqueResult();
        Long tranzakcjeCount = (Long) tranzakcjeQuery.uniqueResult();
        Long tranzakcjeWTokuCount = (Long) tranzakcjeQueryWToku.uniqueResult();

        filmyCounter.setText(filmyCount+" Film??w");
        uzytkownicyCounter.setText(uzytkownicyCount+" U??ytkownik??w");
        tranzakcjeCounter.setText(tranzakcjeCount+" Tranzakcji");
        tranzakcjeWToku.setText("("+tranzakcjeWTokuCount+" w toku)");

        Query query = session.createQuery("from Tranzakcje where isDone = 0 and data_zwrotu <=:dataZwrotu");
        Date t = new Date();
        query.setParameter("dataZwrotu",t);
        List uzytkownicy = new ArrayList<>();
        List listaTranzakcji = query.list();

        for(int i=0; i < listaTranzakcji.size(); i++){
            Tranzakcje x = (Tranzakcje) listaTranzakcji.get(i);
            Uzytkownik temp = session.find(Uzytkownik.class, x.getId_uzytkownika());
            if(!uzytkownicy.contains(temp)) uzytkownicy.add(temp);
        }

        if(uzytkownicy.size() == 1) dluznicyCounter.setText(uzytkownicy.size()+" D??u??nik");
        else dluznicyCounter.setText(uzytkownicy.size()+" D??u??nik??w");

        session.close();
    }
}