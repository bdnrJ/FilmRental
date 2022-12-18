package com.example.filmrental;

import MappingClasses.Film;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PromptEdytujFilmController {
    @FXML
    private Text id;
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
    Film x = new Film();

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
        czasTrwania.setText(String.valueOf(x.getCzasTrwania()));

        String pattern = "yyyy-MM-dd";
        DateFormat df = new SimpleDateFormat(pattern);
        Date date = x.getDataPremiery();
        String dataString = df.format(date);
        dataPremiery.setText(dataString);

        jezyk.setText(x.getJezyk());
        kraj.setText(x.getKraj());
    }

    public void onEdit(){
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
            Film nowy = x;

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





            pushToDatabase(nowy);
        }
    }

    public void pushToDatabase(Film zedytowany){
        System.out.println(zedytowany);
        Configuration config = new Configuration().configure();

        config.addAnnotatedClass(MappingClasses.Film.class);

        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(config.getProperties());
        SessionFactory factory = config.buildSessionFactory(builder.build());
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        session.update(zedytowany);
        transaction.commit();
        session.close();

        Stage stage = (Stage) kraj.getScene().getWindow();
        stage.close();
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


}
