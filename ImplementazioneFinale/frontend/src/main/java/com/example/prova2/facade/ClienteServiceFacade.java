package com.example.prova2.facade;
import com.example.prova2.dto.ClienteDTO;
import com.example.prova2.dto.ClienteDatiDTO;
import com.example.prova2.dto.GetAllRicercheResponse;
import com.example.prova2.dto.UpdateDatiResponseDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.model.Agente;
import com.example.prova2.model.Cliente;
import com.example.prova2.model.GestoreAgenziaImmobiliare;
import com.example.prova2.model.Utente;
import com.example.prova2.service.AgenteService;
import com.example.prova2.service.ClienteService;
import com.example.prova2.service.GestoreService;
import javafx.application.Platform;

import java.util.Collections;
import java.util.List;

public class ClienteServiceFacade {
    public static ClienteDTO registrati(Cliente cliente){
        ClienteDTO dto=ClienteService.registrati(cliente);
        if(dto != null){
            if(dto.isErroreInterno()){
                AlertFactory.generateFailAlertForErroreConnessione();
            }
            System.out.println("token"+dto.getToken());
            return dto;
        }else{
            AlertFactory.generateFailAlertForErroreConnessione();
            return null;
        }
    }

    public static ClienteDatiDTO prendiDati(Utente cliente){
        System.out.println("prendiDati cliente"+cliente.getAccountAgente().getEmail());
        System.out.println("prendiDati cliente"+cliente.getToken());
        ClienteDatiDTO dati=ClienteService.getDatiCliente(cliente.getAccountAgente().getEmail(),cliente.getToken());
        if(dati != null){
            return dati;
        }
        Platform.runLater(AlertFactory::generateFailAlertForErroreInterno);
        return null;
    }

    public static UpdateDatiResponseDTO updateDatiCliente(Cliente cliente,Utente utentevecchio){
        UpdateDatiResponseDTO response=ClienteService.updateDatiCliente(cliente,utentevecchio);
        System.out.println(response.getDuplicato());
        System.out.println(response.getErroreInterno());
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

    public static List<GetAllRicercheResponse> getAllRicerche(Utente utente) {
        switch (utente) {
            case Cliente cliente -> {
                return ClienteService.getAllRicerche(utente);
            }
            case Agente agente -> {
                return AgenteService.getAllRicerche(utente);
            }
            case GestoreAgenziaImmobiliare gestoreAgenziaImmobiliare -> {
                return GestoreService.getAllRicerche(utente);
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }
}
