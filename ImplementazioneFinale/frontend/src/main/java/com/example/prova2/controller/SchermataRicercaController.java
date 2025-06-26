package com.example.prova2.controller;

import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.dto.ImmobileResponseRicercaDTO;
import com.example.prova2.facade.ImmobileFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.factory.AnnunciFactory;
import com.example.prova2.model.*;
import com.example.prova2.service.GeopifyService;
import com.example.prova2.service.ImageService;
import com.example.prova2.service.MappaService2;
import com.example.prova2.service.S3Service;
import com.example.prova2.view.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.prova2.factory.AnnunciFactory.createImageView;


public class SchermataRicercaController {
    @FXML
    public Label scrittaCaricamento;
    @FXML
    public Pane paneInfoAnnunci;
    @FXML
    public Label scrittaCaricamento3;
    public ImageView immagineNoCar;
    @FXML
    public Label scrittaCaricamento1;
    @FXML
    public Label scrittaCaricamento2;
    @FXML
    public ListView<String> listMaps;
    public ImageView immagineX;
    @FXML
    private Button btnIndietro;
    @FXML
    private Hyperlink linkHome;
    @FXML
    private ScrollPane scrollFiltri;
    @FXML
    private WebView webViewMap;
    @FXML
    private ScrollPane scrollAnnunci;
    @FXML
    private MenuButton menuStato;
    @FXML
    private MenuButton menuClasse;
    @FXML
    private TextField textPrezzoMin;
    @FXML
    private TextField textPrezzoMax;
    @FXML
    private TextField textNumeroLocali;
    @FXML
    private TextField textLocalità;
    @FXML
    private Button btnAggiornaRicerca;
    @FXML
    private Label labelComuni;
    @FXML
    private Button btnUserDash;

    private static Utente utenteCheCerca;

    String comuneCercato;

    TipoVendita tv;

    ClasseEnergetica ce;


    Integer numeroLocali;


    public static void setUtenteCheCerca(Utente utenteCheCerca) {
        SchermataRicercaController.utenteCheCerca = utenteCheCerca;
    }

    public static Utente getUtenteCheCerca() {
        return utenteCheCerca;
    }

    private MappaService2 mappaService = new MappaService2();

    private WebEngine webEngine;

    private boolean isMapLoaded = false;

    public void setPrezzoMin(BigDecimal prezzoMin) {
       this.prezzoMin = prezzoMin;
    }

    private BigDecimal prezzoMin ;

    public void setPrezzoMax(BigDecimal prezzoMax) {
        this.prezzoMax = prezzoMax;
    }

    private BigDecimal prezzoMax;

    private boolean flagNonAutoComplete=true;

    @FXML
    private void handleAvanti() {
        double currentHvalue = scrollFiltri.getHvalue();
        scrollFiltri.setHvalue(Math.min(1, currentHvalue + 0.2));
        setBtnIndietro();
    }

    @FXML
    private void handleIndietro() {
        double currentHvalue = scrollFiltri.getHvalue();
        scrollFiltri.setHvalue(Math.max(0, currentHvalue - 0.2));
        setBtnIndietro();
    }

    public void setBtnIndietro() {
        double currentHvalue = scrollFiltri.getHvalue();
        btnIndietro.setVisible(currentHvalue > 0.2);
    }

    @FXML
    public void initialize(String comune, TipoVendita tv, BigDecimal PrezzoMin, BigDecimal PrezzoMax, Integer numStanze, ClasseEnergetica ce) {
        Platform.runLater(() -> {
            webEngine = webViewMap.getEngine();
            comunicationJavaJavascript(webEngine);
            mappaService.setWebEngine(webEngine);
            mappaService.loadMap();
            HomePageController.aggiungiListener(textPrezzoMax);
            HomePageController.aggiungiListener(textPrezzoMin);
            btnIndietro.setVisible(false);
            if(comune == null || comune.isEmpty()){
                labelComuni.setText("Località : " + "Qualsiasi");
                immagineX.setVisible(false);

            }else {
                labelComuni.setText("Località: " + comune);
            }
            checkPrezzoMinMax(PrezzoMin, PrezzoMax);
            checkTipoVendita(tv);
            checkClasseEnergetica(ce);
            checkNumStanze(numStanze);
            ArrayList<ClasseEnergetica> options3 = new ArrayList<>();
            ArrayList<TipoVendita> options4 = new ArrayList<>();
            aggiungiOpzioniArrayClasse(options3);
            aggiungiOpzioniArrayTipi(options4);
            addListenerClasseEn(options3);
            addListenerTipologia(options4);
            addTextFormatter(textLocalità);
            listMaps.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (Boolean.FALSE.equals(newVal)) {
                    listMaps.setVisible(false);
                }
            });

                    scrollAnnunci.addEventFilter(ScrollEvent.SCROLL, event -> {
                        if (event.getDeltaY() != 0) {
                            double delta = event.getDeltaY();
                            scrollAnnunci.setVvalue(scrollAnnunci.getVvalue() - delta / 900);
                            event.consume();
                        }
                    });

            listMaps.setOnMouseClicked(event -> {
                String selected = listMaps.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    flagNonAutoComplete=true;
                    textLocalità.setText(selected);
                    flagNonAutoComplete=false;
                    Platform.runLater(() -> {
                        listMaps.getItems().clear();
                        listMaps.setVisible(false);
                    });
                }
            });
            new Thread(()->{
                prezzoMax=PrezzoMax;
                prezzoMin=PrezzoMin;
                this.comuneCercato =comune;
                this.tv=tv;
                this.ce=ce;
            }).start();
        });
    }

    private void addTextFormatter(TextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String sanitized = newValue.replaceAll("[^a-zA-Z ,]", "");
                if (!sanitized.isEmpty()) {
                    sanitized = sanitized.substring(0, 1).toUpperCase() + sanitized.substring(1);
                }
                if (sanitized.length() > 30) {
                    sanitized = sanitized.substring(0, 30);
                }
                if (!sanitized.equals(newValue)) {
                    textField.setText(sanitized);
                }
            }
        });
    }

    public void setTextIniziali(TipoVendita tv, BigDecimal PrezzoMin, BigDecimal PrezzoMax, Integer numStanze, ClasseEnergetica ce) {
        String tipoVendita = (tv != null) ? tv.toString() : "Stato Vendita";
        String prezzoMin = (PrezzoMin != null) ? PrezzoMin.toString() : "Inserire il prezzo minimo";
        String prezzoMax = (PrezzoMax != null) ? PrezzoMax.toString() : "Inserire il prezzo massimo";
        String numeroLocali = (numStanze != null) ? numStanze.toString() : "Inserisci numero locali";
        String classeEnergetica = (ce != null) ? ce.toString() : "Classe energetica";

        menuStato.setText(tipoVendita);
        menuClasse.setText(classeEnergetica);
        textPrezzoMin.setText(formattaStringa(prezzoMin));
        textPrezzoMax.setText(formattaStringa(prezzoMax));
        textNumeroLocali.setText(numeroLocali);
    }

    public String formattaStringa(String input){
        long number = Long.parseLong(input);
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number);
    }

    private void addListenerTipologia(ArrayList<TipoVendita> options) {
        for (TipoVendita option : options) {
            MenuItem menuItem = new MenuItem(option.toString()); // Imposta il testo visibile nel menu
            menuItem.setOnAction(event -> menuStato.setText(option.toString()));
            menuStato.getItems().add(menuItem);
        }
    }

    private void addListenerClasseEn(ArrayList<ClasseEnergetica> options) {
        for (ClasseEnergetica option : options) {
            MenuItem menuItem = new MenuItem(option.toString()); // Imposta il testo visibile nel menu
            menuItem.setOnAction(event -> menuClasse.setText(option.toString()));
            menuClasse.getItems().add(menuItem);
        }
    }

    private void aggiungiOpzioniArrayClasse(ArrayList<ClasseEnergetica> options) {
        menuClasse.getItems().clear();
        CreaAnnuncioController.setOptions(options);
    }

    private void aggiungiOpzioniArrayTipi(ArrayList<TipoVendita> options) {
        menuStato.getItems().clear();
        options.add(TipoVendita.VENDITA);
        options.add(TipoVendita.AFFITTO);
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


    public void loadAnnunci(String comune, TipoVendita tv, BigDecimal prezzoMin, BigDecimal prezzoMax, Integer numStanze, ClasseEnergetica ce) {
        Platform.runLater(() -> {
            if (!isMapLoaded) {
                webViewMap.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                    if (newState == Worker.State.SUCCEEDED) {
                        caricaAnnunciAsync(comune, tv, prezzoMin, prezzoMax, numStanze, ce);
                    }
                });
            } else {
                caricaAnnunciAsync(comune, tv, prezzoMin, prezzoMax, numStanze, ce);
            }
        });
    }

    private void caricaAnnunciAsync(String comune, TipoVendita tv, BigDecimal prezzoMin, BigDecimal prezzoMax, Integer numStanze, ClasseEnergetica ce) {
        List<ImmobileResponseRicercaDTO> ricerca = ImmobileFacade.search(new Ricerca(ce, tv, comune, numStanze, prezzoMin, prezzoMax), utenteCheCerca);
        if(ricerca.isEmpty()){
            scrittaCaricamento.setVisible(false);
            scrollAnnunci.setVisible(false);
            scrittaCaricamento1.setVisible(true);
            scrittaCaricamento2.setVisible(true);
            scrittaCaricamento3.setVisible(true);
            immagineNoCar.setVisible(true);
        }
        VBox vbox = getVBox();
        for (ImmobileResponseRicercaDTO ricercaDTO : ricerca) {
            Platform.runLater(() -> {
                try {
                    creaPane result = getCreaPane(ricercaDTO, vbox);
                    aggiornaMappa(ricercaDTO, result);
                    Task<List<String>> fetchImagesTask = recuperaImmagini(ricercaDTO, result);
                    Thread thread = new Thread(fetchImagesTask);
                    thread.setDaemon(true);
                    thread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private Task<List<String>> recuperaImmagini(ImmobileResponseRicercaDTO ricercaDTO, creaPane result) {
        Task<List<String>> fetchImagesTask = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                return ImageService.getFotoOfImmobile(ricercaDTO.getIdImmobile()).join();
            }
        };
        fetchImagesTask.setOnSucceeded(event -> {
            List<String> images = fetchImagesTask.getValue();
            if (!images.isEmpty()) {
                String url = S3Service.getImageFromS3(images.get(0));
                System.out.println(url);
                aggiungiImmaginiAlPane(result.pane, url);
                scrittaCaricamento.setVisible(false);
            }
        });
        fetchImagesTask.setOnFailed(event -> {
            Throwable ex = fetchImagesTask.getException();
            ex.printStackTrace();
        });
        return fetchImagesTask;
    }

    private void aggiungiImmaginiAlPane(Pane pane, String image) {
        if (pane != null) {
            System.out.println("hello");
            ImageView imageView = createImageView(image, 331, 413, 14, 14);
            pane.getChildren().addAll(imageView);
        }
    }


    private void aggiornaMappa(ImmobileResponseRicercaDTO ricercaDTO, creaPane result) {
        new Thread(() -> {
            String coordinates = MappaService2.getCoordinatesFromAddress(result.indirizzo());
            if (coordinates != null) {
                String[] dati = coordinates.split(",");
                double lat = Double.parseDouble(dati[0]);
                double lng = Double.parseDouble(dati[1]);
                String coordinatesComune = MappaService2.getCoordinatesFromAddress(result.comuneInd());
                if (coordinatesComune == null) {
                    return;
                }
                String[] datiComune = coordinatesComune.split(",");
                if (datiComune[0] == null || datiComune[1] == null) {
                    return;
                }
                double latComune = Double.parseDouble(datiComune[0]);
                double lngComune = Double.parseDouble(datiComune[1]);
                ricercaDTO.setLatitudine(lat);
                ricercaDTO.setLongitudine(lng);
                Platform.runLater(() -> mappaService.addMarker(latComune, lngComune, lat, lng, result.titolo(),
                        result.Prezzo(), result.descrizione(), String.valueOf(ricercaDTO.getIdImmobile())));
            }
        }).start();
    }

    private creaPane getCreaPane(ImmobileResponseRicercaDTO ricercaDTO, VBox vbox) {
        String Prezzo = String.valueOf(ricercaDTO.getPrezzo());
        String numBagn = String.valueOf(ricercaDTO.getNumeroBagni());
        String numSuper = String.valueOf(ricercaDTO.getSuperficie());
        String numPiano = String.valueOf(ricercaDTO.getPiano());
        String numLocali = String.valueOf(ricercaDTO.getNumeroStanze());
        String tipologia = String.valueOf(ricercaDTO.getTipoimmobile());
        String indirizzo = ricercaDTO.getVia() + "," + ricercaDTO.getNumeroCivico() + "," + ricercaDTO.getComune();
        String descrizione = ricercaDTO.getDescrizione();
        String titolo = ricercaDTO.getTitolo();
        String comuneInd = ricercaDTO.getComune();


        Pane pane = AnnunciFactory.createPaneAnnunci();
        Pane paneInfo = AnnunciFactory.createPaneInfo();
        paneInfo.setId("annunci_" + ricercaDTO.getIdImmobile());
        Pane panePrezzi = AnnunciFactory.createPaneInfoPrezzi(Prezzo);
        Pane paneForniture = AnnunciFactory.createPaneInfoForniture(numBagn, numLocali, numPiano, numSuper);
        Label labelTipo = AnnunciFactory.createLabelInfoTipo(tipologia);
        Label labelIndirizzo = AnnunciFactory.createLabelIndirizzo(indirizzo);
        Pane paneDescrizione = AnnunciFactory.createPaneInfoDescrizione(descrizione);
        Pane paneImmagini = AnnunciFactory.createPaneImmagini("");

        pane.getChildren().addAll(paneInfo, paneImmagini);
        paneInfo.getChildren().addAll(panePrezzi, paneForniture, labelTipo, labelIndirizzo, paneDescrizione);
        pane.setOnMouseClicked(e -> {
            try {
                SchermataAnnuncio.initializeSchermataAnnuncio(pane,ricercaDTO,utenteCheCerca,prezzoMin,prezzoMax);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        vbox.getChildren().add(pane);
        scrollAnnunci.setContent(vbox);
        return new creaPane(Prezzo, indirizzo, descrizione, titolo, comuneInd, paneImmagini);
    }



    public void levaComune(ActionEvent actionEvent) {
        labelComuni.setText("Località : " + "Qualsiasi");
    }

    public record creaPane(String Prezzo, String indirizzo, String descrizione, String titolo, String comuneInd,
                           Pane pane) {
    }


    private static VBox getVBox() {
        VBox vbox = new VBox();
        vbox.setPrefHeight(Region.USE_COMPUTED_SIZE);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);// Usato per accumulare i prezzi
        return vbox;
    }


    public void handleSearch() throws IOException {
        String comuneNuovo;
        if(textLocalità.getText().isEmpty() && labelComuni.getText().equals("Località : Qualsiasi")){
            comuneNuovo = null;
        }else if(!textLocalità.getText().isEmpty()) {
            comuneNuovo = textLocalità.getText().trim();
        }else{
            comuneNuovo = this.comuneCercato;
        }
        String numLocaliInput = textNumeroLocali.getText();
        Integer numStanze = (numLocaliInput == null || numLocaliInput.trim().isEmpty() || !numLocaliInput.matches("\\d+"))
                ? null : Integer.parseInt(numLocaliInput);
        BigDecimal prezzoMin = parseBigDecimal(textPrezzoMin.getText());
        BigDecimal prezzoMax = parseBigDecimal(textPrezzoMax.getText());
        TipoVendita tipoVendita = (Objects.equals(menuStato.getText(), "Stato Vendita") || menuStato.getText().trim().isEmpty())
                ? null : TipoVendita.valueOf(menuStato.getText().trim());
        ClasseEnergetica classeEnergetica = (Objects.equals(menuClasse.getText(), "Classe energetica") || menuClasse.getText().trim().isEmpty())
                ? null : ClasseEnergetica.fromString(menuClasse.getText().trim());


        if(Objects.equals(comuneNuovo, this.comuneCercato) && Objects.equals(numStanze, this.numeroLocali) && Objects.equals(prezzoMin, this.prezzoMin) && Objects.equals(prezzoMax, this.prezzoMax)
        && this.ce == classeEnergetica && this.tv == tipoVendita) {
            return ;
        }
        RicercaPage.caricamento((Stage) btnAggiornaRicerca.getScene().getWindow(), comuneNuovo, tipoVendita, prezzoMin, prezzoMax, numStanze, classeEnergetica);
    }

    private BigDecimal parseBigDecimal(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        String cleanInput = input.replaceAll("[^0-9,]", "").replace(",", ".");
        try {
            return new BigDecimal(cleanInput);
        } catch (NumberFormatException e) {
            System.err.println("Errore nel parsing del BigDecimal: " + input);
            return null;
        }
    }




    public void tornaHome() {
        linkHome.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                try {
                    HomePageController.setUtente(utenteCheCerca);
                    HomePage.initializeHomePage((Stage) linkHome.getScene().getWindow(), utenteCheCerca);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void checkTipoVendita(TipoVendita tipoVendita) {
        String tv = String.valueOf(tipoVendita);
        if (tv.equals("null")) {
            menuStato.setText("Stato Vendita");
        } else {
            menuStato.setText(String.valueOf(tipoVendita));
        }
    }

    private void checkPrezzoMinMax(BigDecimal prezzoMin, BigDecimal prezzoMax) {
        if (prezzoMin == null) {
            textPrezzoMin.setPromptText("Inserisci prezzo minimo");
        }else{
            textPrezzoMin.setText(String.valueOf(prezzoMin));
        }

        if (prezzoMax == null) {
            textPrezzoMax.setPromptText("Inserisci prezzo massimo");
        }else{
            textPrezzoMax.setText(String.valueOf(prezzoMax));
        }
    }

    private void checkClasseEnergetica(ClasseEnergetica classeEnergetica) {
        String ce = String.valueOf(classeEnergetica);
        if (ce.equals("null")) {
            menuClasse.setText("Classe energetica");
        }else{
            menuClasse.setText(String.valueOf(classeEnergetica));
        }
    }

    private void checkNumStanze(Integer numStanze) {
        if (numStanze == null) {
            textNumeroLocali.setPromptText("Inserisci numero locali");
        }else{
            textNumeroLocali.setText(String.valueOf(numStanze));
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

    @FXML
    public void suggestion() {
        fetchAsync(textLocalità.getText());
    }

    private void fetchAsync(String newText) {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                List<String> suggerimenti= GeopifyService.searchLocation(newText);
                Platform.runLater(() -> {
                    if(suggerimenti.isEmpty()){
                        listMaps.setVisible(false);
                        return ;
                    }
                    listMaps.getItems().clear();
                    listMaps.getItems().addAll(suggerimenti);
                    listMaps.setVisible(true);
                });
                return null;
            }
        };
        new Thread(task).start();
    }



}
