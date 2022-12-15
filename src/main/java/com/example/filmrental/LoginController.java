package com.example.filmrental;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.List;

public class LoginController {

    @FXML
    private TextField nr_tel;

    @FXML
    private PasswordField haslo;

    @FXML
    private Label failAlert;

    public void login() throws IOException {
        Configuration config = new Configuration().configure();
        config.addAnnotatedClass(MappingClasses.Uzytkownik.class);

        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(config.getProperties());

        SessionFactory factory = config.buildSessionFactory(builder.build());
        Session session = factory.openSession();

        //FIXME
        // jesli mi sie zachce to zmien te metody bo sa "stare"
        Query query = session.createQuery("from Uzytkownik where Nr_tel=:nr_tel and haslo=:haslo");
        query.setParameter("nr_tel",nr_tel.getText());
        query.setParameter("haslo",haslo.getText());

        List list = query.list();



        if(list.size() == 1){
            Query isAdmin = session.createQuery("from Uzytkownik where Nr_tel=:nr_tel and haslo=:haslo AND isAdmin=1");
            isAdmin.setParameter("nr_tel",nr_tel.getText());
            isAdmin.setParameter("haslo",haslo.getText());
            List listA = isAdmin.list();
            if(listA.size() == 1) {
                System.out.println("admin");
                goToAdminPanel();
                closeCurrnetWindow();
            }else{
                System.out.println("plebejusz");
                goToUserPanel();
                closeCurrnetWindow();
            }
            System.out.println(list.get(0));
        }else{
            //nie ma takiego numeru
            failAlert.setVisible(true);
        }

    }

    @FXML
    public void goToRegister() throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("rejestracja.fxml"));
        Parent root = fxmlloader.load();
        root.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(failAlert.getScene().getWindow());
        stage.setTitle("Rejestracja");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void goToAdminPanel() throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("adminpanel.fxml"));
        Parent root = fxmlloader.load();
        root.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        Stage stage = new Stage();
        stage.setTitle("Admin panel");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();

    }

    @FXML
    public void goToUserPanel() throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("userPanel.fxml"));
        Parent root = (Parent) fxmlloader.load();
        root.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        Stage stage = new Stage();
        stage.setTitle("User panel");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();

    }

    public void closeCurrnetWindow() {

        Stage stage1 = (Stage) failAlert.getScene().getWindow();
        stage1.close();
    }


}
