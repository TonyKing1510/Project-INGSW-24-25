package com.example.prova2.controller.modificaProfilo;
import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.facade.ModificaProfiloFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Account;
import com.example.prova2.model.Agente;
import com.example.prova2.model.Foto;
import com.example.prova2.model.Utente;

import com.example.prova2.view.CambiaFoto;
import com.example.prova2.view.CambioPassword;
import com.example.prova2.view.DashboardAgente;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import static com.example.prova2.controller.registrazione.RegistrazioneUtenteController.campoVuoto;
import static com.example.prova2.controller.registrazione.RegistrazioneUtenteController.levaMessaggioDiErrore;

public class ModificaProfiloAgenteController implements ChangePasswordInterface {
    @FXML
    public Circle bottoneModificaFoto;
    @FXML
    public ImageView fotoProfilo;
    @FXML
    public ImageView matitaButton;
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
    public TextField textFieldBio;
    @FXML
    public Label erroreNome;
    @FXML
    public Label erroreCognome;
    @FXML
    public Label erroreTelefono;
    @FXML
    public Label erroreEmail;
    @FXML
    public Button aggiornaDati;

    protected static Utente utente;

    protected static final String EMAIL_PATTERN="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private static final String NAME_PATTERN = "^[a-zA-ZàèéìòùÀÈÉÌÒÙ'\\-\\s]{2,}$";

    private static final String PHONE_PATTERN = "^\\d{10}$";
    @FXML
    public Pane paneAnnullaCambio;
    @FXML
    public Pane paneConfermaModifica;

    private String passwordVecchia;

    @FXML
    public Label nomeEcognome;
    @FXML
    public Button btnCancella;
    @FXML
    public Button btnCancellaBio;

    @FXML
    public Button aggiornaBio;
    @FXML
    public Label erroreBio;


    public String getPasswordVecchia() {
        return passwordVecchia;
    }



    public void salvaDati() {
        if(checkCampi()){
            Agente dati = new Agente(textFieldNome.getText(),textFieldCognome.getText(),textFieldNumeroTelefono.getText());
            dati.setCf(utente.getCf());
            dati.setAccountAgente(new Account(textFieldEmail.getText()));
            dati.setToken(DashboardAgenteController.getAgente().getToken());
            if(ModificaProfiloFacade.updateDatiAgente(dati,DashboardAgenteController.getAgente().getAccountAgente().getEmail()) != null) {
                paneConfermaModifica.setVisible(true);
                PauseTransition pausa = new PauseTransition(Duration.seconds(4));

                pausa.setOnFinished(event -> paneConfermaModifica.setVisible(false));

                pausa.play();
                aggiornaAgenteProfilo(dati);
                initProfilo();
            }
        }
    }

    private void aggiornaAgenteProfilo(Agente nuovo) {
        utente.setCf(nuovo.getCf());
        utente.setAccountAgente(new Account(textFieldEmail.getText()));
        utente.setNome(nuovo.getNome());
        utente.setCognome(nuovo.getCognome());
        utente.setTelefono(nuovo.getTelefono());
    }


    public boolean checkCampi() {
        boolean flag = true;
        flag&=isValidName(textFieldNome.getText());
        flag&=isValidCognome(textFieldCognome.getText());
        flag&=isValidEmail(textFieldEmail.getText().trim());
        flag&=isValidPhone(textFieldNumeroTelefono.getText().trim());
        return flag;
    }


    public boolean checkBio(){
        if(textFieldBio.getText().length() >= 3000){
            erroreBio.setVisible(true);
           return false;
        }
        return true;
    }


    private boolean emailVecchiaUgualeNuova() {
        return utente.getAccountAgente().getEmail().equals(textFieldEmail.getText().trim());
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
        if(campoVuoto(email)){scriviMessaggioErrore(erroreEmail,"Si prega di inserire una email!");return false;}
        if(campoMatcha(email,EMAIL_PATTERN)){
            levaMessaggioDiErrore(erroreEmail);
            return true;
        }
        return inserisciCampoValido(erroreEmail,"Si prega di inserire una email valida!");
    }

    public static void setUtente(Utente agenteDash) {
        utente = agenteDash;
    }

    public Utente getAgente() {
        return utente;
    }


    public void initTextField(){
        aggiungiListenerMaiuscoleENonNumeri(textFieldNome);
        aggiungiListenerMaiuscoleENonNumeri(textFieldCognome);
        aggiungiListenerSoloNumeri(textFieldNumeroTelefono);
    }

    void aggiungiListenerMaiuscoleENonNumeri(TextField text) {
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            String filtered = newValue.replaceAll("[0-9]", "");
            if (!filtered.isEmpty()) {
                text.setText(filtered.substring(0, 1).toUpperCase() + filtered.substring(1));
            } else {
                text.setText("");
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

    public void initProfilo() {
        aggiornaDati.setDefaultButton(true);
        CompletableFuture.supplyAsync(() -> {
                    initTextField();
                    DashboardAgenteController.setAgenteNuovo((Agente) utente);
                    return DashboardAgenteController.getAgente();
                })
                .thenAccept(agente -> Platform.runLater(() -> {
                    setDatiOnTextField(agente);
                    setBottoniCheCompaioQuandoModifico();
                    setBottoniBio(textFieldBio.textProperty().isNotEqualTo(agente.getBio()), aggiornaBio, btnCancellaBio);
                }))
                .thenRunAsync(this::setFotoProfiloAsincrono);
    }

    public void setBottoniBio(BooleanBinding textFieldBio, Button aggiornaBio, Button btnCancellaBio) {
        aggiornaBio.setVisible(true);
        aggiornaBio.disableProperty().bind(textFieldBio.not());
        btnCancellaBio.visibleProperty().bind(textFieldBio);
    }

    public void setBottoniCheCompaioQuandoModifico() {
        BooleanBinding fieldsModified = textFieldNome.textProperty().isNotEqualTo(utente.getNome())
                .or(textFieldCognome.textProperty().isNotEqualTo(utente.getCognome()))
                .or(textFieldNumeroTelefono.textProperty().isNotEqualTo(utente.getTelefono()))
                .or(textFieldEmail.textProperty().isNotEqualTo(utente.getAccountAgente().getEmail()));
        btnCancella.visibleProperty().bind(fieldsModified);
        aggiornaDati.setVisible(true);
        aggiornaDati.disableProperty().bind(fieldsModified.not());

    }

    private void setFotoProfiloAsincrono() {
        setFotoProfilo(utente.getFotoProfilo().getPath());
    }

    public void caricaFotoNuova(){
        CompletableFuture.supplyAsync(() -> {
            List<String> urls= ModificaProfiloFacade.getFotoAgente(utente.getCf());
            System.out.println("ANHONY ARENELLA"+urls.getFirst());
            String urlE= ModificaProfiloFacade.getImageFromS3(urls.getFirst());
            System.out.println(urlE);
            aggiornaAgente(urlE);
            return null;
        });
    }

    private void aggiornaAgente(String url) {
        //utente.getFotoProfilo().setPath(url);
        fotoProfilo.setImage(new Image(url));
        utente.getFotoProfilo().setPath(fotoProfilo.getImage().getUrl());
        setFotoProfiloAsincrono();
    }


    public void setFotoProfilo(String foto) {
        fotoProfilo.setImage(new Image(foto));
    }

    public void apriPaginaModificaFoto() {
        CambiaFoto cambiaFoto =new CambiaFoto();
        cambiaFoto.initPage((Stage) bottoneModificaFoto.getScene().getWindow());
    }


    public void tornaIndietro() {
        try{
            DashboardAgente.initializePageDashboardAgente(tornaIndietroBottone.getScene().getWindow());
        } catch (IOException e) {
           AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }

    public void setDatiOnTextField(Utente utente){
        Platform.runLater(()-> {
            nomeEcognome.setText(utente.getNome() + " " + utente.getCognome());
            textFieldNome.setText(utente.getNome());
            textFieldCognome.setText(utente.getCognome());
            textFieldEmail.setText(utente.getAccountAgente().getEmail());
            textFieldNumeroTelefono.setText(utente.getTelefono());
            textFieldBio.setText(utente.getBio());
        });
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
        levaMessaggioDiErrore(erroreEmail);
    }


    public void annullaModificheDati() {
        setDatiOnTextField(utente);
        levaTuttiIMessaggiDiErrore();
    }

    private void levaTuttiIMessaggiDiErrore() {
        levaMessaggioDiErroreNome();
        levaMessaggioDiErroreCognome();
        levaMessaggioDiErroreTelefono();
        levaMessaggioDiErroreEmail();
    }


    public void aggiornaBiografia() {
        if(checkBio()){
            AlertFactory.generateSuccessAlertForSuccessUpdateBio();
            CompletableFuture.supplyAsync(()-> {
                if(invioBioNuova()) {
                    utente.setBio(textFieldBio.getText());
                    initProfilo();
                }
                return null;
            });
        }
    }

    private boolean invioBioNuova() {
        return ModificaProfiloFacade.updateBio(textFieldBio.getText(), DashboardAgenteController.getAgente().getCf(),DashboardAgenteController.getAgente().getToken());
    }

    public void resetBio() {
        textFieldBio.setText(DashboardAgenteController.getAgente().getBio());
    }

    public void apriPaginaModificaPassword() {
        try{
            CambioPassword.initializePageCambioPassword(btnCancella.getScene().getWindow(),this,utente);
        } catch (IOException e){
            e.printStackTrace();
            AlertFactory.generateFailAlert("Errore apertura pagina!", "Siamo spiacenti si è verificato un errore nel caricamento della pagina cambio password");
        }
    }


    @Override
    public void setPasswordVecchia(String password) {
        this.passwordVecchia=password;
    }

    @Override
    public void annullaCambioPassword() {
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
        new Thread(()->{     ModificaProfiloFacade.updatePasswordAgente(passwordVecchia,DashboardAgenteController.getAgente());
        }).start();
        Platform.runLater(()->{        paneAnnullaCambio.setVisible(false);
        });
    }
}