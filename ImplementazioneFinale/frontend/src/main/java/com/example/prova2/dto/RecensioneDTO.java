package com.example.prova2.dto;

public class RecensioneDTO {
    private String agenteDaRecensire;
    private int valutazione;

    // Costruttore
    public RecensioneDTO(String agenteDaRecensire, int valutazione) {
        this.agenteDaRecensire = agenteDaRecensire;
        this.valutazione = valutazione;
    }

    // Getter e Setter
    public String getAgenteDaRecensire() {
        return agenteDaRecensire;
    }

    public void setAgenteDaRecensire(String agenteDaRecensire) {
        this.agenteDaRecensire = agenteDaRecensire;
    }

    public int getValutazione() {
        return valutazione;
    }

    public void setValutazione(int valutazione) {
        this.valutazione = valutazione;
    }



}
