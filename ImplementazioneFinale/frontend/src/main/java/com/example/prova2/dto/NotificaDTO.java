package com.example.prova2.dto;

public class NotificaDTO {

    private int id;

    private int numeroNotificheAccettate;

    private int numeroNotificheRifiutate;


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setNumeroNotificheAccettate(int numeroNotificheAccettate) {
        this.numeroNotificheAccettate = numeroNotificheAccettate;
    }

    public int getNumeroNotificheAccettate() {
        return numeroNotificheAccettate;
    }

    public void setNumeroNotificheRifiutate(int numeroNotificheRifiutate) {
        this.numeroNotificheRifiutate = numeroNotificheRifiutate;
    }

    public int getNumeroNotificheRifiutate() {
        return numeroNotificheRifiutate;
    }
}
