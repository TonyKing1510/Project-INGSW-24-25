package com.example.prova2.service;

import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MappaService2 {


    private static final String JSON_ITEMS_KEY = "items";
    private static final String JSON_POSITION_KEY = "position";
    private static final String JSON_LAT_KEY = "lat";
    private static final String JSON_LNG_KEY = "lng";

    private static final String API_KEY = "52sWoux-2_OJfoCMrpIiKHJi7394FWEVwrGKsI4h0Nc";
    private static final String GEOCODE_URL = "https://geocode.search.hereapi.com/v1/geocode?q=%s&apiKey=%s";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    //levato url per reverseGeoCoding non andava

    private static final Logger logger = Logger.getLogger(MappaService2.class.getName());

    private WebEngine webEngine;

    public void setWebEngine(WebEngine webEngine) {
        this.webEngine = webEngine;
    }

    public void loadMap() {
        URL url = getClass().getResource("/com/example/prova2/views/shared/loadMapSearch.html");
        if (url != null) {
            webEngine.load(url.toExternalForm());
        } else {
            logger.log(Level.SEVERE, "File HTML non trovato. Percorso: /com/example/prova2/views/shared/loadMapSearch.html");
        }
    }

    // Aggiorna il marker sulla mappa con le nuove coordinate
    public void updateMarker(double lat, double lng) {
        webEngine.executeScript("placeMarker(" + lat + ", " + lng + ");");
    }

    public void addMarker(double latComune, double lngComune, double lat, double lng, String titolo, String prezzo, String descrizione, String idImmobile) {
        // Sostituisce gli apostrofi singoli con \'
        titolo = titolo.replace("'", "\\'");
        prezzo = prezzo.replace("'", "\\'");
        descrizione = descrizione.replace("'", "\\'");
        idImmobile = idImmobile.replace("'", "\\'");

        webEngine.executeScript("addMarker("
                + latComune + "," + lngComune + ","
                + lat + ", " + lng + ", '"
                + titolo + "', '" + prezzo + "', '"
                + descrizione + "', '" + idImmobile + "');");
    }

    public void addMarker2(double latComune, double lngComune, double lat, double lng){
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                // Ora possiamo eseguire lo script JavaScript
                webEngine.executeScript("addMarkerForAnnuncio(" + latComune + "," + lngComune + "," + lat + "," + lng + ");");
            }
        });
    }




    // Ottieni le coordinate dall'indirizzo
    public static String getCoordinatesFromAddress(String address) {
        try {
            String encodedAddress = java.net.URLEncoder.encode(address, StandardCharsets.UTF_8);
            String urlString = String.format(GEOCODE_URL, encodedAddress, API_KEY);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlString))
                    .GET()
                    .build();

            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.headers());
            return parseCoordinatesFromResponse(response.body());
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Parsing della risposta JSON per ottenere le coordinate
    private static String parseCoordinatesFromResponse(String jsonResponse) {
        try {
            JSONObject json = new JSONObject(jsonResponse);
            if (json.has(JSON_ITEMS_KEY) && !json.getJSONArray(JSON_ITEMS_KEY).isEmpty()) {
                JSONObject firstItem = json.getJSONArray(JSON_ITEMS_KEY).getJSONObject(0);
                JSONObject position = firstItem.getJSONObject(JSON_POSITION_KEY);
                double lat = position.getDouble(JSON_LAT_KEY);
                double lng = position.getDouble(JSON_LNG_KEY);
                return lat + "," + lng; // Restituisce le coordinate come stringa
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
