package com.example.prova2.view;

import com.example.prova2.controller.dashBoard.DashboardAmministratoreController;
import com.example.prova2.controller.notifiche.VisualizzaNotificheGestoreController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class VisualizzaNotificheGestore {
    public static Stage modalStage;


    public static void initPage(Stage owner, DashboardAmministratoreController controllerPrecedente) {
        try {
            FXMLLoader loader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/admin/visualizzaNotifiche.fxml"));
            Pane modalLayout = loader.load();
            modalStage = new Stage();
            modalStage.initOwner(owner);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            VisualizzaNotificheGestoreController controller = loader.getController();
            controller.init();
            controller.setWindowPrecednete(owner);
            controller.setControllerPrecedente(controllerPrecedente);
            modalStage.setTitle("Le tue notifiche");
            modalStage.setScene(new Scene(modalLayout, 1152, 749));
            modalStage.setResizable(false);
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
