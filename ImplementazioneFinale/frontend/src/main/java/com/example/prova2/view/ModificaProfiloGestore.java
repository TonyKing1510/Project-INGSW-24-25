package com.example.prova2.view;

import com.example.prova2.controller.modificaProfilo.ModificaProfiloGestoreController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class ModificaProfiloGestore {
    private ModificaProfiloGestore(){}

    public static void initializePageModificaProfiloGestore(Window window) throws IOException {
        FXMLLoader loader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/admin/modificaProfilo.fxml"));
        Scene scene = new Scene(loader.load(), 1540, 900);
        Stage stage = (Stage) window;
        ModificaProfiloGestoreController controller = loader.getController();
        controller.initProfilo();
        stage.setScene(scene);
        stage.show();
    }
}
