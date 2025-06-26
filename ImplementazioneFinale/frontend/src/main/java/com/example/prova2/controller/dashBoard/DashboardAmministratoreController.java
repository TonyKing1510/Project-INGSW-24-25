package com.example.prova2.controller.dashBoard;
import com.example.prova2.controller.HomePageController;
import com.example.prova2.controller.modificaProfilo.ChangePasswordInterface;
import com.example.prova2.controller.modificaProfilo.ModificaProfiloAgenteController;
import com.example.prova2.controller.notifiche.VisualizzaNotificheGestoreController;
import com.example.prova2.dto.GestoreAgenziaImmobiliareDatiDTO;
import com.example.prova2.model.*;
import com.example.prova2.service.GestoreService;
import com.example.prova2.factory.*;
import com.example.prova2.service.UpdatePasswordService;
import com.example.prova2.view.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Optional;

public class DashboardAmministratoreController implements ChangePasswordInterface {
    @FXML
    public Button btnCreaAgentiImmobiliari;

    @FXML
    public Button buttonTornaIndietro;

    @FXML
    public Button buttonNotifiche;

    @FXML
    public Pane paneAnnullaCambio;

    @FXML
    public Pane modificaProfiloPane;

    @FXML
    public Button modificaProfiloButton;
    @FXML
    public ImageView immagineGestori;

    @FXML
    private Button btnCreaGestore;
    @FXML
    private VBox vbox1;
    @FXML
    private Label labelNome;
    @FXML
    private Label labelCognome;
    @FXML
    private Label labelMail;
    @FXML
    private Label labelNomeProf;
    @FXML
    private Label labelCognomeProf;
    @FXML
    private Label labelTel;

    @FXML
    private Button buttonLogout;

    public String passwordVecchia;

    private Stage previousStage;

    public void setPreviousStage(Stage stage) {
        this.previousStage = stage;
    }



    private static GestoreAgenziaImmobiliare gestore;

    private static final String STRERROR = "errore di navigazione";


    @FXML
    public void initialize(){
        if (gestore != null) {
            btnCreaGestore.setVisible(gestore.isAdmin());
            immagineGestori.setVisible(gestore.isAdmin());
            getGestoreData(gestore.getAccountAgente().getEmail());
        }
    }

    public void setPasswordVecchia(String passwordVecchia) {
        this.passwordVecchia = passwordVecchia;
    }

    public String getPasswordVecchia() {
        return passwordVecchia;
    }

    @FXML
    public void handleTornaHome() {
        if (previousStage != null) {
            Stage currentStage = (Stage) buttonTornaIndietro.getScene().getWindow();
            previousStage.show();
            currentStage.close();
        } else {
            try {
                AccediAdmin.initializePageAccediAdmin(buttonTornaIndietro.getScene().getWindow());
            } catch (IOException e) {
                AlertFactory.generateFailAlertForErroreCaricamentoPagina();
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void apriPaginaCreaGestori(){
        try {
            CreaGestore.initializePageCreaGestore((Stage) btnCreaGestore.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.generateFailAlert(STRERROR,
                    "Siamo spiacenti, si è verificato un errore durante il caricamento della pagina creazione annunci");
        }
    }


    @FXML
    public void apriCreaAgenti() {
        try {
            CreaAgenteImmobiliare.initializePageCreaAgente((Stage) btnCreaAgentiImmobiliari.getScene().getWindow());
        } catch (IOException e){
            e.printStackTrace();
            AlertFactory.generateFailAlert(STRERROR,
                    "Si è verificato un problema durante il caricamento della dashboard.");
        }
    }

    public static void setAdmin(GestoreAgenziaImmobiliare admin) {
        DashboardAmministratoreController.gestore = admin;
    }


    public static GestoreAgenziaImmobiliare getAdmin() {
        if(!gestore.isAdmin()){
            return gestore;
        }else{
            Account a = new Account(gestore.getAccountAgente().getEmail());
            Admin admin = new Admin(gestore.getNome(),gestore.getCognome(),gestore.getCf());
            admin.setAccountAgente(a);
            admin.setToken(gestore.getToken());
            admin.setTelefono(gestore.getTelefono());
            return admin;
        }
    }

    public void getGestoreData(String email) {
            new Thread(()->{
                GestoreAgenziaImmobiliareDatiDTO gestoreData = GestoreService.getGestoreByUsername(email,gestore.getToken());
                if (gestoreData != null) {
                    gestore.setDto(gestoreData);
                    extracted(gestoreData.getNome(),gestoreData.getCognome(),gestoreData.getEmail(), gestoreData.getTelefono());
                } else {
                    Platform.runLater(()->{
                        AlertFactory.generateFailAlert("Errore nel ritrovo dei dati!","Siamo spiacenti al momento" +
                                "non abbiamo potuto recuperare i tuoi dati, si prega di riprovare più tardi");
                    });
                }
            }).start();
    }

    private void extracted(String nome, String cognome, String email, String telefono) {
        Platform.runLater(()->{
            labelNome.setText(nome);
            labelCognome.setText(cognome);
            labelNomeProf.setText(nome);
            labelCognomeProf.setText(cognome);
            labelMail.setText(email);
            labelTel.setText(telefono);
        });
    }

    @FXML
    public void faiLogout()  {
        Alert alert = AlertFactory.generateAlertForLogout();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                LoginPage.startMainApp((Stage) buttonLogout.getScene().getWindow());
            } catch (IOException e) {
                e.printStackTrace();
                AlertFactory.generateFailAlert(STRERROR,
                        "Siamo spiacenti, si è verificato un errore durante il caricamento della pagina creazione annunci");
            }
        }
    }

    public void apriPaginaNotifiche() {
        VisualizzaNotificheGestoreController.setGestoreNotifiche(gestore);
        VisualizzaNotificheGestore.initPage((Stage) buttonNotifiche.getScene().getWindow(),this);
    }

    public void annullaCambioPassword(){
        Platform.runLater(()->{
            paneAnnullaCambio.setVisible(true);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(15), event -> {
                paneAnnullaCambio.setVisible(false);
            }));
            timeline.setCycleCount(1);
            timeline.play();
        });
    }

    public void tornaIndietroPassword() {
        Platform.runLater(()->{        paneAnnullaCambio.setVisible(false);
        });
        new Thread(()->{
            UpdatePasswordService.updatePasswordGestore(passwordVecchia,gestore);
        }).start();
    }

    public void apriPaginaModificaProfilo() {
        try {
            ModificaProfiloAgenteController.setUtente(gestore);
            ModificaProfiloGestore.initializePageModificaProfiloGestore(buttonTornaIndietro.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.generateFailAlertForErroreInterno();
        }
    }

    public void apriPaginaCambiaPassword() {
        try{
            CambioPassword.initializePageCambioPassword(btnCreaGestore.getScene().getWindow(),this,gestore);
        } catch (IOException e){
            e.printStackTrace();
            AlertFactory.generateFailAlert("Errore apertura pagina!", "Siamo spiacenti si è verificato un errore nel caricamento della pagina cambio password");
        }
    }

    public void apriPaginaCercaAnnunci() {
        try {
            HomePageController.setUtente(DashboardAmministratoreController.getAdmin());
            HomePage.initializeHomePage((Stage) buttonLogout.getScene().getWindow(),DashboardAmministratoreController.getAdmin());
        } catch (IOException e) {
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }
}