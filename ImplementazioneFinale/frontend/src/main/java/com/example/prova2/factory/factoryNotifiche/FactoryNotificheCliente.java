package com.example.prova2.factory.factoryNotifiche;

import com.example.prova2.controller.notifiche.VisualizzaNotificheClienteController;
import com.example.prova2.dto.GetNotificheDTO;
import com.example.prova2.dto.ImmobileResponseRicercaDTO;
import com.example.prova2.facade.ImmobileFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Utente;
import com.example.prova2.view.InfoAboutPrenotazioneCliente;
import com.example.prova2.view.SchermataAnnuncio;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.prova2.model.CategoriaNotifica.CONSIGLIOIMMOBILE;
import static com.example.prova2.model.CategoriaNotifica.ESITOVISITA;

public class FactoryNotificheCliente {




    public static Label createScrittaEsitoImmobile(boolean esito) {
        String contenutoLabel;
        if (esito) {
            contenutoLabel = "Visita accettata";
        }else{
            contenutoLabel = "Visita rifiutata";
        }
        Label label = new Label(contenutoLabel);
        label.setLayoutX(47);
        label.setLayoutY(4);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold; -fx-font-style: italic;");
        return label;
    }

    public static Pane createPaneEsitoNotifica(boolean esito) {
        if(esito){
            return createPaneForVisitaAccettata();
        }
        else{
            return createPaneForVisitaRifiutata();
        }
    }

    public static Pane createPaneConsiglioImmobile(){
        Pane titoloPane = new Pane();
        titoloPane.setLayoutX(122);
        titoloPane.setLayoutY(14);
        titoloPane.setPrefSize(210, 35);
        titoloPane.setStyle("-fx-background-color: #694F5D; -fx-background-radius: 10;");
        return titoloPane;
    }

    public static Pane creaPaneForCategoria(String nomeCategoria){
        if(nomeCategoria.equals("CREAZIONE ACCOUNT")){
            return createPaneCreazioneAccount();
        }
        else if(nomeCategoria.equals("CAMBIO PASSWORD")){
            return createPaneForCambioPassword();
        }
        return null;
    }

    public static Pane createPaneCreazioneAccount(){
        Pane titoloPane = new Pane();
        titoloPane.setLayoutX(122);
        titoloPane.setLayoutY(14);
        titoloPane.setPrefSize(210, 35);
        titoloPane.setStyle("-fx-background-color: #222A68; -fx-background-radius: 10;");
        return titoloPane;
    }

    public static Pane createPaneForCambioPassword(){
        Pane titoloPane = new Pane();
        titoloPane.setLayoutX(122);
        titoloPane.setLayoutY(14);
        titoloPane.setPrefSize(210, 35);
        titoloPane.setStyle("-fx-background-color: #103900; -fx-background-radius: 10;");
        return titoloPane;
    }

    public static Label createScrittaConsiglioImmobile(String scritta){
        Label label = new Label(scritta);
        label.setLayoutX(40);
        label.setLayoutY(4);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold; -fx-font-style: italic;");
        return label;
    }

    public static Label setContenuto(GetNotificheDTO notifica, int numeroNotifica){
        String contenutoNotifica = notifica.getNomeNotifica();
        if(contenutoNotifica.length()>=96 && contenutoNotifica.length()<=300) {
            contenutoNotifica = contenutoNotifica.substring(0, 96) + "\n" + contenutoNotifica.substring(96);
        }
        Label messaggio = new Label(numeroNotifica+") "+contenutoNotifica);
        messaggio.setLayoutX(124);
        messaggio.setLayoutY(60);
        messaggio.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        return messaggio;
    }

    public static Label createLabelVisualizzaDiPiu(VisualizzaNotificheClienteController controller, GetNotificheDTO notifica, Utente utente) throws IOException, InterruptedException {
        Label visualizzaDiPiu =new Label("Visualizza dettagli aggiuntivi");
        visualizzaDiPiu.setLayoutX(125);
        visualizzaDiPiu.setLayoutY(130);
        visualizzaDiPiu.setUnderline(true);
        if (notifica.getCategoria() == ESITOVISITA) {
            visualizzaDiPiu.setOnMouseClicked(event -> {
                try {
                    InfoAboutPrenotazioneCliente.initPage(new Stage(), controller, notifica.getIdNotifica());
                } catch (IOException e) {
                    AlertFactory.generateFailAlertForErroreInterno();
                }
            });
        } else if (notifica.getCategoria() == CONSIGLIOIMMOBILE) {
            visualizzaDiPiu.setOnMouseClicked(event -> {
                    new Thread(()->{

                            Platform.runLater(()->{
                                try {
                                    ImmobileResponseRicercaDTO immobile = ImmobileFacade.getInfoById(notifica.getIdImmobile(), utente.getToken());
                                    SchermataAnnuncio.initializeSchermataAnnuncioClienteNotifiche((Stage) visualizzaDiPiu.getScene().getWindow(), immobile, utente);
                                } catch (IOException e) {
                                AlertFactory.generateFailAlertForErroreInterno();
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                AlertFactory.generateFailAlertForErroreInterno();
                                e.printStackTrace();
                            }
                            });
                    }).start();
            });
        }


        visualizzaDiPiu.setStyle("-fx-cursor: hand;  -fx-font-size: 15px; -fx-font-weight: bold;");
        visualizzaDiPiu.setVisible(true);
        return visualizzaDiPiu;
    }

    private static Pane createPaneForVisitaAccettata(){
        Pane titoloPane = new Pane();
        titoloPane.setLayoutX(122);
        titoloPane.setLayoutY(14);
        titoloPane.setPrefSize(210, 35);
        titoloPane.setStyle("-fx-background-color: green; -fx-background-radius: 10;");
        return titoloPane;
    }

    private static Pane createPaneForVisitaRifiutata(){
        Pane titoloPane = new Pane();
        titoloPane.setLayoutX(122);
        titoloPane.setLayoutY(14);
        titoloPane.setPrefSize(210, 35);
        titoloPane.setStyle("-fx-background-color: red; -fx-background-radius: 10;");
        return titoloPane;
    }

    public static ImageView aggiustaImmagineProfilo(ImageView closeIcon){
        closeIcon.setFitWidth(72);
        closeIcon.setFitHeight(74);
        closeIcon.setLayoutX(25);
        closeIcon.setLayoutY(30);
        closeIcon.setStyle("-fx-cursor: hand;");
        Circle clip = new Circle(36, 36, 36);
        closeIcon.setClip(clip);
        return closeIcon;
    }

    public static Label createScrittaLabel(String string) {
        String formattedString = string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
        return createScrittaConsiglioImmobile(formattedString);
    }
}
