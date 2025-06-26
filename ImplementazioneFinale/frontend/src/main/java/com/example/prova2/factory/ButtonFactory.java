
package com.example.prova2.factory;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class ButtonFactory {

    private ButtonFactory(){}

    public static final String STILEBOTTONE= "-fx-background-color : rgba(255, 255, 255, 0.2); " +
            "-fx-backgrounds-radius : 10; " +
            "-fx-border-width: 2; " +
            "-fx-text-fill: #f3f6f4; ";

    public static final String STILEBOTTONE2= "-fx-background-color : rgba(255, 255, 255, 0.4); " +
            "-fx-backgrounds-radius : 10; " +
            "-fx-border-width: 2; " +
            "-fx-text-fill: #f3f6f4; ";


    public static Button createButton() {
        Button button = new Button();
        button.setPrefWidth(175);
        button.setPrefHeight(93.6);

        button.setStyle(STILEBOTTONE2);
        return button;
    }

    public static Button createLettaImageButton() {

        Image image = new Image(Objects.requireNonNull(ButtonFactory.class.getResourceAsStream("/com/example/prova2/images/icons8-visto-48.png"))); // Percorso dell'immagine
        ImageView imageView = new ImageView(image);

        // Configura l'ImageView (opzionale: dimensioni dell'immagine)
        imageView.setFitWidth(20); // Larghezza dell'immagine
        imageView.setFitHeight(20); // Altezza dell'immagine

        // Crea il pulsante e aggiungi l'ImageView
        Button buttonLetta = new Button();
        buttonLetta.setStyle(STILEBOTTONE);
        buttonLetta.setGraphic(imageView); // Imposta l'immagine come contenuto del pulsante

        return buttonLetta;
    }

    public static Button createRifiutaImageButton() {
        // Carica l'immagine
        Image image = new Image(Objects.requireNonNull(ButtonFactory.class.getResourceAsStream("/com/example/prova2/images/icons8-no-48.png"))); // Percorso dell'immagine
        ImageView imageView = new ImageView(image);

        // Configura l'ImageView (opzionale: dimensioni dell'immagine)
        imageView.setFitWidth(20); // Larghezza dell'immagine
        imageView.setFitHeight(20); // Altezza dell'immagine

        // Crea il pulsante e aggiungi l'ImageView
        Button buttonRifiuta = new Button();
        buttonRifiuta.setStyle(STILEBOTTONE);
        buttonRifiuta.setGraphic(imageView); // Imposta l'immagine come contenuto del pulsante

        return buttonRifiuta;
    }
}
