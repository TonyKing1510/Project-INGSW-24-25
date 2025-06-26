package com.example.prova2.controller;

import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.facade.ModificaProfiloFacade;
import com.example.prova2.model.Agente;
import com.example.prova2.model.Foto;
import com.example.prova2.view.ModificaProfiloAgente;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class CambiaFotoController {

    @FXML
    public ImageView fotoProfilo;

    private String imageUrl;

    @FXML
    public Label labelRimuoviFoto;

    @FXML
    public Label labelCambiaFoto;

    @FXML
    public ImageView imageCestino;

    @FXML
    public ImageView imageUpload;
    @FXML
    public Button salvaButton;
    @FXML
    public Button tornaIndietroButton;

    private Stage modalStage;

    private Agente agente;

    public void setAgente(Agente agente) {
        this.agente = agente;
    }

    public Agente getAgente() {
        return agente;
    }

    public void initFoto() {
        initAgente();
        if (agente.getFotoProfilo() != null) {
            System.out.println("ciao ho foto");
            fotoProfilo.setImage(new Image(agente.getFotoProfilo().getPath()));
        } else {
            System.out.println("ciao");
            fotoProfilo.setImage(new Image(Objects.requireNonNull(getClass().getResource("/com/example/prova2/images/user (4).png")).toExternalForm()));

        }
    }

    private void initAgente() {
        setAgente(DashboardAgenteController.getAgente());
    }

    public void setModalStage(Stage modalStage) {
        this.modalStage = modalStage;
    }

    @FXML
    private void handleClose() {
        if (modalStage != null) {
            modalStage.close();
        }
    }

    public void uploadImage() {
        File selectedFile= prendiFileDaConsole(imageUpload.getScene());
        if (selectedFile != null && (immagineMessaDiversaDaImmaginePrecedente(selectedFile))){
                    fotoProfilo.setImage(new Image(selectedFile.getPath()));
                    attivaBottone();
        }
    }

    static File prendiFileDaConsole(Scene scene) {
        Window currentWindow = scene.getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("Tutti i file", "*.*")
        );
        return fileChooser.showOpenDialog(currentWindow);
    }

    private void attivaBottone() {
        salvaButton.setDisable(false);
    }

    private boolean immagineMessaDiversaDaImmaginePrecedente(File selectedFile) {
        if(agente.getFotoProfilo() == null){
            return true;
        }
        return !selectedFile.getAbsolutePath().equals(agente.getFotoProfilo().getPath());
    }

    public void salvaFoto() {
        if (ModificaProfiloFacade.uploadFotoAgente(agente.getCf(), new Foto(fotoProfilo.getImage().getUrl()))) {
            generateSuccessAlertForSuccessFotoProfilo();
        }
    }

    public void generateSuccessAlertForSuccessFotoProfilo() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Complimenti!");
        alert.setHeaderText("Hai aggiornato la tua foto profilo!");
        alert.setContentText("La tua foto profilo è stata aggiornata ora tutti possono vederla!");
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                ModificaProfiloAgente.aggiornaNuovaFotoProfilo();
                handleClose();
            }
        });
    }

    public void tornaIndietro() {
        handleClose();
    }

    public void cestinaImmagine() {
        URL url = getClass().getResource("/com/example/prova2/images/user (4).png");
        if (url != null) {
            String decodedPath = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
            System.out.println(decodedPath);
            Image image = new Image(decodedPath);
            fotoProfilo.setImage(image);
        } else {
            System.out.println("Immagine non trovata!");
        }
        attivaBottone();
    }


    private boolean controllaSeImmagineCambiata() {
        if(fotoProfilo.getImage().getUrl().equals("http://res.cloudinary.com/dlp7ynumt/image/upload/v1738959378/user_5.png"))
        {
            return true;
        }
        return fotoProfilo.getImage().getUrl().equals(String.valueOf(Objects.requireNonNull(getClass().getResource("/com/example/prova2/images/user (4).png"))));
    }
}
