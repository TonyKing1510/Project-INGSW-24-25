package com.example.prova2.dto;

public class AgenteCreazioneDTO {
    private boolean isEmailRegistrata;

    private boolean isCfRegistrato;

    private  boolean errore;
    public AgenteCreazioneDTO(boolean email,boolean cf){
        this.isEmailRegistrata = email;this.isCfRegistrato =cf;
    }

    public void setErrore(boolean errore) {
        this.errore = errore;
    }

    public boolean isErrore() {
        return errore;
    }



    public AgenteCreazioneDTO(boolean errore){
        this.errore = errore;
    }

    public AgenteCreazioneDTO(){}

    public boolean isCfRegistrato() {
        return isCfRegistrato;
    }

    public boolean isEmailRegistrata() {
        return isEmailRegistrata;
    }

    public void setCfRegistrato(boolean cfRegistrato) {
        isCfRegistrato = cfRegistrato;
    }

    public void setEmailRegistrata(boolean emailRegistrata) {
        isEmailRegistrata = emailRegistrata;
    }

}
