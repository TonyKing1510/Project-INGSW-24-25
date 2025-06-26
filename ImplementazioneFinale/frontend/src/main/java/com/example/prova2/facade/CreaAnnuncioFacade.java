package com.example.prova2.facade;

import com.example.prova2.dto.AddImmobileRequestDTO;
import com.example.prova2.factory.AlertFactory;
import com.example.prova2.service.ImmobileService;
import com.example.prova2.service.MappaService;
import com.example.prova2.service.S3Service;
import javafx.scene.web.WebEngine;

public class CreaAnnuncioFacade {
    private static final String ERROR_MESSAGE = "Errore";
    private static final String ERROR_MESSAGE3 = "Immobile non inserito";
    private static MappaService mappaService;
    public static boolean addAnnuncio(AddImmobileRequestDTO annuncio,String token) {
        try {
            return ImmobileService.addAnnuncio(annuncio,token);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            AlertFactory.generateFailAlert(ERROR_MESSAGE, "Operazione interrotta.");
            return false;
        } catch (Exception e) {
            AlertFactory.generateFailAlert(ERROR_MESSAGE, ERROR_MESSAGE3);
            return false;
        }
    }

    public static void initMappa(WebEngine webEngine){
        mappaService = new MappaService();
        mappaService.setWebEngine(webEngine);
        mappaService.loadMap();
    }

    public static void updateMarker(Double lat,Double lng){
        mappaService.updateMarker(lat, lng);
    }

    public static String getCoordinateFromAddress(String address){
        return MappaService.getCoordinatesFromAddress(address);
    }

    public static void uploadImage(String path,String key){
        S3Service.uploadImageWithoutBack(path,key);
    }
}
