package com.example.prova2.controller;

import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.dto.GetAllRicercheResponse;
import com.example.prova2.facade.ClienteServiceFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.factory.FactoryRicerchePassate;
import com.example.prova2.model.*;
import com.example.prova2.service.GeopifyService;
import com.example.prova2.view.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomePageController {

    private static Utente utente;
    @FXML
    public HBox hBoxPerPane;
    @FXML
    public Button btnIndietro;
    @FXML public ImageView immagineNoRicerca;
    @FXML public Text scrittaNoRicerca;
    @FXML
    public ListView<String> listMaps;

    public static void setUtente(Utente utente) {
        HomePageController.utente = utente;
        System.out.println("setUtente"+utente.getNome());
    }


    public static Utente getUtente() {
        return utente;
    }

    @FXML
    public ImageView fotoProfilo;
    @FXML
    private ScrollPane scrollPaneHome;
    @FXML
    private ScrollPane principalScrollPane;

    @FXML
    private Button btnAvanti;

    @FXML
    private Label labelErrorComune;
    @FXML
    private Label labelErrorNumeroStanze;
    @FXML
    private Pane annunciPane;
    @FXML
    private Pane principalPane;
    @FXML
    private Pane ricerchePane;
    @FXML
    private Pane newsPane;
    @FXML
    private Button btnCerca;
    @FXML
    private TextField textComune;
    @FXML
    private ComboBox<String> textPrezzoMin;
    @FXML
    private ComboBox<String> textPrezzoMax;
    @FXML
    private TextField textNumStanze;
    @FXML
    private MenuButton menuClasse;
    @FXML
    private MenuButton menuTipologia;
    @FXML
    private Button btnUserDash;

    @FXML
    public Hyperlink FAQpageLink;
    @FXML
    private ScrollPane scrollPaneHome2;

    boolean flagNonAutoComplete = true;

    @FXML
    private void handleAvanti2() {
        double currentHvalue = scrollPaneHome2.getHvalue();
        scrollPaneHome2.setHvalue(Math.min(1, currentHvalue + 0.2));
    }

    @FXML
    private void handleIndietro2() {
        double currentHvalue = scrollPaneHome2.getHvalue();
        scrollPaneHome2.setHvalue(Math.max(0, currentHvalue - 0.2));
    }

    @FXML
    private void handleAvanti() {
        double currentHvalue = scrollPaneHome.getHvalue();
        scrollPaneHome.setHvalue(Math.min(1, currentHvalue + 0.2));
    }

    @FXML
    private void handleIndietro() {
        double currentHvalue = scrollPaneHome.getHvalue();
        scrollPaneHome.setHvalue(Math.max(0, currentHvalue - 0.2));
    }

    @FXML
    private void scrollToPane(ActionEvent event){
        double targetPosition = annunciPane.getLayoutY() / principalPane.getHeight();
        principalScrollPane.setVvalue(targetPosition);

    }

    @FXML
    private void scrollToPaneRicercheRecenti(ActionEvent event){
        System.out.println("ciao");
        double offset = 0.0001;
        double targetPosition = (ricerchePane.getLayoutY() / principalPane.getHeight())+offset;

        principalScrollPane.setVvalue(targetPosition);

    }

    @FXML
    private void scrollToPaneNews(ActionEvent event){
        double offset = 0.1;
        double targetPosition = (newsPane.getLayoutY() / principalPane.getHeight())+offset;
        principalScrollPane.setVvalue(targetPosition);
    }

    public void initialize(Utente utente) {
        aggiungiListenerPrezzi();
        btnCerca.setDefaultButton(true);
        principalScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                double delta = event.getDeltaY();
                principalScrollPane.setVvalue(principalScrollPane.getVvalue() - delta / 700);
                event.consume();
            }
        });
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
        ArrayList<ClasseEnergetica> options = new ArrayList<>();
        ArrayList<TipoVendita> options2 = new ArrayList<>();
        aggiungiOpzioniArrayClasse(options);
        aggiungiOpzioniArrayTipi(options2);
        addListenerClasseEn(options);
        addListenerTipologia(options2);
        setUtente(utente);

        new Thread(()->{
            List<GetAllRicercheResponse> ricerchePassate=ClienteServiceFacade.getAllRicerche(utente);
            if(ricerchePassate.isEmpty()){
                scrittaNoRicerca.setVisible(true);
                immagineNoRicerca.setVisible(true);
                btnAvanti.setVisible(false);
                btnIndietro.setVisible(false);
            }else{
                if(ricerchePassate.size()>3){
                    btnAvanti.setVisible(true);
                    btnIndietro.setVisible(true);
                }
                scrittaNoRicerca.setVisible(false);
                immagineNoRicerca.setVisible(false);
            }
        makeRicerchePassate(ricerchePassate);}).start();
        addTextFormatter(textComune);
    }

    private void aggiungiListenerPrezzi() {
        textPrezzoMin.getEditor().addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                event.consume(); // Consuma l'evento per impedire l'inserimento
            }
        });
        textPrezzoMax.getEditor().addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                event.consume(); // Consuma l'evento per impedire l'inserimento
            }
        });
        textPrezzoMin.getItems().addAll("10.000","20.000","30.000","40.000","50.000","60.000","70.000");
        textPrezzoMax.getItems().addAll("10.000","20.000","30.000","40.000","50.000","60.000","70.000");
        textPrezzoMax.setEditable(true);
        textPrezzoMin.setEditable(true);
        textPrezzoMin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            String cleanText = newValue.replaceAll("[^\\d]", "");
            if (!cleanText.isEmpty()) {
                StringBuilder formattedText=aggiungiSoloNumeriEPunti(cleanText);
                textPrezzoMin.setValue(formattedText.reverse().toString());
            } else {
                textPrezzoMin.setValue("");
            }
        });
        textPrezzoMax.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            String cleanText = newValue.replaceAll("[^\\d]", "");
            if (!cleanText.isEmpty()) {
                StringBuilder formattedText=aggiungiSoloNumeriEPunti(cleanText);
                textPrezzoMax.setValue(formattedText.reverse().toString());
            } else {
                textPrezzoMax.setValue("");
            }
        });
    }

    private static StringBuilder aggiungiSoloNumeriEPunti(String cleanText) {
        StringBuilder formattedText = new StringBuilder();
        int count = 0;
        for (int i = cleanText.length() - 1; i >= 0; i--) {
            formattedText.append(cleanText.charAt(i));
            count++;
            if (count % 3 == 0 && i != 0) {
                formattedText.append(".");
            }
        }
        return formattedText;
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

    static void aggiungiListener(TextField textSpese) {
        textSpese.textProperty().addListener((observable, oldValue, newValue) -> {
            String cleanText = newValue.replaceAll("[^\\d]", "");
            if (!cleanText.isEmpty()) {
                StringBuilder formattedText= aggiungiSoloNumeriEPunti(cleanText);
                textSpese.setText(formattedText.reverse().toString());
            } else {
                textSpese.setText("");
            }
        });
    }

    private void makeRicerchePassate(List<GetAllRicercheResponse> ricerchePassate) {
        Platform.runLater(()->{
            for (GetAllRicercheResponse ricerca : ricerchePassate){
                Pane pane = createPane(ricerca.getComune(), ricerca.getTipoVendita(),
                        ricerca.getPrezzoMin(), ricerca.getPrezzoMax(), ricerca.getNumeroStanze(),
                        ricerca.getClasseEnergetica());
                hBoxPerPane.getChildren().add(pane);
            }
        });
    }

    private Pane createPane(String comune, TipoVendita stato, BigDecimal prezzoMin, BigDecimal prezzoMax, Integer numStanze,
                            ClasseEnergetica classeEnergetica) {
        Pane pane = FactoryRicerchePassate.createPane();

        Label title = new Label("Case-Appartamenti");
        title.setLayoutX(14);
        title.setLayoutY(14);
        title.setStyle("-fx-text-fill: #011638; -fx-font-size: 32px; -fx-font-weight: bold;");

        Label labelComune = new Label("Comune: " + (!comune.isEmpty() ? comune : "Qualsiasi"));
        labelComune.setLayoutX(15);
        labelComune.setLayoutY(55);
        labelComune.setStyle("-fx-text-fill: #011638; -fx-font-size: 20px;");


        Label labelStato = new Label("Stato: " + (stato != null ? stato : "Qualsiasi"));
        labelStato.setLayoutX(15);
        labelStato.setLayoutY(103);
        labelStato.setStyle("-fx-text-fill: #011638; -fx-font-size: 20px;");

        Label labelPrezzoMin = new Label("Prezzo Minimo: " + (prezzoMin != null ? prezzoMin : "Qualsiasi"));
        labelPrezzoMin.setLayoutX(15);
        labelPrezzoMin.setLayoutY(153);
        labelPrezzoMin.setStyle("-fx-text-fill: #011638; -fx-font-size: 20px;");

        Label labelPrezzoMax = new Label("Prezzo Massimo: " + (prezzoMax != null ? prezzoMax : "Qualsiasi"));
        labelPrezzoMax.setLayoutX(15);
        labelPrezzoMax.setLayoutY(205);
        labelPrezzoMax.setStyle("-fx-text-fill: #011638; -fx-font-size: 20px;");

        Label labelNumStanze = new Label("Numero stanze: " + (numStanze != null ? numStanze : "Qualsiasi"));
        labelNumStanze.setLayoutX(15);
        labelNumStanze.setLayoutY(256);
        labelNumStanze.setStyle("-fx-text-fill: #011638; -fx-font-size: 20px;");

        System.out.println(classeEnergetica);
        Label labelClasseEnergetica = new Label("Classe Energetica: " + (classeEnergetica != null ? classeEnergetica : "Qualsiasi"));
        labelClasseEnergetica.setLayoutX(15);
        labelClasseEnergetica.setLayoutY(308);
        labelClasseEnergetica.setStyle("-fx-text-fill: #011638; -fx-font-size: 20px;");

        Button btnCercaPassati = new Button("Avvia ricerca");
        btnCercaPassati.setLayoutX(92);
        btnCercaPassati.setLayoutY(386);
        btnCercaPassati.setPrefSize(153, 40);
        btnCercaPassati.setStyle(" -fx-cursor: hand; -fx-background-radius: 8; -fx-background-color: #011638; -fx-text-fill: white;");
        btnCercaPassati.setOnMouseEntered(e -> btnCercaPassati.setStyle("-fx-cursor: hand; -fx-background-radius: 8; -fx-background-color: #b0b0b0; -fx-text-fill: white;"));
        btnCercaPassati.setOnMouseExited(e -> btnCercaPassati.setStyle("-fx-cursor: hand; -fx-background-radius: 8; -fx-background-color: #011638; -fx-text-fill: white;"));
        btnCercaPassati.setOnAction(e ->
                faiRicerca(comune, stato, prezzoMin, prezzoMax, numStanze, classeEnergetica, btnCercaPassati)
        );
        pane.getChildren().addAll(title, labelComune, labelStato, labelPrezzoMin, labelPrezzoMax, labelNumStanze, labelClasseEnergetica, btnCercaPassati);
        return pane;
    }

    private static void faiRicerca(String comune, TipoVendita stato, BigDecimal prezzoMin, BigDecimal prezzoMax, Integer numStanze, ClasseEnergetica classeEnergetica, Button btnCerca)  {
        try {
            SchermataRicercaController.setUtenteCheCerca(utente);
            RicercaPage.caricamento((Stage) btnCerca.getScene().getWindow(), comune, stato, prezzoMin, prezzoMax, numStanze, classeEnergetica);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private void addListenerTipologia(ArrayList<TipoVendita> options) {
        for (TipoVendita option : options) {
            MenuItem menuItem = new MenuItem(option.toString()); // Imposta il testo visibile nel menu
            menuItem.setOnAction(event -> menuTipologia.setText(option.toString()));
            menuTipologia.getItems().add(menuItem);
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
        CreaAnnuncioController.setOptions(options);
    }

    private void aggiungiOpzioniArrayTipi(ArrayList<TipoVendita> options) {
        options.add(TipoVendita.VENDITA);
        options.add(TipoVendita.AFFITTO);
    }


    public void handleSearch() {
        String comune = textComune.getText().trim();
            Integer numStanze = (textNumStanze.getText().trim().isEmpty())
                    ? null : Integer.parseInt(textNumStanze.getText());

            BigDecimal prezzoMin = (textPrezzoMin.getValue() == null || textPrezzoMin.getValue().trim().isEmpty())
                    ? null
                    : new BigDecimal(textPrezzoMin.getValue().replace(".", ""));


            BigDecimal prezzoMax = (textPrezzoMax.getValue() == null || textPrezzoMax.getValue().trim().isEmpty())
                    ? null
                    : new BigDecimal(textPrezzoMax.getValue().replace(".", ""));


            TipoVendita tipoVendita = (Objects.equals(menuTipologia.getText(), "Qualsiasi") || menuTipologia.getText().trim().isEmpty())
                    ? null : TipoVendita.valueOf(menuTipologia.getText().trim());

            ClasseEnergetica classeEnergetica = (Objects.equals(menuClasse.getText(), "Qualsiasi") || menuClasse.getText().trim().isEmpty())
                    ? null : ClasseEnergetica.fromString(menuClasse.getText().trim());

            faiRicerca(comune, tipoVendita, prezzoMin, prezzoMax, numStanze, classeEnergetica, btnCerca);
    }

    private boolean checkNumStanze() {
        String numStanzeText = textNumStanze.getText().trim();

        boolean isNumStanzeValid = numStanzeText.isEmpty() || (numStanzeText.matches("-?\\d+") && Integer.parseInt(numStanzeText) >= 0);

        labelErrorNumeroStanze.setVisible(!isNumStanzeValid && !numStanzeText.isEmpty());

        return isNumStanzeValid;
    }

    private boolean checkComune(){
        String comuneText = textComune.getText().trim();
        boolean isComuneValid = !comuneText.isEmpty();

        labelErrorComune.setVisible(!isComuneValid);

        return isComuneValid;

    }


    public void apriDash() {
        switch (utente) {
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
            DashboardAmministratore.initializePageDashboardAmministratore((Stage) btnAvanti.getScene().getWindow());
        }catch (IOException e) {
            AlertFactory.generateFailAlertForErroreInterno();
        }
    }

    private void apriDashCliente() {
        try {
            System.out.println("apriDashCliente"+utente.getNome());
            DashBoardCliente.initializePageDashboardCliente(btnAvanti.getScene().getWindow(), utente);
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
            agenteDash.setAccountAgente(new Account(utente.getAccountAgente().getEmail(),utente.getAccountAgente().getPassword()));
            agenteDash.setToken(utente.getToken());
            DashboardAgenteController.setAgente(agenteDash);
            DashboardAgente.initializePageDashboardAgente(btnAvanti.getScene().getWindow());
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
    public void openFAQpage(){
        try{
            InfoPage.faqPageInit();
        } catch (IOException e) {
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }
    @FXML
    public void openPrivacyPage(){
        try{
            InfoPage.privacyPageInit();
        } catch (IOException e) {
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }
    @FXML
    public void openChiSiamoPage(){
        try{
            InfoPage.chiSiamoPageInit();
        } catch (IOException e) {
            AlertFactory.generateFailAlertForErroreCaricamentoPagina();
        }
    }

    public void suggestion() {
        fetchAsync(textComune.getText());
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
