package com.example.prova2.controller;
import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.dto.AddVisitaRequestDTO;
import com.example.prova2.dto.ImmobileResponseRicercaDTO;
import com.example.prova2.facade.AgenteServiceFacade;
import com.example.prova2.facade.VisitaServiceFacade;
import com.example.prova2.factory.AlertFactory;

import com.example.prova2.model.*;
import com.example.prova2.service.ImageService;
import com.example.prova2.service.MappaService2;
import com.example.prova2.service.S3Service;
import com.example.prova2.view.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;

import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import netscape.javascript.JSObject;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;


import static com.example.prova2.controller.dashBoard.DashboardAgenteController.caricaVal;
import static com.example.prova2.factory.AnnunciFactory.createImageView;

public class SchermataAnnuncioController implements AnnullaVisita {
    @FXML public Pane paneAnnullaVisita;
    @FXML public Label annullaTasto;
    @FXML public Label scrittaCaricamento;
    @FXML public Label labelDotato;
    public Pane paneSuccessoRecensione;
    @FXML
    private ImageView imageAgente;
    @FXML
    private ScrollPane scrollImageAnnuncio;
    @FXML
    private Label labelTipoImmobile;
    @FXML
    private Label labelIndirizzo;
    @FXML
    private Label labelPrezzo;
    @FXML
    private Label labelInfoTitolo;
    @FXML
    private Label labelInfoDescrizione;
    @FXML
    private Label labelNumeroStanze;
    @FXML
    private Label labelNumeroBagni;
    @FXML
    private Label labelNumeroCucine;
    @FXML
    private Label labelNumeroSoggiorni;
    @FXML
    private Label labelNumeroPiano;
    @FXML
    private Label labelSuperficie;
    @FXML
    private Label labelArredamento;
    @FXML
    private Label labelClasseEnergetica;
    @FXML
    private Label labelSpeseCondominiali;
    @FXML
    private Label labelNomeAgente;
    @FXML
    private WebView webViewMap;
    @FXML
    private Button btnPrenotaVisita;
    @FXML
    private Button btnLasciaRecensione;
    @FXML
    private Button btnIndietro2;
    @FXML
    private Button btnIndietroImage;
    @FXML
    private Button btnUserDash;
    @FXML
    private Hyperlink linkHome;
    @FXML
    private Pane paneLasciaValutazione;
    @FXML
    private ScrollPane scrollAnnunci;

    private static Utente utenteCheCerca;

    private WebEngine webEngine;

    private boolean isMapLoaded = false;

    private final MappaService2 mappaService = new MappaService2();

    private static ImmobileResponseRicercaDTO annuncio;
    private static BigDecimal prezzoMin;
    private static BigDecimal prezzoMax;

    private static Scene previousScene;

    private AddVisitaRequestDTO visitaFatta;

    private boolean flag;

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setPreviousScene(Scene previousScene) {
        SchermataAnnuncioController.previousScene = previousScene;
    }

    public static void setUtenteCheCerca(Utente utenteCheCerca) {
        SchermataAnnuncioController.utenteCheCerca = utenteCheCerca;
    }

    public static Utente getUtenteCheCerca() {
        return utenteCheCerca;
    }

    public SchermataAnnuncioController() {
    }

    public static void setDatiRicerca(ImmobileResponseRicercaDTO annuncioDto,BigDecimal prezzoMin, BigDecimal prezzoMax) {
        SchermataAnnuncioController.annuncio = annuncioDto;
        SchermataAnnuncioController.prezzoMin = prezzoMin;
        SchermataAnnuncioController.prezzoMax = prezzoMax;

    }


    public void loadAnnuncio(ImmobileResponseRicercaDTO annuncioDto){
        new Thread(()-> {
            if(utenteCheCerca instanceof Agente ){
                btnPrenotaVisita.setDisable(true);
                btnLasciaRecensione.setDisable(true);
            }
            if(utenteCheCerca instanceof GestoreAgenziaImmobiliare ){
                btnPrenotaVisita.setDisable(true);
                btnLasciaRecensione.setDisable(true);
            }
            Platform.runLater(() -> {
                caricaImmagini(annuncioDto);
                setLabelImmobile(annuncioDto);
                setOnActioPrenota(annuncioDto);
                setPhotoAgente(annuncioDto);
                loadMap(annuncioDto);
                btnIndietroImage.setDisable(false);
            });
        }).start();
    }

    private void loadMap(ImmobileResponseRicercaDTO annuncioDto){
        webEngine = webViewMap.getEngine();
        comunicationJavaJavascript(webEngine);
        mappaService.setWebEngine(webEngine);  // Passa il WebEngine al servizio
        mappaService.loadMap();
        addMarkerMap(annuncioDto);
    }

    @FXML
    private void handleAvanti() {
        System.out.println("AVANTI");
        double currentHvalue = scrollImageAnnuncio.getHvalue();
        scrollImageAnnuncio.setHvalue(Math.min(1, currentHvalue + 0.4));
        setBtnIndietro();
    }

    @FXML
    private void handleIndietro() {
        double currentHvalue = scrollImageAnnuncio.getHvalue();
        double newHvalue = Math.max(0, currentHvalue - 0.5);

        scrollImageAnnuncio.setHvalue(newHvalue);
    }

    public void setBtnIndietro() {
        double currentHvalue = scrollImageAnnuncio.getHvalue();

        // Il bottone "Indietro" sarà visibile solo se Hvalue è maggiore di 0.2
        btnIndietroImage.setVisible(currentHvalue > 0.1);
    }



    private void comunicationJavaJavascript(WebEngine webEngine) {
        Platform.runLater(() -> {
            webEngine.setUserDataDirectory(new File("user-data"));
            webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    JSObject window = (JSObject) webEngine.executeScript("window");
                    window.setMember("java", this);
                    isMapLoaded = true;
                }
            });
        });
    }

    private void addMarkerMap(ImmobileResponseRicercaDTO annuncioDto){
        new Thread(() -> {
            String indirizzo = annuncioDto.getComune() + " " + annuncioDto.getVia() + " " + annuncioDto.getNumeroCivico();
            String coordinates = MappaService2.getCoordinatesFromAddress(indirizzo);
            if (coordinates != null) {
                String[] dati = coordinates.split(",");
                double lat = Double.parseDouble(dati[0]);
                double lng = Double.parseDouble(dati[1]);
                String coordinatesComune = MappaService2.getCoordinatesFromAddress(annuncioDto.getComune());
                if (coordinatesComune == null) {
                    return;
                }
                String[] datiComune = coordinatesComune.split(",");
                if (datiComune[0] == null || datiComune[1] == null) {
                    return;
                }
                double latComune = Double.parseDouble(datiComune[0]);
                double lngComune = Double.parseDouble(datiComune[1]);
                annuncioDto.setLatitudine(lat);
                annuncioDto.setLongitudine(lng);
                Platform.runLater(() -> mappaService.addMarker2(latComune, lngComune, lat, lng));
            }
        }).start();
    }

    private void setPhotoAgente(ImmobileResponseRicercaDTO annuncioDto) {
        new Thread(()->{
            Platform.runLater(()->{
                String cfAgente = annuncioDto.getCfAgente();
                System.out.println("cf agente" + cfAgente);
                List<String> image = AgenteServiceFacade.getFotoAgente(cfAgente);
                String urlImage = S3Service.getImageFromS3(image.getFirst());
                if(urlImage != null){
                    imageAgente.setImage(new Image(urlImage));
                }
            });
        }).start();

    }

    private void setOnActioPrenota(ImmobileResponseRicercaDTO annuncioDto) {
        btnPrenotaVisita.setOnAction(event -> apriPaginaPrenotazioni(annuncioDto));
    }

    private void caricaImmagini(ImmobileResponseRicercaDTO annuncioDto){
        Task<List<String>> imagesTask = prendiImmagini(annuncioDto);
        Thread thread = new Thread(imagesTask);
        thread.setDaemon(true);
        thread.start();
    }


    private Task<List<String>> prendiImmagini(ImmobileResponseRicercaDTO annuncioDto) {
        Task<List<String>> fetchImagesTask = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                return ImageService.getFotoOfImmobile(annuncioDto.getIdImmobile()).join();
            }
        };

        fetchImagesTask.setOnSucceeded(event -> {
            List<String> images = fetchImagesTask.getValue();
            if (images != null && !images.isEmpty()) {
                double x = 0;
                HBox hBox = new HBox(50);
                hBox.setAlignment(Pos.CENTER);
                for (String imageName : images) {
                    String url = S3Service.getImageFromS3(imageName);
                    aggiungiImmaginiAlloScroll(hBox, scrollImageAnnuncio, url,x);
                    x += 0.50;
                }
                scrittaCaricamento.setVisible(false);
                btnIndietro2.setVisible(true);

                if(utenteCheCerca instanceof Cliente){
                    btnLasciaRecensione.setDisable(false);
                    btnPrenotaVisita.setDisable(false);
                }

            }
        });

        fetchImagesTask.setOnFailed(event -> {
            Throwable ex = fetchImagesTask.getException();
            ex.printStackTrace();
        });

        return fetchImagesTask;
    }

    private void aggiungiImmaginiAlloScroll(HBox hBox,ScrollPane scrollPane, String url,double x){
        if (scrollPane != null) {
            ImageView imageView = createImageView(url, 925, 400, x, 14);
            hBox.getChildren().add(imageView);
            scrollPane.setContent(hBox);
        }

    }

    private void setLabelImmobile(ImmobileResponseRicercaDTO annuncioDto) {
        setDatiLabel(annuncioDto);
    }

    private void setDatiLabel(ImmobileResponseRicercaDTO annuncioDto) {
        String tipoImmobile = String.valueOf(annuncioDto.getTipoimmobile()).toLowerCase();
        tipoImmobile = tipoImmobile.substring(0, 1).toUpperCase() + tipoImmobile.substring(1);
        String tipoArredamento = String.valueOf(annuncioDto.getArredamento()).toLowerCase();
        tipoArredamento = tipoArredamento.substring(0, 1).toUpperCase() + tipoArredamento.substring(1);

        labelTipoImmobile.setText(tipoImmobile);
        labelArredamento.setText(tipoArredamento);

        labelIndirizzo.setText(annuncioDto.getComune() + " " + annuncioDto.getVia() + " " + annuncioDto.getNumeroCivico());
        labelPrezzo.setText("€ " + annuncioDto.getPrezzo());
        String tipoVendita = capitalize(annuncioDto.getTipovendita().toString());

        labelInfoTitolo.setText(annuncioDto.getTitolo() + " in " + tipoVendita);
        labelInfoDescrizione.setText(annuncioDto.getDescrizione());
        labelNumeroStanze.setText(String.valueOf(annuncioDto.getNumeroStanze()));
        labelNumeroBagni.setText(String.valueOf(annuncioDto.getNumeroBagni()));
        labelNumeroCucine.setText(String.valueOf(annuncioDto.getNumeroCucine()));
        labelNumeroSoggiorni.setText(String.valueOf(annuncioDto.getNumeroSoggiorni()));
        labelNumeroPiano.setText(String.valueOf(annuncioDto.getPiano()));
        labelSuperficie.setText(annuncioDto.getSuperficie() + " m²");

        labelClasseEnergetica.setText(String.valueOf(annuncioDto.getClasseEnergetica()));
        labelSpeseCondominiali.setText("€ " + annuncioDto.getSpeseCondominiali());
        labelNomeAgente.setText(annuncioDto.getNomeAgente() + " " + annuncioDto.getCognomeAgente());
        if(annuncioDto.isAscensore()){
            labelDotato.setText("Dotato di ascensore");
        }else{
            labelDotato.setText("Non Dotato di ascensore");
        }

    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    private EventHandler<ActionEvent> apriPaginaPrenotazioni(ImmobileResponseRicercaDTO ricercaDTO) {
        try {
                EffettuaPrenotazione.initPage((Stage) btnPrenotaVisita.getScene().getWindow(),
                        utenteCheCerca.getAccountAgente().getEmail(), new Immobile(ricercaDTO),this);
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
        return null;
    }

    public void mostraPaneAnnullaVisita(AddVisitaRequestDTO visita){
        this.visitaFatta=visita;
        paneAnnullaVisita.setVisible(true);
        PauseTransition pausa = new PauseTransition(Duration.seconds(6));

        pausa.setOnFinished(event -> paneAnnullaVisita.setVisible(false));

        pausa.play();
    }

    private static boolean visitaGiaPrenotata(ImmobileResponseRicercaDTO ricercaDTO) {
        return VisitaServiceFacade.checkVisitaGiaPresente(utenteCheCerca.getAccountAgente().getEmail(), ricercaDTO.getIdImmobile(), utenteCheCerca.getToken());
    }

    @FXML
    public void setLinkHome() throws IOException {
        caricaHome(linkHome.getScene().getWindow(),utenteCheCerca);
    }

    private void caricaHome(Window window, Utente utente) throws IOException {
        try {
            HomePage.caricamentoHome((Stage) window, utente);
        } catch (IOException e) {
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }


    public void apriDash() {
        switch (utenteCheCerca) {
            case Cliente cliente -> {
                apriDashCliente();
            }
            case Agente agente -> {
                apriDashAgente();
            }
            case GestoreAgenziaImmobiliare gestoreAgenziaImmobiliare -> {
                apriDashGestore();
            }
            default -> {
                AlertFactory.generateFailAlertForErroreInterno();
            }
        }
    }

    private void apriDashGestore() {
        try {
            DashboardAmministratore.initializePageDashboardAmministratore((Stage) btnUserDash.getScene().getWindow());
        }catch (IOException e) {
            AlertFactory.generateFailAlertForErroreInterno();
        }
    }

    private void apriDashCliente() {
        try {
            DashBoardCliente.initializePageDashboardCliente(btnUserDash.getScene().getWindow(), utenteCheCerca);
        } catch (InterruptedException e) {
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            AlertFactory.generateFailAlertForErroreInterno();
        }
    }

    private void apriDashAgente() {
        try{
            Agente agenteDash = new Agente();
            agenteDash.setAccountAgente(new Account(utenteCheCerca.getAccountAgente().getEmail(),utenteCheCerca.getAccountAgente().getPassword()));
            agenteDash.setToken(utenteCheCerca.getToken());
            DashboardAgenteController.setAgente(agenteDash);
            DashboardAgente.initializePageDashboardAgente(btnUserDash.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        } catch (InterruptedException e) {
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public void initialize(){
        scrollAnnunci.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                double delta = event.getDeltaY();
                scrollAnnunci.setVvalue(scrollAnnunci.getVvalue() - delta / 200);
                event.consume();
            }
        });
        scrollImageAnnuncio.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        webViewMap.addEventFilter(ScrollEvent.SCROLL, Event::consume);
    }

    public void tornaAgliAnnunci(){
        if (previousScene != null) {
            Stage stage = (Stage) btnIndietro2.getScene().getWindow();
            stage.setScene(previousScene);
        }
    }

    public void apriLasciRecensione(){
        String nomeCognomeAgente = annuncio.getNomeAgente() + " " + annuncio.getCognomeAgente();
        String cfAgente = annuncio.getCfAgente();
        try {
            LasciaRecensione.initPage((Stage) btnLasciaRecensione.getScene().getWindow(),nomeCognomeAgente,utenteCheCerca,cfAgente,this);
        }catch (RuntimeException e){
            e.printStackTrace();
            AlertFactory.generateFailAlertForErroreInterno();
        }
    }

    public void loadValutazione(){
        Image starEmpty = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/prova2/images/casaVuota.png")));
        Image starFull = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/prova2/images/casaPiena.png")));

        HBox starBox = new HBox(15);
        starBox.setPadding(new Insets(10));
        starBox.setAlignment(Pos.CENTER);
        starBox.setLayoutX(13);
        starBox.setLayoutY(31);

        starBox.getChildren().clear();

        CompletableFuture.supplyAsync(() ->
                AgenteServiceFacade.getValutazioneAgente(annuncio.getCfAgente(), HomePageController.getUtente().getToken())
        ).thenAcceptAsync(valutazioneObj -> {
            Platform.runLater(() -> {
                starBox.getChildren().clear();
                System.out.println("grande valutazione"+valutazioneObj);
                caricaVal(starEmpty, starFull, starBox, valutazioneObj, paneLasciaValutazione);
            });
        });

    }

    public void annulla(MouseEvent mouseEvent) {
        paneAnnullaVisita.setVisible(false);
        new Thread(()->{
            VisitaServiceFacade.annullaVisita(visitaFatta);
        }).start();

    }

    public void tornaAllaHome(ActionEvent event) {
        try {
            HomePage.caricamentoHome((Stage) btnIndietro2.getScene().getWindow(), utenteCheCerca);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            e.printStackTrace();
            AlertFactory.generateFailAlertForErroreInterno();
        }
    }
}
