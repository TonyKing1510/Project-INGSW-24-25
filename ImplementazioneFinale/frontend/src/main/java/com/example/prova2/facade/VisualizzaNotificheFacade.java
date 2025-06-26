package com.example.prova2.facade;

import com.example.prova2.dto.DatiDTO;
import com.example.prova2.dto.GetNotificheDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.service.AgenteService;
import com.example.prova2.service.NotificaService;
import com.example.prova2.service.VisitaService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VisualizzaNotificheFacade {

    public static void annullaInvioVisita(int id,String token) {
        VisitaService.annullaInvioVisita(id,token);
    }

    public static List<GetNotificheDTO> getNotificaAgente(String cf,String token){
        try {
            return NotificaService.getNotificationsForAgente(cf,token);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static DatiDTO getAgenteByEmail(String email, String token) {
        DatiDTO dto = AgenteService.getAgente(email,token);
        if (dto == null) {
            AlertFactory.generateFailAlertForErroreInterno();
        }
        return dto;
    }

    public static void setAccept(int idNotifica,String token) {
        NotificaService.setNotificationAccepted(idNotifica, token);
    }

    public static void setDecline(int idNotifica,String token) {
        NotificaService.setNotificationRejected(idNotifica, token);
    }
}
