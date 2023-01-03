package com.example.filmrental;

import MappingClasses.Film;
import MappingClasses.Tranzakcje;
import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @FXML
    private CheckBox onlyDone;


    private List<Tranzakcje> tranzakcje;
    ObservableList<Tranzakcje> observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();

        String todayDateString = dateFormat.format(today);

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
            if(Job.getValue().isDone()) {
                property.setValue("TAK");

            }
            return property;
        });
        proby_kontaktu.setCellValueFactory(new PropertyValueFactory<>("probyKontaktu"));

        Callback<TableColumn<Tranzakcje,String>, TableCell<Tranzakcje,String>> cellFactory = (param) ->{
            final TableCell<Tranzakcje,String> edit = new TableCell<Tranzakcje,String>(){
                @Override
                public void updateItem(String item,boolean empty){
                    super.updateItem(item,empty);
                    if(empty){
                        setGraphic(null);
                        setText(null);
                    }
                    else{
                        Color paint = new Color(0.3882, 0.3725, 0.7804, 1.0);
                        GlyphIcon pen = GlyphsBuilder.create(FontAwesomeIcon.class)
                                .glyph(FontAwesomeIcons.INFO_CIRCLE)
                                .build();
                        pen.setFill(paint);
                        pen.setSize("1.8em");
                        //test

                        Button editButton = new Button();
                        editButton.getStyleClass().add("editTableBtn");
                        editButton.setGraphic(pen);
                        editButton.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
                        editButton.setOnAction(event ->{
                            Tranzakcje f = getTableView().getItems().get(getIndex());
                            try {
                                goToInfo(f);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });
                        setGraphic(editButton);
                        setText(null);
                    }
                }
            };
            return edit;
        };
        info.setCellFactory(cellFactory);


        reFetchAndRedisplay();
    }

    public void search() {

        ObservableList<Tranzakcje> x = FXCollections.observableArrayList();
        int listLength = observableList.toArray().length;
        if (!onlyDone.isSelected()) {
            if (searchBox.getText().isEmpty()) {
                tableView.setItems(observableList);
                tableView.refresh();
            } else {
                for (int i = 0; i < listLength; i++) {
                    if (String.valueOf(observableList.get(i).getId_uzytkownika()).contains(searchBox.getText().toLowerCase())) {
                        x.add(observableList.get(i));
                    }
                }
                tableView.setItems(x);
            }
        } else {
            if (searchBox.getText().isEmpty()) {
                filterDoneTransactions();
            } else {
                for (int i = 0; i < listLength; i++) {
                    if (String.valueOf(observableList.get(i).getId_uzytkownika()).contains(searchBox.getText().toLowerCase()) &&
                    !observableList.get(i).isDone()) {
                        x.add(observableList.get(i));
                    }
                }
                tableView.setItems(x);
            }
        }
    }


    public void filterDoneTransactions(){
        if(onlyDone.isSelected()) {
            ObservableList<Tranzakcje> x = FXCollections.observableArrayList();
            int listLength = observableList.toArray().length;
            for (int i = 0; i < listLength; i++) {
                if (!observableList.get(i).isDone()) {
                    x.add(observableList.get(i));
                }
            }
            tableView.setItems(x);
            tableView.refresh();
        }else{
            tableView.setItems(observableList);
            tableView.refresh();
        }
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
        config.addAnnotatedClass(MappingClasses.Item.class);

        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(config.getProperties());
        SessionFactory factory = config.buildSessionFactory(builder.build());
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        tranzakcje = loadAllData(Tranzakcje.class,session);

        transaction.commit();
        session.close();
    }
    public void reFetchAndRedisplay(){
        fetchData();
        observableList.removeAll(observableList);
        for(Tranzakcje temp : tranzakcje){
            observableList.add(temp);
        }
        if(onlyDone.isSelected()){
            filterDoneTransactions();
        }else {
            tableView.setItems(observableList);
        }
    }
    @FXML
    public void goToInfo(Tranzakcje tranzakcja) throws IOException{
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("adminTranzakcjePromptInfo.fxml"));
        Parent root = fxmlloader.load();
        root.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(tableView.getScene().getWindow());
        stage.setTitle("Tranzakcja - Informacje");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setUserData(tranzakcja);
        stage.show();
        stage.setOnHidden(new EventHandler<WindowEvent>(){
            public void handle(WindowEvent we) {
                reFetchAndRedisplay();
            }
        });
    }
}
