package com.example.prova2.view;

import com.example.prova2.controller.CreaAgenteImmobiliareController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CreaAgenteImmobiliare {

    private CreaAgenteImmobiliare(){}

    public static void initializePageCreaAgente(Stage stagePrec) throws IOException {
        FXMLLoader loader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/admin/creaAgenteImmobiliare.fxml"));
        Scene scene = new Scene(loader.load(), 1540, 900);
        CreaAgenteImmobiliareController controller = loader.getController();
        controller.init();
        controller.setStagePrecedente(stagePrec);
        controller.setScenePrecedente(stagePrec.getScene());


        stagePrec.setScene(scene);
        stagePrec.show();
    }
}
