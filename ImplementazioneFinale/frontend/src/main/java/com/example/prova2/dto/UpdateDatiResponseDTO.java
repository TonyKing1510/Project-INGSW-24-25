package com.example.prova2.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateDatiResponseDTO {

    public UpdateDatiResponseDTO() {}

    private boolean duplicato;

    private boolean erroreInterno;

    public UpdateDatiResponseDTO(boolean emailDuplicate, boolean error) {
        this.duplicato = emailDuplicate;
        this.erroreInterno = error;
    }


    public boolean getDuplicato() {
        return duplicato;
    }

    public void setDuplicato(boolean duplicato) {
        this.duplicato = duplicato;
    }

    public boolean getErroreInterno() {
        return erroreInterno;
    }

    public void setErroreInterno(boolean erroreInterno) {
        this.erroreInterno = erroreInterno;
    }
}
