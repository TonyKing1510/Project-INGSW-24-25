package com.example.prova2.view;
import com.example.prova2.controller.notifiche.VisualizzaNotificheAgenteController;
import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.model.Agente;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class VisualizzaNotificheAgente {

    public static Stage modalStage;

    public static void initPage(Stage owner) {
        try {
            FXMLLoader loader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/agente/visualizzaNotifiche.fxml"));
            Pane modalLayout = loader.load();
            modalStage = new Stage();
            modalStage.initOwner(owner);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            VisualizzaNotificheAgenteController controller = loader.getController();
            controller.init(new Agente(DashboardAgenteController.getAgente().getAccountAgente().getEmail(),DashboardAgenteController.getAgente().getToken()));
            modalStage.setTitle("Le tue notifiche");
            modalStage.setScene(new Scene(modalLayout, 1152, 749));
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
