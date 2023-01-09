package com.example.filmrental;

import MappingClasses.Film;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Blob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdmindodajFilmController {

    @FXML
    private TextField czasTrwania;

    @FXML
    private Label czasTrwaniaAlert;

    @FXML
    private TextField dataPremiery;

    @FXML
    private Label dataPremieryAlert;

    @FXML
    private TextField jezyk;

    @FXML
    private Label jezykAlert;

    @FXML
    private TextField kraj;

    @FXML
    private Label krajAlert;

    @FXML
    private TextField tytul;

    @FXML
    private Label tytulAlert;

    private String imageSrc;

    @FXML
    private Label imgsrc;

    @FXML
    private ImageView chosenImage;

    Stage thisStage;

    Film nowy = new Film();

    private FileInputStream fis;

    public void onAdd() throws IOException {
        czasTrwaniaAlert.setVisible(false);
        tytulAlert.setVisible(false);
        jezykAlert.setVisible(false);
        krajAlert.setVisible(false);
        dataPremieryAlert.setVisible(false);


        //sprawdza czy wszystko git
        boolean allGood = true;
        if(!isTytulOK(tytul.getText())) {
            System.out.println(tytul.getText());
            tytulAlert.setVisible(true);
            allGood=false;
        }
        if(!isJezykOK(jezyk.getText())) {
            jezykAlert.setVisible(true);
            allGood=false;
        }
        if(!isKrajOK(kraj.getText())){
            krajAlert.setVisible(true);
            allGood=false;
        }
        if(!isCzasTrwaniaOK(czasTrwania.getText())){
            czasTrwaniaAlert.setVisible(true);
            allGood=false;
        }
        if(!isDataOK(dataPremiery.getText())){
            dataPremieryAlert.setVisible(true);
            allGood=false;
        }

        if(allGood) {
            //z stringa na date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date convertedCurrentDate = sdf.parse(dataPremiery.getText());
                nowy.setDataPremiery(convertedCurrentDate);
            } catch (ParseException pe) {
                System.out.println("zly string");
            }

            nowy.setCzasTrwania(Integer.parseInt(czasTrwania.getText()));
            nowy.setTytul(tytul.getText());
            nowy.setJezyk(jezyk.getText());
            nowy.setKraj(kraj.getText());


            System.out.println(nowy);


            pushToDatabase(nowy);
        }
    }


    //regex
    private boolean isDataOK(String s){
        return s.matches("^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$");
    }
    private boolean isJezykOK(String s){
        return s.matches("\\w{1,30}[a-zA-Z ]");
    }
    private boolean isTytulOK(String s){
        return s.matches("^[a-zA-Z ]{1,40}$");
    }
    private boolean isKrajOK(String s){
        return s.matches("\\w{2,24}[a-zA-Z]");
    }
    private boolean isCzasTrwaniaOK(String s){
        return s.matches("\\d{1,3}");
    }


    public void pushToDatabase(Film nowyfilm) throws IOException {
        Configuration config = new Configuration().configure();

        config.addAnnotatedClass(MappingClasses.Film.class);

        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(config.getProperties());
        SessionFactory factory = config.buildSessionFactory(builder.build());
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.persist(nowyfilm);
            transaction.commit();
            session.close();

            Stage stage = (Stage) kraj.getScene().getWindow();
            stage.close();
        }catch (Exception e){
            goToFailPrompt();
        }
    }

    @FXML
    public void goToFailPrompt() throws IOException{
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("adminFilmImageTooBigPrompt.fxml"));
        Parent root = fxmlloader.load();
        root.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(thisStage.getScene().getWindow());
        stage.setTitle("Error");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void fileChooser() throws IOException {
        thisStage = (Stage) tytulAlert.getScene().getWindow();
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files (*.jpg)","*.jpg"));
        File f = fc.showOpenDialog(thisStage);

        if(f != null){
            fis = new FileInputStream(f);
            imgsrc.setVisible(false);
            Image image = new Image(f.getAbsolutePath());
            chosenImage.setImage(image);
            nowy.setImage(fis.readAllBytes());
        }
    }


}
