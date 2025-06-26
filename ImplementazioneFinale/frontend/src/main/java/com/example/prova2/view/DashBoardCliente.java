package com.example.prova2.view;
import com.example.prova2.controller.dashBoard.DashBoardClienteController;
import com.example.prova2.model.Utente;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class DashBoardCliente {
    private DashBoardCliente(){}
    public static void initializePageDashboardCliente(Window window, Utente utente) throws IOException, InterruptedException {
        FXMLLoader loader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/cliente/dashboardCliente.fxml"));
        Scene newScene = new Scene(loader.load(), 1540, 900);
        DashBoardClienteController controller = loader.getController();

        // Imposta la scena precedente prima di cambiare la finestra
        Stage stage = (Stage) window;
        Scene previousScene = stage.getScene(); // Prende la scena attuale come precedente
        controller.setPreviousScene(previousScene);
        System.out.println("Inizializzazione dash : "+ utente.getNome());
        controller.initDati(utente);
        stage.setScene(newScene);
        stage.show();
    }

}
