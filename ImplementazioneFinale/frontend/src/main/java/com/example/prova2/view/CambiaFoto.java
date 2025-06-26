package com.example.prova2.view;

import com.example.prova2.controller.CambiaFotoController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CambiaFoto {


    public void initPage(Stage owner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/prova2/views/shared/cambiaFoto.fxml"));
            Pane modalLayout = loader.load();
            CambiaFotoController controller = loader.getController();
            Stage modalStage = new Stage();
            modalStage.initOwner(owner);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Cambia foto!");
            modalStage.setScene(new Scene(modalLayout, 500, 550));
            modalStage.setResizable(false);
            controller.setModalStage(modalStage);
            controller.initFoto();
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
