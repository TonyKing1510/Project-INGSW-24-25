package com.example.prova2.factory.factoryNotifiche;
import com.example.prova2.controller.notifiche.VisualizzaNotificheAgenteController;
import com.example.prova2.dto.GetNotificheDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.view.InfoAboutPrenotazioneAgente;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


public class FactoryForPageNotifica {

    public static Label createLabel(String categoria) {
        if(categoria.equals("CREAZIONE ACCOUNT")){
            return createLabelForCreazioneAccount(categoria);
        }
        else if(categoria.equals("VISITA IMMOBILE")){
            return createLabelForVisitaImmobile(categoria);
        }
        return createLabelForVisitaImmobile(categoria);
    }

    public static Pane createPane(String categoria) {
        if(categoria.equals("CREAZIONE ACCOUNT")){
            return createPaneForCreazioneAccount();
        }
        else if(categoria.equals("VISITA IMMOBILE")){
            return createPaneForVisitaImmobile();
        }else{
            return createPaneForVisitaImmobile();
        }
    }



    public static Label createLabelForVisitaImmobile(String labelName) {
        Label label = new Label(labelName);
        label.setLayoutX(47);
        label.setLayoutY(7);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold; -fx-font-style: italic;");
        return label;
    }

    public static Label createLabelForCreazioneAccount(String labelName) {
        Label label = new Label(labelName);
        label.setLayoutX(29);
        label.setLayoutY(7);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold; -fx-font-style: italic;");
        return label;
    }



    public static Pane createPaneForVisitaImmobile(){
        Pane titoloPane = new Pane();
        titoloPane.setLayoutX(122);
        titoloPane.setLayoutY(14);
        titoloPane.setPrefSize(210, 35);
        titoloPane.setStyle("-fx-background-color: green; -fx-background-radius: 10;");
        return titoloPane;
    }

    public static Pane createPaneForCreazioneAccount(){
        Pane titoloPane = new Pane();
        titoloPane.setLayoutX(122);
        titoloPane.setLayoutY(14);
        titoloPane.setPrefSize(210, 35);
        titoloPane.setStyle("-fx-background-color: #8E6E15; -fx-background-radius: 10;");
        return titoloPane;
    }

    public static Label createContenuto(GetNotificheDTO notifica, int numeroNotifica){
        Label messaggio = new Label(numeroNotifica+") "+notifica.getNomeNotifica());
        messaggio.setLayoutX(124);
        messaggio.setLayoutY(60);
        messaggio.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        return messaggio;
    }

    public static Label createLabelVisualizzaDiPiu(VisualizzaNotificheAgenteController controller, GetNotificheDTO notifica, Pane pane){
        Label visualizzaDiPiu =new Label("Visualizza dettagli aggiuntivi");
        visualizzaDiPiu.setLayoutX(125);
        visualizzaDiPiu.setLayoutY(130);
        visualizzaDiPiu.setUnderline(true);
        visualizzaDiPiu.setOnMouseClicked(event -> {
            try {
                InfoAboutPrenotazioneAgente.initPage(new Stage(),controller,notifica.getIdNotifica(),pane);
            } catch (IOException e) {
                e.printStackTrace();
                AlertFactory.generateFailAlertForErroreInterno();
            }
        });
        visualizzaDiPiu.setStyle("-fx-cursor: hand;  -fx-font-size: 15px; -fx-font-weight: bold;");
        visualizzaDiPiu.setVisible(true);
        return visualizzaDiPiu;
    }

    public static Button createButtonConferma(String nomeBottone){
        Button accetta = new Button(nomeBottone);
        accetta.setLayoutX(719);
        accetta.setLayoutY(125);
        accetta.setPrefSize(130, 28);
        accetta.setStyle("-fx-background-color: #011638; -fx-text-fill: white; -fx-cursor: hand;");
        return accetta;
    }

    public static Button createButtonOk(String nomeBottone){
        Button accetta = new Button(nomeBottone);
        accetta.setLayoutX(719);
        accetta.setLayoutY(125);
        accetta.setPrefSize(80, 28);
        accetta.setStyle("-fx-background-color: #011638; -fx-text-fill: white; -fx-cursor: hand;");
        return accetta;
    }

    public static Button createButtonSelezionaAltroOrario(String nomeBottone){
        Button accetta = new Button(nomeBottone);
        accetta.setLayoutX(500);
        accetta.setLayoutY(125);
        accetta.setPrefSize(150, 28);
        accetta.getStyleClass().add("button-custom");
        return accetta;
    }

    public static Button creaButtonRifiuta(String nomeBottone){
        Button rifiuta = new Button("Rifiuta");
        rifiuta.setLayoutX(622);
        rifiuta.setLayoutY(125);
        rifiuta.setPrefSize(80, 28);
        rifiuta.setStyle("-fx-cursor: hand;");
        return rifiuta;
    }


    public static ImageView generaLogo(ImageView closeIcon){
        closeIcon.setFitWidth(128);
        closeIcon.setFitHeight(103);
        closeIcon.setLayoutX(0);
        closeIcon.setLayoutY(14);
        closeIcon.setStyle("-fx-cursor: hand;");
        return closeIcon;
    }


}
