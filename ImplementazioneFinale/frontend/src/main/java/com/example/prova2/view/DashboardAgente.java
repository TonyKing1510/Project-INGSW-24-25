package com.example.prova2.view;

import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class DashboardAgente {

    private DashboardAgente(){}
    public static void initializePageDashboardAgente(Window window) throws IOException, InterruptedException {
        FXMLLoader loader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/agente/dashboardAgente.fxml"));
        Scene newScene = new Scene(loader.load(), 1540, 790);
        DashboardAgenteController controller = loader.getController();
        new Thread(controller::initialize).start();
        // Imposta la scena precedente prima di cambiare la finestra
        Stage stage = (Stage) window;
        Scene previousScene = stage.getScene(); // Prende la scena attuale come precedente
        controller.setPreviousScene(previousScene);

        stage.setScene(newScene);
        stage.show();
    }
}
