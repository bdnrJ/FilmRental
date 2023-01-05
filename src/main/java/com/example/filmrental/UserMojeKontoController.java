package com.example.filmrental;

import MappingClasses.Film;
import MappingClasses.Uzytkownik;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.util.ResourceBundle;

public class UserMojeKontoController implements Initializable {

    @FXML
    private Text userFilmy;

    @FXML
    private Text userImie;

    @FXML
    private Text userKoszt;

    @FXML
    private Text userNazwisko;

    @FXML
    private Text userNrtel;

    @FXML
    private Text userTranzakcjeCount;

    Uzytkownik x;
    Stage thisStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(new Runnable() {
            public void run() {
                getAndPrintData();
            }
        });
    }

    public void getAndPrintData(){
        System.out.println("wykona sie");
        Stage stage = (Stage) userFilmy.getScene().getWindow();
        x = (Uzytkownik) stage.getUserData();
        thisStage = stage;

        userImie.setText(x.getImie());
        userNazwisko.setText(x.getNazwisko());
        userNrtel.setText(x.getNr_tel());

        Configuration config = new Configuration().configure();

        config.addAnnotatedClass(MappingClasses.Tranzakcje.class);
        config.addAnnotatedClass(MappingClasses.Item.class);

        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(config.getProperties());
        SessionFactory factory = config.buildSessionFactory(builder.build());
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        Query tranzakcjeQueryUzytkownika = session.createQuery("select count(*) from Tranzakcje where id_uzytkownika =:idUser");
        tranzakcjeQueryUzytkownika.setParameter("idUser",x.getId());
        Long tranzakcjeWTokuCount = (Long) tranzakcjeQueryUzytkownika.uniqueResult();
        System.out.println("t "+tranzakcjeWTokuCount);

        Query kosztQuery = session.createQuery("select sum(koszt) from Tranzakcje where id_uzytkownika =:idUser");
        kosztQuery.setParameter("idUser",x.getId());
        Long koszta = (Long) kosztQuery.uniqueResult();

        Query filmyQuery = session.createQuery("select sum(filmyCounter) from Tranzakcje where id_uzytkownika =:idUser");
        filmyQuery.setParameter("idUser",x.getId());
        Long filmyCount = (Long) filmyQuery.uniqueResult();

        userTranzakcjeCount.setText(String.valueOf(tranzakcjeWTokuCount));
        userKoszt.setText(koszta+"$");
        userFilmy.setText(String.valueOf(filmyCount));
        if(filmyCount == null) userFilmy.setText("0");
        if(koszta == null) userKoszt.setText("0");

        transaction.commit();
        session.close();
    }

    public void editDane() throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("userMojeKontoEditAuth.fxml"));
        Parent root = fxmlloader.load();
        root.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(userFilmy.getScene().getWindow());
        stage.setTitle("Edycja danych - potwierdź hasło");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setUserData(x);
        stage.show();

        userMojeKontoEditAuthController controller = fxmlloader.getController();
        controller.getParentScene(thisStage);
        stage.setOnHidden(new EventHandler<WindowEvent>(){
            public void handle(WindowEvent we) {
                    getAndPrintData();
                }
        });
    }

    public void updateData(){
        Configuration config = new Configuration().configure();

        config.addAnnotatedClass(MappingClasses.Tranzakcje.class);
        config.addAnnotatedClass(MappingClasses.Item.class);

        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(config.getProperties());
        SessionFactory factory = config.buildSessionFactory(builder.build());
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        Uzytkownik zakt = session.find(Uzytkownik.class,x.getId());
        userNrtel.setText(zakt.getNr_tel());


        session.close();
    }
}
