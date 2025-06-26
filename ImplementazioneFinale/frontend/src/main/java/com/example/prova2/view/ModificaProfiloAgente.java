package com.example.prova2.view;

import com.example.prova2.controller.modificaProfilo.ModificaProfiloAgenteController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class ModificaProfiloAgente {

    private static FXMLLoader loader;

    private ModificaProfiloAgente(){}

    public static void initializePageModificaProfilo(Window window) throws IOException {
        loader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/agente/modificaProfilo.fxml"));
        Scene scene = new Scene(loader.load(), 1540, 900);
        Stage stage = (Stage) window;
        ModificaProfiloAgenteController controller = loader.getController();
        controller.initProfilo();
        stage.setScene(scene);
        stage.show();
    }

    public static void aggiornaNuovaFotoProfilo(){
        ModificaProfiloAgenteController controller = loader.getController();
        controller.caricaFotoNuova();
    }
}
