package com.example.prova2.controller;
import com.example.prova2.controller.modificaProfilo.ChangePasswordInterface;
import com.example.prova2.facade.GestoreServiceFacade;
import com.example.prova2.facade.UtenteFacade;
import com.example.prova2.model.Utente;
import com.example.prova2.factory.AlertFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;

import static com.example.prova2.controller.registrazione.RegistrazioneUtenteController.visualizzaPassword;

public class CambioPasswordController {

    @FXML
    public PasswordField passwordAttualeField;

    @FXML
    public PasswordField nuovaPasswordField;

    @FXML
    public PasswordField rinsertNuovaPassword;

    @FXML
    public Button salvaBottone;

    @FXML
    public Button tornaIndietroBottone;

    @FXML
    public Label errorreVecchiaPass;

    @FXML
    public Label erroreNuovaPass;

    @FXML
    public Label erroreRinserNuovaPass;

    protected static final String PASSWORDPATTERN = "^.{8,}$";

    @FXML
    public Label labelNuovaPass;

    @FXML
    public Label labelInsertNuovaPass;
    public CheckBox mostraPasswordCheckBox;
    public TextField mostraPa1;
    public TextField mostraPa2;
    public TextField mostraPa3;
    public CheckBox mostraPasswordCheckBox1;
    public CheckBox mostraPasswordCheckBox11;

    private Scene scene;

    private Utente utenteCheCambiaPassword;

    public Utente getUtenteCheCambiaPassword() {
        return utenteCheCambiaPassword;
    }

    public void setUtenteCheCambiaPassword(Utente utenteCheCambiaPassword) {
       this.utenteCheCambiaPassword = utenteCheCambiaPassword;
    }

    private ChangePasswordInterface controllerPrecedente;

    public ChangePasswordInterface getControllerPrecedente() {
        return controllerPrecedente;
    }

    public void setControllerPrecedente(ChangePasswordInterface controllerPrecedente) {
        this.controllerPrecedente = controllerPrecedente;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }

    public void cambiaPassword(){
        if(checkCampi()){
            if(isCambioAvvenuto()) {
                mostrapopupconfermaPassword();
            }
            else{
                mostraPopUpErrorePassword();
            }
        }
    }

    private static void mostraPopUpErrorePassword() {
        Alert alert = AlertFactory.generateFailAlert("Errore nel cambio della password!","Non è stato possibile cambiare la password.Siamo spiacenti si è verificato un errore durante l'aggiornamento della password.");
        alert.showAndWait();
    }

    private void mostrapopupconfermaPassword() {
        Alert alert = AlertFactory.generateAlertSuccessCambioPassword(
                "Ok",
                "Operazione completata!",
                "La password è stata cambiata con successo.",
                "Complimenti! La password è stata aggiornata con successo.\nOra potrai utilizzare la nuova password per accedere a tutti i \nservizi di DietiEstates."
        );
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get().getText().equals("Ok")) {
                Stage stage = (Stage) salvaBottone.getScene().getWindow();
                stage.close();
                controllerPrecedente.setPasswordVecchia(passwordAttualeField.getText());
                controllerPrecedente.annullaCambioPassword();
            }
        }

    }

    public void init(){
        mostraPa1.textProperty().bindBidirectional(passwordAttualeField.textProperty());
        mostraPa2.textProperty().bindBidirectional(nuovaPasswordField.textProperty());
        mostraPa3.textProperty().bindBidirectional(rinsertNuovaPassword.textProperty());
    }

    private boolean isCambioAvvenuto() {
        new Thread(()->{
            UtenteFacade.updatePassword(nuovaPasswordField.getText(), utenteCheCambiaPassword);
        }).start();
        return true;
    }


    public boolean checkCampi(){
        boolean flag = true;
        flag &= checkVecchiaPassword();
        flag &= checkNuovaPass();
        System.out.println(flag);
        flag &= checkRePassword();
        return flag;
    }

    public boolean checkVecchiaPassword() {
        String password = passwordAttualeField.getText();
        if(isPasswordVecchiaValida(password)){
            errorreVecchiaPass.setVisible(false);
            return true;
        }
        else{
            if(password.isEmpty()){
                scriviMessaggioErrore("Si prega di inserire una password!");
            }else{
                scriviMessaggioErrore("Ci dispiace ma la password inserita non corrisponde alla password attuale!");
            }
            errorreVecchiaPass.setVisible(true);
            return false;
        }
    }

    private void scriviMessaggioErrore(String s) {
        errorreVecchiaPass.setText(s);
    }

    private boolean isPasswordVecchiaValida(String password) {
        return GestoreServiceFacade.isPasswordVecchiaValida(utenteCheCambiaPassword.getAccountAgente().getEmail(), password
        ,utenteCheCambiaPassword.getToken());
    }

    public boolean checkNuovaPass() {
       if(nuovaPasswordField.getText().matches(PASSWORDPATTERN)){
           erroreNuovaPass.setVisible(false);
           return true;
       }else{
           erroreNuovaPass.setText("Si prega di inserire una password di almeno 8 caratteri!");
           erroreNuovaPass.setVisible(true);
           return false;
       }
    }


    public boolean checkRePassword() {
        if(passwordNuovaERinsertSonoUguali()){
            erroreRinserNuovaPass.setVisible(false);
            return true;
        }else{
            erroreRinserNuovaPass.setText("Si prega di inserire due password uguali!");
            erroreRinserNuovaPass.setVisible(true);
            return false;
        }
    }

    private boolean passwordNuovaERinsertSonoUguali() {
        return rinsertNuovaPassword.getText().equals(nuovaPasswordField.getText());
    }


    public void tornaDash() {
        Stage stage = (Stage) salvaBottone.getScene().getWindow();
        stage.close();
    }

    public void levaMessaggioDiErroreVecchiaPass() {
        errorreVecchiaPass.setVisible(false);
    }

    public void levaMessaggioDiErroreNuovaPass() {
        erroreNuovaPass.setVisible(false);
    }

    public void levaMessaggioDiErroreRinserNuovaPass() {
        erroreRinserNuovaPass.setVisible(false);
    }

    @FXML
    public void gestisciMostraPass1() {
        visualizzaPassword(mostraPasswordCheckBox,mostraPa1,passwordAttualeField);
    }

    @FXML
    public void gestisciMostraPass2() {
        visualizzaPassword(mostraPasswordCheckBox1,mostraPa2,nuovaPasswordField);
    }


    public void gestisciMostraPass3() {
        visualizzaPassword(mostraPasswordCheckBox11,mostraPa3,rinsertNuovaPassword);
    }

    public void svuota(ActionEvent actionEvent) {
        passwordAttualeField.setText("");
        nuovaPasswordField.setText("");
        rinsertNuovaPassword.setText("");
        mostraPa1.setText("");
        mostraPa2.setText("");
        mostraPa3.setText("");
    }
}
