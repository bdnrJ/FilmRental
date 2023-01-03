package com.example.filmrental;

import MappingClasses.Film;
import MappingClasses.Uzytkownik;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import java.util.Date;
import java.util.List;

public class AdminUserPromptDeleteController {
    @FXML
    private Text id;
    @FXML
    private Text imie;
    @FXML
    private Text nazwisko;
    @FXML
    private Text nr_tel;
    @FXML
    private Button delete;
    @FXML
    private Button exit;

    Uzytkownik x = new Uzytkownik();

    Stage thisStage;

    public void initialize(){

        Platform.runLater(new Runnable() {
            public void run() {
                getData();
            }
        });
    }



    public void getData(){
        Stage stage = (Stage) id.getScene().getWindow();
        x = (Uzytkownik) stage.getUserData();
        id.setText(String.valueOf(x.getId()));
        imie.setText(x.getImie());
        nazwisko.setText(x.getNazwisko());
        nr_tel.setText(x.getNr_tel());
        thisStage = (Stage) id.getScene().getWindow();
    }

    public void usunFilm() throws IOException {
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
        if(listaTranzakcji.size() == 0){
            session.delete(x);
            transaction.commit();
            session.close();
            exit();
        }else{
            session.close();
            goToDeleteAlert(x);
        }
    }

    public void exit(){
        Stage stage1 = (Stage) id.getScene().getWindow();
        stage1.close();
    }

    @FXML
    public void goToDeleteAlert(Uzytkownik uzytkownik) throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("adminFilmPrompDeleteFail.fxml"));
        Parent root = fxmlloader.load();
        root.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(id.getScene().getWindow());
        stage.setTitle("Usuwanie użytkownika - uwaga");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setUserData(uzytkownik);
        stage.show();


        //boze wybacz
        AdminUzytkownikPromptDeleteFailController controller = fxmlloader.getController();
        controller.outerScene(thisStage);
    }


}
