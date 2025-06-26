package com.example.prova2.view;

import com.example.prova2.controller.SchermataAnnuncioController;
import com.example.prova2.controller.prenotaVisita.AlertVisitaController;
import com.example.prova2.model.Immobile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AlertVisitaPrenotata {

    public void initPage(Stage owner, Immobile immobile, SchermataAnnuncioController controllerPrec) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/prova2/views/shared/visitaGiaPrenotataAlert.fxml"));
            Pane modalLayout = loader.load();
            AlertVisitaController controller = loader.getController();
            Stage modalStage = new Stage();
            modalStage.initOwner(owner);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Visita già prenotata!");
            modalStage.setScene(new Scene(modalLayout, 567, 380));
            modalStage.setResizable(false);
            controller.init(immobile,controllerPrec);
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
