package com.example.prova2.facade;
import com.example.prova2.dto.UpdateDatiResponseDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Agente;
import com.example.prova2.model.Foto;
import com.example.prova2.model.Utente;
import com.example.prova2.service.AgenteService;
import com.example.prova2.service.GestoreService;
import com.example.prova2.service.S3Service;
import com.example.prova2.service.UpdatePasswordService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModificaProfiloFacade {
    public static List<String> getFotoAgente(String cf){
        return AgenteService.getFotoAgente(cf);
    }

    public static String getImageFromS3(String key){
        return S3Service.getImageFromS3(key);
    }

    public static boolean updatePasswordAgente(String pass, Agente agente){
        return UpdatePasswordService.updatePasswordAgente(pass, agente);
    }

    public static boolean uploadFotoAgente(String cf, Foto foto){
        S3Service.uploadImage(foto.getPath(),"images/"+new File(foto.getPath()).getName(),cf);
        return true;
    }

    public static UpdateDatiResponseDTO updateDatiAgente(Agente agente, String agenteVecchio){
        UpdateDatiResponseDTO dto=AgenteService.updateDatiAgente(agente,agenteVecchio);
        if(dto == null){
            return null;
        }
        if(dto.getDuplicato()){
            AlertFactory.generateFailAlertForEmailReg();
            return null;
        }
        if(dto.getErroreInterno()){
            AlertFactory.generateFailAlertForEmailReg();
            return null;
        }
        return dto;
    }

    public static boolean updateBio(String bio,String cf,String token){
        boolean esito=AgenteService.updateBio(cf,bio,token);
        if(!esito){
            AlertFactory.generateFailAlertForErroreInterno();
            return false;
        }
        return true;
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
}
