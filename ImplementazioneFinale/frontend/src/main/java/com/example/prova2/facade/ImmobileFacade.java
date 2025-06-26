package com.example.prova2.facade;

import com.example.prova2.dto.AddVisitaRequestDTO;
import com.example.prova2.dto.DatiImmobileDTO;
import com.example.prova2.dto.ImmobileResponseRicercaDTO;
import com.example.prova2.dto.VisitaResponseDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.*;
import com.example.prova2.service.ImmobileService;

import java.io.IOException;
import java.util.List;

public class ImmobileFacade {
    public static List<ImmobileResponseRicercaDTO> search(Ricerca ricerca, Utente utente) {
        int sessioneUtente = switch (utente) {
            case Cliente cliente -> 2;
            case Agente agente -> 1;
            case GestoreAgenziaImmobiliare gestoreAgenziaImmobiliare -> 0;
            case null, default -> -1;
        };
        ricerca.setSessioneUtente(sessioneUtente);
        return ImmobileService.searchAnnunci(ricerca, utente);
    }

    public static VisitaResponseDTO addVisita(AddVisitaRequestDTO addVisitaRequestDTO) {
        VisitaResponseDTO response = ImmobileService.addVisita(addVisitaRequestDTO);
        if (response == null) {
            AlertFactory.generateFailAlertForErroreInterno();
            return null;
        }
        if (response.getFail()) {
            AlertFactory.generateFailAlertForErroreInterno();
        }
        return response;
    }

    public static DatiImmobileDTO getInfo(int idImmobile, String token) {
        DatiImmobileDTO dati = ImmobileService.getInfo(idImmobile, token);
        if (dati == null) {
            AlertFactory.generateFailAlertForErroreInterno();
        }
        return dati;
    }

    public static ImmobileResponseRicercaDTO getInfoById(int idImmobile, String token) throws IOException, InterruptedException {
        ImmobileResponseRicercaDTO dati = ImmobileService.getInfoById(idImmobile, token);
        if (dati == null) {
            AlertFactory.generateFailAlertForErroreInterno();
        }
        return dati;
    }

    public static boolean deleteImmobile(int id){
        boolean response=ImmobileService.deleteImmobile(id);
        if(!response){
            AlertFactory.generateAlertForErroreImmobileEliminato();
            return false;
        }
        return true;
    }

}
