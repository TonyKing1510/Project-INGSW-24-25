package com.example.prova2.controller.notifiche;
import com.example.prova2.controller.dashBoard.DashboardAmministratoreController;
import com.example.prova2.dto.GetNotificheDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.factory.factoryNotifiche.FactoryForPageNotifica;
import com.example.prova2.factory.factoryNotifiche.FactoryNotificheCliente;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.service.NotificaService;
import com.example.prova2.view.CambioPassword;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
public class VisualizzaNotificheGestoreController extends VisualizzaNotificheAgenteController{
    private static GestoreAgenziaImmobiliare gestoreNotifiche;

    private Button okButton;


    private int notificationIndex = 0;


    private DashboardAmministratoreController controllerPrecedente;

    private Stage windowPrecednete;

    public void setWindowPrecednete(Stage windowPrecednete) {
        this.windowPrecednete = windowPrecednete;
    }

    public void setControllerPrecedente(DashboardAmministratoreController controllerPrecedente) {
        this.controllerPrecedente = controllerPrecedente;
    }

    public DashboardAmministratoreController getControllerPrecedente() {
        return controllerPrecedente;
    }

    public static GestoreAgenziaImmobiliare getGestoreNotifiche() {
        return gestoreNotifiche;
    }

    public static void setGestoreNotifiche(GestoreAgenziaImmobiliare gestoreNotifiche) {
        VisualizzaNotificheGestoreController.gestoreNotifiche = gestoreNotifiche;
    }

    public void init() {
        setGestoreNotifiche(DashboardAmministratoreController.getAdmin());
        Platform.runLater(this::loadNotifications);
    }

    @Override
    public void loadNotifications() {
        notificationIndex=0;
        if (gestoreNotifiche.getCf() != null) {
            List<GetNotificheDTO> notificationsOfAgente = prendiNotificheGestore();
            gestisciImmagineNoNotifica(notificationsOfAgente);
            Platform.runLater(() -> {
                notificheContainer.getChildren().clear();
                VBox notificationList = new VBox();
                notificationList.setSpacing(10);
                for (GetNotificheDTO notifica : notificationsOfAgente) {
                    Pane pane = creaNotificaPaneCliente(notifica);
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

    private List<GetNotificheDTO> prendiNotificheGestore() {
        try {
            return NotificaService.getNotificationsForAdmin(DashboardAmministratoreController.getAdmin());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private Pane creaNotificaPaneCliente(GetNotificheDTO notifica) {
        notificationIndex++;
        Pane pane = new Pane();
        pane.setPrefSize(900, 162);
        pane.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
        Label titolo;
        Pane titoloPane;
        ImageView immagineAsinistra = new ImageView();

        titoloPane=FactoryNotificheCliente.creaPaneForCategoria(notifica.getCategoria().toString());
        titolo = FactoryNotificheCliente.createScrittaLabel(notifica.getCategoria().toString());
        immagineAsinistra.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/prova2/images/logo1.png"))));
        FactoryForPageNotifica.generaLogo(immagineAsinistra);
        assert titoloPane != null;
        titoloPane.getChildren().add(titolo);
        Label messaggio = FactoryNotificheCliente.setContenuto(notifica, notificationIndex);
        if(notificaIsCambioPassword(notifica)){
            okButton = FactoryForPageNotifica.createButtonConferma("Cambia password");
            aggiungiListenerPerCambioPasswordButton(okButton);
        }else {
            okButton = FactoryForPageNotifica.createButtonOk("Ok");
            aggiungiListenerPerOkNotifica(notifica, okButton,pane);
        }
        pane.getChildren().addAll(titoloPane, immagineAsinistra, okButton,messaggio);
        return pane;
    }

    private void aggiungiListenerPerCambioPasswordButton(Button okButton) {
        okButton.setOnAction(e -> {
           apriPaginaCambiaPassword();
        });
    }

    private void apriPaginaCambiaPassword() {
        try{
            Stage stage = (Stage) okButton.getScene().getWindow();
            CambioPassword.initializePageCambioPassword(windowPrecednete, getControllerPrecedente(),gestoreNotifiche);
            PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
            delay.setOnFinished(event -> stage.close()); // Chiude la finestra dopo 2 secondi
            delay.play(); // Avvia il ritardo

        } catch (IOException e){
            e.printStackTrace();
            AlertFactory.generateFailAlert("Errore apertura pagina!", "Siamo spiacenti si è verificato un errore nel caricamento della pagina cambio password");
        }
    }

    private static boolean notificaIsCambioPassword(GetNotificheDTO notifica) {
        return notifica.getCategoria().getLabel().equals("CAMBIO PASSWORD");
    }

    private void aggiungiListenerPerOkNotifica(GetNotificheDTO notifica, Button accetta, Pane pane) {
        accetta.setOnAction(e -> {
            new Thread(()->{
                pane.setVisible(false);
                NotificaService.setNotificationAccepted(notifica.getIdNotifica(),DashboardAmministratoreController.getAdmin().getToken());
            }).start();
        });
    }
}
