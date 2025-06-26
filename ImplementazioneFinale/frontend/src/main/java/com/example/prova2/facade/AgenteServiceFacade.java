package com.example.prova2.facade;
import com.example.prova2.dto.AgenteCreazioneDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Agente;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.service.AgenteService;

import java.io.IOException;
import java.util.List;


public class AgenteServiceFacade {


    public static boolean nonErrore = true;


    public static boolean addAgente(Agente agente, GestoreAgenziaImmobiliare g) {
        try {
            AgenteCreazioneDTO dto=AgenteService.addAgente(agente,g);
            System.out.println(dto.isCfRegistrato());
            System.out.println(dto.isEmailRegistrata());
            if(dto.isCfRegistrato()){
                AlertFactory.generateFailAlertForCFReg();
                return false;
            }
            if(dto.isEmailRegistrata()){
                AlertFactory.generateFailAlertForEmailReg();
                return false;
            }
            return true;
        }catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public static Integer getValutazioneAgente(String cf, String token) {
        try{
            return AgenteService.getValutazioneAgente(cf,token);
        } catch (NullPointerException e) {
            AlertFactory.generateFailAlertForErroreInterno();
        }
        return null;
    }

    public static Integer getValutazioneAgenteByEmail(String email, String token) {
        try{
            return AgenteService.getValutazioneAgenteByEmail(email,token);
        } catch (NullPointerException e) {
            AlertFactory.generateFailAlertForErroreInterno();
        }
        return null;
    }

    public static List<String> getFotoAgente(String cf){
        return AgenteService.getFotoAgente(cf);
    }






    private static void gestisciCasoErrore(boolean error) {
        if (error) {
            AlertFactory.generateFailAlertForErroreInterno();
            nonErrore=false;
        }
    }

    private static void gestisciCasoPositivo(boolean error) {
        if (!error) {
            AlertFactory.generateSuccessAlertForSuccessUpdateDati();
        }
        nonErrore=false;
    }

}
