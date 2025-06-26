package com.example.prova2.view;

import com.example.prova2.controller.CambioPasswordController;
import com.example.prova2.controller.modificaProfilo.ChangePasswordInterface;
import com.example.prova2.model.Utente;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class CambioPassword {
    private CambioPassword(){}


    public static void initializePageCambioPassword(Window window, ChangePasswordInterface controllerPrecedente, Utente utente) throws IOException {
        FXMLLoader loader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/shared/cambioPasswordNew.fxml"));
        Scene scene = new Scene(loader.load(), 1540, 790);

        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.initOwner(window);
        newStage.initModality(Modality.WINDOW_MODAL);

        CambioPasswordController controller = loader.getController();
        controller.init();
        controller.setScene(scene);
        controller.setUtenteCheCambiaPassword(utente);
        controller.setControllerPrecedente(controllerPrecedente);
        newStage.show();
    }

}
