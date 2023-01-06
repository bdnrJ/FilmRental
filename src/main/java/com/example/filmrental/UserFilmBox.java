package com.example.filmrental;

import MappingClasses.Film;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import javax.print.DocFlavor;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class UserFilmBox {

    @FXML
    private Label czas;

    @FXML
    private Label data;

    @FXML
    private ImageView filmImage;

//    @FXML
//    private Label jezyk;
//
//    @FXML
//    private Label kraj;

    @FXML
    private Text tytul;

    public void setData(Film film) throws IOException {
        byte[] blob = film.getImage();
        if(blob != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(blob);
            BufferedImage bImage = ImageIO.read(bis);
            Image image = SwingFXUtils.toFXImage(bImage, null);

            filmImage.setImage(image);
        }else{
            Image image = new Image("/noImg.jpg");
            filmImage.setImage(image);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        tytul.setText(film.getTytul());
//        jezyk.setText(film.getJezyk());
//        kraj.setText(film.getKraj());
        czas.setText(film.getCzasTrwania()+" min");
        data.setText(dateFormat.format(film.getDataPremiery()));
    }
}
