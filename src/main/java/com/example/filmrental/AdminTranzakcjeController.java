package com.example.filmrental;

import MappingClasses.Tranzakcje;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class AdminTranzakcjeController implements Initializable {


    @FXML
    private TableView<Tranzakcje> tableView;

    @FXML
    private TableColumn<Tranzakcje, String> data_tranzakcji;

    @FXML
    private TableColumn<Tranzakcje, String> data_zwrotu;

    @FXML
    private TableColumn<Tranzakcje, Integer> id;

    @FXML
    private TableColumn<Tranzakcje, Integer> id_user;

    @FXML
    private TableColumn<Tranzakcje, Integer> ilosc_filmow;

    @FXML
    private TableColumn<Tranzakcje, String> info;

    @FXML
    private TableColumn<Tranzakcje, Integer> koszt;

    @FXML
    private TableColumn<Tranzakcje, Integer> proby_kontaktu;

    @FXML
    private TableColumn<Tranzakcje, String> zwrocono;

    @FXML
    private TextField searchBox;

    public void search(){
        System.out.println("x");
    }

    private List<Tranzakcje> tranzakcje;
    ObservableList<Tranzakcje> observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        id_user.setCellValueFactory(new PropertyValueFactory<>("id_uzytkownika"));
        data_tranzakcji.setCellValueFactory(Job -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(dateFormat.format(Job.getValue().getData_tranzakcji()));
            return property;
        });
        data_zwrotu.setCellValueFactory(Job -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(dateFormat.format(Job.getValue().getData_zwrotu()));
            return property;
        });
        koszt.setCellValueFactory(new PropertyValueFactory<>("koszt"));
        ilosc_filmow.setCellValueFactory(new PropertyValueFactory<>("filmyCounter"));
        zwrocono.setCellValueFactory(Job -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue("NIE");
            if(Job.getValue().isDone()) property.setValue("TAK");
            return property;
        });
        proby_kontaktu.setCellValueFactory(new PropertyValueFactory<>("probyKontaktu"));


        reFetchAndRedisplay();
    }

    private static <T> List<T> loadAllData(Class<T> type, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        List<T> data = session.createQuery(criteria).getResultList();
        return data;
    }

    public void fetchData(){
        Configuration config = new Configuration().configure();

        config.addAnnotatedClass(MappingClasses.Tranzakcje.class);

        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(config.getProperties());
        SessionFactory factory = config.buildSessionFactory(builder.build());
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        tranzakcje =loadAllData(Tranzakcje.class,session);

        transaction.commit();
        session.close();
    }
    public void reFetchAndRedisplay(){
        fetchData();
        observableList.removeAll(observableList);
        for(Tranzakcje temp : tranzakcje){
            observableList.add(temp);
        }
        tableView.setItems(observableList);
    }

}
