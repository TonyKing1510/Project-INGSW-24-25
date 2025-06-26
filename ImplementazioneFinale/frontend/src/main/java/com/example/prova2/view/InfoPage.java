package com.example.prova2.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class InfoPage {
    public static void faqPageInit() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/shared/FAQpage.fxml"));
        Parent root = fxmlLoader.load();


        Stage faqStage = new Stage();
        faqStage.initModality(Modality.APPLICATION_MODAL); // Blocca l'interazione con altre finestre finché il popup è aperto
        faqStage.setTitle("FAQ - DietiEstates25");

        Scene scene = new Scene(root, 650, 600); // Dimensioni del popup
        faqStage.setScene(scene);

        faqStage.showAndWait(); // Mostra il popup e aspetta la chiusura prima di tornare alla finestra principale
    }

    public static void privacyPageInit() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/shared/privacyPage.fxml"));
        Parent root = fxmlLoader.load();

        Stage privacyStage = new Stage();
        privacyStage.initModality(Modality.APPLICATION_MODAL); // Blocca l'interazione con altre finestre finché il popup è aperto
        privacyStage.setTitle("Privacy - DietiEstates25");

        Scene scene = new Scene(root, 650, 600); // Dimensioni del popup
        privacyStage.setScene(scene);

        privacyStage.showAndWait(); // Mostra il popup e aspetta la chiusura prima di tornare alla finestra principale
    }


    public static void chiSiamoPageInit() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("/com/example/prova2/views/shared/chiSiamopage.fxml"));
        Parent root = fxmlLoader.load();

        Stage chiSiamoStage = new Stage();
        chiSiamoStage.initModality(Modality.APPLICATION_MODAL); // Blocca l'interazione con altre finestre finché il popup è aperto
        chiSiamoStage.setTitle("FAQ - DietiEstates25");

        Scene scene = new Scene(root, 650, 600); // Dimensioni del popup
        chiSiamoStage.setScene(scene);

        chiSiamoStage.showAndWait(); // Mostra il popup e aspetta la chiusura prima di tornare alla finestra principale
    }

}
