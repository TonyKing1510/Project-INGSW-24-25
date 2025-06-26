package com.example.prova2.view;

import com.example.prova2.controller.modificaProfilo.ModificaProfiloGoogleController;
import com.example.prova2.model.Utente;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class ModificaProfiloGoogle {

    public static void initializePageModificaProfiloClienteGoogle(Window window, Utente utente) throws IOException {
        FXMLLoader loader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/cliente/modificaProfiloGoogle.fxml"));
        Scene scene = new Scene(loader.load(), 1540, 900);
        Stage stage = (Stage) window;
        ModificaProfiloGoogleController controller = loader.getController();
        controller.setUtenteGoogle(utente);
        System.out.println("init ModificaProfilo"+utente.getToken());
        controller.initProfiloGoogle(utente);
        stage.setScene(scene);
        stage.show();
    }
}
