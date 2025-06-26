package com.example.prova2.controller.notifiche;
import com.example.prova2.controller.AnnullaVisita;
import com.example.prova2.controller.dashBoard.DashBoardClienteController;
import com.example.prova2.dto.AddVisitaRequestDTO;
import com.example.prova2.dto.DatiImmobileDTO;
import com.example.prova2.dto.GetNotificheDTO;
import com.example.prova2.facade.ImmobileFacade;
import com.example.prova2.facade.NotificaServiceFacade;
import com.example.prova2.facade.UtenteFacade;
import com.example.prova2.facade.VisitaServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.factory.factoryNotifiche.FactoryForPageNotifica;
import com.example.prova2.factory.factoryNotifiche.FactoryNotificheCliente;
import com.example.prova2.model.Cliente;
import com.example.prova2.model.Immobile;
import com.example.prova2.service.MappaService2;
import com.example.prova2.service.NotificaService;
import com.example.prova2.view.DisattivaCategorieCliente;
import com.example.prova2.view.EffettuaPrenotazione;
import com.example.prova2.view.VisualizzaNotificheAgente;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class VisualizzaNotificheClienteController extends VisualizzaNotificheAgenteController implements AnnullaVisita {

    private static Cliente clienteNotifiche;

    @FXML
    public Button bottoneTornaAllaHome;

    public Label singoleCategorie;
    @FXML public Pane paneAnnullaVisitaPreno;
    @FXML public Label annullaTasto;
    private AddVisitaRequestDTO visitaPrenotata;

    public static void setClienteNotifiche(Cliente clienteNotifiche) {
        VisualizzaNotificheClienteController.clienteNotifiche = clienteNotifiche;
    }

    public static Cliente getClienteNotifiche() {
        return clienteNotifiche;
    }

    private int notificationIndex = 0;


    public void init() {
        Platform.runLater(this::loadNotifications);
    }

    @Override
    public void loadNotifications() {
        notificationIndex=0;
        if (clienteNotifiche.getAccountAgente().getEmail() != null) {
            List<GetNotificheDTO> notificationsOfAgente = prendiNotificheCliente();
            gestisciImmagineNoNotifica(notificationsOfAgente);
            Platform.runLater(() -> {
                notificheContainer.getChildren().clear();
                VBox notificationList = new VBox();
                notificationList.setSpacing(10);
                for (GetNotificheDTO notifica : notificationsOfAgente) {
                    Pane pane = null;
                    try {
                        pane = creaNotificaPaneCliente(notifica);
                    } catch (IOException e) {
                        AlertFactory.generateFailAlertForErroreInterno();
                    } catch (InterruptedException e) {
                        AlertFactory.generateFailAlertForErroreInterno();
                    }
                    notificationList.getChildren().add(pane);
                }
                if (notificationsOfAgente.size() > 3) {
                    aggiungiScrollPane(notificationList);
                } else {
                    notificheContainer.getChildren().add(notificationList);
                }
            });
        }
    }


    public static void aggiungiScrollPane(VBox notificationList, VBox notificheContainer) {
        ScrollPane scrollPane = new ScrollPane(notificationList);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        StackPane stackPane = new StackPane(scrollPane);
        stackPane.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 15px; -fx-padding: 10px;");
        stackPane.setMaxHeight(500);
        stackPane.setPrefHeight(900);
        notificheContainer.getChildren().add(stackPane);
    }


    private Pane creaNotificaPaneCliente(GetNotificheDTO notifica) throws IOException, InterruptedException {
        notificationIndex++;
        Pane pane = new Pane();
        pane.setPrefSize(900, 162);
        pane.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
        Label titolo;
        Pane titoloPane;
        ImageView immagineAsinistra = new ImageView();
        Button selezionaAltroOrario = null;
        if(categoriaIsEsitoVisita(notifica)) {
            boolean esitoNotifica = notifica.getNomeNotifica().contains("accettata");
            if(isNotificaRifiutata(esitoNotifica)) {
                selezionaAltroOrario=selezionaNuovoOrario(notifica);
            }
            titolo = FactoryNotificheCliente.createScrittaEsitoImmobile(esitoNotifica);
            titoloPane = FactoryNotificheCliente.createPaneEsitoNotifica(esitoNotifica);
            new Thread(()->{            UtenteFacade.caricaImmagineProfiloAsync(notifica, immagineAsinistra,DashBoardClienteController.getCliente().getToken());
            }).start();
            FactoryNotificheCliente.aggiustaImmagineProfilo(immagineAsinistra);
        }else{
            titoloPane=FactoryNotificheCliente.createPaneConsiglioImmobile();
            titolo = FactoryNotificheCliente.createScrittaConsiglioImmobile("Consiglio immobile");
            immagineAsinistra.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/prova2/images/logo1.png"))));
            FactoryForPageNotifica.generaLogo(immagineAsinistra);
        }
        titoloPane.getChildren().add(titolo);
        Label messaggio = FactoryNotificheCliente.setContenuto(notifica, notificationIndex);

        Label visualizzaDiPiu = FactoryNotificheCliente.createLabelVisualizzaDiPiu(this, notifica,clienteNotifiche);
        Button okButton = FactoryForPageNotifica.createButtonOk("Ok");
        if(selezionaOrarioNonDisponibile(selezionaAltroOrario)){
            aggiungiOggettiAlPane(pane, titoloPane, messaggio, immagineAsinistra, visualizzaDiPiu, okButton);
        }else {
            aggiungiButtonSelezionaOrario(pane, titoloPane, messaggio, immagineAsinistra, visualizzaDiPiu, okButton, selezionaAltroOrario);
        }
        aggiungiListenerPerOkNotifica(notifica, okButton,pane,titoloPane);
        return pane;
    }

    private static boolean selezionaOrarioNonDisponibile(Button selezionaAltroOrario) {
        return selezionaAltroOrario == null;
    }

    private static void aggiungiButtonSelezionaOrario(Pane pane, Pane titoloPane, Label messaggio, ImageView immagineAsinistra, Label visualizzaDiPiu, Button okButton, Button selezionaAltroOrario) {
        pane.getChildren().addAll(titoloPane, messaggio, immagineAsinistra, visualizzaDiPiu, okButton, selezionaAltroOrario);
    }

    private static void aggiungiOggettiAlPane(Pane pane, Pane titoloPane, Label messaggio, ImageView immagineAsinistra, Label visualizzaDiPiu, Button okButton) {
        pane.getChildren().addAll(titoloPane, messaggio, immagineAsinistra, visualizzaDiPiu, okButton);
    }

    private Button selezionaNuovoOrario(GetNotificheDTO notifica) {
        Button selezionaUnAltroOrarioButton = FactoryForPageNotifica.createButtonSelezionaAltroOrario("Seleziona un altro orario");
        selezionaUnAltroOrarioButton.setVisible(true);
        aggiungiListenerperSelezionaOrario(selezionaUnAltroOrarioButton,notifica);
        return selezionaUnAltroOrarioButton;
    }

    private void aggiungiListenerperSelezionaOrario(Button selezionaUnAltroOrarioButton, GetNotificheDTO notifica) {
        selezionaUnAltroOrarioButton.setOnAction(e->{
                DatiImmobileDTO dto=ImmobileFacade.getInfo(notifica.getIdImmobile(),DashBoardClienteController.getCliente().getToken());
                Platform.runLater(()->{
                    String indirizzoCompleto=dto.getIndirizzo()+","+dto.getNumeroCivico()+","+dto.getComune();
                    String coordinates = MappaService2.getCoordinatesFromAddress(indirizzoCompleto);
                    Double lat = null;
                    Double lng = null;
                    if(coordinates != null) {
                        String[] dati = coordinates.split(",");
                        lat = Double.parseDouble(dati[0]);
                        lng = Double.parseDouble(dati[1]);
                    }
                    Immobile immobile = new Immobile(dto,notifica,lat,lng);
                    apriPaginaPrenotazione(selezionaUnAltroOrarioButton, immobile);
                });
        });
    }

    private void apriPaginaPrenotazione(Button selezionaUnAltroOrarioButton, Immobile immobile) {
        try {
            EffettuaPrenotazione.initPage((Stage) selezionaUnAltroOrarioButton.getScene().getWindow(),
                    clienteNotifiche.getAccountAgente().getEmail(), immobile,this);
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.generateFailAlertForErroreInterno();
        }
    }

    private static boolean isNotificaRifiutata(boolean esitoNotifica) {
        return !esitoNotifica;
    }

    static boolean categoriaIsEsitoVisita(GetNotificheDTO notifica) {
        return notifica.getCategoria().toString().equals("ESITOVISITA");
    }


    private List<GetNotificheDTO> prendiNotificheCliente() {
        return NotificaServiceFacade.getNotificaCliente(clienteNotifiche);
    }

    public void tornaAllaDash() {
        VisualizzaNotificheAgente.getModalStage().close();
    }

    private void aggiungiListenerPerOkNotifica(GetNotificheDTO notifica, Button accetta, Pane pane, Pane titoloPane) {
        accetta.setOnAction(e -> {
            notificheContainer.getChildren().remove(pane);
            pane.setVisible(false);
            new Thread(()->{            NotificaService.setNotificationAccepted(notifica.getIdNotifica(), DashBoardClienteController.getCliente().getToken());
            }).start();
        });
    }

    public void disattivaCategorie() {
        try {
            DisattivaCategorieCliente.initPage((Stage) singoleCategorie.getScene().getWindow(),this);
        } catch (IOException ex) {
            AlertFactory.generateFailAlertForErroreInterno();
        }
    }

    @Override
    public void mostraPaneAnnullaVisita(AddVisitaRequestDTO dto) {
        paneAnnullaVisitaPreno.setVisible(true);
        this.visitaPrenotata=dto;
    }

    public void annulla(MouseEvent mouseEvent) {
        paneAnnullaVisitaPreno.setVisible(false);
        VisitaServiceFacade.annullaVisita(visitaPrenotata);
    }
}
