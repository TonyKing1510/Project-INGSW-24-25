package com.example.prova2.facade;

import com.example.prova2.dto.UpdateDatiResponseDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.model.Utente;
import com.example.prova2.service.GestoreService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestoreServiceFacade {

    private GestoreServiceFacade(){}

    public static List<String> getCfGestore(String token){
        try {
            return GestoreService.getAllCfGestore(token);
        }catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            return new ArrayList<>();
        }
        catch (IOException e){
            return new ArrayList<>();
        }
    }

    public static List<String> getEmailGestore(Utente utente){
        try {
            return GestoreService.getAllEmailGestore(utente.getToken());
        }catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            return new ArrayList<>();
        }
        catch (IOException e){
            return new ArrayList<>();
        }
    }

    public static UpdateDatiResponseDTO updateDatiGestore(GestoreAgenziaImmobiliare gestore, Utente utentevecchio){
        UpdateDatiResponseDTO response= GestoreService.updateDatiGestore(gestore, utentevecchio);
        if(response == null){
            AlertFactory.generateFailAlertForErroreInterno();
            return null;
        }
        if(response.getErroreInterno()){
            AlertFactory.generateFailAlertForErroreInterno();
            return null;
        }
        if(response.getDuplicato()){
            AlertFactory.generateFailAlertForEmailReg();
            return null;
        }
        return response;
    }

    public static boolean isPasswordVecchiaValida(String email,String password,String token) {
        return GestoreService.checkPassword(email, password,token);
    }
}
