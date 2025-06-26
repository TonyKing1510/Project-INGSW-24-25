package com.example.prova2.view;

import com.example.prova2.controller.SchermataAnnuncioController;
import com.example.prova2.dto.ImmobileResponseRicercaDTO;
import com.example.prova2.model.Utente;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;

public class SchermataAnnuncio {

    public static void initializeSchermataAnnuncio(Pane pane, ImmobileResponseRicercaDTO annuncio, Utente utente , BigDecimal prezzoMin, BigDecimal prezzoMax) throws IOException {
        FXMLLoader loader = new FXMLLoader(SchermataAnnuncio.class.getResource("/com/example/prova2/views/shared/schermataAnnuncio.fxml"));
        Scene scene = new Scene(loader.load(), 1540, 900);
        SchermataAnnuncioController controller = loader.getController();
        Stage stage = (Stage) pane.getScene().getWindow();
        controller.setPreviousScene(stage.getScene());
        SchermataAnnuncioController.setDatiRicerca(annuncio,prezzoMin,prezzoMax);
        SchermataAnnuncioController.setUtenteCheCerca(utente);
        controller.loadAnnuncio(annuncio);
        controller.loadValutazione();
        stage.setScene(scene);
        stage.show();

    }

    public static void initializeSchermataAnnuncioClienteNotifiche(Stage pane, ImmobileResponseRicercaDTO annuncio, Utente utente) throws IOException {
        FXMLLoader loader = new FXMLLoader(SchermataAnnuncio.class.getResource("/com/example/prova2/views/cliente/schermataAnnuncioClienteNotifiche.fxml"));
        Scene scene = new Scene(loader.load(), 1540, 900);
        SchermataAnnuncioController controller = loader.getController();
        SchermataAnnuncioController.setDatiRicerca(annuncio, null, null);
        SchermataAnnuncioController.setUtenteCheCerca(utente);
        controller.loadAnnuncio(annuncio);
        controller.loadValutazione();
        controller.setPreviousScene(pane.getScene().getWindow().getScene());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setWidth(1550);
        stage.setHeight(810);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        if (pane != null) {
            pane.close();
        }
        stage.show();
    }

}
