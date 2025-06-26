package com.example.prova2.view;

import com.example.prova2.controller.registrazione.RegistrazioneUtenteController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class RegistrazioneUtente {
    private RegistrazioneUtente(){}

    public static void initializePageRegistrazioneUtente(Window window) throws IOException {
        FXMLLoader loader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/cliente/registrazioneCliente.fxml"));
        Scene scene = new Scene(loader.load(), 1540, 900);
        Stage stage = (Stage) window;
        stage.setScene(scene);
        RegistrazioneUtenteController controller = loader.getController();
        controller.init();
        stage.show();
    }
}
