package com.example.prova2.facade;

import com.example.prova2.dto.AddVisitaRequestDTO;
import com.example.prova2.dto.GetVisiteResponse;
import com.example.prova2.dto.InformazioniVisitaDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.service.VisitaService;

import java.util.List;

public class VisitaServiceFacade {
    public static List<GetVisiteResponse> getVisiteOfAgente(String cf,String token){
        return VisitaService.getVisiteOfAgente(cf,token);
    }

    public static InformazioniVisitaDTO getInfoAboutVisita(int idNotifica,String token){
        InformazioniVisitaDTO response=VisitaService.getInfoAboutVisita(idNotifica,token);
        if(response == null){
            AlertFactory.generateFailAlertForErroreInterno();
        }
        return response;
    }

    public static boolean checkVisitaGiaPresente(String email,int idImmobile,String token){
        return VisitaService.checkVisitaClienteGiaPresente(email,idImmobile,token);
    }

    public static boolean annullaVisita(AddVisitaRequestDTO dto){
        return VisitaService.annullaVisita(dto);
    }
}
