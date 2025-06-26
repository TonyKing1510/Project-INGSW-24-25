package it.unina.dietiEstates25.service;
import it.unina.dietiEstates25.dao.NotDao;
import it.unina.dietiEstates25.dao.NotDaoImpl;
import it.unina.dietiEstates25.dto.response.NotificaDTO;
import it.unina.dietiEstates25.model.Notifica;

import java.util.List;

public class NotificationsService {

    private NotificationsService(){}

    public static List<Notifica> getNotificationOfAdminService(String cf) {
            NotDaoImpl dao = new NotDaoImpl();
            return dao.getNotofAdmin(cf);
    }

    public static NotificaDTO setNotificationAccepted(int idNotifica){
        NotDao notDao = new NotDaoImpl();
        return notDao.setNotificaAccepted(idNotifica);
    }

    public static NotificaDTO setNotificationRejected(int idNotifica){
        NotDao notDao = new NotDaoImpl();
        return notDao.setNotificaRejected(idNotifica);
    }

    public static List<Notifica> getNotificaOfAgenteService(String cf){
        NotDao notDao = new NotDaoImpl();
        return notDao.getNotificaAgente(cf);
    }

    public static List<Notifica> getNotificaOfClienteService(String email){
        NotDao notDao = new NotDaoImpl();
        return notDao.getNotificaCliente(email);
    }


    public static boolean annullaInvioNotifica(int id){
            NotDao dao = new NotDaoImpl();
            return dao.annullaInvioNotifica(id);
    }
}
