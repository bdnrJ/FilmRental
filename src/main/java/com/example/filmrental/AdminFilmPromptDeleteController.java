package com.example.filmrental;

import MappingClasses.Film;
import MappingClasses.Uzytkownik;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.List;

public class AdminFilmPromptDeleteController {

    @FXML
    private Text id;
    @FXML
    private Text tytul;
    @FXML
    private Button delete;
    @FXML
    private Button exit;

    Film x = new Film();

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
        x = (Film) stage.getUserData();
        id.setText(String.valueOf(x.getId()));
        tytul.setText(x.getTytul());
        thisStage= (Stage) id.getScene().getWindow();
    }

    public void usunFilm() throws IOException {
        Configuration config = new Configuration().configure();

        config.addAnnotatedClass(MappingClasses.Film.class);
        config.addAnnotatedClass(MappingClasses.Item.class);
        config.addAnnotatedClass(MappingClasses.Tranzakcje.class);


        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(config.getProperties());

        SessionFactory factory = config.buildSessionFactory(builder.build());

        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("from Item where id_film =:idFilmu");
        query.setParameter("idFilmu",x.getId());
        List listaTranzakcji = query.list();

        if(listaTranzakcji.size() == 0){
            session.delete(x);
            transaction.commit();
            session.close();
            exit();
            System.out.println(listaTranzakcji.size());
        }else{
            System.out.println(listaTranzakcji.size());
            goToDeleteAlert();
        }

    }

    public void exit(){
            Stage stage1 = (Stage) id.getScene().getWindow();
            stage1.close();
    }

    @FXML
    public void goToDeleteAlert() throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("adminFilmPromptDeleteFail.fxml"));
        Parent root = fxmlloader.load();
        root.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(id.getScene().getWindow());
        stage.setTitle("Usuwanie filmu - uwaga");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();


        //boze wybacz
        AdminFilmPromptDeleteFailController controller = fxmlloader.getController();
        controller.outerScene(thisStage);
    }
}
