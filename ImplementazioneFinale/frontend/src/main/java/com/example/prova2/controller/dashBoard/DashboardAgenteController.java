package com.example.prova2.controller.dashBoard;
import com.example.prova2.controller.HomePageController;
import com.example.prova2.controller.modificaProfilo.ModificaProfiloAgenteController;
import com.example.prova2.controller.notifiche.VisualizzaNotificheAgenteController;
import com.example.prova2.dto.DatiDTO;
import com.example.prova2.dto.ImmobileResponseRicercaDTO;
import com.example.prova2.facade.AgenteServiceFacade;
import com.example.prova2.facade.VisualizzaNotificheFacade;
import com.example.prova2.factory.*;
import com.example.prova2.model.*;
import com.example.prova2.service.ImmobileService;
import com.example.prova2.service.S3Service;
import com.example.prova2.view.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class DashboardAgenteController {
    @FXML
    public ImageView immagineProfiloAgente;

    @FXML
    public Button btnCreaAnnunci;

    @FXML
    public Button btnModificaIlProfilo;
    @FXML
    public Button btnNotifiche;
    @FXML
    public Button btnIndietro;

    @FXML
    public Button btnGestisciApp;

    @FXML
    public Button buttonLogout;
    public Button bottoneNoAnnunci;
    public Label scrittaNoAnnunci1;
    public Label scrittaNoAnnunci2;
    @FXML public Label labelValutazione;
    @FXML public Pane paneLasciaValutazione;
    @FXML public Pane paneEliminaImmobile;

    @FXML
    private Label labelNome;
    @FXML
    private Label labelCognome;
    @FXML
    private Label labelMail;
    @FXML
    private Label labelNomeProf;
    @FXML
    private Label labelCognomeProf;
    @FXML
    private Label labelTel;
    @FXML
    private Label labelBioProf;

    @FXML
    private ScrollPane scrollAnnunci;

    private static Agente agente;

    private static Scene previousScene;

    public static void setAgenteNuovo(Agente agenteNuovo) {
        agente = agenteNuovo;
    }

    public static Agente getAgente() {
        return agente;
    }

    public static void setAgente(Agente agente) {
        DashboardAgenteController.agente = agente;
    }

    public void initialize() {
        CompletableFuture<Void> first = CompletableFuture.runAsync(() ->
                getDatiAgente(agente.getAccountAgente().getEmail(), agente.getToken())
        );

        CompletableFuture<Void> second = CompletableFuture.runAsync(this::getAnnunciForAgente);

        CompletableFuture<Void> third = CompletableFuture.runAsync(this::loadValutazione, Platform::runLater);

    }


    private void fetchFoto() {
        List<String> urls = AgenteServiceFacade.getFotoAgente(agente.getCf());
        if (agenteHaFoto(urls)) {
            String urlImmagine=S3Service.getImageFromS3(urls.getFirst());
            setImmagineProfilo(urlImmagine);
        } else {
            setImmagineProfilo("C:/Users/Utente/Desktop/frontend/src/main/resources/com/example/prova2/images/user (5).png.jpg/");
        }
    }

    private static boolean agenteHaFoto(List<String> urls) {
        return !urls.isEmpty();
    }

    private void setImmagineProfilo(String s) {
        immagineProfiloAgente.setImage(new Image(s));
        agente.setFotoProfilo(s);
    }

    private void getDatiAgente(String email,String token) {
        CompletableFuture.supplyAsync(() ->
                        VisualizzaNotificheFacade.getAgenteByEmail(email,token))
                .thenAcceptAsync(datiAgente -> {
                    if (datiAgente != null) {
                        Platform.runLater(() -> {
                                extracted(datiAgente);
                            CompletableFuture.runAsync(this::fetchFoto);
                            });
                    }
                });
    }


    private void extracted(DatiDTO dto) {
        labelNome.setText(dto.getNome());
        labelCognome.setText(dto.getCognome());
        labelNomeProf.setText(dto.getNome());
        labelCognomeProf.setText(dto.getCognome());
        labelMail.setText(dto.getEmail());
        labelTel.setText(dto.getTelefono());
        labelBioProf.setText(dto.getBio());
        agente.setCf(dto.getCf());
        agente.setDto(dto);
    }


    public void apriPaginaCreaAnnunci() {
        try {
            CreaAnnuncioAgente.initializePageCreaAnnuncio((Stage) btnCreaAnnunci.getScene().getWindow(), String.valueOf(agente.getCf()),this);
        } catch (IOException e) {
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
            e.printStackTrace();
        }
    }


    public void apriPaginaModificaProfilo() {
        try {
            ModificaProfiloAgenteController.setUtente(agente);
            ModificaProfiloAgente.initializePageModificaProfilo(btnModificaIlProfilo.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }

    public void apriPaginaVisualizzaNotifiche() {
        VisualizzaNotificheAgenteController.setAgente(agente);
        VisualizzaNotificheAgente.initPage((Stage) btnNotifiche.getScene().getWindow());
    }

    public void apriCalendario() {
        CalendarioAgente.initCalendario((Stage) btnGestisciApp.getScene().getWindow());
    }


    public void faiLogout() {
        Alert alert = AlertFactory.generateAlertForLogout();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            agente = null;
            vaiIndietro();
        } else {
            System.out.println("Logout annullato");
        }
    }

    public void vaiIndietro() {
        try {
            LoginAgenteImmobiliare.initializePageLoginAgente(buttonLogout.getScene().getWindow());
        } catch (IOException e) {
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }

    public void vaiAllaSchermataPrecedente() {
        if (previousScene != null) {
            Stage stage = (Stage) btnIndietro.getScene().getWindow();
            stage.setScene(previousScene);
        } else {
            System.out.println("Errore: previousScene è null");
            AlertFactory.generateFailAlertForErroreInterno();
        }
    }

    public void apriPaginaCercaImmobili() {
        try {
            HomePageController.setUtente(DashboardAgenteController.getAgente());
            HomePage.initializeHomePage((Stage) buttonLogout.getScene().getWindow(),agente);
        } catch (IOException e) {
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }

    private List<ImmobileResponseRicercaDTO> prendiAnnunci() throws IOException, InterruptedException {
        return ImmobileService.getAnnunciForAgente(agente.getAccountAgente().getEmail(),agente.getToken());
    }

    public void loadValutazione() {
        Image starEmpty = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/prova2/images/casaVuota.png")));
        Image starFull = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/prova2/images/casaPiena.png")));

        HBox starBox = new HBox(15);
        starBox.setPadding(new Insets(10));
        starBox.setAlignment(Pos.CENTER);
        starBox.setLayoutX(13);
        starBox.setLayoutY(31);
        CompletableFuture.supplyAsync(() ->
                AgenteServiceFacade.getValutazioneAgenteByEmail(agente.getAccountAgente().getEmail(), agente.getToken())
        ).thenAcceptAsync(valutazioneObj -> {
            Platform.runLater(() -> {
                starBox.getChildren().clear();
                caricaVal(starEmpty, starFull, starBox, valutazioneObj, paneLasciaValutazione);
            });
        });
    }


    public static void caricaVal(Image starEmpty, Image starFull, HBox starBox, Integer valutazioneObj, Pane paneLasciaValutazione) {
        int valutazione = (valutazioneObj != null) ? valutazioneObj : 0;
        System.out.println(valutazione+"la tua");
        for (int i = 0; i < 5; i++) {
            ImageView star = new ImageView(i < valutazione ? starFull : starEmpty);
            star.setFitWidth(40);
            star.setFitHeight(40);
            starBox.getChildren().add(star);
        }

        paneLasciaValutazione.getChildren().add(starBox);
    }


    public void getAnnunciForAgente() {
        CompletableFuture.runAsync(() -> {
            try {
                List<ImmobileResponseRicercaDTO> immobili = prendiAnnunci();
                if(immobili.isEmpty()){
                   return ;
                }
                bottoneNoAnnunci.setVisible(false);
                scrittaNoAnnunci1.setVisible(false);
                scrittaNoAnnunci2.setVisible(false);
                VBox vBox = new VBox();
                vBox.setSpacing(10);
                vBox.setPadding(new Insets(10));
                vBox.setFillWidth(true);

                int i = 1;
                for (ImmobileResponseRicercaDTO annunci : immobili) {
                    String titolo = annunci.getTitolo();
                    String indirizzo = "Comune di: " + annunci.getComune();
                    HBox hbox = AnnunciFactory.createHboxAnnunciAgente(String.valueOf(i), titolo, paneEliminaImmobile, indirizzo,vBox,annunci.getIdImmobile());
                    vBox.getChildren().add(hbox);
                    i++;
                }
                Platform.runLater(() -> scrollAnnunci.setContent(vBox));

            } catch (IOException e){
                e.printStackTrace();
                AlertFactory.generateFailAlertForErroreCaricamentoPagina();
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                AlertFactory.generateFailAlertForErroreCaricamentoPagina();
            }
        });
    }
}
