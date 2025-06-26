package com.example.prova2.controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.example.prova2.controller.dashBoard.DashboardAgenteController;
import com.example.prova2.dto.GetVisiteResponse;
import com.example.prova2.facade.VisitaServiceFacade;

import java.time.LocalDateTime;
import java.util.List;

public class CalendarioAgenteController {
    public static void initEventiAgente(Calendar calendar) {
        List<GetVisiteResponse> visite = VisitaServiceFacade.getVisiteOfAgente(DashboardAgenteController.getAgente().getCf(),DashboardAgenteController.getAgente().getToken());
        if (!visite.isEmpty())
            for (GetVisiteResponse visita : visite) {
                Entry<String> visitaCalendario = new Entry<>(visita.getDescrizione());
                LocalDateTime start = LocalDateTime.of(
                        visita.getDataVisita().getYear(),
                        visita.getDataVisita().getMonth(),
                        visita.getDataVisita().getDayOfMonth(),
                        visita.getHoraInizioVisita().getHour(),
                        visita.getHoraInizioVisita().getMinute()
                );
                LocalDateTime end = LocalDateTime.of(
                        visita.getDataVisita().getYear(),
                        visita.getDataVisita().getMonth(),
                        visita.getDataVisita().getDayOfMonth(),
                        visita.getHoraFineVisita().getHour(),
                        visita.getHoraFineVisita().getMinute()
                );
                visitaCalendario.setInterval(start, end); // Imposta l'intervallo con LocalDateTime
                calendar.addEntry(visitaCalendario);
            }

    }
}
