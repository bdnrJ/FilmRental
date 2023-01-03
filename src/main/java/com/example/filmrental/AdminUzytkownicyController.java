package com.example.filmrental;

import MappingClasses.Film;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminUzytkownicyController implements Initializable {

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
    private TableColumn editCol;
    @FXML
    private TableColumn deleteCol;
    @FXML
    private TableColumn infoCol;
    @FXML
    private TextField searchBox;

    private List<Uzytkownik> uzytkownicy;
    ObservableList<Uzytkownik> observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        id.setCellValueFactory(new PropertyValueFactory<Uzytkownik,Integer>("id"));
        imie.setCellValueFactory(new PropertyValueFactory<Uzytkownik,String>("imie"));
        nazwisko.setCellValueFactory(new PropertyValueFactory<Uzytkownik,String>("nazwisko"));
        nr_tel.setCellValueFactory(new PropertyValueFactory<Uzytkownik,String>("Nr_tel"));

        Callback<TableColumn<Uzytkownik,String>,TableCell<Uzytkownik,String>> editCellFactory = (param) ->{
            final TableCell<Uzytkownik,String> edit = new TableCell<Uzytkownik,String>(){
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
                                .glyph(FontAwesomeIcons.PENCIL)
                                .build();
                        pen.setFill(paint);
                        pen.setSize("1.8em");
                        //test

                        Button editButton = new Button();
                        editButton.getStyleClass().add("editTableBtn");
                        editButton.setGraphic(pen);
                        editButton.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
                        editButton.setOnAction(event ->{
                            Uzytkownik f = getTableView().getItems().get(getIndex());
                            try {
                               // goToEdytujFilmPrompt(f);
                                System.out.println("edit");
                                System.out.println(f);
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
        Callback<TableColumn<Uzytkownik,String>,TableCell<Uzytkownik,String>> deleteCellFactory = (param) ->{
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
                                .glyph(FontAwesomeIcons.TRASH)
                                .build();
                        trash.setFill(paint);
                        trash.setSize("1.8em");

                        final Button deleteButton = new Button();
                        deleteButton.setGraphic(trash);
                        deleteButton.getStyleClass().add("delTableBtn");
                        deleteButton.getStylesheets().add(getClass().getResource("app.css").toExternalForm());

                        deleteButton.setOnAction(event ->{
                            Uzytkownik f = getTableView().getItems().get(getIndex());
                            try {
                                goToDeletePrompt(f);
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
        Callback<TableColumn<Uzytkownik,String>,TableCell<Uzytkownik,String>> infoCellFactory = (param) ->{
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

        editCol.setCellFactory(editCellFactory);
        deleteCol.setCellFactory(deleteCellFactory);
        infoCol.setCellFactory(infoCellFactory);

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
    public void reFetchAndRedisplay(){
        fetchData();
        observableList.removeAll(observableList);
        for(Uzytkownik temp : uzytkownicy){
            observableList.add(temp);
        }
        tableView.setItems(observableList);
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

    @FXML
    public void goToDeletePrompt(Uzytkownik user) throws IOException{
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("adminUserPromptDelete.fxml"));
        Parent root = fxmlloader.load();
        root.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(tableView.getScene().getWindow());
        stage.setTitle("Usuwanie Uzytkownika");
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

    @FXML
    public void goToMoreInfo(Uzytkownik user) throws IOException{
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
}
