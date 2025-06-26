package com.example.prova2.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class AccediAdmin {

    private AccediAdmin(){}

    public static void initializePageAccediAdmin(Window window) throws IOException {
        FXMLLoader loader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/admin/loginAdminAccedi.fxml"));
        Scene scene = new Scene(loader.load(), 1540, 900);
        Stage stage = (Stage) window;
        stage.setScene(scene);
        stage.show();
    }
}
