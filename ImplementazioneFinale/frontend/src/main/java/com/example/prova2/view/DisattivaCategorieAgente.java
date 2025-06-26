package com.example.prova2.view;

import com.example.prova2.controller.notifiche.DisattivaCategorieAgenteController;
import com.example.prova2.controller.notifiche.VisualizzaNotificheAgenteController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DisattivaCategorieAgente {
        public static Stage modalStage;

        public static void initPage(Stage owner, VisualizzaNotificheAgenteController visualizzaNotificheController) throws IOException {
            FXMLLoader loader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/agente/sbloccaNotifiche.fxml"));
            Pane modalLayout = loader.load();
            modalStage = new Stage();
            modalStage.initOwner(owner);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Le tue notifiche");
            modalStage.setScene(new Scene(modalLayout, 977, 741));
            modalStage.setResizable(false);
            DisattivaCategorieAgenteController controller = loader.getController();
            controller.initCategoriDisattivate(visualizzaNotificheController);
            modalStage.showAndWait();
    }
}
