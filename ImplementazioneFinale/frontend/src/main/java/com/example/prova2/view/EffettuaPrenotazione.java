package com.example.prova2.view;
import com.example.prova2.controller.AnnullaVisita;
import com.example.prova2.controller.prenotaVisita.EffettuaPrenotazioneController;
import com.example.prova2.model.Immobile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class EffettuaPrenotazione {

    private static Stage stagePrecedente;

    private static Stage stagePrenotazione;

    public static Stage getStagePrecedente() {
        return stagePrecedente;
    }

    public static Stage getStagePrenotazione() {
        return stagePrenotazione;
    }

    public static void setStagePrecedente(Stage stagePrecedente) {
        EffettuaPrenotazione.stagePrecedente = stagePrecedente;
    }

    public static void setStagePrenotazione(Stage stagePrenotazione) {
        EffettuaPrenotazione.stagePrenotazione = stagePrenotazione;
    }

    public static void initPage(Stage owner, String email, Immobile immobile, AnnullaVisita controllerPrec) throws IOException {
        FXMLLoader loader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/cliente/prenotazioneEffettua.fxml"));
        Pane modalLayout = loader.load();
        Stage modalStage = new Stage();
        modalStage.initOwner(owner);
        setStagePrecedente(owner);
        setStagePrenotazione(modalStage);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Effettua la tua prenotazione");
        EffettuaPrenotazioneController controller = loader.getController();
        controller.initalize(email,immobile,controllerPrec);
        modalStage.setScene(new Scene(modalLayout, 1106, 766));
        modalStage.setResizable(false);
        modalStage.showAndWait();
    }

    public static void initPage(Stage owner, String email, Immobile immobile) throws IOException {
        FXMLLoader loader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/cliente/prenotazioneEffettua.fxml"));
        Pane modalLayout = loader.load();
        Stage modalStage = new Stage();
        modalStage.initOwner(owner);
        setStagePrecedente(owner);
        setStagePrenotazione(modalStage);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Effettua la tua prenotazione");
        EffettuaPrenotazioneController controller = loader.getController();
        controller.initalize(email,immobile,null);
        modalStage.setScene(new Scene(modalLayout, 1106, 766));
        modalStage.setResizable(false);
        modalStage.showAndWait();
    }
}
