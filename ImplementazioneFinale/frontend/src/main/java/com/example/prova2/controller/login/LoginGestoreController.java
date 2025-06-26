package com.example.prova2.controller.login;


import com.example.prova2.controller.dashBoard.DashboardAmministratoreController;
import com.example.prova2.controller.registrazione.RegistrazioneUtenteController;
import com.example.prova2.dto.AgenteDTO;
import com.example.prova2.facade.AuthServiceFacade;
import com.example.prova2.model.Account;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.view.DashboardAmministratore;
import com.example.prova2.view.LoginPage;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginGestoreController {
    @FXML
    private CheckBox mostraPasswordCheckBox;
    @FXML
    private TextField mostraPa;
    @FXML
    public Button bottoneTornaAllaHome;
    @FXML
    public TextField usernameTextField;
    @FXML
    public Label errorLabel;
    @FXML
    public Button bottoneAccedi;
    @FXML
    public Label compilaPassword;
    @FXML
    public PasswordField passwordTextField;


    protected static final String PASSWORDPATTERN = "^.{8,}$";

    protected static final String EMAILPATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";


    public void apriDashBoard(Account accountGestore, AgenteDTO dto) {
        try {
            GestoreAgenziaImmobiliare gestore = creaGestore(accountGestore, dto);
            DashboardAmministratoreController.setAdmin(gestore);
            DashboardAmministratore.initializePageDashboardAmministratore((Stage) bottoneAccedi.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
            gestisciErroreCaricamentoDashboard(e);
        }
    }

    private GestoreAgenziaImmobiliare creaGestore(Account accountGestore, AgenteDTO dto) {
        GestoreAgenziaImmobiliare gestore = new GestoreAgenziaImmobiliare();
        gestore.setAccountAgente(accountGestore);
        gestore.setAdmin(dto.isAdmin());
        gestore.setToken(dto.getToken());
        return gestore;
    }
    public void svuotaLogin() {
        usernameTextField.clear();
        passwordTextField.clear();
        mostraPa.clear();
    }

    private void gestisciErroreCaricamentoDashboard(Exception e) {
        AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        if (e instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }
        e.printStackTrace();
    }

    public void eliminaMessaggioDiErroreUsr() {
        errorLabel.setText("");
    }

    public void eliminaMessaggioDiErrorePass() {
        compilaPassword.setText("");
    }

    public void tornaHome() {
        try{
            LoginPage.startMainApp((Stage) bottoneTornaAllaHome.getScene().getWindow());
        } catch (IOException e) {
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
            e.printStackTrace();
        }
    }

    public void accedi() {
        if (!controllaCampi()) {
            return;
        }
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        Account account = new Account(username,password);
        GestoreAgenziaImmobiliare gestoreAgenziaImmobiliare = new GestoreAgenziaImmobiliare();
        gestoreAgenziaImmobiliare.setAccountAgente(account);
        AgenteDTO dto = AuthServiceFacade.login(gestoreAgenziaImmobiliare.getAccountAgente().getEmail(),gestoreAgenziaImmobiliare.getAccountAgente().getPassword());
        if(dto != null){
            apriDashBoard(new Account(username,password),dto);
            return ;
        }
        AlertFactory.generateFailAlertForCredenzialiSbagliate();

    }


    public boolean controllaCampi() {
        boolean flag = true;
        flag&=checkCampo(usernameTextField.getText(), errorLabel, EMAILPATTERN,"Si prega di inserire una email valida!");
        flag&=checkCampo(passwordTextField.getText(),compilaPassword,PASSWORDPATTERN,"Si prega di inserire una password valida!");
        return flag;
    }

    public boolean checkCampo(String username,Label errorLabel,String pattern,String message) {
        if(username.matches(pattern)) {
            return true;
        }else{
            if(username.isEmpty()) {
                errorLabel.setVisible(true);
                errorLabel.setText("Si prega di compilare questo campo!");
            }else{
                errorLabel.setVisible(true);
                errorLabel.setText(message);
            }
            return false;
        }
    }

    public void initialize() {
        sincronizzaPasswordFieldTextField();
        bottoneAccedi.setDefaultButton(true);
    }


    private void sincronizzaPasswordFieldTextField() {
        mostraPa.textProperty().bindBidirectional(passwordTextField.textProperty());
    }
    public void gestisciMostraPass() {
        RegistrazioneUtenteController.visualizzaPassword(mostraPasswordCheckBox, mostraPa, passwordTextField);
    }
}
