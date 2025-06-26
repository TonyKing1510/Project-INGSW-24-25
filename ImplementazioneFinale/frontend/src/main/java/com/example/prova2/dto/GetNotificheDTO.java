package com.example.prova2.dto;

import com.example.prova2.model.CategoriaNotifica;

public class GetNotificheDTO {

        private int idImmobile;

        private int idNotifica;


        private String nomeNotifica;


        private CategoriaNotifica categoria;

        private String descrizioneNotifica;

    public String getDescrizioneNotifica() {
        return descrizioneNotifica;
    }

    public void setDescrizioneNotifica(String descrizioneNotifica) {
        this.descrizioneNotifica = descrizioneNotifica;
    }

    public CategoriaNotifica getCategoria() {
            return categoria;
        }

        public void setCategoria(CategoriaNotifica categoria) {
            this.categoria = categoria;
        }

    public String getNomeNotifica() {
        return nomeNotifica;
    }

    public void setNomeNotifica(String nomeNotifica) {
        this.nomeNotifica = nomeNotifica;
    }

    public GetNotificheDTO(){}

    public GetNotificheDTO(int idNotifica){
            this.idNotifica = idNotifica;
    }

        public int getIdImmobile() {
            return idImmobile;
        }

        public void setIdImmobile(int idImmobile) {
            this.idImmobile = idImmobile;
        }

        public int getIdNotifica() {
            return idNotifica;
        }

        public void setIdNotifica(int idNotifica) {
            this.idNotifica = idNotifica;
        }

}
