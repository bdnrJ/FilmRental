package com.example.filmrental;

import MappingClasses.Film;
import MappingClasses.Item;
import MappingClasses.Tranzakcje;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private Text film1;
    @FXML
    private Text film2;
    @FXML
    private Text film3;
    @FXML
    private Text film4;

    @FXML
    private Button del;

    @FXML
    private Button del1;

    @FXML
    private Button del2;

    @FXML
    private Button del3;

    @FXML
    private Button del4;

    @FXML
    private Text koszt;
    @FXML
    private Text todayDate;
    @FXML
    private Text dataZwrotu;

    @FXML
    private Text alertAlreadyUsed;
    @FXML
    private Text alertFilmCap;
    @FXML
    private Text alertUserNotSelected;
    @FXML
    private Text alertNoneFilmSelected;

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

    private ArrayList<Film> wybraneFilmy = new ArrayList<>();


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
        okresWynajmu.setOnAction(this::setDataZwrotuAndKoszt);

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
        alertFilmCap.setVisible(false);
        alertAlreadyUsed.setVisible(false);
        if(tableViewFilm.getSelectionModel().getSelectedItem() != null) {
            if(wybraneFilmy.size() < 5) {
                if (!wybraneFilmy.contains(tableViewFilm.getSelectionModel().getSelectedItem())) {


                    wybranyFilm = tableViewFilm.getSelectionModel().getSelectedItem();
                    wybraneFilmy.add(wybranyFilm);
                    alertNoneFilmSelected.setVisible(false);
                    setDataZwrotuAndKoszt(null);
                    populateArrayLook();
                } else {
                    alertAlreadyUsed.setVisible(true);
                }
            } else {
                alertFilmCap.setVisible(true);
            }
        }
    }

    public Text ktoryFilm(int arraySize){
        switch (arraySize){
            case 0:{
                return film;
            }
            case 1: {
                return film1;
            }
            case 2: {
                return film2;
            }
            case 3: {
                return film3;
            }
            case 4: {
                return film4;
            }
        }
        return film;
    }

    public Button ktoryGuzik(int arraySize){
        switch (arraySize){
            case 0:{
                return del;
            }
            case 1: {
                return del1;
            }
            case 2: {
                return del2;
            }
            case 3: {
                return del3;
            }
            case 4: {
                return del4;
            }
        }
        return del1;
    }

    public void delete(ActionEvent event){
        //XD
        if(event.getSource() == del){
            wybraneFilmy.remove(0);
        }
        if(event.getSource() == del1){
            wybraneFilmy.remove(1);
        }
        if(event.getSource() == del2){
            wybraneFilmy.remove(2);
        }
        if(event.getSource() == del3){
            wybraneFilmy.remove(3);
        }
        if(event.getSource() == del4){
            wybraneFilmy.remove(4);
        }

        setDataZwrotuAndKoszt(null);
        populateArrayLook();
    }

    public void populateArrayLook(){
        setAllInvisible();

        DateFormat dateFormat = new SimpleDateFormat("yyyy");

        for(int i=0; i < wybraneFilmy.size(); i++){
            String g = dateFormat.format(wybraneFilmy.get(i).getDataPremiery());
            ktoryFilm(i).setText(wybraneFilmy.get(i).getTytul() + " (" + g + ")");
            ktoryFilm(i).setVisible(true);
            ktoryGuzik(i).setVisible(true);
        }
    }

    public void setAllInvisible(){
        film.setVisible(false);
        film1.setVisible(false);
        film2.setVisible(false);
        film3.setVisible(false);
        film4.setVisible(false);

        del.setVisible(false);
        del1.setVisible(false);
        del2.setVisible(false);
        del3.setVisible(false);
        del4.setVisible(false);
    }

    public void setDataZwrotu(int x){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH,x);
        dataZwrotu.setText(dateFormat.format(cal.getTime()));
    }

    public void setKoszt(int x){
        int cost = x*wybraneFilmy.size();
        if(cost == 0 ) cost = x;
        koszt.setText(+cost+"$");
    }

    public void setDataZwrotuAndKoszt(ActionEvent event){
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
            alertUserNotSelected.setVisible(false);
            wybranyUzytkownik = tableViewUzytkownik.getSelectionModel().getSelectedItem();
            user.setText(wybranyUzytkownik.getNazwisko()+" "+wybranyUzytkownik.getImie()
            +" "+wybranyUzytkownik.getNr_tel());
        }
    }

    public void addTranzakcja() throws ParseException {
        alertUserNotSelected.setVisible(false);
        alertNoneFilmSelected.setVisible(false);
        if(wybranyUzytkownik.getImie() != null) {
            if(wybraneFilmy.size()!=0) {
                Configuration config = new Configuration().configure();

                config.addAnnotatedClass(MappingClasses.Tranzakcje.class);
                config.addAnnotatedClass(MappingClasses.Item.class);

                StandardServiceRegistryBuilder builder =
                        new StandardServiceRegistryBuilder().applySettings(config.getProperties());
                SessionFactory factory = config.buildSessionFactory(builder.build());
                Session session = factory.openSession();
                Transaction transaction = session.beginTransaction();

                Tranzakcje T = new Tranzakcje();
                String d_zwrotu = dataZwrotu.getText();
                Date data_zwrotu = new SimpleDateFormat("yyyy-MM-dd").parse(d_zwrotu);


                T.setId_uzytkownika(wybranyUzytkownik.getId());
                T.setDone(false);
                T.setProbyKontaktu(0);
                T.setKoszt(Integer.parseInt(koszt.getText().replace("$", "")));
                T.setData_tranzakcji(today);
                T.setData_zwrotu(data_zwrotu);
                T.setFilmyCounter(wybraneFilmy.size());

                Set<Item> itemSet = new HashSet<Item>();


                for (int i = 0; i < wybraneFilmy.size(); i++) {
                    Item temp = new Item(wybraneFilmy.get(i).getId(), T);
                    itemSet.add(temp);
                }

                session.persist(T);

                for (int i = 0; i < wybraneFilmy.size(); i++) {
                    Item temp = new Item(wybraneFilmy.get(i).getId(), T);
                    session.persist(temp);
                }

                transaction.commit();
                session.close();
            } else {

                alertNoneFilmSelected.setVisible(true);
            }
        } else {
            if(wybraneFilmy.size() == 0) alertNoneFilmSelected.setVisible(true);
            alertUserNotSelected.setVisible(true);
        }
    }
}
