package it.unina.dietiEstates25.dao;
import it.unina.dietiEstates25.db.DatabaseConnection;
import it.unina.dietiEstates25.dto.request.UpdateDatiRequestDTO;
import it.unina.dietiEstates25.dto.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static it.unina.dietiEstates25.dao.NotDaoImpl.esiste;

public class UserDaoImpl implements UserDao {
    private Connection connection;

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    private static final String ERRORE = "Si è verificato un errore nel db";

    private static final String ADMIN="admin";

    public UserDaoImpl() throws SQLException{
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            logger.error(ERRORE, e);
        }
    }


    @Override
    public UpdateDatiResponseDTO updateDatiAgente(UpdateDatiRequestDTO datiAgenteNuovi, String email) {
        String sql = "UPDATE AGENTE SET nome=?,cognome=?,telefono=?,email=? where email = ?";
        return aggiornaDati(datiAgenteNuovi,email,sql);
    }

    @Override
    public UpdateDatiResponseDTO updateDatiCliente(UpdateDatiRequestDTO datiNuovi, String emailAttuale) {
        String sql = "UPDATE CLIENTE SET nome=?,cognome=?,telefono=?,email=? where email = ?";
        return aggiornaDati(datiNuovi,emailAttuale,sql);
    }

    @Override
    public UpdateDatiResponseDTO updateDatiGestore(UpdateDatiRequestDTO datiNuovi, String emailAttuale) {
        String sql = "UPDATE GESTOREAGENZIAIMMOBILIARE SET nome=?,cognome=?,telefono=?,email=? where email = ?";
        return aggiornaDati(datiNuovi,emailAttuale,sql);
    }

    @Override
    public AgenteDTO add(String email, String password) throws SQLException {
        String sql = """
        
                SELECT email, ruolo, admin
                        FROM (
                            SELECT email, ruolo, false AS admin FROM cliente
                            UNION
                            SELECT email, ruolo, admin FROM gestoreagenziaimmobiliare
                            UNION
                            SELECT email, ruolo, false AS admin FROM agente
                        ) AS combined
                        WHERE email = ?;
                        
        """;
        String aggiungiUtente = "INSERT INTO cliente (email, password) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (utenteEsiste(rs)) {
                AgenteDTO utente = new AgenteDTO(false, false);
                utente.setRole(rs.getString("ruolo"));
                utente.setEmail(email);
                utente.setAdmin(rs.getBoolean(ADMIN));
                return utente;
            } else {
                return aggiungiUtente(email, password, aggiungiUtente);
            }
        } catch (SQLException e) {
            logger.error(ERRORE, e);
        }
        return null;
    }

    @Override
    public LoginUtenteResponse login(String email, String password){
        String sql = """
                SELECT email, ruolo, admin
                        FROM (
                            SELECT email, ruolo, false AS admin FROM cliente
                                    UNION
                            SELECT email, ruolo, admin FROM gestoreagenziaimmobiliare
                                    UNION
                            SELECT email, ruolo, false AS admin FROM agente
                        ) AS combined
                        WHERE email = ?
                        
        """;
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (utenteEsiste(rs)) {
                LoginUtenteResponse utente = new LoginUtenteResponse();
                utente.setRole(rs.getString("ruolo"));
                utente.setAdmin(rs.getBoolean(ADMIN));
                return utente;
            }
        }catch (SQLException e) {
            logger.error(ERRORE, e);
        }
        return null;

    }

    @Override
    public String getPassword(String email) throws SQLException {
        String sql = """
                SELECT password
                        FROM (
                            SELECT email,password FROM cliente
                                    UNION
                            SELECT email,password FROM gestoreagenziaimmobiliare
                                    UNION
                            SELECT email,password FROM agente
                        ) AS combined
                        WHERE email = ?;
                        
        """;
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (utenteEsiste(rs)) {
                return rs.getString("password");
            }
        }catch (SQLException e) {

            logger.error(ERRORE, e);
        }
        return "";
    }

    private AgenteDTO aggiungiUtente(String email, String password, String aggiungiUtente) throws SQLException {
        try (PreparedStatement insertPs = connection.prepareStatement(aggiungiUtente)) {
            insertPs.setString(1, email);
            insertPs.setString(2, password);
            insertPs.executeUpdate();
            AgenteDTO utente = new AgenteDTO(false, false);
            utente.setRole("cliente");
            utente.setEmail(email);
            return utente;
        }
    }

    private static boolean utenteEsiste(ResultSet rs) throws SQLException {
        return rs.next();
    }

    public UpdateDatiResponseDTO aggiornaDati(UpdateDatiRequestDTO datiNuovi, String identificatore, String sql) {
        if (isEmailGiaReg(datiNuovi.getEmail(),identificatore)) {
            return new UpdateDatiResponseDTO(true, true);
        }
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, datiNuovi.getNome());
            ps.setString(2, datiNuovi.getCognome());
            ps.setString(3, datiNuovi.getTelefono());
            ps.setString(4, datiNuovi.getEmail());
            ps.setString(5, identificatore);
            int nRows = ps.executeUpdate();
            System.out.println(nRows);
            return new UpdateDatiResponseDTO(false, nRows <= 0);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(ERRORE, e);
            return new UpdateDatiResponseDTO(false, true);
        }
    }




    static DatiDTO getDatiDTO(String cf, String sql, Connection connection) {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cf);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DatiDTO(
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("email"),
                            rs.getString("telefono"),
                            rs.getString("Bio"),
                            rs.getString("cf")
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            return null;
        }
    }


    private boolean isEmailGiaReg(String email,String emailVecchia) {
        if(email.equals(emailVecchia)){
            return false;
        }
        String sql = "select email from gestoreagenziaimmobiliare union select email from cliente union select email from agente";
        return esisteCampo(email, sql);
    }


    private boolean esisteCampo(String campo, String sql) {
        return esiste(campo, sql, connection);
    }


}
