package com.example.prova2.controller.notifiche;
import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.dto.GetNotificheDTO;
import com.example.prova2.facade.VisualizzaNotificheFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.factory.factoryNotifiche.FactoryForPageNotifica;
import com.example.prova2.model.Agente;
import com.example.prova2.view.DisattivaCategorieAgente;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class VisualizzaNotificheAgenteController {
    @FXML
    public VBox notificheContainer;

    private static Agente agenteNotifiche;

    @FXML
    public Label tornaIndietro;
    @FXML
    public Pane paneAnnullaVisita;
    @FXML
    public Label annullaInvio;
    @FXML
    public Label labelAnnullaVisita;
    @FXML
    public ImageView immagineNoNotifica;
    @FXML
    public Label scrittaNoNotifica;

    @FXML public ImageView immagineCarica;
    @FXML  public Label immagineCarica2;
    @FXML public Label immagineCarica1;

    public static void setAgente(Agente agente) {
        agenteNotifiche = agente;
    }

    public Agente getAgente() {
        return agenteNotifiche;
    }

    private int notificationIndex = 0; // Indice che tiene traccia delle notifiche già caricate



    public void init(Agente agente) {
        CompletableFuture.supplyAsync(() ->
                        VisualizzaNotificheFacade.getAgenteByEmail(agente.getAccountAgente().getEmail(),agente.getToken()))
                .thenAcceptAsync(datiAgente -> {
                    if (datiAgente != null) {
                        agenteNotifiche.setCf(datiAgente.getCf());
                        loadNotifications();
                    }
                });
    }

    public void loadNotifications() {
        notificationIndex=1;
        if (agenteNotifiche.getAccountAgente().getEmail() != null) {
            List<GetNotificheDTO> notificationsOfAgente = prendiNotifiche();
            gestisciImmagineNoNotifica(notificationsOfAgente);
            Platform.runLater(() -> {
                notificheContainer.getChildren().clear();
                VBox notificationList = new VBox();
                notificationList.setSpacing(10);
                for (GetNotificheDTO notifica : notificationsOfAgente) {
                    Pane pane = creaNotificaPane(notifica,notificationIndex);
                    notificationList.getChildren().add(pane);
                    notificationIndex++;
                }
                if(notificationsOfAgente.size()>3) {
                    aggiungiScrollPane(notificationList);
                }else{
                    notificheContainer.getChildren().add(notificationList);
                }
            });
        }
    }

    void gestisciImmagineNoNotifica(List<GetNotificheDTO> notificationsOfAgente) {
        if(notificationsOfAgente.isEmpty()){
            immagineCarica.setVisible(false);
            immagineCarica2.setVisible(false);
            immagineCarica1.setVisible(false);
            visualizzaNoNotifiche();
        }
        else{
            immagineCarica.setVisible(false);
            immagineCarica2.setVisible(false);
            immagineCarica1.setVisible(false);
            disattivaNoNotifiche();
        }
    }

    protected void disattivaNoNotifiche() {
        scrittaNoNotifica.setVisible(false);
        immagineNoNotifica.setVisible(false);
    }

    protected void visualizzaNoNotifiche() {
        scrittaNoNotifica.setVisible(true);
        immagineNoNotifica.setVisible(true);
    }

    protected void aggiungiScrollPane(VBox notificationList) {
        VisualizzaNotificheClienteController.aggiungiScrollPane(notificationList, notificheContainer);
    }


    private Pane creaNotificaPane(GetNotificheDTO notifica, int index) {
        Pane pane = new Pane();
        pane.setPrefSize(835, 162);
        pane.setStyle("-fx-background-color: white; -fx-background-radius: 20;");

        Label titolo= FactoryForPageNotifica.createLabel(notifica.getCategoria().toString());

        Pane titoloPane = FactoryForPageNotifica.createPane(notifica.getCategoria().toString());
        titoloPane.getChildren().add(titolo);

        Label messaggio=FactoryForPageNotifica.createContenuto(notifica,index);
        Button accetta = FactoryForPageNotifica.createButtonOk("Accetta");
        Button rifiuta= FactoryForPageNotifica.creaButtonRifiuta("Rifiuta");

        ImageView logo = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/prova2/images/logo1.png"))));
        FactoryForPageNotifica.generaLogo(logo);
        logo.setVisible(true);
        pane.getChildren().addAll(titoloPane, messaggio, accetta,logo, rifiuta);
        Label visualizzaDiPiu = FactoryForPageNotifica.createLabelVisualizzaDiPiu(this,notifica,pane);
        pane.getChildren().addAll(visualizzaDiPiu);
        aggiungiListenerPerRifiutaNotifica(notifica, rifiuta,pane);
        aggiungiListenerPerAccettaNotifica(notifica,accetta,pane);
        return pane;
    }

    private void aggiungiListenerPerRifiutaNotifica(GetNotificheDTO notifica, Button rifiuta, Pane pane) {
        rifiuta.setOnAction(e -> {
            Platform.runLater(()->{            pane.setVisible(false);
            });
            new Thread(()->{
                VisualizzaNotificheFacade.setDecline(notifica.getIdNotifica(),DashboardAgenteController.getAgente().getToken());}).start();
            mostraPaneAnnullaNotifica(notifica,"Visita rifiutata",pane);
        });
    }

    public void mostraPaneAnnullaNotifica(GetNotificheDTO notifica, String messaggio, Pane pane) {
        labelAnnullaVisita.setText(messaggio);
        paneAnnullaVisita.setVisible(true);
        pane.setVisible(false);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(40), event -> {
            paneAnnullaVisita.setVisible(false);
        }));
        timeline.setCycleCount(1);
        timeline.play();

        annullaInvio.setOnMouseClicked(e1 -> {
            Platform.runLater(()->{pane.setVisible(true);
            });
            new Thread(()->{
                VisualizzaNotificheFacade.annullaInvioVisita(notifica.getIdNotifica(),DashboardAgenteController.getAgente().getToken());
            }).start();
            paneAnnullaVisita.setVisible(false);
        });
    }


    private void aggiungiListenerPerAccettaNotifica(GetNotificheDTO notifica, Button accetta, Pane pane) {
        accetta.setOnAction(e -> {
            Platform.runLater(()->{pane.setVisible(false);
            });
            new Thread(()->{VisualizzaNotificheFacade.setAccept(notifica.getIdNotifica(),DashboardAgenteController.getAgente().getToken());}).start();
            mostraPaneAnnullaNotifica(notifica,"Visita accettata",pane);
        });
    }


    private List<GetNotificheDTO> prendiNotifiche() {
        return VisualizzaNotificheFacade.getNotificaAgente(agenteNotifiche.getCf(),agenteNotifiche.getToken());
    }



    public void apriPaginaAttivaNotifiche() {
        try{
            DisattivaCategorieAgente.initPage(new Stage(),this);
        }catch (IOException e)
        {
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
            e.printStackTrace();
        }
    }
}
