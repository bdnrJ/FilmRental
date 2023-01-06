package com.example.filmrental;

import MappingClasses.Film;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserFilmyNewController implements Initializable {
    @FXML
    private GridPane gridpane;

    @FXML
    private TextField searchBox;

    private List<Film> wyszukiwaneFilmy;
    private List<Film> filmy;
    ObservableList<Film> observableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reFetchAndRedisplay();
        displayFilms(filmy);
    }

    public void search(){
        ObservableList<Film> x = FXCollections.observableArrayList();
        int listLength  = observableList.toArray().length;
        if(searchBox.getText().isEmpty()){
            displayFilms(filmy);
        }else {
            for (int i = 0; i < listLength; i++) {
                if (observableList.get(i).getTytul().toLowerCase().contains(searchBox.getText().toLowerCase())) {
                    System.out.println(observableList.get(i).getTytul());
                    x.add(observableList.get(i));
                }
            }
            displayFilms(x);
        }
    }

    //ładowanie
    private static <T> List<T> loadAllData(Class<T> type, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        List<T> data = session.createQuery(criteria).getResultList();
        return data;
    }

    public void fetchData(){
        Configuration config = new Configuration().configure();

        config.addAnnotatedClass(MappingClasses.Film.class);

        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(config.getProperties());
        SessionFactory factory = config.buildSessionFactory(builder.build());
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        filmy=loadAllData(Film.class,session);

        transaction.commit();
        session.close();
    }
    public void reFetchAndRedisplay(){
        fetchData();
        observableList.removeAll(observableList);
        for(Film temp : filmy){
            observableList.add(temp);
        }
    }

    public void displayFilms(List<Film> searched){
        gridpane.getChildren().clear();

        int column=0;
        int row=1;

        try {
            for (int i = 0; i < searched.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("userFilmBox.fxml"));
                AnchorPane filmBox = fxmlLoader.load();
                UserFilmBox controller = fxmlLoader.getController();
                controller.setData(searched.get(i));

                if(column == 3){
                    column=0;
                    ++row;
                }

                gridpane.add(filmBox,column++,row);
                GridPane.setMargin(filmBox, new Insets(10));
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }


}
