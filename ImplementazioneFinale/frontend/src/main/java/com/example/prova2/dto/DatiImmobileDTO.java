package com.example.prova2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DatiImmobileDTO {
    private String indirizzo;

    private String comune;

    private String città;

    private String agente;

    private String numeroCivico;

    private String via;

    public String getNumeroCivico() {
        return numeroCivico;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getComune() {
        return comune;
    }

    public String getAgente() {
        return agente;
    }

    public String getCittà() {
        return città;
    }

    public DatiImmobileDTO() {}

    public DatiImmobileDTO(String indirizzo, String comune, String agente, String città, String numeroCivico) {
        this.indirizzo = indirizzo;
        this.comune = comune;
        this.agente = agente;
        this.città=città;
        this.numeroCivico = numeroCivico;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setNumeroCivico(String numeroCivico) {
        this.numeroCivico = numeroCivico;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }
}
