package it.unina.dietiEstates25.dao;
import it.unina.dietiEstates25.dto.response.NotificaDTO;
import it.unina.dietiEstates25.model.Notifica;

import java.util.List;

public interface NotDao {

    List<Notifica> getNotofAdmin(String partitaIva);

    NotificaDTO setNotificaAccepted(int idNotifica);

    NotificaDTO setNotificaRejected(int idNotifica);

    List<Notifica> getNotificaAgente(String cf);

    boolean annullaInvioNotifica(int id);

    List<Notifica> getNotificaCliente(String email);
}
