package com.example.prova2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GestoreAgenziaImmobiliareDatiDTO {

    @JsonProperty("token")
    private String token;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("cognome")
    private String cognome;

    @JsonProperty("telefono")
    private String telefono;

    @JsonProperty("email")
    private String email;

    @JsonProperty("partitaIva")
    private String partitaIva;

    @JsonProperty("username")
    private String username;

    @JsonProperty("cf")
    private String cf;

    public String getCf() {
        return cf;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getNome() {
        return nome;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getPartitaIva() {
        return partitaIva;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCognome() {
        return cognome;
    }

    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
