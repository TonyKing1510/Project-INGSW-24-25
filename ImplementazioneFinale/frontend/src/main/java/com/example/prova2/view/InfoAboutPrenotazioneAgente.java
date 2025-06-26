package com.example.prova2.view;

import com.example.prova2.controller.notifiche.InfoAbouPrenotazioneAgenteController;
import com.example.prova2.controller.notifiche.VisualizzaNotificheAgenteController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class InfoAboutPrenotazioneAgente {
    public static void initPage(Stage owner,VisualizzaNotificheAgenteController controller1, int notifica,Pane pane) throws IOException {
        FXMLLoader loader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/agente/infoAboutPrenotazione.fxml"));
        Pane modalLayout = loader.load();
        Stage modalStage = new Stage();
        modalStage.initOwner(owner);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Informazioni sulla tua prenotazione!");
        InfoAbouPrenotazioneAgenteController controller =loader.getController();
        controller.setController(controller1);
        controller.initPage(notifica,pane);
        modalStage.setScene(new Scene(modalLayout, 787,560 ));
        modalStage.setResizable(false);
        modalStage.showAndWait();
    }
}
