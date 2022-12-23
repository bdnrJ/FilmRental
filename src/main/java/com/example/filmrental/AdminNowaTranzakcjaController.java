package com.example.filmrental;

import MappingClasses.Film;
import MappingClasses.Uzytkownik;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AdminNowaTranzakcjaController implements Initializable {

    @FXML
    private TableView<Uzytkownik> tableViewUzytkownik;
    @FXML
    private TableColumn<Uzytkownik,Integer> idUzytkownika;
    @FXML
    private TableColumn<Uzytkownik,String> imie;
    @FXML
    private TableColumn<Uzytkownik,String> nazwisko ;
    @FXML
    private TableColumn<Uzytkownik,String> nrtel;


    @FXML
    private TableView<Film> tableViewFilm;
    @FXML
    private TableColumn<Film,Integer> id;
    @FXML
    private TableColumn<Film,String> tytul;
    @FXML
    private TableColumn<Film, String> date;
    @FXML
    private Text user;
    @FXML
    private Text film;
    @FXML
    private Text koszt;
    @FXML
    private Text todayDate;
    @FXML
    private Text dataZwrotu;

    @FXML
    private ChoiceBox<String> okresWynajmu;
    private String[] opcje = {"7 dni","14 dni","30 dni"};

    @FXML
    private Button selectUzytkownikBtn;
    @FXML
    private Button selectFilmBtn;

    @FXML
    private TextField searchBoxFilm;
    @FXML
    private TextField searchBoxUzytkownik;

    Film wybranyFilm = new Film();
    Uzytkownik wybranyUzytkownik = new Uzytkownik();
    Date today = new Date();

    private List<Film> filmy;
    ObservableList<Film> observableListFilmy = FXCollections.observableArrayList();

    private List<Uzytkownik> uzytkownicy;
    ObservableList<Uzytkownik> observableListUzytkownicy = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        todayDate.setText(dateFormat.format(today));

        setKoszt(7);
        setDataZwrotu(7);

        okresWynajmu.getItems().addAll(opcje);
        okresWynajmu.setValue("7 dni");
        okresWynajmu.setOnAction(this::getDataZwrotu);

        id.setCellValueFactory(new PropertyValueFactory<Film,Integer>("id"));
        tytul.setCellValueFactory(new PropertyValueFactory<Film,String>("tytul"));
        date.setCellValueFactory(Job -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(dateFormat.format(Job.getValue().getDataPremiery()));
            return property;
        });

        idUzytkownika.setCellValueFactory(new PropertyValueFactory<Uzytkownik,Integer>("id"));
        imie.setCellValueFactory(new PropertyValueFactory<Uzytkownik,String>("imie"));
        nazwisko.setCellValueFactory(new PropertyValueFactory<Uzytkownik,String>("nazwisko"));
        nrtel.setCellValueFactory(new PropertyValueFactory<Uzytkownik,String>("Nr_tel"));

        fetchAndDisplayUzytkownicy();
        fetchAndDisplayFilmy();
    }

    public void searchFilm(){
        ObservableList<Film> x = FXCollections.observableArrayList();
        int listLength  = observableListFilmy.toArray().length;
        if(searchBoxFilm.getText().isEmpty()){
            tableViewFilm.setItems(observableListFilmy);
            tableViewFilm.refresh();
        }else {
            for (int i = 0; i < listLength; i++) {
                if (observableListFilmy.get(i).getTytul().toLowerCase().contains(searchBoxFilm.getText().toLowerCase())) {
                    System.out.println(observableListFilmy.get(i).getTytul());
                    x.add(observableListFilmy.get(i));
                }
            }
            tableViewFilm.setItems(x);
        }
    }

    private static <T> List<T> loadAllData(Class<T> type, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        List<T> data = session.createQuery(criteria).getResultList();
        return data;
    }

    public void fetchFilmy(){
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
    public void fetchAndDisplayFilmy(){
        fetchFilmy();
        observableListFilmy.removeAll(observableListFilmy);
        for(Film temp : filmy){
            observableListFilmy.add(temp);
        }
        tableViewFilm.setItems(observableListFilmy);
    }

    public void selectFilm(){
        if(tableViewFilm.getSelectionModel().getSelectedItem() != null) {
            wybranyFilm = tableViewFilm.getSelectionModel().getSelectedItem();
            DateFormat dateFormat = new SimpleDateFormat("yyyy");
            String g = dateFormat.format(wybranyFilm.getDataPremiery());
            film.setText(wybranyFilm.getTytul() + " (" + g + ")");
        }
    }

    public void setDataZwrotu(int x){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH,x);
        dataZwrotu.setText(dateFormat.format(cal.getTime()));
    }

    public void setKoszt(int x){
        koszt.setText(+x+"$");
    }

    public void getDataZwrotu(ActionEvent event){
        String wybor = okresWynajmu.getValue();
        switch (wybor) {
            case "7 dni":{
                setDataZwrotu(7);
                setKoszt(7);
                break;
                }
            case "14 dni":{
                setDataZwrotu(14);
                setKoszt(10);
                break;
            }
            case "30 dni":{
                setDataZwrotu(30);
                setKoszt(14);
                break;
            }
        }
    }

    public void fetchUzytkownicy(){
        Configuration config = new Configuration().configure();

        config.addAnnotatedClass(MappingClasses.Uzytkownik.class);

        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(config.getProperties());
        SessionFactory factory = config.buildSessionFactory(builder.build());
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        uzytkownicy=loadAllData(Uzytkownik.class,session);

        transaction.commit();
        session.close();
    }
    public void fetchAndDisplayUzytkownicy(){
        fetchUzytkownicy();
        observableListUzytkownicy.removeAll(observableListUzytkownicy);
        for(Uzytkownik temp : uzytkownicy){
            observableListUzytkownicy.add(temp);
        }
        tableViewUzytkownik.setItems(observableListUzytkownicy);
    }

    public void searchUzytkownik(){
        ObservableList<Uzytkownik> x = FXCollections.observableArrayList();
        int listLength  = observableListFilmy.toArray().length;
        if(searchBoxUzytkownik.getText().isEmpty()){
            tableViewUzytkownik.setItems(observableListUzytkownicy);
            tableViewUzytkownik.refresh();
        }else {
            for (int i = 0; i < listLength; i++) {
                if (observableListUzytkownicy.get(i).getNazwisko().toLowerCase().contains(searchBoxUzytkownik.getText().toLowerCase())) {
                    System.out.println(observableListUzytkownicy.get(i).getNazwisko());
                    x.add(observableListUzytkownicy.get(i));
                }
            }
            tableViewUzytkownik.setItems(x);
        }
    }

    public void switcherooFilmy(){
        tableViewUzytkownik.setVisible(false);
        searchBoxUzytkownik.setVisible(false);
        selectUzytkownikBtn.setVisible(false);

        selectFilmBtn.setVisible(true);
        tableViewFilm.setVisible(true);
        searchBoxFilm.setVisible(true);
    }

    public void switcherooUzytkownik(){
        selectFilmBtn.setVisible(false);
        tableViewFilm.setVisible(false);
        searchBoxFilm.setVisible(false);

        tableViewUzytkownik.setVisible(true);
        searchBoxUzytkownik.setVisible(true);
        selectUzytkownikBtn.setVisible(true);
    }

    public void selectUzytkownik(){
        if(tableViewUzytkownik.getSelectionModel().getSelectedItem() != null) {
            wybranyUzytkownik = tableViewUzytkownik.getSelectionModel().getSelectedItem();
            user.setText(wybranyUzytkownik.getNazwisko()+" "+wybranyUzytkownik.getImie()
            +" "+wybranyUzytkownik.getNr_tel());
        }
    }
}
