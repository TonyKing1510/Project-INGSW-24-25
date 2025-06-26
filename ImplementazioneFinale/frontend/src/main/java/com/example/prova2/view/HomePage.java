package com.example.prova2.view;

import com.example.prova2.controller.HomePageController;
import com.example.prova2.dto.ClienteDatiDTO;
import com.example.prova2.facade.ClienteServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Cliente;
import com.example.prova2.model.Utente;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class HomePage {

    private HomePage() {}

    public static void caricamentoHome(Stage primaryStage,Utente utente) throws IOException {
        // Percorso del video
        String videoPath = Objects.requireNonNull(
                HomePage.class.getResource("/com/example/prova2/images/Caricamento.mp4")
        ).toExternalForm();
        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        StackPane root = new StackPane(mediaView);
        Scene splashScene = new Scene(root, 1540, 790);
        primaryStage.setScene(splashScene);
        primaryStage.setTitle("Caricamento...");
        primaryStage.show();
        initializeHomePage(primaryStage,utente);  // Chiama il metodo per caricare la HomePage

    }
    public static void initializeHomePage(Stage primaryStage,Utente utente) throws IOException {
        FXMLLoader loader = new FXMLLoader(HomePage.class.getResource("/com/example/prova2/views/shared/schermataHome.fxml"));
        Scene mainScene = new Scene(loader.load(), 1540, 790);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Home");
        HomePageController homePage = loader.getController();
        homePage.initialize(utente);
    }

    public static void initializeHomePageWithGoogle(Stage primaryStage,Utente utente) throws IOException {
        FXMLLoader loader = new FXMLLoader(HomePage.class.getResource("/com/example/prova2/views/shared/schermataHome.fxml"));
        Scene mainScene = new Scene(loader.load(), 1540, 790);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Home");

        // Setta l'utente nella home
        HomePageController homePage = loader.getController();
        ClienteDatiDTO datiC = ClienteServiceFacade.prendiDati(utente);
        Cliente cliente1 = new Cliente(datiC.getNome(),datiC.getCognome(),datiC.getEmail(),datiC.getTelefono());
        cliente1.setToken(utente.getToken());
        HomePageController.setUtente(cliente1);

        homePage.initialize(cliente1);

        // Mostra la home PRIMA di fare il check
        primaryStage.show();

        // Esegui il controllo dopo un piccolo ritardo per permettere alla GUI di caricarsi
        Platform.runLater(() -> {
            try {
                checkUtenteProfile(primaryStage, cliente1);
            } catch (IOException e) {
                AlertFactory.generateFailAlertForErroreInterno();
            }
        });
    }

    private static void checkUtenteProfile(Stage primaryStage, Utente utente) throws IOException {
        if (isProfiloIncompleto(utente)) {
            boolean shouldComplete = mostraAlertProfiloIncompleto();
            if (shouldComplete) {
                ModificaProfiloGoogle.initializePageModificaProfiloClienteGoogle(primaryStage, utente);
                return;
            }
        }
    }

    private static boolean isProfiloIncompleto(Utente cliente) {
        System.out.println("Nome" + cliente.getNome() + " " + cliente.getCognome());
        return cliente.getNome() == null || cliente.getNome().isEmpty() ||
                cliente.getCognome() == null || cliente.getCognome().isEmpty();
    }

    private static boolean mostraAlertProfiloIncompleto() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Profilo Incompleto");
        alert.setHeaderText("Attenzione! Il tuo profilo è incompleto.");
        alert.setContentText("Per continuare, completa il tuo profilo con nome e cognome.");

        ButtonType completaOra = new ButtonType("Completa ora");
        ButtonType continua = new ButtonType("Continua senza modificare", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(completaOra, continua);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == completaOra; // Ritorna true se clicca "Completa ora"
    }


}
