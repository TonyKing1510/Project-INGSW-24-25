package com.example.prova2.view;

import com.example.prova2.controller.LasciaRecensioneController;
import com.example.prova2.controller.SchermataAnnuncioController;
import com.example.prova2.model.Utente;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LasciaRecensione {
    public static Stage modalStage;

    public static SchermataAnnuncioController schermataAnnuncioController;

    public static void initPage(Stage owner, String nomeCognomeAgente, Utente utente, String cfAgente, SchermataAnnuncioController controllerPrec) {
        try {
            FXMLLoader loader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/shared/lasciaRecensione.fxml"));
            Pane modalLayout = loader.load();
            schermataAnnuncioController = controllerPrec;
            modalStage = new Stage();
            modalStage.initOwner(owner);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            LasciaRecensioneController controller = loader.getController();
            controller.setUtente(utente);
            controller.initialize(nomeCognomeAgente,cfAgente);
            controller.setControllerPrec(controllerPrec);
            modalStage.setTitle("Lascia Recensione");
            modalStage.setScene(new Scene(modalLayout, 955, 496));
            modalStage.setResizable(false);
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage getModalStage() {
        return modalStage;
    }
}
