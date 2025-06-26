package com.example.prova2.controller.notifiche;
import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.dto.GetNotificheDTO;
import com.example.prova2.dto.InformazioniVisitaDTO;
import com.example.prova2.facade.VisitaServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.service.NotificaService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.Optional;
public class InfoAbouPrenotazioneAgenteController {
    @FXML
    public Label labelCasa;
    @FXML
    public Label labelVia;
    @FXML
    public Label labelOra;
    @FXML
    public Label labelNome;
    @FXML
    public Button btnAccetta;
    @FXML
    public Button btnRifiuta;

    private VisualizzaNotificheAgenteController controller;

    public void setController(VisualizzaNotificheAgenteController controller) {
        this.controller = controller;
    }

    public Pane panePrecedente;

    private int numeroNotifica;

    public InfoAbouPrenotazioneAgenteController(){}


    public void initPage(int notifica,Pane pane){
        this.numeroNotifica=notifica;
        new Thread(()->{
            InformazioniVisitaDTO visita=VisitaServiceFacade.getInfoAboutVisita(notifica,DashboardAgenteController.getAgente().getToken());
            Platform.runLater(()->{
                setDati(visita);
                labelNome.setText(visita.getEmailClientePrenotatoVisita());
                this.panePrecedente=pane;
            });
        }).start();

    }

    public void setDati(InformazioniVisitaDTO visita){
        labelCasa.setText(visita.getTipoImmobile().toString());
        labelVia.setText("Via"+visita.getViaImmobile()+" "+"n°"+visita.getNumeroCivico()+" "+visita.getComune());
        labelOra.setText(visita.getDataVisita().toString()+" "+visita.getOraInizioVisita().toString()+"-"+visita.getOraFineVisita());
    }


    public void rifiutaVisita() {
        Alert alert = AlertFactory.generateAlertForRifiutaNotifica();
        Optional<ButtonType> result = alert.showAndWait();
        gestisciRifiuto(result);

    }

    private void gestisciRifiuto(Optional<ButtonType> result) {
        if (result.isPresent()) {
            if (result.isPresent() && result.get() == ButtonType.OK) {
                NotificaService.setNotificationRejected(this.numeroNotifica, DashboardAgenteController.getAgente().getToken());
                Alert alert=AlertFactory.generateAlertForSuccessRifiutaNotifica();
                chiudiFinestra(alert,btnRifiuta);
                controller.mostraPaneAnnullaNotifica(new GetNotificheDTO(this.numeroNotifica),"Visita rifiutata",panePrecedente);
            }
        }
    }


    public void accettaVisita() {
        Alert alert = AlertFactory.generateAlertForAccettaNotifica();
        Optional<ButtonType> result = alert.showAndWait();
        gestisciAccettazione(result);
    }

    private void gestisciAccettazione(Optional<ButtonType> result) {
        if (result.isPresent() && result.get() == ButtonType.OK) {
            NotificaService.setNotificationAccepted(this.numeroNotifica,DashboardAgenteController.getAgente().getToken());
            Alert alert=AlertFactory.generateAlertForSuccessAccettaNotifica();
            chiudiFinestra(alert,btnAccetta);
            controller.mostraPaneAnnullaNotifica(new GetNotificheDTO(this.numeroNotifica),"Visita accettata",panePrecedente);
        }
    }

    public void chiudiFinestra(Alert alert, Button button) {
        Optional<ButtonType> result1 = alert.showAndWait();
        if (!result1.isPresent() || result1.get() == ButtonType.OK) {
            btnAccetta.getScene().getWindow().hide();
        }
    }

}
