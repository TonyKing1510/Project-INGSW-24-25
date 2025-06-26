package com.example.prova2.controller.modificaProfilo;
import com.example.prova2.controller.dashBoard.DashboardAmministratoreController;
import com.example.prova2.facade.GestoreServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Account;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.model.Utente;
import com.example.prova2.service.UpdatePasswordService;
import com.example.prova2.view.DashboardAmministratore;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class ModificaProfiloGestoreController extends ModificaProfiloAgenteController{

    @Override
    public void salvaDati() {
        System.out.println("ciao");
        if(checkCampi()){
            GestoreAgenziaImmobiliare gestoreNuovo = new GestoreAgenziaImmobiliare(textFieldNome.getText().trim(),textFieldCognome.getText().trim(),textFieldNumeroTelefono.getText().trim());
            gestoreNuovo.setAccountAgente(new Account(textFieldEmail.getText().trim()));
            if(GestoreServiceFacade.updateDatiGestore(gestoreNuovo, utente)!=null) {
                aggiornaClienteProfilo(gestoreNuovo);
                initProfilo();
                paneConfermaModifica.setVisible(true);
                PauseTransition pausa = new PauseTransition(Duration.seconds(4));

                pausa.setOnFinished(event -> paneConfermaModifica.setVisible(false));

                pausa.play();
            }
        }
    }

    private void aggiornaClienteProfilo(Utente utente) {
        ModificaProfiloAgenteController.utente.setAccountAgente(new Account(utente.getAccountAgente().getEmail()));
        ModificaProfiloAgenteController.utente.setNome(utente.getNome());
        ModificaProfiloAgenteController.utente.setCognome(utente.getCognome());
        ModificaProfiloAgenteController.utente.setTelefono(utente.getTelefono());
    }


    @Override
    public void initProfilo() {
        aggiornaDati.setDefaultButton(true);
        CompletableFuture.supplyAsync(() -> {
                    initTextField();
                    DashboardAmministratoreController.setAdmin((GestoreAgenziaImmobiliare) utente);
                    return DashboardAmministratoreController.getAdmin();
                })
                .thenAccept(gestoreNuovo -> Platform.runLater(() -> {
                    setDatiOnTextField(gestoreNuovo);
                    setBottoniCheCompaioQuandoModifico();
                }));
    }

    @Override
    public void setDatiOnTextField(Utente utente){
        Platform.runLater(()-> {
            nomeEcognome.setText(utente.getNome() + " " + utente.getCognome());
            textFieldNome.setText(utente.getNome());
            textFieldCognome.setText(utente.getCognome());
            textFieldEmail.setText(utente.getAccountAgente().getEmail());
            textFieldNumeroTelefono.setText(utente.getTelefono());
        });
    }

    @Override
    public void tornaIndietro() {
        try{
            System.out.println("ciao");
            DashboardAmministratore.initializePageDashboardAmministratore((Stage) tornaIndietroBottone.getScene().getWindow());
        } catch (IOException e) {
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }

    public void tornaIndietroPassword() {
        new Thread(()->{        UpdatePasswordService.updatePasswordGestore(getPasswordVecchia(), DashboardAmministratoreController.getAdmin());
        }).start();
        Platform.runLater(()->{        paneAnnullaCambio.setVisible(false);
        });
    }
}
