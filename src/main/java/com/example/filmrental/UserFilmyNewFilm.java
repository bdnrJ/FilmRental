package com.example.filmrental;

import MappingClasses.Film;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class UserFilmyNewFilm {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Text czasTrwania;

    @FXML
    private ImageView image;

    @FXML
    private Text lang;

    @FXML
    private Text rok;

    @FXML
    private Text title;

    public void setData(Film film){
        Image x = new Image("C:\\Users\\reset\\Desktop\\8038812.6.jpg");
        image.setImage(x);

        title.setText(film.getTytul());
        czasTrwania.setText(String.valueOf(film.getCzasTrwania()));
        lang.setText(film.getJezyk());
        rok.setText(film.getKraj());
    }
}
