package com.example.prova2.view;
import com.example.prova2.controller.CreaGestoreController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CreaGestore {

    private CreaGestore(){}

    public static void initializePageCreaGestore(Stage stagePrec) throws IOException {
        FXMLLoader loader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/admin/creaGestore.fxml"));
        Scene scene = new Scene(loader.load(), 1540, 900);
        CreaGestoreController controller = loader.getController();
        controller.init();
        controller.setStage(stagePrec);
        controller.setScenePrecedente(stagePrec.getScene());


        stagePrec.setScene(scene);
        stagePrec.show();
    }

}
