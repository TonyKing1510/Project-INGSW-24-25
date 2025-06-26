package it.unina.dietiEstates25.dao;


import it.unina.dietiEstates25.dto.request.AddVisitaRequestDTO;
import it.unina.dietiEstates25.dto.response.*;

import java.util.List;

public interface VisitaDao {
    VisitaResponseDTO addVisita(AddVisitaRequestDTO visita);

    List<GetVisiteResponse> getAllVisiteAccettateByCF(String cf);

    List<Integer> getAllImmobileByCf(String cf);

    InformazioniVisitaDTO getInformazioniVisita(int idNotifica);

    List<DateEOreOccupateAgente> getDateEOreOccupateAgente(String cf);

    CheckVisitaPerClienteDTO controllaSeClienteHaGiaVisitaPerImmobile(String email, int idImmobile);

    boolean eliminaVisita(AddVisitaRequestDTO visita);
}
