package com.example.prova2.view;

import com.example.prova2.controller.login.LoginAgenteController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class LoginAgenteImmobiliare {

    private LoginAgenteImmobiliare(){}

    public static void initializePageLoginAgente(Window window) throws IOException {
        FXMLLoader loader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/agente/loginAgente.fxml"));
        Scene scene = new Scene(loader.load(), 1540, 900);
        Stage stage = (Stage) window;
        LoginAgenteController controller = loader.getController();
        controller.initialize();
        stage.setScene(scene);
        stage.show();
    }
}