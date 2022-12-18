package com.example.filmrental;
import MappingClasses.Film;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import de.jensd.fx.glyphs.testapps.AwesomeIconNameComparator;
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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class FilmyController implements Initializable {


    @FXML
    private TableView<Film> tableView;
    @FXML
    private TableColumn<Film,Integer> id;
    @FXML
    private TableColumn<Film,String> tytul;
    @FXML
    private TableColumn<Film,Integer> time;
    @FXML
    private TableColumn<Film,String> lang;
    @FXML
    private TableColumn<Film, String> date;
    @FXML
    private TableColumn<Film,String> kraj;
    @FXML
    private TableColumn editCol;
    @FXML
    private TableColumn deleteCol;
    @FXML
    private TextField searchBox;

    private List<Film>filmy;
    ObservableList<Film> observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        id.setCellValueFactory(new PropertyValueFactory<Film,Integer>("id"));
        tytul.setCellValueFactory(new PropertyValueFactory<Film,String>("tytul"));
        time.setCellValueFactory(new PropertyValueFactory<Film,Integer>("czasTrwania"));
        lang.setCellValueFactory(new PropertyValueFactory<Film,String>("jezyk"));
        date.setCellValueFactory(Job -> {
            SimpleStringProperty property = new SimpleStringProperty();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            property.setValue(dateFormat.format(Job.getValue().getDataPremiery()));
            return property;
        });
        kraj.setCellValueFactory(new PropertyValueFactory<Film,String>("kraj"));
        //guziki
        Callback<TableColumn<Film,String>,TableCell<Film,String>> cellFactory = (param) ->{
          final TableCell<Film,String> edit = new TableCell<Film,String>(){
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
                        Film f = getTableView().getItems().get(getIndex());
                        try {
                            goToEdytujFilmPrompt(f);
                        } catch (IOException e) {
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
        Callback<TableColumn<Film,String>,TableCell<Film,String>> cellFactory2 = (param) ->{
            final TableCell<Film,String> delete = new TableCell<Film,String>(){
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
                            Film f = getTableView().getItems().get(getIndex());
                            try {
                                goToDeletePrompt(f);
                            } catch (IOException e) {
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
        editCol.setCellFactory(cellFactory);
        deleteCol.setCellFactory(cellFactory2);

        reFetchAndRedisplay();
    }

    public void search(){
        ObservableList<Film> x = FXCollections.observableArrayList();
        int listLength  = observableList.toArray().length;
        if(searchBox.getText().isEmpty()){
            tableView.setItems(observableList);
            tableView.refresh();
        }else {
            for (int i = 0; i < listLength; i++) {
                if (observableList.get(i).getTytul().toLowerCase().contains(searchBox.getText().toLowerCase())) {
                    System.out.println(observableList.get(i).getTytul());
                    x.add(observableList.get(i));
                }
            }
            tableView.setItems(x);
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

    @FXML
    public void goToAddFilm() throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("dodajFilm.fxml"));
        Parent root = fxmlloader.load();
        root.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(tableView.getScene().getWindow());
        stage.setTitle("Dodawanie Filmu");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
        stage.setOnHidden(new EventHandler<WindowEvent>(){
            public void handle(WindowEvent we) {
                reFetchAndRedisplay();
            }
        });
    }

    @FXML
    public void goToEdytujFilmPrompt(Film film) throws IOException{
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("promptEdytujFilm.fxml"));
        Parent root = fxmlloader.load();
        root.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(tableView.getScene().getWindow());
        stage.setTitle("Usuwanie Filmu");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setUserData(film);
        stage.show();
        stage.setOnHidden(new EventHandler<WindowEvent>(){
            public void handle(WindowEvent we) {
                reFetchAndRedisplay();
            }
        });
    }

    @FXML
    public void goToDeletePrompt(Film film) throws IOException{
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("promptDeleteFilm.fxml"));
        Parent root = fxmlloader.load();
        root.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(tableView.getScene().getWindow());
        stage.setTitle("Usuwanie Filmu");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setUserData(film);
        stage.show();
        stage.setOnHidden(new EventHandler<WindowEvent>(){
            public void handle(WindowEvent we) {
                reFetchAndRedisplay();
            }
        });
    }
    public void reFetchAndRedisplay(){
        fetchData();
        observableList.removeAll(observableList);
        for(Film temp : filmy){
            observableList.add(temp);
        }
        tableView.setItems(observableList);
    }
}
