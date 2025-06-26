package com.example.prova2.controller.modificaProfilo;

import com.example.prova2.controller.dashBoard.DashBoardClienteController;
import com.example.prova2.facade.ClienteServiceFacade;
import com.example.prova2.facade.ModificaProfiloFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Account;
import com.example.prova2.model.Cliente;
import com.example.prova2.model.Utente;
import com.example.prova2.service.UpdatePasswordService;
import com.example.prova2.view.CambioPassword;
import com.example.prova2.view.DashBoardCliente;
import com.example.prova2.view.HomePage;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModificaProfiloGoogleController extends ModificaProfiloClienteController{

    @FXML
    private Button aggiornaDati;

    public void setUtenteGoogle(Utente utente) {
        this.utente = utente;
    }

    private Utente utente;

    @Override
    public void salvaDati() {
        if(checkCampi()){
            Cliente clienteNuovo = new Cliente(textFieldNome.getText(),textFieldCognome.getText(),textFieldEmail.getText(),textFieldNumeroTelefono.getText());
            ClienteServiceFacade.updateDatiCliente(clienteNuovo,utente);
            aggiornaClienteProfilo(clienteNuovo);
            initProfilo();
        }
    }

    public void salvaDatiGoogle(){
        if(checkCampi()){
            Cliente clienteNuovo = new Cliente(textFieldNome.getText(),textFieldCognome.getText(),textFieldEmail.getText(),textFieldNumeroTelefono.getText());
            ClienteServiceFacade.updateDatiCliente(clienteNuovo,utente);
            aggiornaClienteProfilo(clienteNuovo);
            initProfilo();
            try{
                clienteNuovo.setToken(utente.getToken());
                HomePage.caricamentoHome((Stage) aggiornaDati.getScene().getWindow(),clienteNuovo);
            } catch (IOException e) {
                AlertFactory.generateFailAlertForErroreCaricamentoPagina();
            }
        }
    }

    @Override
    public boolean checkCampi() {
        boolean flag = true;
        flag&=isValidName(textFieldNome.getText());
        flag&=isValidCognome(textFieldCognome.getText());
        flag&=isValidEmail(textFieldEmail.getText().trim());
        flag&=isValidPhone(textFieldNumeroTelefono.getText().trim());
        flag&=checkDuplicatoEmail(textFieldEmail.getText().trim(),utente);
        return flag;
    }


    public boolean checkDuplicatoEmail(String email,Utente utente) {
        List<String> emails= ModificaProfiloFacade.getEmailGestore(utente);
        if(emailVecchiaUgualeNuova()){
            return true;
        }
        if(exists(email,emails)){
            scriviMessaggioErrore(erroreEmail,"Si prega di inserire una email non registrata!");
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

    private void aggiornaClienteProfilo(Cliente nuovoCliente) {
        utente.setAccountAgente(new Account(nuovoCliente.getAccountAgente().getEmail()));
        utente.setNome(nuovoCliente.getNome());
        utente.setCognome(nuovoCliente.getCognome());
        utente.setTelefono(nuovoCliente.getTelefono());
    }

    public void initProfiloGoogle(Utente utenteGoogle) {
        CompletableFuture.supplyAsync(() -> {
                    initTextField();

                    // Castiamo l'utente a Cliente e lo settiamo
                    Cliente clienteNuovo = (Cliente) utenteGoogle;
                    DashBoardClienteController.setCliente(clienteNuovo);

                    return clienteNuovo;
                })
                .thenAccept(clienteNuovo -> Platform.runLater(() -> {
                    setDatiOnTextFieldWithGoogle(clienteNuovo);
                    setBottoniCheCompaioQuandoModificoWithGoogle(clienteNuovo);
                }));
    }

    public void setBottoniCheCompaioQuandoModificoWithGoogle(Utente utente){
        BooleanBinding fieldsModified = textFieldNome.textProperty().isNotEqualTo(utente.getNome())
                .or(textFieldCognome.textProperty().isNotEqualTo(utente.getCognome()))
                .or(textFieldNumeroTelefono.textProperty().isNotEqualTo(utente.getTelefono()));
        btnCancella.visibleProperty().bind(fieldsModified);
        aggiornaDati.setVisible(true);
        aggiornaDati.disableProperty().bind(fieldsModified.not());
    }


    public void setDatiOnTextFieldWithGoogle(Utente utente){
        Platform.runLater(()-> {
            textFieldEmail.setText(utente.getAccountAgente().getEmail());
        });
    }



    @Override
    public void tornaIndietro() {
        try{
            DashBoardCliente.initializePageDashboardCliente(tornaIndietroBottone.getScene().getWindow(),utente);
        } catch (IOException e) {
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }

    @Override
    public void tornaIndietroPassword() {
        new Thread(()->{        UpdatePasswordService.updatePasswordCliente(getPasswordVecchia(), DashBoardClienteController.getCliente());
        }).start();
        Platform.runLater(()->{        paneAnnullaCambio.setVisible(false);
        });
    }


    public void apriPaginaModificaPassword() {
        try{
            CambioPassword.initializePageCambioPassword(btnCancella.getScene().getWindow(),this,utente);
        } catch (IOException e){
            e.printStackTrace();
            AlertFactory.generateFailAlert("Errore apertura pagina!", "Siamo spiacenti si è verificato un errore nel caricamento della pagina cambio password");
        }
    }

}
