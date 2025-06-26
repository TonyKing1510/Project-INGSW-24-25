package com.example.prova2.controller.login;
import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.dto.AgenteDTO;
import com.example.prova2.facade.AuthServiceFacade;
import com.example.prova2.model.Account;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Agente;
import com.example.prova2.view.DashboardAgente;
import com.example.prova2.view.LoginPage;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.prova2.controller.registrazione.RegistrazioneUtenteController.*;


public class LoginAgenteController {
    @FXML
    public TextField textFieldEmail;
    @FXML
    public Button loginButton;
    @FXML
    public Button tornaAllaHome;
    @FXML
    public PasswordField textFieldPassword;
    @FXML
    public TextField mostraPa;
    @FXML
    public CheckBox mostraPasswordCheckBox;

    protected static final String PASSWORDPATTERN = "^.{8,}$";

    protected static final String EMAILPATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @FXML
    public Label errorUsername;

    @FXML
    public Label errorPassword;

    private Agente agenteLogin;

    public Agente getAgente() {
        return agenteLogin;
    }

    public void setAgente(Agente agente) {
        this.agenteLogin = agente;
    }

    public void initialize(){
        loginButton.setDefaultButton(true);
        sincronizzaPasswordFieldTextField();

    }

    private void sincronizzaPasswordFieldTextField() {
        mostraPa.textProperty().bindBidirectional(textFieldPassword.textProperty());
    }

    public void svuotaLogin() {
        textFieldEmail.clear();
        textFieldPassword.clear();
        mostraPa.clear();
    }

    public void handelogin() {
        if(checkCampi()){
            inviaDati();
        }
    }

    private void inviaDati(){
        Account accountSemplice = new Account(textFieldEmail.getText().trim(), textFieldPassword.getText().trim());
        gestisciAuth(accountSemplice);
    }

    private void gestisciAuth(Account accountSemplice){
        if(isAuthRiuscita(accountSemplice)) {
            apriDashBoardAgente();
        }
    }

    private boolean isAuthRiuscita(Account accountSemplice) {
        Agente agente = new Agente();
        agente.setAccountAgente(accountSemplice);
        AgenteDTO dto= AuthServiceFacade.login(agente.getAccountAgente().getEmail(),agente.getAccountAgente().getPassword());
        if(dto == null){
            AlertFactory.generateFailAlertForCredenzialiSbagliate();
            return false;
        }
        if(!dto.getRole().equals("Agente")){
            AlertFactory.generateFailAlertForCredenzialiSbagliate();
            return false;
        }
        agenteLogin = new Agente();
        agenteLogin.setToken(dto.getToken());
        return true;
    }

    private boolean checkCampi() {
        boolean flag = true;
        flag&= checkEmail();
        flag&=checkPassword();
        return flag;
    }

    public boolean checkEmail() {
        String username = this.textFieldEmail.getText();
        return isValidEmail(username);
    }

    public boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        if(campoVuoto(email)){scriviMessaggioErrore(errorUsername,"Si prega di inserire una email!");return false;}
        if(campoMatcha(email, EMAILPATTERN)){
            levaMessaggioDiErrore(errorUsername);
            return true;
        }
        return inserisciCampoValido(errorUsername,"Si prega di inserire uno email valida!");
    }

    public boolean checkPassword() {
        String password = this.textFieldPassword.getText();
        return isValidPassword(password);
    }

    public boolean isValidPassword(String password) {
        return passwordValidation(password, errorPassword, PASSWORDPATTERN);
    }


    public void gestisciMostraPass() {
        visualizzaPassword(mostraPasswordCheckBox, mostraPa, textFieldPassword);
    }


    public void disattivaMessaggioErroreUsername(){
        errorUsername.setVisible(false);
    }

    public void disattivaMessaggioErrorePassword(){
        errorPassword.setVisible(false);
    }

    public void apriDashBoardAgente(){
        try{
            Agente agenteDash = new Agente();
            agenteDash.setAccountAgente(new Account(textFieldEmail.getText().trim(), textFieldPassword.getText().trim()));
            agenteDash.setToken(agenteLogin.getToken());
            DashboardAgenteController.setAgente(agenteDash);
            DashboardAgente.initializePageDashboardAgente(loginButton.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        } catch (InterruptedException e) {
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public void tornaHome(){
        try{
            LoginPage.startMainApp((Stage) tornaAllaHome.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }


}
