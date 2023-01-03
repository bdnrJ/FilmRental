package com.example.filmrental;

import MappingClasses.Tranzakcje;
import MappingClasses.Uzytkownik;
import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
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
import org.hibernate.query.Query;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AdminDluznicyController implements Initializable {

    @FXML
    private TableView<Uzytkownik> tableView;
    @FXML
    private TableColumn<Uzytkownik,Integer> id;
    @FXML
    private TableColumn<Uzytkownik,String> imie;
    @FXML
    private TableColumn<Uzytkownik,String> nazwisko ;
    @FXML
    private TableColumn<Uzytkownik,String> nr_tel;
    @FXML
    private TextField searchBox;

    @FXML
    private TableColumn infoCol;

    private List<Tranzakcje> tranzakcje;
    private List<Uzytkownik> uzytkownicy = new ArrayList<>();
    ObservableList<Uzytkownik> observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        imie.setCellValueFactory(new PropertyValueFactory<>("imie"));
        nazwisko.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
        nr_tel.setCellValueFactory(new PropertyValueFactory<>("Nr_tel"));

        Callback<TableColumn<Uzytkownik,String>, TableCell<Uzytkownik,String>> infoCellFactory = (param) ->{
            final TableCell<Uzytkownik,String> delete = new TableCell<Uzytkownik,String>(){
                @Override
                public void updateItem(String item,boolean empty){
                    super.updateItem(item,empty);
                    if(empty){
                        setGraphic(null);
                        setText(null);
                    }
                    else{
                        Color paint = new Color(0.9176, 0.3333, 0.3333, 1.0);

                        GlyphIcon trash = GlyphsBuilder.create(FontAwesomeIcon.class)
                                .glyph(FontAwesomeIcons.INFO)
                                .build();
                        trash.setFill(paint);
                        trash.setSize("1.8em");

                        final Button deleteButton = new Button();
                        deleteButton.setGraphic(trash);
                        deleteButton.getStyleClass().add("infoTableBtn");
                        deleteButton.getStylesheets().add(getClass().getResource("app.css").toExternalForm());

                        deleteButton.setOnAction(event ->{
                            Uzytkownik f = getTableView().getItems().get(getIndex());
                            try {
                                goToMoreInfo(f);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });
                        setGraphic(deleteButton);
                        setText(null);
                    }
                }
            };
            return delete;
        };

        infoCol.setCellFactory(infoCellFactory);

        reFetchAndRedisplay();
    }

    public void fetchData(){
        Configuration config = new Configuration().configure();

        config.addAnnotatedClass(MappingClasses.Tranzakcje.class);
        config.addAnnotatedClass(MappingClasses.Item.class);
        config.addAnnotatedClass(MappingClasses.Uzytkownik.class);

        StandardServiceRegistryBuilder builder =
                new StandardServiceRegistryBuilder().applySettings(config.getProperties());
        SessionFactory factory = config.buildSessionFactory(builder.build());
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("from Tranzakcje where isDone = 0 and data_zwrotu <=:dataZwrotu");
        Date t = new Date();
        query.setParameter("dataZwrotu",t);
        uzytkownicy = new ArrayList<>();
        List listaTranzakcji = query.list();
        tranzakcje = listaTranzakcji;


        for(int i=0; i < listaTranzakcji.size(); i++){
            Tranzakcje x = (Tranzakcje) listaTranzakcji.get(i);
            Uzytkownik temp = session.find(Uzytkownik.class, x.getId_uzytkownika());
            if(!uzytkownicy.contains(temp)) uzytkownicy.add(temp);
        }

        transaction.commit();
        session.close();
    }

    private static <T> List<T> loadAllData(Class<T> type, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        List<T> data = session.createQuery(criteria).getResultList();
        return data;
    }

    public void reFetchAndRedisplay(){
        fetchData();
        observableList.removeAll(observableList);
        for(Uzytkownik temp : uzytkownicy){
            observableList.add(temp);
        }
        tableView.setItems(observableList);
    }

    public void goToMoreInfo(Uzytkownik user) throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("adminUzytkownikPromptInfo.fxml"));
        Parent root = fxmlloader.load();
        root.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(tableView.getScene().getWindow());
        stage.setTitle(user.getNazwisko() +" - dodatkowe informacje");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setUserData(user);
        stage.show();
        stage.setOnHidden(new EventHandler<WindowEvent>(){
            public void handle(WindowEvent we) {
                reFetchAndRedisplay();
            }
        });
    }

    public void search(){
        ObservableList<Uzytkownik> x = FXCollections.observableArrayList();
        int listLength  = observableList.toArray().length;
        if(searchBox.getText().isEmpty()){
            tableView.setItems(observableList);
            tableView.refresh();
        }else {
            for (int i = 0; i < listLength; i++) {
                if (observableList.get(i).getNazwisko().toLowerCase().contains(searchBox.getText().toLowerCase())) {
                    System.out.println(observableList.get(i).getNazwisko());
                    x.add(observableList.get(i));
                }
            }
            tableView.setItems(x);
        }
    }
}
