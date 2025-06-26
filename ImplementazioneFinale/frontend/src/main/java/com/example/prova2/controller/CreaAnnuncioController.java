package com.example.prova2.controller;

import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.dto.AddImmobileRequestDTO;
import com.example.prova2.facade.CreaAnnuncioFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.factory.AnnunciFactory;
import com.example.prova2.model.Arredamento;
import com.example.prova2.model.ClasseEnergetica;
import com.example.prova2.model.TipoImmobile;
import com.example.prova2.model.TipoVendita;
import com.example.prova2.service.GeopifyService;
import com.example.prova2.view.CreaAnnuncioAgente;
import com.example.prova2.view.DashboardAgente;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import netscape.javascript.JSObject;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import java.util.logging.Logger;


import java.io.File;
import java.io.IOException;
import java.util.*;

public class CreaAnnuncioController {

    private DashboardAgenteController controllerPrec;

    boolean flagNonAutoComplete = false;
    @FXML
    public Button btnAvantiCategoria;

    @FXML
    public Button btnAvantiLocalita;

    @FXML
    public Label erroreComune;
    @FXML
    public Label erroreIndirizzo;
    @FXML
    public Label erroreCivico;

    @FXML
    public Label errorSuperficie;

    @FXML
    public Label errorPiano;

    @FXML
    public Label errorStanze;

    @FXML
    public Label errorArredamento;

    @FXML
    public Label errorClasse;

    @FXML
    public Button btnAvantiCarat;
    @FXML
    public Label prezzoErrore;
    @FXML
    public Label speseErrore;
    @FXML
    public Label venditaErrore;
    @FXML
    public Button btnAvantiPrezzi;
    @FXML
    public Label errorFoto;
    @FXML
    public Button btnAvantiFoto;
    @FXML
    public Label errorDes;
    @FXML
    public Label errorTitolo;
    @FXML
    public Label errorTipologia;

    @FXML
    public ImageView fotoSpese;

    @FXML
    public ImageView fotoPrezzo;
    @FXML
    public ImageView immagineStanze;

    @FXML
    public ImageView immagineSuperficie;
    @FXML
    public ImageView immaginePiano;
    @FXML
    public MenuButton selAscensore;
    @FXML public Label errorAscensore;
    @FXML
    private Button btnIndietro;
    @FXML
    private Button btnFoto;
    @FXML
    private TextField textNumeroStanze;
    @FXML
    private TextField textComune;
    @FXML
    private ListView<String> listMaps;
    @FXML
    private WebView webViewMap;
    @FXML
    private Pane paneLocalita;
    @FXML
    private Pane paneCaratteristiche;
    @FXML
    private Pane paneCategoria;
    @FXML
    private Pane panePrezzi;
    @FXML
    private Pane paneDescrizione;
    @FXML
    private Pane paneFoto;
    @FXML
    private Button btnMenoBagni;
    @FXML
    private Button btnMenoSoggiorno;
    @FXML
    private Button btnMenoCucine;
    @FXML
    private Button btnPiuBagni;
    @FXML
    private Button btnPiuSoggiorno;
    @FXML
    private Button btnPiuCucine;
    @FXML
    private Label labelCucine;
    @FXML
    private Label labelBagni;
    @FXML
    private Label labelSoggiorno;
    @FXML
    private RadioButton rbApp;
    @FXML
    private RadioButton rbLoft;
    @FXML
    private RadioButton rbRes;
    @FXML
    private RadioButton rbVilla;
    @FXML
    private RadioButton rbBaita;
    @FXML
    private RadioButton rbMans;
    @FXML
    private RadioButton rbAtt;
    @FXML
    private RadioButton rbOpn;
    @FXML
    private RadioButton rbArr;
    @FXML
    private RadioButton rbParzArr;
    @FXML
    private RadioButton rbNonArr;
    @FXML
    private TextField textPiani;
    @FXML
    private TextField textSuperficie;
    @FXML
    private MenuButton selClasse;
    @FXML
    private TextField textPrezzi;
    @FXML
    private TextField textSpese;
    @FXML
    private RadioButton rbVendi;
    @FXML
    private RadioButton rbAffitti;
    @FXML
    private TextField textIndirizzo;
    @FXML
    private TextField textNumCivic;
    @FXML
    private TextField textTitolo;
    @FXML
    private TextField textDescrizione;
    @FXML
    private ScrollPane scrollImage;
    @FXML
    private Button btnCarica;

    ObservableList<String> selectedFiles = FXCollections.observableArrayList();

    ObservableList<File> absPath = FXCollections.observableArrayList();

    public Stage stagePrecedente;

    public Stage stageOra;

    public void setControllerPrec(DashboardAgenteController controllerPrec) {
        this.controllerPrec = controllerPrec;
    }

    private static final String ERROR_MESSAGE = "Errore";
    private static final String ERROR_MESSAGE2 = "Non hai completato i passaggi rivedi";
    private static final String ERROR_MESSAGE3 = "Immobile non inserito";
    private static final String ERROR_MESSAGE_SCHERMATA = "Errore di navigazione";
    private static final String ERROR_MESSAGE_CARICAMENTOSCHERMATA = "Si è verificato un problema durante il caricamento della dashboard.";
    private static final String LOGGER_MESSAGE="Nessun tipo selezionato";
    private static final String PATTERN_NUMEROCIVICO="^(\\d+|N\\/A)$";
    private static final Logger logger = Logger.getLogger(CreaAnnuncioController.class.getName());

    private static final Random random = new Random();
    private final ToggleGroup group = new ToggleGroup();
    private final ToggleGroup group2 = new ToggleGroup();
    private final ToggleGroup group3 = new ToggleGroup();
    private int numeroBagni = 0;
    private int numeroSoggiorno = 0;
    private int numeroCucine = 0;
    private String agenteCf = null;
    private final AddImmobileRequestDTO annuncioDTO= new AddImmobileRequestDTO();
    boolean isSelecting=false;


    public void setCf(String cf) {
        agenteCf = cf;
    }

    public void setAgenteCfForAnnuncio(){
        annuncioDTO.setCfAgente(agenteCf);
    }

    private void setRandomNumber(){
        int numeroCasuale = 100 + random.nextInt(900);
        annuncioDTO.setIdImmobile(numeroCasuale);
    }


    private final HBox imageContainer = new HBox(10);

    public void uploadImage() {
        Window currentWindow = btnFoto.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona uno o più file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("Tutti i file", "*.*")
        );
        List<File> files = fileChooser.showOpenMultipleDialog(currentWindow);
        if(files == null){
            return;
        }
        errorFoto.setVisible(false);
        for (File fileUtente : files) {
            selectedFiles.add("images/"+fileUtente.getName());
            absPath.add(new File(fileUtente.getAbsolutePath()));
            Pane pane = AnnunciFactory.createImageViewForFoto(fileUtente, selectedFiles,absPath);
            imageContainer.getChildren().add(pane);
        }

        annuncioDTO.setUriFotoImmobile(selectedFiles);
        scrollImage.setContent(imageContainer);
    }

    private final Timeline delayTimer = new Timeline(new KeyFrame(Duration.millis(300), e -> fetchSuggestions()));



    private void fetchSuggestions() {
        /*
        String comune = textComune.getText();
        RicercaComuniService.getSuggestions(comune, listMaps);
        listMaps.setVisible(true);

         */
    }


    //Viene chiamato ja javascript
    public void updateAddress(String address) {
        flagNonAutoComplete = true;
        divideAddress(address);
        flagNonAutoComplete=false;
    }


    public void initialize() {
        btnAvantiCategoria.setDefaultButton(true);
        btnAvantiFoto.setDefaultButton(true);
        btnCarica.setDefaultButton(true);
        listMaps.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (Boolean.FALSE.equals(newVal)) {
                listMaps.setVisible(false);
            }
        });

        listMaps.setOnMouseClicked(event -> {
            String selected = listMaps.getSelectionModel().getSelectedItem();
            if (selected != null) {
                flagNonAutoComplete=true;
                textComune.setText(selected);
                flagNonAutoComplete=false;
                Platform.runLater(() -> {
                    listMaps.getItems().clear();
                    listMaps.setVisible(false);
                });
            }
        });
        CompletableFuture.runAsync(() -> {
            Platform.runLater(() -> {
                WebEngine webEngine = webViewMap.getEngine();
                comunicationJavaJavascript(webEngine);
                CreaAnnuncioFacade.initMappa(webEngine);
            });
        });

        CompletableFuture.runAsync(() -> {
            ArrayList<ClasseEnergetica> options2 = new ArrayList<>();
            Platform.runLater(() -> {
                setPaneCategoriaVisible();
                setRadioButtonCategoria();
                setRadioButtonCaratteristiche();
                setRadioButtonPrezzi();
                aggiungiOpzioniArrayClasse(options2);
                addListenerClasseEn(options2);
                addListenerAscensore("Si", "No");
                initOnClick();
                initPage();
            });
        });
    }
    private void fetchAsync(String newText) {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                List<String> suggerimenti=GeopifyService.searchLocation(newText);
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

    private void initOnClick() {
        onClickBagni();
        onClickSoggiorno();
        onClickCucine();
    }

    private void initPage() {
        initRadioCategoria();
        initButtonAvantiCategoria();
        initBottoneAvantiLocalita();
        initBottoneCaratteristiche();
        initBottoneAvantiPrezzi();
        initListenerForPosizione();
        initBottoneFoto();
        initBottoneCarica();
        aggiungiListenerSoloNumeriEPunti(textPrezzi);
        aggiungiListenerSoloNumeriEPunti(textSpese);
        aggiungiListenerSoloNumeri(textSuperficie);
        aggiungiListenerSoloNumeriEMeno(textPiani);
        aggiungiListenerSoloNumeri(textNumeroStanze);
        aggiungiListenerMaiuscole(textComune);
        aggiungiListenerMaiuscole(textIndirizzo);
    }

    private void initRadioCategoria() {
        rbBaita.setOnAction(event -> errorTipologia.setVisible(false));
        rbLoft.setOnAction(event -> errorTipologia.setVisible(false));
        rbMans.setOnAction(event -> errorTipologia.setVisible(false));
        rbApp.setOnAction(event -> errorTipologia.setVisible(false));
        rbAtt.setOnAction(event -> errorTipologia.setVisible(false));
        rbOpn.setOnAction(event -> errorTipologia.setVisible(false));
        rbRes.setOnAction(event -> errorTipologia.setVisible(false));
        rbVilla.setOnAction(event -> errorTipologia.setVisible(false));
    }

    private void aggiungiListenerSoloNumeriEPunti(TextField textField) {
        HomePageController.aggiungiListener(textField);
    }

    private void aggiungiListenerSoloNumeri(TextField textField){
        CreaGestoreController.aggiungi(textField);
    }

    private void aggiungiListenerSoloNumeriEMeno(TextField textField){
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?\\d*")) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }

    private void aggiungiListenerMaiuscole(TextField text) {
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                text.setText(newValue.substring(0, 1).toUpperCase() + newValue.substring(1));
            }
        });

    }

    private void initBottoneCarica() {
        btnCarica.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> (textDescrizione.getText().isEmpty() || textTitolo.getText().isEmpty()),
                        textDescrizione.textProperty(),
                        textTitolo.textProperty()
                )
        );
    }

    private void initBottoneFoto() {
        btnAvantiFoto.setVisible(true);
        btnAvantiFoto.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> selectedFiles.isEmpty(),
                        selectedFiles
                )
        );
    }

    private void initListenerForPosizione() {
        textComune.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                levaMessaggioComune();
            }
        });

        textIndirizzo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
               levaMessaggioIndirizzo();
            }
        });

        textNumCivic.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                levaMessaggioCivico();
            }
        });
    }

    private void initBottoneAvantiPrezzi() {
        btnAvantiPrezzi.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> !(isPrezzoValido() && isSpeseCondominialiValide()),
                        textPrezzi.textProperty(),
                        textSpese.textProperty(),
                        rbVendi.selectedProperty(),
                        rbAffitti.selectedProperty()
                )
        );
    }

    private void initBottoneCaratteristiche() {
        btnAvantiCarat.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> !(superficieValida() && numeroStanzeValido() && numeroPianoValido() && classeValida() && !arredamentoValido() && ascensoreValida()),
                        textSuperficie.textProperty(),textNumeroStanze.textProperty(),textPiani.textProperty()
                        ,selClasse.textProperty(),rbArr.selectedProperty(),rbNonArr.selectedProperty(),
                        rbParzArr.selectedProperty(),selAscensore.textProperty()
                )
        );
    }

    private void initBottoneAvantiLocalita() {
        btnAvantiLocalita.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> {
                            boolean isComuneValid = textComune.getText().length() >= 4 || textComune.getText().trim().equals("N/A");
                            boolean isIndirizzoValid = textIndirizzo.getText().length() >= 4;
                            boolean isCivicoValid = textNumCivic.getText().matches(PATTERN_NUMEROCIVICO);
                            return !(isComuneValid && isIndirizzoValid && isCivicoValid);
                        },
                        textComune.textProperty(),
                        textIndirizzo.textProperty(),
                        textNumCivic.textProperty()
                )
        );

    }

    private void initButtonAvantiCategoria() {
        btnAvantiCategoria.disableProperty().bind(
                rbAffitti.selectedProperty().
                        or(rbApp.selectedProperty()
                                .or(rbBaita.selectedProperty())
                                .or(rbMans.selectedProperty())
                                .or(rbRes.selectedProperty())
                                .or(rbAtt.selectedProperty())
                                .or(rbOpn.selectedProperty())
                                .or(rbLoft.selectedProperty())
                                .or(rbVilla.selectedProperty())
                                .and((rbVendi.selectedProperty()).or(rbAffitti.selectedProperty()))).not());

    }

    private void comunicationJavaJavascript(WebEngine webEngine) {
        webEngine.setUserDataDirectory(new File("user-data"));
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("java", this);  // Imposta il riferimento al controller Java
            }
        });
    }

    private void setPaneCategoriaVisible() {
        paneCaratteristiche.setVisible(false);
        paneLocalita.setVisible(false);
        paneDescrizione.setVisible(false);
        paneFoto.setVisible(false);
        panePrezzi.setVisible(false);
        paneCategoria.setVisible(true);
    }


    @FXML
    public void onSearchAddress() {
        String address = textComune.getText().trim() + "," + textIndirizzo.getText().trim() + "," + textNumCivic.getText().trim();
        String coordinates = CreaAnnuncioFacade.getCoordinateFromAddress(address);
        if (coordinates != null) {
            String[] latLng = coordinates.split(",");
            double lat = Double.parseDouble(latLng[0]);
            double lng = Double.parseDouble(latLng[1]);
            CreaAnnuncioFacade.updateMarker(lat, lng);
        } else {
            showError();
        }
    }

    private void showError() {
        AlertFactory.generateFailAlert(ERROR_MESSAGE, "Impossibile trovare le coordinate per l'indirizzo fornito.");
    }

    public void attivaLocalita() {
        if(checkCategoria()){
            paneCategoria.setVisible(false);
            paneLocalita.setVisible(true);
            paneCaratteristiche.setVisible(false);
            paneDescrizione.setVisible(false);
            paneFoto.setVisible(false);
            panePrezzi.setVisible(false);
        }else{
            AlertFactory.generateFailAlert("Attenzione","Mancano dei dati nelle schermate precedenti, controlla!!");
        }
    }

    private boolean checkCategoria() {
        boolean flag=true;
        flag&= checkTipo();
        flag&=checkVendita();
        return flag;
    }

    private boolean checkTipo() {
        List<RadioButton> tipi = Arrays.asList(rbBaita, rbLoft, rbMans, rbApp, rbAtt, rbOpn, rbRes, rbVilla);
        boolean tipoSelezionato = tipi.stream().anyMatch(RadioButton::isSelected);
        errorTipologia.setVisible(!tipoSelezionato);
        return tipoSelezionato;
    }

    private boolean checkLocalita() {
       boolean flag = true;
       flag&=checkComune();
       flag&=checkIndirizzo();
       flag&=checkNumeroCivico();
       return flag;
    }

    private boolean checkPrezzi(){
        boolean flag = true;
        flag&=checkPrezzo();
        flag&=checkSpeseCondominiali();
        return flag;
    }

    private boolean checkVendita() {
        if(isVenditaValida()){
            venditaErrore.setVisible(false);
            return true;
        }
        venditaErrore.setVisible(true);
        return false;
    }

    private boolean isVenditaValida() {
        return rbVendi.isSelected() || rbAffitti.isSelected();
    }

    private boolean checkSpeseCondominiali() {
        try {
            String input = textSpese.getText();
            input = input.replace(".", "").trim();
            if (input.isEmpty()) {
                speseErrore.setText("Il campo non può essere vuoto.");
                speseErrore.setVisible(true);
                return false;
            }
            BigDecimal prezzo = new BigDecimal(input);
            BigDecimal maxPrezzo = BigDecimal.valueOf(100000);
            if (prezzo.compareTo(BigDecimal.ZERO) <= 0) {
                speseErrore.setText("Il prezzo deve essere maggiore di zero.");
                speseErrore.setVisible(true);
                return false;
            }
            if (prezzo.compareTo(maxPrezzo) > 0) {
                speseErrore.setText("Il prezzo non può superare questa soglia.");
                speseErrore.setVisible(true);
                return false;
            }
            speseErrore.setVisible(false);
            return true;

        } catch (NumberFormatException e) {
            speseErrore.setText("Prezzo non valido.");
            speseErrore.setVisible(true);
            return false;
        }
    }

    private boolean isSpeseCondominialiValide() {
        try {
            String input = textSpese.getText();
            input = input.replace(".", "").trim();
            BigDecimal maxPrezzo = BigDecimal.valueOf(100000);
            if (input.isEmpty()) {
                return false;
            }
            BigDecimal prezzo = new BigDecimal(input);
            return (prezzo.compareTo(BigDecimal.ZERO) > 0 && prezzo.compareTo(maxPrezzo) <= 0);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isPrezzoValido() {
        try {
            String input = textPrezzi.getText();
            input = input.replace(".", "").trim();
            BigDecimal maxPrezzo = BigDecimal.valueOf(1000000000);
            if (input.isEmpty()) {
                return false;
            }
            BigDecimal prezzo = new BigDecimal(input);
            return (prezzo.compareTo(BigDecimal.ZERO) > 0 && prezzo.compareTo(maxPrezzo) <= 0);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean checkPrezzo() {
        try {
            String input = textPrezzi.getText();
            input = input.replace(".", "").trim();
            if (input.isEmpty()) {
                prezzoErrore.setText("Il campo non può essere vuoto.");
                prezzoErrore.setVisible(true);
                return false;
            }
            BigDecimal prezzo = new BigDecimal(input);
            BigDecimal maxPrezzo = BigDecimal.valueOf(1000000000);
            if (prezzo.compareTo(BigDecimal.ZERO) <= 0) {
                prezzoErrore.setText("Il prezzo deve essere maggiore di zero.");
                prezzoErrore.setVisible(true);
                return false;
            }
            if (prezzo.compareTo(maxPrezzo) > 0) {
                prezzoErrore.setText("Il prezzo non può superare questa soglia.");
                prezzoErrore.setVisible(true);
                return false;
            }
            prezzoErrore.setVisible(false);
            return true;

        } catch (NumberFormatException e) {
            prezzoErrore.setText("Prezzo non valido.");
            prezzoErrore.setVisible(true);
            return false;
        }
    }


    private boolean checkNumeroCivico() {
        if(textNumCivic.getText().matches(PATTERN_NUMEROCIVICO)){
            erroreCivico.setVisible(false);
            return true;
        }
        erroreCivico.setVisible(true);
        return false;
    }

    private boolean checkIndirizzo() {
        if(textIndirizzo.getText().trim().length() >= 4){
            erroreIndirizzo.setVisible(false);
            return true;
        }
        erroreIndirizzo.setVisible(true);
        return false;
    }

    private boolean checkComune() {
        if(textComune.getText().trim().length() >= 4){
            erroreComune.setVisible(false);
            return true;
        }
        erroreComune.setVisible(true);
        return false;
    }

    private boolean checkCaratteristiche(){
        boolean flag = true;
        flag&=checkSupeficie();
        flag&=checkPiano();
        flag&=checkNumeroStanze();
        flag&=checkArredamento();
        flag&=checkClasseEnergetica();
        flag&=checkAscensore();
        return flag;
    }

    private boolean checkClasseEnergetica() {
        if(selClasse.getText().equals("Seleziona la classe energetica")){
            errorClasse.setVisible(true);
            return false;
        }
        errorClasse.setVisible(false);
        return true;
    }

    private boolean checkAscensore() {
        if(selAscensore.getText().equals("Seleziona la presenza dell'ascensore")){
            errorAscensore.setVisible(true);
            return false;
        }
        errorAscensore.setVisible(false);
        return true;
    }

    private boolean classeValida() {
        return !selClasse.getText().trim().equals("Seleziona la classe energetica");
    }

    private boolean ascensoreValida() {
        return !selAscensore.getText().trim().equals("Seleziona la presenza dell'ascensore");
    }

    private boolean checkArredamento() {
        if (arredamentoValido()) {
            errorArredamento.setVisible(true);
            return false;
        }
        errorArredamento.setVisible(false);
        return true;
    }

    private boolean arredamentoValido() {
        return !rbArr.isSelected() && !rbNonArr.isSelected() && !rbParzArr.isSelected();
    }

    private boolean numeroPianoValido(){
        try {
            Integer.parseInt(textPiani.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean superficieValida() {
        try {
            Integer.parseInt(textSuperficie.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean numeroStanzeValido(){
        try {
            Integer.parseInt(textNumeroStanze.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean checkPiano() {
        try {
            int piano=Integer.parseInt(textPiani.getText());
            if (piano > 150) {
                errorPiano.setText("Il numero di piani inserito è troppo grande. Inserisci un valore inferiore a 150.");
                errorPiano.setVisible(true);
                return false;
            }
            if(textPiani.getText().isEmpty()){
                errorPiano.setText("Inserisci un numero di piani.");
                errorPiano.setVisible(true);
                return false;
            }
            errorPiano.setVisible(false);
            return true;
        } catch (NumberFormatException e) {
            errorPiano.setVisible(true);
            return false;
        }
    }

    private boolean checkSupeficie() {
        try {
            int superficie = Integer.parseInt(textSuperficie.getText().trim());
            if (superficie > 0 && superficie <= 100000) {
                errorSuperficie.setVisible(false);
                return true;
            }else if(textSuperficie.getText().isEmpty()){
                errorSuperficie.setVisible(true);
                errorSuperficie.setText("Inserisci un valore numerico valido per la superficie.");
                return false;
            }else if(superficie == 0){
                errorSuperficie.setText("La superficie non può essere 0.");
                errorSuperficie.setVisible(true);
                return false;
            }
            else {
                errorSuperficie.setText("La superficie inserita è troppo grande.Inserire una superficie con valore minore.");
                errorSuperficie.setVisible(true);
                return false;
            }
        } catch (NumberFormatException e) {
            errorSuperficie.setVisible(true);
            return false;
        }
    }


    private boolean checkNumeroStanze(){
        try {
            int numeroStanze=Integer.parseInt(textNumeroStanze.getText());
            if(numeroStanze>50){
                errorStanze.setText("Il numero di stanze inserite è troppo grande.Inserire un numero minore.");
                errorStanze.setVisible(true);
                return false;
            }
            if(textNumeroStanze.getText().isEmpty()){
                errorStanze.setText("Inserire un numero di stanze.");
                errorStanze.setVisible(true);
                return false;
            }
            errorStanze.setVisible(false);
            return true;
        } catch (NumberFormatException e) {
            errorStanze.setVisible(true);
            return false;
        }
    }

    private void setRadioButtonCategoria(){
        rbApp.setToggleGroup(group);
        rbApp.setUserData(TipoImmobile.APPARTAMENTO);

        rbLoft.setToggleGroup(group);
        rbLoft.setUserData(TipoImmobile.LOFT);

        rbRes.setToggleGroup(group);
        rbRes.setUserData(TipoImmobile.RESIDENZIALE);

        rbVilla.setToggleGroup(group);
        rbVilla.setUserData(TipoImmobile.VILLA);

        rbBaita.setToggleGroup(group);
        rbBaita.setUserData(TipoImmobile.BAITA);

        rbMans.setToggleGroup(group);
        rbMans.setUserData(TipoImmobile.MANSARDA);

        rbAtt.setToggleGroup(group);
        rbAtt.setUserData(TipoImmobile.ATTICO);

        rbOpn.setToggleGroup(group);
        rbOpn.setUserData(TipoImmobile.OPENSPACE);
        group.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                TipoImmobile selectedCategoria = (TipoImmobile) newToggle.getUserData();
                logger.info("Categoria selezionata: " + selectedCategoria);
            }
        });
    }

    private void setRadioButtonCaratteristiche(){
        rbArr.setToggleGroup(group2);
        rbArr.setUserData(Arredamento.ARREDATO);
        rbNonArr.setToggleGroup(group2);
        rbNonArr.setUserData(Arredamento.NONARREDATO);
        rbParzArr.setToggleGroup(group2);
        rbParzArr.setUserData(Arredamento.PARZIALMENTEARREDATO);
    }

    private void setRadioButtonPrezzi(){
        rbVendi.setToggleGroup(group3);
        rbVendi.setUserData(TipoVendita.VENDITA);
        rbAffitti.setToggleGroup(group3);
        rbAffitti.setUserData(TipoVendita.AFFITTO);
    }


    public void addListenerClasseEn(ArrayList<ClasseEnergetica> options) {
        for (ClasseEnergetica option : options) {
            MenuItem menuItem = new MenuItem(option.toString()); // Imposta il testo del MenuItem
            menuItem.setOnAction(event ->
            {
                selClasse.setText(option.toString())
                    ;levaMessaggioClasse();
            }); // Azione per selezionare l'opzione
            selClasse.getItems().add(menuItem); // Aggiungi il MenuItem
        }
    }

    public void addListenerAscensore(String si,String no){
        MenuItem menuItem = new MenuItem(si);
        MenuItem menuItem2 = new MenuItem(no);
        menuItem2.setOnAction(event ->{
            selAscensore.setText(no)
            ;levaMessaggioAscensore();
        });
        menuItem.setOnAction(event ->{
            selAscensore.setText(si)
            ;levaMessaggioAscensore();
        });
        selAscensore.getItems().add(menuItem);
        selAscensore.getItems().add(menuItem2);
    }

    private void levaMessaggioAscensore() {
        errorAscensore.setVisible(false);
    }

    public void aggiungiOpzioniArrayClasse(ArrayList<ClasseEnergetica> options) {
        addClasseMenu(options);
    }

    public static void addClasseMenu(ArrayList<ClasseEnergetica> options) {
        setOptions(options);
    }

    public static void setOptions(ArrayList<ClasseEnergetica> options) {
        options.add(ClasseEnergetica.A_PLUS_PLUS);
        options.add(ClasseEnergetica.A_PLUS);
        options.add(ClasseEnergetica.A);
        options.add(ClasseEnergetica.B);
        options.add(ClasseEnergetica.C);
        options.add(ClasseEnergetica.D);
        options.add(ClasseEnergetica.E);
        options.add(ClasseEnergetica.F);
        options.add(ClasseEnergetica.G);
    }


    public void attivaCategoria(){
        paneCategoria.setVisible(true);
        paneCaratteristiche.setVisible(false);
        paneLocalita.setVisible(false);
        paneDescrizione.setVisible(false);
        paneFoto.setVisible(false);
        panePrezzi.setVisible(false);
    }

    public void attivaCaratteristiche() {
        if(checkCategoria() && checkLocalita()){
            paneLocalita.setVisible(false);
            paneCategoria.setVisible(false);
            paneCaratteristiche.setVisible(true);
            paneDescrizione.setVisible(false);
            paneFoto.setVisible(false);
            panePrezzi.setVisible(false);
        }else{
            AlertFactory.generateFailAlert("Attenzione","Mancano dei dati nelle schermate precedenti, controlla!!");
        }


    }

    public void attivaPrezzi(){
        if (checkCaratteristiche()) {
            paneLocalita.setVisible(false);
            paneCaratteristiche.setVisible(false);
            paneCategoria.setVisible(false);
            paneDescrizione.setVisible(false);
            paneFoto.setVisible(false);
            panePrezzi.setVisible(true);
        } else {
            AlertFactory.generateFailAlert(ERROR_MESSAGE,ERROR_MESSAGE2);
        }
    }

    public void attivaDocumenti() {
        if(checkPrezzi()){
            paneLocalita.setVisible(false);
            paneCaratteristiche.setVisible(false);
            paneCategoria.setVisible(false);
            paneDescrizione.setVisible(false);
            panePrezzi.setVisible(false);
            paneFoto.setVisible(true);
        }else{
            AlertFactory.generateFailAlert(ERROR_MESSAGE,ERROR_MESSAGE2);
        }
    }

    public void attivaDescrizione() {
        if(checkNumeroFoto()){
            paneLocalita.setVisible(false);
            paneCaratteristiche.setVisible(false);
            paneCategoria.setVisible(false);
            panePrezzi.setVisible(false);
            paneFoto.setVisible(false);
            paneDescrizione.setVisible(true);
        }else{
            AlertFactory.generateFailAlert(ERROR_MESSAGE,ERROR_MESSAGE2);
        }
    }

    private boolean checkNumeroFoto() {
        if(selectedFiles.isEmpty()){
            errorFoto.setVisible(true);
            return false;
        }
        errorFoto.setVisible(false);
        return true;
    }


    private void onClickBagni(){
        // Configura il pulsante "Plus"
        btnPiuBagni.setOnAction(event -> {
            numeroBagni++;
            updateBagniLabel();
        });

        // Configura il pulsante "Minus"
        btnMenoBagni.setOnAction(event -> {
            if (numeroBagni > 0) { // Evita valori negativi
                numeroBagni--;
                updateBagniLabel();
            }
        });

        // Inizializza la label con il valore iniziale
        updateBagniLabel();
    }

    private void onClickSoggiorno(){
        // Configura il pulsante "Plus"
        btnPiuSoggiorno.setOnAction(event -> {
            numeroSoggiorno++;
            updateSoggiornoLabel();
        });

        // Configura il pulsante "Minus"
        btnMenoSoggiorno.setOnAction(event -> {
            if (numeroSoggiorno > 0) { // Evita valori negativi
                numeroSoggiorno--;
                updateSoggiornoLabel();
            }
        });
        // Inizializza la label con il valore iniziale
        updateSoggiornoLabel();
    }

    private void onClickCucine(){
        // Configura il pulsante "Plus"
        btnPiuCucine.setOnAction(event -> {
            numeroCucine++;
            updateCucineLabel();
        });

        // Configura il pulsante "Minus"
        btnMenoCucine.setOnAction(event -> {
            if (numeroCucine > 0) { // Evita valori negativi
                numeroCucine--;
                updateCucineLabel();
            }
        });
        // Inizializza la label con il valore iniziale
        updateCucineLabel();
    }


    private void updateBagniLabel() {
        labelBagni.setText(String.valueOf(numeroBagni));
    }

    private void updateSoggiornoLabel() {
        labelSoggiorno.setText(String.valueOf(numeroSoggiorno));
    }
    private void updateCucineLabel() {
        labelCucine.setText(String.valueOf(numeroCucine));
    }

    private void divideAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            logger.warning("L'indirizzo fornito è nullo o vuoto.");
            return;
        }
        try {
            TrasformaIndirizzo result = getTrasformaIndirizzo(address);
            textIndirizzo.setText(result.street() + ", " + result.zipCode() + ", " + result.province() + ", " + result.country());
            textNumCivic.setText(result.number() != null ? result.number() : "N/A");
            textComune.setText(result.city);
        } catch (Exception e) {
            logger.info("Errore durante il parsing dell'indirizzo: " + e.getMessage());
        }
    }

    private static TrasformaIndirizzo getTrasformaIndirizzo(String address) {
        String[] parts = address.split(", ");
        String street = parts.length > 0 ? parts[0] : "N/A"; // Via
        String number = null;
        if (parts.length > 1 && parts[1].matches("\\d+")) { // Controlla se è solo numerico
            number = parts[1]; // Numero civico
        }
        // Estrazione delle condizioni ternarie in variabili separate
        int cityIndex = (number != null) ? 2 : 1;
        int countryIndex = (number != null) ? 3 : 2;


        String[] cityDetails = parts.length > cityIndex
                ? parts[cityIndex].split(" ")
                : new String[]{"N/A", "N/A", "N/A"};


        String zipCode = cityDetails.length > 0 ? cityDetails[0] : "N/A";      // CAP
        String city = cityDetails.length > 1 ? cityDetails[1] : "N/A";         // Città
        String province = cityDetails.length > 2 ? cityDetails[2] : "N/A";     // Provincia


        String country = parts.length > countryIndex
                ? parts[countryIndex]
                : "N/A";

        return new TrasformaIndirizzo(street, number, zipCode, city, province, country);
    }

    public void levaMessaggioIndirizzo() {
        erroreIndirizzo.setVisible(false);
    }

    public void levaMessaggioCivico() {
        erroreCivico.setVisible(false);
    }

    public void levaMessaggioComune() {
        erroreComune.setVisible(false);
        if(!flagNonAutoComplete) {
            fetchAsync(textComune.getText());
        }
    }

    public void levaMessaggioClasse() {
        errorClasse.setVisible(false);
    }

    public void levaMessaggioArredamenti() {
        errorArredamento.setVisible(false);
    }

    public void levaMessaggioStanze() {
        errorStanze.setVisible(false);
        if(!textNumeroStanze.getText().isEmpty()) {
            if (checkNumeroStanze()) {
                mostraImmagineV(immagineStanze);
            } else {
                mostraImmagineX(immagineStanze);
            }
        }else{
            immagineStanze.setVisible(false);
        }
    }

    public void levaMessaggioPiano() {
        errorPiano.setVisible(false);
        if(!textPiani.getText().isEmpty()) {
            if (checkPiano()) {
                mostraImmagineV(immaginePiano);
            } else {
                mostraImmagineX(immaginePiano);
            }
        }else{
            immaginePiano.setVisible(false);
        }
    }

    public void levaMessaggioSuperficie() {
        errorSuperficie.setVisible(false);
        if(!textSuperficie.getText().isEmpty()) {
            if (checkSupeficie()) {
                mostraImmagineV(immagineSuperficie);
            } else {
                mostraImmagineX(immagineSuperficie);
            }
        }else{
            immagineSuperficie.setVisible(false);
        }
    }

    private void mostraImmagineV(ImageView image){
        image.setImage(new Image(Objects.requireNonNull(getClass().getResource("/com/example/prova2/images/icons8-done-64 (2).png")).toExternalForm()));
        image.setVisible(true);
    }

    private void mostraImmagineX(ImageView image){
        image.setImage(new Image(Objects.requireNonNull(getClass().getResource("/com/example/prova2/images/close.png")).toExternalForm()));
        image.setVisible(true);
    }

    public void levaMessaggioVendita() {
        venditaErrore.setVisible(false);
    }

    public void levaMessaggioSpesa() {
        speseErrore.setVisible(false);
        if(!textSpese.getText().isEmpty()){
            if(isSpeseCondominialiValide()){
                mostraImmagineV(fotoSpese);
            }else{
                mostraImmagineX(fotoSpese);
            }
        }else{
            fotoSpese.setVisible(false);
        }
    }

    public void levaMessaggioPrezzo() {
        prezzoErrore.setVisible(false);
        if(!textPrezzi.getText().isEmpty()){
            if(isPrezzoValido()){
                mostraImmagineV(fotoPrezzo);
            }else{
                mostraImmagineX(fotoPrezzo);
            }
        }else{
            fotoPrezzo.setVisible(false);
        }
    }


    public void levaMessaggioTitolo() {
        errorTitolo.setVisible(false);
    }

    public void levaMessaggioDescrizione() {
        errorDes.setVisible(false);
    }


    private record TrasformaIndirizzo(String street, String number, String zipCode, String city, String province, String country) {
    }

    public void creaAnnuncio(){
        if(checkDescrizione()) {
            Alert alert = AlertFactory.generateAlertForCaricaImmobile();
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                inviaDati();
            } else {
                System.out.println("Logout annullato");
            }
        }
    }

    private boolean checkDescrizione() {
        boolean flag=true;
        flag&=checkDesInseritaUtente();
        flag&=checkTitolo();
        return flag;
    }

    private boolean checkTitolo() {
        if(textTitolo.getText().isEmpty()){
            errorTitolo.setVisible(true);
            return false;
        }
        errorTitolo.setVisible(false);
        return true;
    }

    private boolean checkDesInseritaUtente() {
        if(textDescrizione.getText().isEmpty()){
            errorDes.setVisible(true);
            return false;
        }
        errorDes.setVisible(false);
        return true;
    }

    private void inviaDati() {
        new Thread(() -> Platform.runLater(() -> inviaDatiAnnuncio(DashboardAgenteController.getAgente().getToken()))).start();
    }


    private boolean aggiungiAnnuncio(String token){
        setAgenteCfForAnnuncio();
        setRandomNumber();
        setTypeEstates();
        setIndirizzoAnnuncio();
        setCaratteristiche();
        setSpeseEPrezzi();
        setScritture();
        new Thread(()->{
            for (File immagine : absPath){
                CreaAnnuncioFacade.uploadImage(immagine.getAbsolutePath(),"images/"+immagine.getName());
            }
        }).start();
        return CreaAnnuncioFacade.addAnnuncio(annuncioDTO,token);
    }




    public static Alert generateSuccessMessage() {
        return AlertFactory.generateAlertSuccess("Torna alla dashboard", "Continua a creare altri annunci", "Operazione completata!", "Hai creato con successo il tuo annuncio!", "Complimenti l'operazione è andata a buon fine, vedrai il tuo annuncio nella sezione Annunci recenti sulla tua dashboard");
    }

    private void inviaDatiAnnuncio(String token) {
        if (aggiungiAnnuncio(token)) {
            mostraPopUpConferma();
        } else {
            AlertFactory.generateFailAlert("Errore nella creazione dell'annuncio !", "Siamo spiacenti si " +
                    "è verificato un errore interno nel sistema si prega di riprovare!");
        }
    }

    private void navigaAllaDashboard() {
            try{
                DashboardAgente.initializePageDashboardAgente(btnCarica.getScene().getWindow());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                AlertFactory.generateFailAlert("Operazione interrotta", "Il thread è stato interrotto durante l'operazione.");
            } catch (IOException e) {
                AlertFactory.generateFailAlert(ERROR_MESSAGE_SCHERMATA, ERROR_MESSAGE_CARICAMENTOSCHERMATA);
            }
    }

    public void tornaIndietro() throws IOException, InterruptedException {
        Alert alert = AlertFactory.generateAlertForIndietro();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stagePrecedente.show();
            stageOra.close();
        } else {
            System.out.println("Logout annullato");
        }
    }

    public void closeWindows(){
        Stage currentStage = (Stage) btnIndietro.getScene().getWindow();
        currentStage.close();
    }
    private void mostraPopUpConferma(){
        Alert alert = generateSuccessMessage();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            Stage currentStage = (Stage) btnCarica.getScene().getWindow();
            if (result.get().getText().equals("Torna alla dashboard")) {
                controllerPrec.initialize();
                currentStage.close();
            } else if (result.get().getText().equals("Continua a creare altri annunci")) {
                currentStage.close();
                navigaCreaAnnunci();
            }
        }
    }


    private void navigaCreaAnnunci() {
        try {
            CreaAnnuncioAgente.initializePageCreaAnnuncioFromCreateAnnunci(agenteCf);
        } catch (IOException e) {
            AlertFactory.generateFailAlert(ERROR_MESSAGE_SCHERMATA, ERROR_MESSAGE_CARICAMENTOSCHERMATA);
        }
    }




    private void setScritture() {
        String titolo = textTitolo.getText();
        String descrizione = textDescrizione.getText();

        annuncioDTO.setTitolo(titolo);
        annuncioDTO.setDescrizione(descrizione);
    }

    private void setSpeseEPrezzi() {
        String input = textPrezzi.getText();
        input = input.replace(".", "").trim();
        BigDecimal prezzo = new BigDecimal(input);

        String input2 = textSpese.getText();
        input = input.replace(".", "").trim();
        BigDecimal spese = new BigDecimal(input2);


        // Recupero del RadioButton selezionato
        RadioButton selectedTypeSell = (RadioButton) group3.getSelectedToggle();

        if (selectedTypeSell != null) {
            // Recupero del valore enum associato con setUserData
            TipoVendita tipoSelezionato = (TipoVendita) selectedTypeSell.getUserData();
            annuncioDTO.setTipovendita(tipoSelezionato);
        } else {
            logger.warning(LOGGER_MESSAGE);
        }
        annuncioDTO.setPrezzo(prezzo);
        annuncioDTO.setSpeseCondominiali(spese);
    }

    private void setCaratteristiche() {
        int superfice = Integer.parseInt(textSuperficie.getText());
        int piani = Integer.parseInt(textPiani.getText());
        int numStanze = Integer.parseInt(textNumeroStanze.getText());
        String classeEn = selClasse.getText();
        String ascensore = selAscensore.getText();
        switch (ascensore){
            case "Si":
                annuncioDTO.setAscensore(true);
                break;
            case "No":
                annuncioDTO.setAscensore(false);
                break;
                default:
                    annuncioDTO.setAscensore(false);
        }
        // Recupero del RadioButton selezionato
        RadioButton selectedTypeArr = (RadioButton) group2.getSelectedToggle();

        if (selectedTypeArr != null) {
            // Recupero del valore enum associato con setUserData
            Arredamento tipoSelezionato = (Arredamento) selectedTypeArr.getUserData();
            annuncioDTO.setArredamento(tipoSelezionato);
        } else {
            logger.warning(LOGGER_MESSAGE);
        }

        annuncioDTO.setSuperficie(superfice);
        annuncioDTO.setPiano(piani);
        annuncioDTO.setNumeroStanze(numStanze);
        annuncioDTO.setNumeroBagni(numeroBagni);
        annuncioDTO.setNumeroSoggiorni(numeroSoggiorno);
        annuncioDTO.setNumeroCucine(numeroCucine);
        annuncioDTO.setClasseEnergetica(ClasseEnergetica.fromString(classeEn));
    }

    private void setTypeEstates() {
        RadioButton selectedTypeEstates = (RadioButton) group.getSelectedToggle();
        if (selectedTypeEstates != null) {
            TipoImmobile tipoSelezionato = (TipoImmobile) selectedTypeEstates.getUserData();
            annuncioDTO.setTipoimmobile(tipoSelezionato);
        } else {
            logger.warning(LOGGER_MESSAGE);
        }



    }

    private void setIndirizzoAnnuncio() {
        String comune = textComune.getText();
        String indirizzo = textIndirizzo.getText();
        String numCivic = textNumCivic.getText();

        annuncioDTO.setComune(comune);
        annuncioDTO.setVia(indirizzo);
        annuncioDTO.setNumeroCivico(numCivic);
    }


}