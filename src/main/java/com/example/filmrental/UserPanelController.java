package com.example.filmrental;

import MappingClasses.Film;
import MappingClasses.Tranzakcje;
import MappingClasses.Uzytkownik;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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

public class UserPanelController implements Initializable {
    @FXML
    private BorderPane bp;
    @FXML
    private AnchorPane ap;
    @FXML
    private Text username;

    @FXML
    private Text filmyCounter;

    @FXML
    private Text tranzakcjeCounter;

    Uzytkownik x = new Uzytkownik();

    @FXML
    private void tranzakcje() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("userTranzakcje.fxml"));
        bp.setCenter(fxml);
    }

    @FXML
    private void filmy() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("userFilmyNew.fxml"));
        bp.setCenter(fxml);
    }

    @FXML
    private void mojekonto() throws IOException {
        Parent fxml = FXMLLoader.load(getClass().getResource("userMojeKonto.fxml"));
        bp.setCenter(fxml);
    }

    @FXML
    private void home(){
        bp.setCenter(ap);
    }

    public void exit(){
        Platform.exit();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(new Runnable() {
            public void run() {
                getData();
            }
        });
    }
    public void getData(){
        Stage stage = (Stage) bp.getScene().getWindow();
        x = (Uzytkownik) stage.getUserData();
        username.setText(x.getImie());
        getInfoAndDisplay();
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
        Query tranzakcjeQueryUzytkownika = session.createQuery("select count(*) from Tranzakcje where id_uzytkownika =:idUser");
        tranzakcjeQueryUzytkownika.setParameter("idUser",x.getId());

        Long filmyCount = (Long) filmyQuery.uniqueResult();
        Long tranzakcjeWTokuCount = (Long) tranzakcjeQueryUzytkownika.uniqueResult();

        filmyCounter.setText("("+filmyCount+" Filmów)");
        tranzakcjeCounter.setText("("+tranzakcjeWTokuCount+" Tranzakcji)");

//        Query query = session.createQuery("from Tranzakcje where isDone = 0 and data_zwrotu <=:dataZwrotu");
//        Date t = new Date();
//        query.setParameter("dataZwrotu",t);
//        List uzytkownicy = new ArrayList<>();
//        List listaTranzakcji = query.list();
//
//        for(int i=0; i < listaTranzakcji.size(); i++){
//            Tranzakcje x = (Tranzakcje) listaTranzakcji.get(i);
//            Uzytkownik temp = session.find(Uzytkownik.class, x.getId_uzytkownika());
//            if(!uzytkownicy.contains(temp)) uzytkownicy.add(temp);
//        }
//
//        if(uzytkownicy.size() == 1) dluznicyCounter.setText(uzytkownicy.size()+" Dłużnik");
//        else dluznicyCounter.setText(uzytkownicy.size()+" Dłużników");

        session.close();
    }
}
