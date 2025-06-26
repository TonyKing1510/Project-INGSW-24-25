package com.example.prova2.controller.login;
import com.example.prova2.controller.CreaAgenteImmobiliareController;
import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.controller.dashBoard.DashboardAmministratoreController;
import com.example.prova2.controller.registrazione.RegistrazioneUtenteController;
import com.example.prova2.dto.AgenteDTO;
import com.example.prova2.facade.AuthServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Account;
import com.example.prova2.model.Agente;
import com.example.prova2.model.Cliente;
import com.example.prova2.model.GestoreAgenziaImmobiliare;


import com.example.prova2.view.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class LoginHomeController {
    @FXML
    private CheckBox mostraPasswordCheckBox;
    @FXML
    private TextField mostraPa;
    @FXML
    public Label registratiBotton;

    @FXML public Button btnGoogle;
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label emailErrorLabel;

    @FXML
    private Label passwordErrorLabel;

    @FXML
    private Button buttonAgenteImmobiliare;

    @FXML
    private Button signInButton;

    @FXML
    private Button btnAmministratore;

    protected static final String PASSWORDPATTERN = "^.{8,}$";

    protected static final String EMAILPATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private static String tokenId;

    public static void setToken(String token) {
        tokenId = token;
    }

    public  String getEmail(){
        return emailField.getText();
    }

    public String getPassword(){
        return passwordField.getText();
    }

    private static final String CLIENT_ID = "603934349943-q9t7jpn8i2hb0ivlgg0u1rco1nm6hji5.apps.googleusercontent.com";
    private static final String REDIRECT_URI = "http://localhost:9094/auth/exchange_token";
    private static final String AUTH_URL = "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&scope=email";


    @FXML
    private void handleButtonAgente() throws IOException {
        LoginAgenteImmobiliare.initializePageLoginAgente(buttonAgenteImmobiliare.getScene().getWindow());
    }

    @FXML
    private void handleButtonAmministratore() throws IOException {
        AccediAdmin.initializePageAccediAdmin(btnAmministratore.getScene().getWindow());
    }


    @FXML
    private  void handleSignIn(){
        boolean flag=true;
        flag&=checkEmail();
        flag&=checkPassword();
        if(campiValidi(flag) && (credenzialiEsatte())) {
                apriHome();
        }
    }

    public void apriPaginaRegistrati(){
        try {
           RegistrazioneUtente.initializePageRegistrazioneUtente(registratiBotton.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }


    private void apriHome(){
        try {
            Cliente utente = new Cliente();
            utente.setAccountAgente(new Account(getEmail(),getPassword()));
            utente.setToken(tokenId);
            HomePage.caricamentoHome((Stage) signInButton.getScene().getWindow(),utente);
        } catch (IOException e) {
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }

    private boolean credenzialiEsatte() {
        Account a = new Account(emailField.getText().trim(),passwordField.getText().trim());
        Cliente cliente = new Cliente();
        cliente.setAccountAgente(a);
        AgenteDTO dto= AuthServiceFacade.login(cliente.getAccountAgente().getEmail(),cliente.getAccountAgente().getPassword());
        if(dto != null){
            if(!dto.getRole().equals("Cliente")){
                AlertFactory.generateFailAlertForCredenzialiSbagliate();
                return false;
            }
            setToken(dto.getToken());
            return true;
        }
        AlertFactory.generateFailAlertForCredenzialiSbagliate();
        return false;
    }

    private static boolean campiValidi(boolean flag) {
        return flag;
    }

    private boolean checkEmail(){
        return CreaAgenteImmobiliareController.isValidEmail(emailField.getText(),emailErrorLabel,EMAILPATTERN);
    }

    private boolean checkPassword(){
        return CreaAgenteImmobiliareController.isValidPassword(passwordField.getText(),passwordErrorLabel,PASSWORDPATTERN);
    }

    public void levaMessaggioDiErrorePassword() {
        levaMessaggioDiErrore(passwordErrorLabel);
    }

    public void levaMessaggioDiErroreEmail() {
        levaMessaggioDiErrore(emailErrorLabel);
    }

    public void levaMessaggioDiErrore(Label label) {
        label.setVisible(false);
    }


        public void authGoogle() {
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webEngine.load(AUTH_URL);
            webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.startsWith(REDIRECT_URI)) {
                    String code = extractCodeFromUrl(newValue);
                    if (code != null) {
                        AgenteDTO response=AuthServiceFacade.authWithGoogle(code);
                        Stage stage = (Stage) webView.getScene().getWindow();
                        stage.close();
                        apriPaginaPerUtente(response);
                    }
                }
            });

            Stage stage = new Stage();
            stage.setScene(new Scene(webView));
            stage.show();
        }

    private void apriPaginaPerUtente(AgenteDTO response) {
        switch (response.getRole()){
            case "Cliente":
                apriPaginaCliente(response);
                break;
            case "Agente":
                apriPaginaAgente(response);
                break;
            case "Gestore":
                apriPaginaGestore(response);
                break;
        }
    }

    private void apriPaginaGestore(AgenteDTO response)  {
        try {
            GestoreAgenziaImmobiliare gestore = new GestoreAgenziaImmobiliare();
            gestore.setToken(response.getToken());
            gestore.setAccountAgente(new Account(response.getEmail()));
            gestore.setAdmin(response.isAdmin());
            DashboardAmministratoreController.setAdmin(gestore);
            DashboardAmministratore.initializePageDashboardAmministratore((Stage) btnGoogle.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }

    private void apriPaginaAgente(AgenteDTO response){
        try {
            Agente agente = new Agente();
            agente.setToken(response.getToken());
            agente.setAccountAgente(new Account(response.getEmail()));
            DashboardAgenteController.setAgente(agente);
            DashboardAgente.initializePageDashboardAgente(btnGoogle.getScene().getWindow());
        }catch (IOException e){
            e.printStackTrace();
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
        catch (InterruptedException e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }

    private void apriPaginaCliente(AgenteDTO response) {
        try {
            Cliente cliente = new Cliente();
            cliente.setToken(response.getToken());
            System.out.println("token giu:"+response.getToken());
            cliente.setAccountAgente(new Account(response.getEmail()));

            HomePage.initializeHomePageWithGoogle((Stage) signInButton.getScene().getWindow(), cliente);
        }catch (IOException e){
            e.printStackTrace();
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }

    private String extractCodeFromUrl(String url) {
                try {
                    URI uri = new URI(url);
                    String query = uri.getQuery();
                    for (String param : query.split("&")) {
                        if (param.startsWith("code=")) {
                            return param.split("=")[1];
                        }
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                return null;
            }

    public void initialize() {
        sincronizzaPasswordFieldTextField();
        signInButton.setDefaultButton(true);
    }


    private void sincronizzaPasswordFieldTextField() {
        mostraPa.textProperty().bindBidirectional(passwordField.textProperty());
    }
    public void gestisciMostraPass() {
        RegistrazioneUtenteController.visualizzaPassword(mostraPasswordCheckBox, mostraPa, passwordField);
    }
}




