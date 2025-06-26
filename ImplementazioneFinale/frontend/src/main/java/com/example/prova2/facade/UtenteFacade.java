package com.example.prova2.facade;

import com.example.prova2.controller.dashBoard.DashBoardClienteController;
import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.controller.dashBoard.DashboardAmministratoreController;
import com.example.prova2.dto.GetNotificheDTO;
import com.example.prova2.dto.InformazioniVisitaDTO;
import com.example.prova2.model.Agente;
import com.example.prova2.model.Cliente;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.model.Utente;
import com.example.prova2.service.*;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UtenteFacade {
    private UtenteFacade() {
    }

    public static void caricaImmagineProfiloAsync(GetNotificheDTO notifica, ImageView imageView, String token) {
        Task<String> task = new Task<String>() {
            @Override
            protected String call() {
                InformazioniVisitaDTO visita = VisitaServiceFacade.getInfoAboutVisita(notifica.getIdNotifica(), token);
                return S3Service.getImageFromS3(visita.getFotoProfiloAgente());
            }
        };
        task.setOnSucceeded(event -> imageView.setImage(new Image(task.getValue())));
        task.setOnFailed(event -> System.err.println("Errore nel caricamento dell'immagine: " + task.getException()));
        new Thread(task).start();
    }

    public static boolean updatePassword(String nuovaPass, Utente utente) {
        switch (utente) {
            case Cliente cliente -> {
                return UpdatePasswordService.updatePasswordCliente(nuovaPass, DashBoardClienteController.getCliente());
            }
            case Agente agente -> {
                return UpdatePasswordService.updatePasswordAgente(nuovaPass, DashboardAgenteController.getAgente());
            }
            case GestoreAgenziaImmobiliare gestoreAgenziaImmobiliare -> {
                return UpdatePasswordService.updatePasswordGestore(nuovaPass, DashboardAmministratoreController.getAdmin());
            }
            default -> {
                return false;
            }
        }
    }
}
