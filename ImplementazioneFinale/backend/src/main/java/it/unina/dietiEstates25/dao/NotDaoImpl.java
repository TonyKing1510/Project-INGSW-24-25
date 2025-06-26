package it.unina.dietiEstates25.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.unina.dietiEstates25.db.DatabaseConnection;
import it.unina.dietiEstates25.dto.response.NotificaDTO;
import it.unina.dietiEstates25.model.CategoriaNotifica;
import it.unina.dietiEstates25.model.Notifica;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static it.unina.dietiEstates25.dao.ImmobileDaoImpl.update;


public class NotDaoImpl implements NotDao {

    private Connection connection;

    private static final Logger logger = LoggerFactory.getLogger(NotDaoImpl.class);

    private static final String ERRORE = "Si è verificato un errore nel db";

    private static final String CLIENTE = "clienteNotificato";

    private static final String AGENTE = "agenteNotificato";

    private static final String GESTORE = "gestoreNotificato";


    private static final Map<String, String> COLUMN_MAP = Map.of(
            CLIENTE, CLIENTE,
            AGENTE, AGENTE,
            GESTORE, GESTORE
    );


    public NotDaoImpl(){
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            logger.error(ERRORE, e);
        }
    }


    @Override
    public List<Notifica> getNotofAdmin(String cf) {
        return getNotifiche(GESTORE, cf);
    }

    @Override
    public List<Notifica> getNotificaAgente(String cf) {
        return getNotifiche(AGENTE, cf);
    }

    @Override
    public List<Notifica> getNotificaCliente(String email) {
        return getNotifiche(CLIENTE, email);
    }


    @Override
    public boolean annullaInvioNotifica(int id) {
        String sql = "UPDATE NOTIFICA SET accettata = null where id_notifica = ?";
        return update(id, sql, connection, logger, ERRORE);
    }


    private List<Notifica> getNotifiche(String colonna, String valore) {
        List<Notifica> notifications = new ArrayList<>();
        String colonnaSicura = COLUMN_MAP.get(colonna);
        if (colonnaSicura == null) {
            logger.error("Colonna non valida: {}", colonna);
            return notifications;
        }
        String query = "SELECT titolo, contenuto, id_notifica, categoria, id_immobile FROM notifica WHERE "
                + colonnaSicura + " = ? AND accettata IS NULL AND da_inviare = true";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, valore); // Parametro sicuro
            ResultSet resultSet = preparedStatement.executeQuery();
            aggiungiNotifiche(notifications, resultSet);
        } catch (SQLException e) {
            logger.error(ERRORE, e);
        }
        return notifications;
    }





    static boolean esiste(String campo, String sql, Connection connection) {
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                if(rs.getString(1).equals(campo)){
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }


    private void aggiungiNotifiche(List<Notifica> notifications, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String titolo = resultSet.getString("titolo");
            String contenuto = resultSet.getString("contenuto");
            int idNotifica = resultSet.getInt("id_notifica");
            String categoriaString = resultSet.getString("categoria");
            int idImmobile=resultSet.getInt("id_immobile");
            CategoriaNotifica categoriaNotifica = CategoriaNotifica.fromString(categoriaString);
            Notifica notification = new Notifica(titolo, contenuto,idNotifica,categoriaNotifica,idImmobile);
            notifications.add(notification);
        }
    }


    @Override
    public NotificaDTO setNotificaAccepted(int idNotifica) {
        return setNotificaStato(idNotifica,true);
    }

    @Override
    public NotificaDTO setNotificaRejected(int idNotifica) {
        return setNotificaStato(idNotifica,false);
    }

    private NotificaDTO setNotificaStato(int idNotifica, boolean accettata) {
        String sql = "UPDATE notifica SET accettata = ? WHERE id_notifica = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBoolean(1, accettata);
            preparedStatement.setInt(2, idNotifica);
            int nRows = preparedStatement.executeUpdate();
            if (nRows > 0) {
                NotificaDTO dto = new NotificaDTO();
                if (accettata) {
                    dto.setNumeroNotificheAccettate(nRows);
                } else {
                    dto.setNumeroNotificheRifiutate(nRows);
                }
                return dto;
            }
            return null;
        } catch (SQLException e) {
            logger.error(ERRORE, e);
            return null;
        }
    }



}
