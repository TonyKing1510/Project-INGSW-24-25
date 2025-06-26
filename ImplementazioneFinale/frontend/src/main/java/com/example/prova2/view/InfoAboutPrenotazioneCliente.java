package com.example.prova2.view;
import com.example.prova2.controller.notifiche.InfoAboutPrenotazioneClienteController;
import com.example.prova2.controller.notifiche.VisualizzaNotificheClienteController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class InfoAboutPrenotazioneCliente {
    public static void initPage(Stage owner, VisualizzaNotificheClienteController controller1, int notifica) throws IOException {
        FXMLLoader loader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/cliente/infoAboutPrenotazioneCliente.fxml"));
        Pane modalLayout = loader.load();
        Stage modalStage = new Stage();
        modalStage.initOwner(owner);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Informazioni sulla tua prenotazione!");
        InfoAboutPrenotazioneClienteController controller =loader.getController();
        controller.setController(controller1);
        controller.initPage(notifica);
        modalStage.setScene(new Scene(modalLayout, 787,560 ));
        modalStage.setResizable(false);
        modalStage.showAndWait();
    }
}
