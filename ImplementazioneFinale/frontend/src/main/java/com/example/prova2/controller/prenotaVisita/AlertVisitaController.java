package com.example.prova2.controller.prenotaVisita;

import com.example.prova2.controller.SchermataAnnuncioController;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Immobile;
import com.example.prova2.view.EffettuaPrenotazione;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;


public class AlertVisitaController {
    @FXML
    public Label nomeAgente;
    @FXML
    public Label richiediVisitaLabel;

    private Immobile immobile;

    private SchermataAnnuncioController controllerPrec;

    public void init(Immobile immobile, SchermataAnnuncioController controllerPrec) {
        this.immobile = immobile;
        this.controllerPrec = controllerPrec;
        nomeAgente.setText(immobile.getNomeAgente()+" "+immobile.getCognomeAgente());
    }

    public void richiediVisita() {
        try {
            EffettuaPrenotazione.initPage((Stage) richiediVisitaLabel.getScene().getWindow(),
                    SchermataAnnuncioController.getUtenteCheCerca().getAccountAgente().getEmail(), immobile,controllerPrec);
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.generateFailAlertForErroreInterno();
        }
    }
}
