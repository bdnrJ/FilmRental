package com.example.filmrental;

import MappingClasses.Film;
import MappingClasses.Tranzakcje;
import MappingClasses.Uzytkownik;
import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.query.sql.internal.SQLQueryParser;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class AdminUzytkownikPromptInfoController implements Initializable {

    @FXML
    private Text userId;

    @FXML
    private Text userImie;

    @FXML
    private Text userNazwisko;

    @FXML
    private Text userNrtel;

    @FXML
    private TableView<Tranzakcje> tableView;

    @FXML
    private TableColumn<Tranzakcje, String> data_tranzakcji;

    @FXML
    private TableColumn<Tranzakcje, String> data_zwrotu;

    @FXML
    private TableColumn<Tranzakcje, Integer> id;

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

    private Uzytkownik x;

    private List<Tranzakcje> tranzakcje;
    ObservableList<Tranzakcje> observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(new Runnable() {
            public void run() {
                getData();
            }
        });

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        id.setCellValueFactory(new PropertyValueFactory<>("id"));
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
                                .glyph(FontAwesomeIcons.UMBRELLA)
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

    }

    public void getData(){
        Stage stage = (Stage) userId.getScene().getWindow();
        x = (Uzytkownik) stage.getUserData();
        userId.setText(String.valueOf(x.getId()));
        userImie.setText(x.getImie());
        userNazwisko.setText(x.getNazwisko());
        userNrtel.setText(x.getNr_tel());

        reFetchAndRedisplay();
    }

    public void reFetchAndRedisplay(){
        fetchData();
        observableList.removeAll(observableList);
        for(Tranzakcje temp : tranzakcje){
            observableList.add(temp);
        }
            tableView.setItems(observableList);
    }
    @FXML
    public void goToInfo(Tranzakcje tranzakcja) throws IOException {
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

//        tranzakcje = loadAllData(Tranzakcje.class,session);

        Query query = session.createQuery("from Tranzakcje where id_uzytkownika =:idUzytkownik");
        query.setParameter("idUzytkownik",x.getId());

        List list = query.list();

        tranzakcje = list;

        System.out.println(list);

        transaction.commit();
        session.close();
    }
}
