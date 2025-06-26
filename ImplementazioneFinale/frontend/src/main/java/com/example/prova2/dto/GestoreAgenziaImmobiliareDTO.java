package com.example.prova2.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GestoreAgenziaImmobiliareDTO {
    @JsonProperty("additionalInfo")
    private boolean additionalInfo;

    @JsonProperty("credSbagliate")
    private boolean credSbagliate;

    @JsonProperty("token")
    private String token;

    @JsonProperty("cf")
    private String cf;

    @JsonProperty("agenzia")
    private String agenzia;



    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public void setAgenzia(String agenzia) {
        this.agenzia = agenzia;
    }

    public String getAgenzia() {
        return agenzia;
    }

    public GestoreAgenziaImmobiliareDTO(){}


    // Getter e Setter
    public boolean isAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(boolean additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public boolean isCredSbagliate() {
        return credSbagliate;
    }

    public void setCredSbagliate(boolean credSbagliate) {
        this.credSbagliate = credSbagliate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}