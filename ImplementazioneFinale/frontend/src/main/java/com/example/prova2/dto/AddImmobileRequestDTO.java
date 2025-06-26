package com.example.prova2.dto;

import com.example.prova2.model.Arredamento;
import com.example.prova2.model.ClasseEnergetica;
import com.example.prova2.model.TipoImmobile;
import com.example.prova2.model.TipoVendita;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class AddImmobileRequestDTO {

        private int idImmobile;
        private TipoImmobile tipoimmobile;
        private TipoVendita tipovendita;
        private String descrizione;
        private String via;
        private int numeroStanze;
        private int piano;
        private ClasseEnergetica classeEnergetica;

        private int superficie;
        private Arredamento arredamento;
        private BigDecimal prezzo;
        private BigDecimal speseCondominiali;
        private int numeroBagni;
        private int numeroCucine;
        private int numeroSoggiorni;
        private String cfAgente;
        private double latitudine;
        private double longitudine;
        private String numeroCivico;
        private String comune;
        private String citta;
        private String titolo;
        private List<String> uriFotoImmobile;
        private String nomeAgente;
        private String cognomeAgente;
        private  boolean ascensore;

    public boolean isAscensore() {
        return ascensore;
    }

    public void setAscensore(boolean ascensore) {
        this.ascensore = ascensore;
    }

    public AddImmobileRequestDTO(){}

        public void setNomeAgente(String nomeAgente) {
            this.nomeAgente = nomeAgente;
        }

        public String getNomeAgente() {
            return nomeAgente;
        }

        public String getCognomeAgente() {
            return cognomeAgente;
        }

        public String formattaStringa(String input){
            long number = Long.parseLong(input);
            DecimalFormat formatter = new DecimalFormat("#,###");
            return formatter.format(number);
        }

        public void setCognomeAgente(String cognomeAgente) {
            this.cognomeAgente = cognomeAgente;
        }

        private AddImmobileRequestDTO(com.example.prova2.dto.AddImmobileRequestDTO.Builder builder) {
            this.idImmobile = builder.idImmobile;
            this.tipoimmobile = builder.tipoimmobile;
            this.tipovendita = builder.tipovendita;
            this.descrizione = builder.descrizione;
            this.via = builder.via;
            this.numeroStanze = builder.numeroStanze;
            this.piano = builder.piano;
            this.classeEnergetica = builder.classeEnergetica;
            this.superficie = builder.superficie;
            this.arredamento = builder.arredamento;
            this.prezzo = builder.prezzo;
            this.speseCondominiali = builder.speseCondominiali;
            this.numeroBagni = builder.numeroBagni;
            this.numeroCucine = builder.numeroCucine;
            this.numeroSoggiorni = builder.numeroSoggiorni;
            this.cfAgente = builder.cfAgente;
            this.latitudine = builder.latitudine;
            this.longitudine = builder.longitudine;
            this.numeroCivico = builder.numeroCivico;
            this.comune = builder.comune;
            this.citta = builder.citta;
            this.titolo = builder.titolo;
            this.nomeAgente= builder.nomeAgente;
            this.cognomeAgente= builder.cognomeAgente;
        }

        public static class Builder {
            private int idImmobile;
            private TipoImmobile tipoimmobile;
            private TipoVendita tipovendita;
            private String descrizione;
            private String via;
            private int numeroStanze;
            private int piano;
            private ClasseEnergetica classeEnergetica;
            private int superficie;
            private Arredamento arredamento;
            private BigDecimal prezzo;
            private BigDecimal speseCondominiali;
            private int numeroBagni;
            private int numeroCucine;
            private int numeroSoggiorni;
            private String cfAgente;
            private double latitudine;
            private double longitudine;
            private String numeroCivico;
            private String comune;
            private String citta;
            private String titolo;
            private String cognomeAgente;
            private String nomeAgente;

            public String getNomeAgente() {
                return nomeAgente;
            }

            public void setNomeAgente(String nomeAgente) {
                this.nomeAgente = nomeAgente;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder idImmobile(int idImmobile) {
                this.idImmobile = idImmobile;
                return this;
            }


            public com.example.prova2.dto.AddImmobileRequestDTO.Builder nomeAgente(String nomeAgente) {
                this.nomeAgente = nomeAgente;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder cognomeAgente(String cognomeAgente) {
                this.cognomeAgente = cognomeAgente;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder tipoimmobile(TipoImmobile tipoimmobile) {
                this.tipoimmobile = tipoimmobile;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder tipovendita(TipoVendita tipovendita) {
                this.tipovendita = tipovendita;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder descrizione(String descrizione) {
                this.descrizione = descrizione;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder via(String via) {
                this.via = via;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder numeroStanze(int numeroStanze) {
                this.numeroStanze = numeroStanze;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder piano(int piano) {
                this.piano = piano;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder classeEnergetica(ClasseEnergetica classeEnergetica) {
                this.classeEnergetica = classeEnergetica;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder superficie(int superficie) {
                this.superficie = superficie;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder arredamento(Arredamento arredamento) {
                this.arredamento = arredamento;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder prezzo(BigDecimal prezzo) {
                this.prezzo = prezzo;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder speseCondominiali(BigDecimal speseCondominiali) {
                this.speseCondominiali = speseCondominiali;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder numeroBagni(int numeroBagni) {
                this.numeroBagni = numeroBagni;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder numeroCucine(int numeroCucine) {
                this.numeroCucine = numeroCucine;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder numeroSoggiorni(int numeroSoggiorni) {
                this.numeroSoggiorni = numeroSoggiorni;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder cfAgente(String cfAgente) {
                this.cfAgente = cfAgente;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder latitudine(double latitudine) {
                this.latitudine = latitudine;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder longitudine(double longitudine) {
                this.longitudine = longitudine;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder numeroCivico(String numeroCivico) {
                this.numeroCivico = numeroCivico;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder comune(String comune) {
                this.comune = comune;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder citta(String citta) {
                this.citta = citta;
                return this;
            }

            public com.example.prova2.dto.AddImmobileRequestDTO.Builder titolo(String titolo) {
                this.titolo = titolo;
                return this;
            }

            public AddImmobileRequestDTO build() {
                return new com.example.prova2.dto.AddImmobileRequestDTO(this);
            }
        }


        public int getIdImmobile() {
            return idImmobile;
        }

        public List<String> getUriFotoImmobile() {
            return uriFotoImmobile;
        }

        public void setIdImmobile(int idImmobile) {
            this.idImmobile = idImmobile;
        }

        public void setUriFotoImmobile(List<String> uriFotoImmobile) {
            this.uriFotoImmobile = uriFotoImmobile;
        }




        public  String getNumeroCivico() {
            return numeroCivico;
        }

        public void setNumeroCivico( String numeroCivico) {
            this.numeroCivico = numeroCivico;
        }



        public String getComune() {
            return comune;
        }

        public void setComune(String comune) {
            this.comune = comune;
        }

        public  String getCitta() {
            return citta;
        }

        public void setCitta(String citta) {
            this.citta = citta;
        }

        public TipoImmobile getTipoimmobile() {
            return tipoimmobile;
        }

        public int getPiano() {
            return piano;
        }

        public Arredamento getArredamento() {
            return arredamento;
        }

        public int getNumeroStanze() {
            return numeroStanze;
        }

        public BigDecimal getSpeseCondominiali() {
            return speseCondominiali;
        }

        public int getSuperficie() {
            return superficie;
        }

        public ClasseEnergetica getClasseEnergetica() {
            return classeEnergetica;
        }

        public BigDecimal getPrezzo() {
            return prezzo;
        }

        public String getDescrizione() {
            return descrizione;
        }

        public int getNumeroBagni() {
            return numeroBagni;
        }

        public String getVia() {
            return via;
        }

        public TipoVendita getTipovendita() {
            return tipovendita;
        }

        public int getNumeroSoggiorni() {
            return numeroSoggiorni;
        }

        public String getTitolo() {
            return titolo;
        }

        public int getNumeroCucine() {
            return numeroCucine;
        }

        public double getLatitudine() {
            return latitudine;
        }

        public double getLongitudine() {
            return longitudine;
        }

        public String getCfAgente() {
            return cfAgente;
        }

        public void setClasseEnergetica(ClasseEnergetica classeEnergetica) {
            this.classeEnergetica = classeEnergetica;
        }

        public void setCfAgente(String cfAgente) {
            this.cfAgente = cfAgente;
        }

        public void setNumeroSoggiorni(int numeroSoggiorni) {
            this.numeroSoggiorni = numeroSoggiorni;
        }

        public void setPrezzo(BigDecimal prezzo) {
            this.prezzo = prezzo;
        }

        public void setArredamento(Arredamento arredamento) {
            this.arredamento = arredamento;
        }

        public void setSpeseCondominiali(BigDecimal speseCondominiali) {
            this.speseCondominiali = speseCondominiali;
        }

        public void setSuperficie(int superficie) {
            this.superficie = superficie;
        }

        public void setPiano(int piano) {
            this.piano = piano;
        }

        public void setNumeroStanze(int numeroStanze) {
            this.numeroStanze = numeroStanze;
        }

        public void setNumeroBagni(int numeroBagni) {
            this.numeroBagni = numeroBagni;
        }

        public void setNumeroCucine(int numeroCucine) {
            this.numeroCucine = numeroCucine;
        }

        public void setDescrizione(String descrizione) {
            this.descrizione = descrizione;
        }

        public void setVia(String via) {
            this.via = via;
        }

        public void setTipoimmobile(TipoImmobile tipoimmobile) {
            this.tipoimmobile = tipoimmobile;
        }

        public void setTipovendita(TipoVendita tipovendita) {
            this.tipovendita = tipovendita;
        }

        public void setTitolo(String titolo) {
            this.titolo = titolo;
        }

        public void setLongitudine(double longitudine) {
            this.longitudine = longitudine;
        }

        public void setLatitudine(double latitudine) {
            this.latitudine = latitudine;
        }

    }
