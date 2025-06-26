package com.example.prova2.controller;
import com.example.prova2.controller.dashBoard.DashboardAmministratoreController;
import com.example.prova2.facade.AgenteServiceFacade;
import com.example.prova2.facade.GestoreServiceFacade;
import com.example.prova2.model.Account;
import com.example.prova2.model.Agente;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.view.DashboardAmministratore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

import static com.example.prova2.controller.registrazione.RegistrazioneUtenteController.visualizzaPassword;


public class CreaAgenteImmobiliareController {


    private static final String NAME_PATTERN = "^[a-zA-ZàèéìòùÀÈÉÌÒÙ'\\-\\s]{2,}$";

    protected static final String CFPATTERN = "^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$";

    protected static final String TELEFONOPATTERN = "^[0-9]{10}$";

    protected static final String EMAILPATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    protected static final String PASSWORDPATTERN = "^.{8,}$";

    @FXML
    public Button tornaIndietroBottone;
    @FXML
    public TextField textFieldNome;
    @FXML
    public TextField textFieldCognome;
    @FXML
    public TextField textFieldNumeroTelefono;
    @FXML
    public TextField textFieldEmail;
    @FXML
    public PasswordField textFieldPassword;
    @FXML
    public Label erroreNome;
    @FXML
    public Label erroreCognome;
    @FXML
    public Label erroreTelefono;
    @FXML
    public Label erroreEmail;
    @FXML
    public Label errorePassword;
    @FXML
    public Button buttonCrea;

    @FXML
    public TextField textFieldCf;

    @FXML
    public Label erroreCodiceFiscale;
    @FXML public Label scrittaCaricamento;
    @FXML public ImageView logoCaricamento;
    @FXML public Label puntiniCaricamento;
    @FXML
    public CheckBox mostraPasswordCheckBox;
    @FXML
    public TextField mostraPa;

    protected Stage stagePrecedente;

    private Scene scenePrecedente;

    public Scene getScenePrecedente() {
        return scenePrecedente;
    }

    public void setScenePrecedente(Scene scenePrecedente) {
        this.scenePrecedente = scenePrecedente;
    }

    public void setStagePrecedente(Stage stagePrecedente) {
        this.stagePrecedente = stagePrecedente;
    }

    public void init(){
        aggiungiListenerMaiuscoleENonNumeri(textFieldNome);
        aggiungiListenerMaiuscoleENonNumeri(textFieldCognome);
        aggiungiListenerSoloNumeri(textFieldNumeroTelefono);
        aggiungiListenerTutteMaiuscole(textFieldCf);
        mostraPa.textProperty().bindBidirectional(textFieldPassword.textProperty());
    }

    private void aggiungiListenerMaiuscoleENonNumeri(TextField text) {
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            String filtered = newValue.replaceAll("[0-9]", "");
            if (!filtered.isEmpty()) {
                text.setText(filtered.substring(0, 1).toUpperCase() + filtered.substring(1));
            } else {
                text.setText("");
            }
        });
    }


    private void aggiungiListenerTutteMaiuscole(TextField text) {
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(newValue.toUpperCase())) {
                text.setText(newValue.toUpperCase());
            }
        });
    }


    private void aggiungiListenerSoloNumeri(TextField textField){
        aggiungi(textField);
    }

    static void aggiungi(TextField textField) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("0|([1-9]\\d*)?")) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }

    public void registraCliente() {
        boolean flag = true;
        flag&=checkCampi();
        if(flag){
            inviaDati();
        }
    }

    private void inviaDati() {
        if (salvaDatiConSuccesso()) {
            mostraPopUpConferma();
        }
    }



    private boolean salvaDatiConSuccesso() {
        Agente agente = new Agente(textFieldNome.getText().trim(),textFieldCognome.getText().trim(),
                textFieldCf.getText().trim(),textFieldNumeroTelefono.getText().trim());
        Account accountAgente = new Account(textFieldEmail.getText()
                , textFieldPassword.getText());
        GestoreAgenziaImmobiliare gestoreRiferimento = DashboardAmministratoreController.getAdmin();
        agente.setAccountAgente(accountAgente);
        agente.setGestoreRiferimento(gestoreRiferimento);
        return AgenteServiceFacade.addAgente(agente,gestoreRiferimento);
    }


    private void mostraPopUpConferma() {
        Alert alert = generateSuccessMessage();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get().getText().equals("Torna alla dashboard")) {
                navigaAllaDashboard();
            } else if (result.get().getText().equals("Continua a creare altri agenti")) {
                navigaACreaGestore();
            }
        }
    }

    public static Alert generateSuccessMessage() {
        return AlertFactory.generateAlertSuccess("Torna alla dashboard", "Continua a creare altri agenti", "Operazione completata!", "Hai creato con successo il tuo agente!", "Complimenti ti arriverà una notifica di riepilogo, ricordati che puoi sempre cancellare gli agenti creati nella tua dashboard");
    }

    private void navigaAllaDashboard() {
        try {
            DashboardAmministratore.initializePageDashboardAmministratore((Stage) buttonCrea.getScene().getWindow());
        } catch (IOException e) {
            AlertFactory.generateFailAlert("Errore di navigazione", "Si è verificato un problema durante il caricamento della dashboard.");
        }
    }

    private void navigaACreaGestore() {

    }


    public boolean checkCampi() {
        boolean flag = true;
        flag &= isValidName(textFieldNome.getText());
        flag &= isValidCognome(textFieldCognome.getText());
        flag &= isValidPassword(textFieldPassword.getText().trim(),errorePassword,PASSWORDPATTERN);
        flag &= isValidEmail(textFieldEmail.getText().trim(),erroreEmail,EMAILPATTERN);
        flag &= isValidPhone(textFieldNumeroTelefono.getText().trim());
        flag&=isValidCf(textFieldCf.getText().trim());
        return flag;
    }

    public void svuotaDati(){
        textFieldCf.clear();
        mostraPa.clear();
        textFieldNumeroTelefono.clear();
        textFieldEmail.clear();
        textFieldPassword.clear();
        textFieldCognome.clear();
        textFieldNome.clear();
    }

    public boolean isValidName(String name) {
        if (name == null) {
            return false;
        }
        if (campoVuoto(name)) {
            scriviMessaggioErrore(erroreNome, "Si prega di inserire un nome!");
            return false;
        }
        if (campoMatcha(name, NAME_PATTERN)) {
            levaMessaggioDiErrore(erroreNome);
            return true;
        }
        return inserisciCampoValido(erroreNome, "Si prega di inserire un nome valido!");
    }

    public static boolean inserisciCampoValido(Label label, String s) {
        scriviMessaggioErrore(label, s);
        return false;
    }

    public static boolean campoMatcha(String name, String pattern) {
        return name.matches(pattern);
    }

    public static void scriviMessaggioErrore(Label label, String text) {
        label.setText(text);
        label.setVisible(true);
    }

    public boolean isValidCf(String cf) {
        if (cf == null) {
            return false;
        }
        if (campoVuoto(cf)) {
            scriviMessaggioErrore(erroreCodiceFiscale, "Si prega di inserire un codice fiscale!");
            return false;
        }
        if (campoMatcha(cf, CFPATTERN)) {
            levaMessaggioDiErrore(erroreCodiceFiscale);
            return true;
        }
        return inserisciCampoValido(erroreCodiceFiscale, "Si prega di inserire un codice fiscale valido!");
    }


    public boolean isValidCognome(String cognome) {
        if (cognome == null) {
            return false;
        }
        if (campoVuoto(cognome)) {
            scriviMessaggioErrore(erroreCognome, "Si prega di inserire un cognome!");
            return false;
        }
        if (campoMatcha(cognome, NAME_PATTERN)) {
            levaMessaggioDiErrore(erroreCognome);
            return true;
        }
        return inserisciCampoValido(erroreCognome, "Si prega di inserire un cognome valido!");
    }

    public boolean isValidPhone(String phone) {
        if (phone == null) {
            return false;
        }
        if (campoVuoto(phone)) {
            scriviMessaggioErrore(erroreTelefono, "Si prega di inserire un numero di telefono!");
            return false;
        }
        if (campoMatcha(phone, TELEFONOPATTERN)) {
            levaMessaggioDiErrore(erroreTelefono);
            return true;
        }
        return inserisciCampoValido(erroreTelefono, "Si prega di inserire un telefono valido!");
    }

    public static boolean isValidEmail(String email,Label erroreLabel,String emailPattern) {
        if (email == null) {
            return false;
        }
        if (campoVuoto(email)) {
            scriviMessaggioErrore(erroreLabel, "Si prega di inserire una email!");
            return false;
        }
        if (campoMatcha(email, emailPattern)) {
            levaMessaggioDiErrore(erroreLabel);
            return true;
        }
        return inserisciCampoValido(erroreLabel, "Si prega di inserire una email valida!");
    }

    public static boolean isValidPassword(String password,Label errorePass,String passwordPattern) {
        return passwordValidation(password, errorePass,passwordPattern);
    }

    public static boolean passwordValidation(String password, Label errorePassword,String patternPassword) {
        if (password == null) {
            return false;
        }
        if (campoVuoto(password)) {
            scriviMessaggioErrore(errorePassword, "Si prega di inserire una password!");
            return false;
        }
        if (campoMatcha(password,patternPassword)) {
            levaMessaggioDiErrore(errorePassword);
            return true;
        }
        return inserisciCampoValido(errorePassword, "Si prega di inserire una password valida!");
    }


    public static boolean campoVuoto(String campo) {
        return campo.isEmpty();
    }

    public static void levaMessaggioDiErrore(Label label) {
        label.setVisible(false);
    }




    @FXML
    private boolean checkDuplicatoCF(String cf) {
        List<String> cfs=GestoreServiceFacade.getCfGestore(DashboardAmministratoreController.getAdmin().getToken());
        if(exists(cf,cfs)){
            scriviMessaggioErrore(erroreCodiceFiscale,"Si prega di inserire un codice fiscale non registrato!");
            return false;
        }
        return true;
    }

    @FXML
    private boolean checkDuplicatoEmail(String email) {
        List<String> emails=GestoreServiceFacade.getEmailGestore(DashboardAmministratoreController.getAdmin());
        if(exists(email,emails)){
            scriviMessaggioErrore(erroreEmail,"Si prega di inserire una email non registrata!");
            return false;
        }
        return true;
    }

    private boolean exists(String campo,List<String> campi)
    {
        for (String s:campi){
            if(campo.equals(s)){
                return true;
            }
        }
        return false;
    }

    public void tornaIndietro() {
        if (stagePrecedente != null && scenePrecedente != null) {
            stagePrecedente.setScene(scenePrecedente); // Ripristina la scena precedente
        }
    }


    public void levaMessaggioDiErrorePassword() {
        levaMessaggioDiErrore(errorePassword);
    }

    public void levaMessaggioDiErroreEmail() {
        levaMessaggioDiErrore(erroreEmail);
    }


    public void levaMessaggioDiErroreTelefono() {
        levaMessaggioDiErrore(erroreTelefono);
    }

    public void levaMessaggioDiErroreCognome() {
        levaMessaggioDiErrore(erroreCognome);
    }

    public void levaMessaggioDiErroreNome() {
        levaMessaggioDiErrore(erroreNome);
    }

    public void levaMessaggioDiErroreCF() {
        levaMessaggioDiErrore(erroreCodiceFiscale);
    }

    public void gestisciMostraPass(ActionEvent actionEvent) {
        visualizzaPassword(mostraPasswordCheckBox,mostraPa,textFieldPassword);
    }
}

