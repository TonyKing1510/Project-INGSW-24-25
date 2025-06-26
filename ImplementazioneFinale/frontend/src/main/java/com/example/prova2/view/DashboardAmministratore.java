package com.example.prova2.view;
import com.example.prova2.controller.dashBoard.DashboardAmministratoreController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardAmministratore {
    private DashboardAmministratore(){}
    public static void initializePageDashboardAmministratore(Stage previousStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/admin/dashBoardAdminNuova (1).fxml"));
        Scene scene = new Scene(loader.load(), 1540, 790);

        DashboardAmministratoreController controller = loader.getController();
        controller.setPreviousStage(previousStage);

        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.setTitle("Dashboard Amministratore");
        newStage.show();

        previousStage.close();
    }

}
