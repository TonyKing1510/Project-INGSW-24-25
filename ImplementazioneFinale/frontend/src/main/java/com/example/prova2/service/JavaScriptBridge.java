package com.example.prova2.service;

public class JavaScriptBridge {
    // Metodo che verrà chiamato dal JavaScript
    /* non funziona
    public void sendCoordinates(double lat, double lng) {
        System.out.println("Coordinate ricevute: Latitudine: " + lat + ", Longitudine: " + lng);
    }
*/
    // Questo metodo permette di convertire l'oggetto in una forma che può essere chiamata da JavaScript
    @Override
    public String toString() {
        return "{sendCoordinates: function(lat, lng) { window.javaApp.sendCoordinates(lat, lng); }}";
    }
}
