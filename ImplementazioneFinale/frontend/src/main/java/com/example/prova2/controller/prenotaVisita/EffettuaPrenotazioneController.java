package com.example.prova2.controller.prenotaVisita;
import com.example.prova2.controller.AnnullaVisita;
import com.example.prova2.controller.HomePageController;
import com.example.prova2.dto.AddVisitaRequestDTO;
import com.example.prova2.dto.VisitaResponseDTO;

import com.example.prova2.dto.WeatherResponseDTO;
import com.example.prova2.facade.ImmobileFacade;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Cliente;
import com.example.prova2.model.Immobile;

import com.example.prova2.service.WeatherService;
import com.example.prova2.view.EffettuaPrenotazione;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class EffettuaPrenotazioneController {
    @FXML
    public HBox hboxContenitore;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public Button bottoneInvia;
    @FXML
    public Pane terzaFasciaOrari;
    @FXML
    public Pane secondaFasciaOrari;
    @FXML
    public Pane primaFasciaOrari;
    @FXML
    public Label primaFasciaLabel;
    @FXML
    public Label secondaFasciaLabel;
    @FXML
    public Label terzaFasciaLabel;
    @FXML
    public Label labelGradi;
    @FXML
    public Pane paneMeteo;
    @FXML
    public Label scrittaData;
    @FXML
    public Label scrittaPosizione;
    @FXML
    public Label percentualePioggia;

    @FXML
    public ImageView sunImage;

    @FXML
    public ImageView tempestaImage;

    @FXML
    public Button btnAvantiDate;

    @FXML
    public Button btnIndietroDate;
    @FXML
    public Label scrittaMeteo;

    private Cliente cliente;

    private Immobile immobile;

    Pane panePrecedentePerDate = null;
    
    Pane panePrecedentePerOrari = null;
    
    HashMap<Pane,Label> paneEOrari = new HashMap<>();

    AnnullaVisita controllerPrec;

    AddVisitaRequestDTO requestDTOIn;

    public void initalize(String email, Immobile immobile,AnnullaVisita controllerPrec) {
        this.controllerPrec = controllerPrec;
        this.cliente = new Cliente(email);
        this.immobile = immobile;
        btnAvantiDate.setOnAction(event -> scrollHorizontally(100));
        btnIndietroDate.setOnAction(event -> scrollHorizontally(-100));
        List<Pane> orari = new ArrayList<>();
        LocalDate oggi = LocalDate.now();
        hboxContenitore.setSpacing(20);
        new Thread(()->{
            Platform.runLater(()->{
                for (int i = 0; i <= 14; i++) {
                    Pane pane=createDayPane(oggi.plusDays(i));
                    hboxContenitore.getChildren().add(pane);
                }
                initOrari(orari);
                gestisciUnClickOrari(orari);
            });
        }).start();
    }

    public void tornaAllaDash() {

    }

    private void scrollHorizontally(double amount) {
            Platform.runLater(() -> {
                btnIndietroDate.setVisible(true);
                double current = scrollPane.getHvalue();
                double scrollAmount = amount / (hboxContenitore.getBoundsInLocal().getWidth() - scrollPane.getWidth());
                double newValue = Math.max(0, Math.min(1, current + scrollAmount));
                scrollPane.setHvalue(newValue);
            });
    }


    private void initOrari(List<Pane> orari) {
        orari.add(primaFasciaOrari);
        orari.add(secondaFasciaOrari);
        orari.add(terzaFasciaOrari);
        paneEOrari.put(primaFasciaOrari,primaFasciaLabel);
        paneEOrari.put(secondaFasciaOrari,secondaFasciaLabel);
        paneEOrari.put(terzaFasciaOrari,terzaFasciaLabel);
    }

    private void gestisciUnClickOrari(List<Pane> fasceOrarie) {
        for (Pane orari :fasceOrarie ) {
            orari.setOnMouseEntered(event ->
                orari.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 30; -fx-cursor: hand;")
            );
            orari.setOnMouseExited(event -> {
                if (orari != panePrecedentePerOrari) {
                    orari.setStyle("-fx-background-color: white; -fx-background-radius: 30; -fx-cursor: hand;");
                }
            });
            orari.setOnMouseClicked(event -> {
                if (panePrecedentePerOrari != null && !panePrecedentePerOrari.getId().equals(orari.getId())) {
                    panePrecedentePerOrari.setStyle("-fx-background-color: white; -fx-background-radius: 30; -fx-cursor: hand;");
                }
                orari.setStyle("-fx-background-color: #B8B8B8; -fx-background-radius: 30; -fx-cursor: hand;");
                panePrecedentePerOrari = orari;
                attivaBottone(panePrecedentePerDate);
            });
        }
    }

    private void attivaBottone(Pane pane) {
        bottoneInvia.setVisible(pane != null);
    }

    public Pane createDayPane(LocalDate date) {
        Pane pane = new Pane();
        pane.setPrefSize(183, 166);
        pane.setStyle("-fx-background-color: white; -fx-background-radius: 30; -fx-cursor: hand;");

        Label labelGiorno = new Label(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ITALIAN));
        labelGiorno.setLayoutX(63);
        labelGiorno.setLayoutY(14);
        labelGiorno.setPrefSize(61, 36);
        labelGiorno.setStyle("-fx-text-fill: #011638;");
        labelGiorno.setFont(new Font("System Bold", 25));

        Label labelNumero = new Label(date.getDayOfMonth() + "");
        labelNumero.setLayoutX(63);
        labelNumero.setLayoutY(54);
        labelNumero.setPrefSize(61, 36);
        labelNumero.setStyle("-fx-text-fill: #011638;");
        labelNumero.setFont(new Font("System Bold", 41));

        Label labelMese = new Label(date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ITALIAN));
        labelMese.setLayoutX(68);
        labelMese.setLayoutY(113);
        labelMese.setPrefSize(61, 36);
        labelMese.setStyle("-fx-text-fill: #011638;");
        labelMese.setFont(new javafx.scene.text.Font("System Bold", 25));
        pane.setId(labelNumero.getText()+" "+labelMese.getText());
        pane.setOnMouseEntered(event ->
                pane.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 30; -fx-cursor: hand;")

        );
        pane.setOnMouseExited(event -> {
            if (pane != panePrecedentePerDate) {
                pane.setStyle("-fx-background-color: white; -fx-background-radius: 30; -fx-cursor: hand;");
            }
        });
        pane.setOnMouseClicked(event -> {
            if (panePrecedentePerDate != null && !panePrecedentePerDate.getId().equals(pane.getId())) {
                panePrecedentePerDate.setStyle("-fx-background-color: white; -fx-background-radius: 30; -fx-cursor: hand;");
            }
            pane.setStyle("-fx-background-color: #B8B8B8; -fx-background-radius: 30; -fx-cursor: hand;");
            panePrecedentePerDate = pane;
            vediMeteo();
            attivaBottone(panePrecedentePerOrari);
        });
        pane.getChildren().addAll(labelGiorno, labelNumero, labelMese);

        return pane;
    }

    private void vediMeteo() {
        fetchWeatherDate();
    }

    private void fetchWeatherDate() {
        new Thread(() -> {
            String formattedDate = getFormattedDate();
            String date=getDataToWriteOnLabel(panePrecedentePerDate.getId()+" "+LocalDate.now().getYear());
            WeatherResponseDTO dto = WeatherService.getWeather(immobile.getLatitudine(), immobile.getLongitudine(), formattedDate, formattedDate);
            Platform.runLater(()->{
                if (dto != null) {
                    labelGradi.setText(dto.getDaily().getTemperature_2m_max().toString().replace("[", "").replace("]", "") + "°");
                    scrittaData.setText(date);
                    scrittaPosizione.setText(immobile.getComune()+","+immobile.getViaImmobile()+" n°"+immobile.getNumeroCivico());
                    percentualePioggia.setText("Probabilità di pioggia : "+dto.getDaily().getPrecipitation_sum().toString().replace("[","").replace("]","")+"%");
                    scegliImmagineDaVisualizzare(dto.getDaily().getPrecipitation_sum().getFirst());
                    scrittaMeteo.setVisible(false);
                    paneMeteo.setVisible(true);
                }
            });
        }).start();
    }

    private void scegliImmagineDaVisualizzare(Integer percentualePioggia) {
        Platform.runLater(()->{
            if(percentualePioggia <=30){
                sunImage.setVisible(true);
                tempestaImage.setVisible(false);
            }else if(percentualePioggia >= 70){
                sunImage.setVisible(false);
                tempestaImage.setVisible(true);
                paneMeteo.setStyle(paneMeteo.getStyle()+"-fx-background-color: #BAB2CD;");
            }
        });
    }

    private String getDataToWriteOnLabel(String dataInput) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ITALIAN);
        LocalDate data = LocalDate.parse(dataInput, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.ITALIAN);
        String dataFormattata = data.format(outputFormatter);
        return rendiMaiuscoloPrimoCarattere(dataFormattata);
    }

    private static String rendiMaiuscoloPrimoCarattere  (String dataFormattata) {
        if (!dataFormattata.isEmpty()) {
            dataFormattata = dataFormattata.substring(0, 1).toUpperCase() + dataFormattata.substring(1);
        }
        return dataFormattata;
    }

    public void inviaRichiesta() {
        String formattedDate = getFormattedDate();
        Label fasciaOrariaSelezionata=paneEOrari.get(panePrecedentePerOrari);
        String[] parts = fasciaOrariaSelezionata.getText().split("-");
        String startTime = String.format("%02d:00", Integer.parseInt(parts[0]));
        String endTime = String.format("%02d:00", Integer.parseInt(parts[1]));
        AddVisitaRequestDTO requestDTO = new AddVisitaRequestDTO(LocalDate.parse(formattedDate),
                LocalTime.parse(startTime),LocalTime.parse(endTime),"ciao",cliente.getAccountAgente().getEmail(),
                immobile.getAgenteProprietario(),immobile.getId_immobile(), HomePageController.getUtente().getToken());
            VisitaResponseDTO response=ImmobileFacade.addVisita(requestDTO);
            this.requestDTOIn=requestDTO;
            if(controllerPrec != null){
                this.controllerPrec.mostraPaneAnnullaVisita(requestDTOIn);
            }
        Platform.runLater(()->{showPopUp(response);});

    }

    private void showPopUp(VisitaResponseDTO response) {
        if(response == null){
            AlertFactory.generateFailAlertForErroreInterno();
            return;
        }
        if(response.isSuccess()){
            Alert alert = AlertFactory.generateSuccessAlertForSuccessInvioPrenotazione();
            alert.showAndWait().ifPresentOrElse(responseUtente -> {
                chiudiFinestra();
            }, this::chiudiFinestra);
        }
    }

    private void chiudiFinestra() {
        EffettuaPrenotazione.getStagePrenotazione().close();
    }


    private String getFormattedDate() {
        String dataConAnno = panePrecedentePerDate.getId() + " " + LocalDate.now().getYear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ITALIAN);
        LocalDate dataParsed = LocalDate.parse(dataConAnno, formatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dataParsed.format(outputFormatter);

    }

}
