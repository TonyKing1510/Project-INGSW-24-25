package com.example.prova2.controller.dashBoard;
import com.example.prova2.controller.modificaProfilo.ModificaProfiloAgenteController;
import com.example.prova2.controller.notifiche.VisualizzaNotificheClienteController;
import com.example.prova2.dto.ClienteDatiDTO;
import com.example.prova2.facade.ClienteServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Cliente;
import com.example.prova2.model.Utente;
import com.example.prova2.view.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class DashBoardClienteController {

    private static Cliente cliente = new Cliente();
    @FXML
    public Label labelNomeProf;
    @FXML
    public Button btnCercaAnnunci;
    @FXML
    public Label labelCognomeProf;
    @FXML
    public Label labelTel;
    @FXML
    public Label labelMail;
    @FXML
    public Label labelNome;
    @FXML
    public Label labelCognome;
    @FXML
    public Button btnLogout;
    @FXML
    public Button btnIndietro;

    @FXML
    public ImageView frecciaIndietro;

    private Scene previousScene;

    public static Cliente getCliente() {
        return cliente;
    }

    public static void setCliente(Cliente cliente) {
        DashBoardClienteController.cliente = cliente;
    }

    public void initDati(Utente utente){
        new Thread(()->{
            System.out.println("Init: "+utente.getNome());
            System.out.println("Ini2t: "+utente.getToken());
            ClienteDatiDTO dati=ClienteServiceFacade.prendiDati(utente);
            Platform.runLater(()->{
                Cliente cliente1 = new Cliente(dati.getNome(),dati.getCognome(),dati.getEmail());
                cliente1.setToken(utente.getToken());
                setCliente(cliente1);
                labelNomeProf.setText(dati.getNome());
                labelCognomeProf.setText(dati.getCognome());
                labelMail.setText(dati.getEmail());
                labelNome.setText(dati.getNome());
                labelCognome.setText(dati.getCognome());
            });}).start();
    }

    public void apriPaginaCercaAnnunci() {
        try{
            HomePage.initializeHomePage((Stage) btnCercaAnnunci.getScene().getWindow(),cliente);
        } catch (IOException e) {
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }

    public void apriPaginaModificaProfilo() {
        try {
            System.out.println(cliente.getToken());
            ModificaProfiloAgenteController.setUtente(cliente);
            ModificaProfiloCliente.initializePageModificaProfiloCliente(labelCognome.getScene().getWindow());
        } catch (IOException e) {
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }

    public void apriPaginaVisualizzaNotifiche() {
        try {
            VisualizzaNotificheClienteController.setClienteNotifiche(cliente);
            VisualizzaNotificheCliente.initPage((Stage) labelNomeProf.getScene().getWindow());
        }catch (RuntimeException e){
            e.printStackTrace();
            AlertFactory.generateFailAlertForErroreInterno();
        }
    }

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }

    public void vaiIndietro() {
        if (previousScene != null) {
            Stage stage = (Stage) btnIndietro.getScene().getWindow();
            stage.setScene(previousScene);
        } else {
            System.out.println("Errore: previousScene è null");
            AlertFactory.generateFailAlertForErroreInterno();
        }
    }

    public void faiLogout() {
        Alert alert = AlertFactory.generateAlertForLogout();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                System.out.println("ciao");
                DashBoardClienteController.setCliente(null);
                LoginPage.startMainApp((Stage) btnLogout.getScene().getWindow());
            } catch (IOException e) {
                AlertFactory.generateFailAlertForErroreCaricamentoPagina();
            }
        }else {
            System.out.println("Logout annullato");
        }
    }
}
