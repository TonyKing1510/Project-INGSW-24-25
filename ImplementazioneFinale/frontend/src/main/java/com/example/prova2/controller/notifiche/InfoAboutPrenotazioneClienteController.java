package com.example.prova2.controller.notifiche;
import com.example.prova2.controller.dashBoard.DashBoardClienteController;
import com.example.prova2.dto.InformazioniVisitaDTO;
import com.example.prova2.facade.VisitaServiceFacade;
import javafx.application.Platform;

public class InfoAboutPrenotazioneClienteController extends InfoAbouPrenotazioneAgenteController {

    VisualizzaNotificheClienteController controller;

    public void setController(VisualizzaNotificheClienteController controller) {
        this.controller = controller;
    }

    public VisualizzaNotificheClienteController getController() {
        return controller;
    }


    public void initPage(int notifica){
        btnAccetta.setVisible(false);
        btnRifiuta.setVisible(false);
        new Thread(()->{
            Platform.runLater(()->{
                InformazioniVisitaDTO visita= VisitaServiceFacade.getInfoAboutVisita(notifica, DashBoardClienteController.getCliente().getToken());
                setDati(visita);
                labelNome.setText(visita.getAgente());
            });
        }).start();
    }
}
