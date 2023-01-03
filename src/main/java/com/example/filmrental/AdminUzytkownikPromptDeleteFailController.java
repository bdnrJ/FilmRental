package com.example.filmrental;

import MappingClasses.Tranzakcje;
import MappingClasses.Uzytkownik;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminUzytkownikPromptDeleteFailController implements Initializable {

    @FXML
    private Text sukcesja1;

    Uzytkownik x;

    //za grzechy ktore wlasnie popelniam
    Stage outer;
    public void outerScene(Stage stage){
        outer = stage;
    }

    public void zamknij(){
        Stage stage25 = (Stage) sukcesja1.getScene().getWindow();
        outer.close();
        stage25.close();
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
        Stage stage = (Stage) sukcesja1.getScene().getWindow();
        x = (Uzytkownik) stage.getUserData();
    }

    public void deleteUserAndTransactions(){
        Configuration config = new Configuration().configure();

        config.addAnnotatedClass(MappingClasses.Uzytkownik.class);
        config.addAnnotatedClass(MappingClasses.Tranzakcje.class);
        config.addAnnotatedClass(MappingClasses.Item.class);

        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(config.getProperties());

        SessionFactory factory = config.buildSessionFactory(builder.build());

        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("from Tranzakcje where id_uzytkownika =:idUser");
        query.setParameter("idUser",x.getId());
        List listaTranzakcji = query.list();

        for(int i=0; i < listaTranzakcji.size(); i++){
            Tranzakcje temp = (Tranzakcje) listaTranzakcji.get(i);
            session.delete(temp);
        }

        session.delete(x);

        transaction.commit();
        session.close();

        zamknij();
    }

}
