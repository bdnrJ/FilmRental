package com.example.filmrental;

import MappingClasses.Film;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class UserFilmyNewController implements Initializable {


    @FXML
    private GridPane gridPane;


    private List<Film> lista = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Date today = new Date();
        Film a = new Film("test",23,"polski",today,"polska");
        Film b = new Film("test1",23,"polski",today,"polska");
        Film c = new Film("test2",23,"polski",today,"polska");

//        Film d = new Film("test3",23,"polski",today,"polska");
//        lista.add(d);


        lista.add(a);
        lista.add(b);
        lista.add(c);

        int column=0;
        int row=0;

        try {
            for (int i = 0; i < lista.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("userFilmyNewFilm.fxml"));
                AnchorPane filmBox = fxmlLoader.load();
                UserFilmyNewFilm controller = fxmlLoader.getController();
                controller.setData(lista.get(i));

                if(column == 3){
                    column = 0;
                    ++row;
                }

                gridPane.add(filmBox, column++, row);
            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    @FXML
    void search(KeyEvent event) {

    }
}
