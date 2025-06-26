package com.example.prova2.controller;

import com.example.prova2.facade.RecensioneFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Utente;
import com.example.prova2.view.LasciaRecensione;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Objects;

public class LasciaRecensioneController {

    @FXML
    private Label labelNomeAgenteRecensione;
    @FXML
    private Pane paneLasciaValutazione;
    @FXML
    private Button btnConferma;


    private Utente utente;
    private String cfAgente;
    private static final int MAX_STARS = 5;
    private int rating = 0; // Valutazione selezionata
    private final ImageView[] stars = new ImageView[MAX_STARS];
    private SchermataAnnuncioController controllerPrec;

    public void setControllerPrec(SchermataAnnuncioController controllerPrec) {
        this.controllerPrec = controllerPrec;
    }

    public void initialize(String nomeAgenteRecensione, String cfAgente) {
        setLabelNomeAgenteRecensione(nomeAgenteRecensione);
        setCfAgente(cfAgente);
        loadValutazione();
    }

    private void setLabelNomeAgenteRecensione(String nomeAgenteRecensione) {
        labelNomeAgenteRecensione.setText(nomeAgenteRecensione);
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
    public void setCfAgente(String cfAgente) {
        this.cfAgente = cfAgente;
    }

    private void loadValutazione() {
        Image starEmpty = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/prova2/images/casaVuota.png")));
        Image starFull = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/prova2/images/casaPiena.png")));

        HBox starBox = new HBox(15); // Spazio tra le stelle più largo
        starBox.setPadding(new Insets(10)); // Aggiunge margine attorno
        starBox.setAlignment(Pos.CENTER); // Centra le stelle orizzontalmente

        starBox.getChildren().clear(); // Svuota le stelle esistenti

        for (int i = 0; i < MAX_STARS; i++) {
            final int starIndex = i + 1;
            stars[i] = new ImageView(starEmpty);
            stars[i].setFitWidth(60); // Ingrandito
            stars[i].setFitHeight(60);// Ingrandito
            stars[i].setCursor(Cursor.HAND);
            // Evento click per selezionare la valutazione
            stars[i].addEventHandler(MouseEvent.MOUSE_CLICKED, event -> setRating(starIndex, starFull, starEmpty));

            starBox.getChildren().add(stars[i]);
        }
        paneLasciaValutazione.getChildren().add(starBox);
    }

    private void setRating(int newRating, Image starFull, Image starEmpty) {
        rating = newRating;
        for (int i = 0; i < MAX_STARS; i++) {
            stars[i].setImage(i < rating ? starFull : starEmpty);
        }
        btnConferma.setDisable(false);
    }

    public int getRating() {
        return rating;
    }


    public void setValutazione() {
    try{
        // Chiama il metodo per aggiornare la recensione
        boolean recensione = RecensioneFacade.updateRecensione(cfAgente, rating, utente.getToken());

        if (recensione) {
            Alert alert=AlertFactory.generateSuccessAlertForSuccessValutazioni();
            alert.showAndWait().ifPresentOrElse(responseUtente -> {
                chiudiFinestra();
            }, this::chiudiFinestra);
        } else {
            AlertFactory.generateFailAlert("Errore nell'aggiornamento della recensione.","Scusateci, qualcosa è andato storto, riprova!");
        }
    } catch (Exception e) {
        // Log dell'errore (opzionale)
        e.printStackTrace();

        // Mostra un alert di errore
        AlertFactory.generateFailAlertForErroreInterno();
    }
    }

    private void chiudiFinestra() {
        LasciaRecensione.modalStage.close();
        controllerPrec.paneSuccessoRecensione.setVisible(true);
        PauseTransition pausa = new PauseTransition(Duration.seconds(3));

        pausa.setOnFinished(event -> LasciaRecensione.schermataAnnuncioController.paneSuccessoRecensione.setVisible(false));

        pausa.play();
    }


}
