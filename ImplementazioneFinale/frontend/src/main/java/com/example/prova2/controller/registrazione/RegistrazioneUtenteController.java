package com.example.prova2.controller.registrazione;
import com.example.prova2.dto.ClienteDTO;
import com.example.prova2.facade.ClienteServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Account;
import com.example.prova2.model.Cliente;

import com.example.prova2.view.HomePage;
import com.example.prova2.view.LoginPage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class RegistrazioneUtenteController {
    @FXML
    public TextField textFieldNome;
    @FXML
    public TextField textFieldCognome;
    @FXML
    public TextField textFieldTelefono;
    @FXML
    public TextField textFieldEmail;
    @FXML
    public Label erroreNome;
    @FXML
    public Label erroreCognome;
    @FXML
    public Label erroreTelefono;
    @FXML
    public Label erroreMail;
    @FXML
    public Label errorePassword;

    private static final String PASSWORDPATTERN = "^.{8,}$";

    protected static final String EMAIL_PATTERN="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private static final String NAME_PATTERN = "^[a-zA-ZàèéìòùÀÈÉÌÒÙ'\\-\\s]{2,}$";

    private static final String PHONE_PATTERN = "^\\d{10}$";
    @FXML
    public PasswordField passwordField;

    @FXML
    public Button bottoneTornaAllaHome;
    @FXML
    public CheckBox mostraPasswordCheckBox;
    @FXML
    public TextField mostraPa;

    private String tokenId;

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void init(){
        aggiungiListenerMaiuscoleENonNumeri(textFieldNome);
        aggiungiListenerMaiuscoleENonNumeri(textFieldCognome);
        aggiungiListenerMaxCampo(textFieldEmail);
        aggiungiListenerMaxCampo(passwordField);
        aggiungiListenerMaxCampo(mostraPa);
        mostraPa.textProperty().bindBidirectional(passwordField.textProperty());
    }

    private void aggiungiListenerMaxCampo(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            String filtered = newValue.replaceAll("[0-9]", "");
            if (filtered.length() > 40) {
                filtered.substring(0, 40);
            }
        });
    }

    private void aggiungiListenerMaiuscoleENonNumeri(TextField text) {
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            String filtered = newValue.replaceAll("[0-9]", "");
            if (filtered.length() > 20) {
                filtered = filtered.substring(0, 20); // Limita a 20 caratteri
            }
            if (!filtered.isEmpty()) {
                text.setText(filtered.substring(0, 1).toUpperCase() + filtered.substring(1));
            } else {
                text.setText("");
            }
        });
    }

    public void registraCliente() {
        if(checkCampi()){
            Account accountSemplice = new Account(textFieldEmail.getText().trim(),passwordField.getText().trim());
            ClienteDTO dto = ClienteServiceFacade.registrati(new Cliente(textFieldNome.getText().trim(), textFieldCognome.getText().trim(), accountSemplice));
            if (dto != null) {
                setTokenId(dto.getToken());
                Platform.runLater(()->{                        isErroreInEmail(dto);
                });
            }
        }
    }

    public void tornaAllaHome(){
        try {
            LoginPage.startMainApp((Stage) bottoneTornaAllaHome.getScene().getWindow());
        } catch (IOException e) {
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }

    private void isErroreInEmail(ClienteDTO dto) {
        if(dto.isDuplicato()){
            erroreMail.setVisible(true);
            erroreMail.setText("L'email è già in uso, scegli un'altra!");
            return;
        }
        if(dto.isErroreInterno()){
           AlertFactory.generateFailAlertForErroreInterno();
           return;
        }
        visualizzaMessaggioConferma();
    }


    public void visualizzaMessaggioConferma(){
        Alert alert = generateSuccessMessage();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get().getText().equals("Torna alla pagina di accesso")) {
                apriLoginPage();
            } else if (result.get().getText().equals("Inizia a esplorare le opportunità immobiliari")) {
                apriHome();
            }
        }
    }

    public void apriLoginPage(){
        try {
            LoginPage.startMainApp((Stage) bottoneTornaAllaHome.getScene().getWindow());
        } catch (IOException e) {
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }

    public void apriHome(){
        try {
            Cliente cliente = new Cliente();
            cliente.setAccountAgente(new Account(textFieldEmail.getText(),passwordField.getText()));
            cliente.setToken(tokenId);
            System.out.println(tokenId);
            HomePage.caricamentoHome((Stage) bottoneTornaAllaHome.getScene().getWindow(), cliente);
        } catch (IOException e) {
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }

    public static Alert generateSuccessMessage() {
        return AlertFactory.generateAlertSuccess(
                "Torna alla pagina di accesso",
                "Inizia a esplorare le opportunità immobiliari",
                "Registrazione Completata con Successo",
                "Benvenuto in DietiEstates25",
                "Siamo lieti di darti il benvenuto su DietiEstates25. Ora puoi iniziare a cercare la tua futura casa con facilità e sicurezza. Se hai bisogno di assistenza, non esitare a contattarci."
        );

    }

    public boolean checkCampi() {
        boolean flag = true;
        flag&=isValidName(textFieldNome.getText());
        flag&=isValidCognome(textFieldCognome.getText());
        flag&=isValidPassword(passwordField.getText().trim());
        flag&=isValidEmail(textFieldEmail.getText().trim());
        return flag;
    }

    public void levaMessaggioDiErroreNome() {
        levaMessaggioDiErrore(erroreNome);
    }

    public void levaMessaggioDiErroreCognome() {
        levaMessaggioDiErrore(erroreCognome);
    }

    public void levaMessaggioDiErroreTelefono() {
        levaMessaggioDiErrore(erroreTelefono);
    }

    public void levaMessaggioDiErroreEmail() {
        levaMessaggioDiErrore(erroreMail);
    }

    public void levaMessaggioDiErrorePassword() {
        levaMessaggioDiErrore(errorePassword);
    }

    public boolean isValidName(String name) {
        if (name == null) {
            return false;
        }
        if(campoVuoto(name)){scriviMessaggioErrore(erroreNome,"Si prega di inserire un nome!");return false;}
        if(campoMatcha(name,NAME_PATTERN)){
            levaMessaggioDiErrore(erroreNome);
            return true;
        }
        return inserisciCampoValido(erroreNome,"Si prega di inserire un nome valido!");
    }

    public static boolean inserisciCampoValido(Label label,String s) {
        scriviMessaggioErrore(label,s);
        return false;
    }

    public static boolean campoMatcha(String name,String pattern) {
        return name.matches(pattern);
    }

    public static void scriviMessaggioErrore(Label label,String text){
        label.setText(text);
        label.setVisible(true);
    }

    public boolean isValidCognome(String cognome) {
        if (cognome == null) {
            return false;
        }
        if(campoVuoto(cognome)){scriviMessaggioErrore(erroreCognome,"Si prega di inserire un cognome!");return false;}
        if(campoMatcha(cognome,NAME_PATTERN)){
            levaMessaggioDiErrore(erroreCognome);
            return true;
        }
        return inserisciCampoValido(erroreCognome,"Si prega di inserire un cognome valido!");
    }

    public boolean isValidPhone(String phone) {
        if (phone == null) {
            return false;
        }
        if(campoVuoto(phone)){scriviMessaggioErrore(erroreTelefono,"Si prega di inserire un numero di telefono!");return false;}
        if(campoMatcha(phone,PHONE_PATTERN)){
            levaMessaggioDiErrore(erroreTelefono);
            return true;
        }
        return inserisciCampoValido(erroreTelefono,"Si prega di inserire un telefono valido!");
    }

    public boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        if(campoVuoto(email)){scriviMessaggioErrore(erroreMail,"Si prega di inserire una email!");return false;}
        if(campoMatcha(email,EMAIL_PATTERN)){
            levaMessaggioDiErrore(erroreMail);
            return true;
        }
        return inserisciCampoValido(erroreMail,"Si prega di inserire una email valida!");
    }

    public boolean isValidPassword(String password) {
        return passwordValidation(password, errorePassword, PASSWORDPATTERN);
    }

    public static boolean passwordValidation(String password, Label errorePassword, String passwordpattern) {
        if (password == null) {
            return false;
        }
        if(campoVuoto(password)){scriviMessaggioErrore(errorePassword,"Si prega di inserire una password!");return false;}
        if(campoMatcha(password, passwordpattern)){
            levaMessaggioDiErrore(errorePassword);
            return true;
        }
        return inserisciCampoValido(errorePassword,"Si prega di inserire una password valida!");
    }


    public static boolean campoVuoto(String campo){
        return campo.isEmpty();
    }

    public static void levaMessaggioDiErrore(Label label){
        label.setVisible(false);
    }

    public void gestisciMostraPass() {
        visualizzaPassword(mostraPasswordCheckBox, mostraPa, passwordField);
    }

    public static void visualizzaPassword(CheckBox mostraPasswordCheckBox, TextField mostraPa, PasswordField passwordField) {
        if (mostraPasswordCheckBox.isSelected()) {
            mostraPa.setVisible(true);
            mostraPa.setEditable(true);
            passwordField.setVisible(false);
        } else {
            mostraPa.setVisible(false);
            mostraPa.setEditable(false);
            passwordField.setVisible(true);
            passwordField.setVisible(true);
        }
    }

    public void ripulisci() {
        textFieldNome.setText("");
        textFieldCognome.setText("");
        textFieldEmail.setText("");
        passwordField.setText("");
        mostraPa.setText("");
    }
}

