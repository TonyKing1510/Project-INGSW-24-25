package it.unina.dietiEstates25.dao;
import it.unina.dietiEstates25.db.DatabaseConnection;
import it.unina.dietiEstates25.dto.request.AddAgenteRequestDTO;
import it.unina.dietiEstates25.dto.request.UpdatePasswordRequestDTO;
import it.unina.dietiEstates25.dto.response.AgenteCreazioneDTO;
import it.unina.dietiEstates25.dto.response.DatiDTO;
import it.unina.dietiEstates25.dto.response.GetAllRicercheResponse;
import it.unina.dietiEstates25.model.ClasseEnergetica;
import it.unina.dietiEstates25.model.TipoVendita;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static it.unina.dietiEstates25.dao.GestoreDaoImpl.getGetAllRicercheResponses;
import static it.unina.dietiEstates25.dao.NotDaoImpl.esiste;
import static it.unina.dietiEstates25.dao.UserDaoImpl.getDatiDTO;

public class AgenteDaoImpl implements AgenteDao {
    private Connection connection;

    private static final Logger logger = LoggerFactory.getLogger(AgenteDaoImpl.class);

    private static final String ERRORE = "Si è verificato un errore nel db";


    public AgenteDaoImpl() throws SQLException {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            logger.error(ERRORE, e);
        }
    }

    public AgenteCreazioneDTO addAgente(AddAgenteRequestDTO agente){
        if(isEmailGiaReg(agente.getEmail())){
           return new AgenteCreazioneDTO(true,false);
        }
        if(isCFGiaReg(agente.getCf())){
            return new AgenteCreazioneDTO(false,true);
        }
        String sql = "insert into Agente (cf,nome,cognome,telefono,password,email,gestoreriferimento)" +
                     "values (?,?,?,?,?,?,?)";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, agente.getCf());
            ps.setString(2, agente.getNome());
            ps.setString(3, agente.getCognome());
            ps.setString(4, agente.getTelefono());
            ps.setString(5,agente.getPassword());
            ps.setString(6,agente.getEmail());
            ps.setString(7,agente.getCfGestore());
            int rRowsChanged=ps.executeUpdate();
            if(rRowsChanged>0){
                return new AgenteCreazioneDTO(false);
            }
        }catch (SQLException e) {
            logger.error(ERRORE, e);
        }
        return new AgenteCreazioneDTO(true);
    }

    private boolean isEmailGiaReg(String email) {
        String sql = "select email from gestoreagenziaimmobiliare union select email from cliente union select email from agente";
        return esisteCampo(email, sql);
    }

    private boolean isCFGiaReg(String cf) {
        String sql = "select cf from gestoreagenziaimmobiliare UNION select cf from agente";
        return esisteCampo(cf, sql);
    }

    private boolean esisteCampo(String campo, String sql) {
        return esiste(campo, sql, connection);
    }

    public DatiDTO getAgenteByEmail(String email) {
        String sql = "SELECT nome, cognome, telefono, email,password, \"Bio\",cf FROM agente WHERE email = ?";
        return getAgenteDatiDTO(email, sql);
    }

    private DatiDTO getAgenteDatiDTO(String cf, String sql) {
        return getDatiDTO(cf, sql, connection);
    }

    public boolean updateBiografia(String newBio,String cf){
        String sql = "UPDATE AGENTE SET \"Bio\" = ? where cf = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, newBio);
            ps.setString(2, cf);
            int rRowsChanged=ps.executeUpdate();
            return rRowsChanged > 0;
        }catch (SQLException e) {
            logger.error(ERRORE, e);
            return false;
        }
    }


    @Override
    public Integer getValutazionebyEmail(String email) {
        String sql = "select valutazione from agente a where email = ?";
        return getInteger(email, sql);
    }

    private Integer getInteger(String email, String sql) {
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getInt(1);
            }
            return null;
        }catch (SQLException e){
            logger.error(ERRORE, e);
            return null;
        }
    }

    public Integer getValutazione(String cf) {
        String sql = "select valutazione from agente a where cf = ?";
        return getInteger(cf, sql);
    }


    @Override
    public List<GetAllRicercheResponse> getAllRicerche(String cf) {
        String sql = "select id_ricerca,agentechecerca,n°stanze,classe_energetica,prezzo,comune,città,tipovendita,prezzo_max from ricerca r where r.agentechecerca=?";
        return getGetAllRicercheResponses(cf, sql, connection);
    }

    static void getRicerche(String cf, List<GetAllRicercheResponse> ricerche, PreparedStatement ps) throws SQLException {
        ps.setString(1,cf);
        ResultSet rs=ps.executeQuery();
        while(rs.next()){
            Double prezzo = rs.getDouble(5);
            if (rs.wasNull()) {
                prezzo = null;
            }
            Double prezzoMassimo = rs.getDouble(9);
            if (rs.wasNull()) {
                prezzoMassimo = null;
            }
            Integer numeroStanze = rs.getInt(3);
            if(rs.wasNull()){
                numeroStanze = null;
            }
            GetAllRicercheResponse re= new GetAllRicercheResponse.Builder()
                    .idRicerca(rs.getInt(1))
                    .cliente(rs.getString(2))
                    .numeroStanze(numeroStanze)
                    .classeEnergetica(ClasseEnergetica.fromString(rs.getString(4)))
                    .prezzo(prezzo)
                    .comune(rs.getString(6))
                    .citta(rs.getString(7))
                    .tipovendita(TipoVendita.fromString(rs.getString(8)))
                    .prezzoMassimo(prezzoMassimo)
                    .build();
            ricerche.add(re);
        }
    }


    public boolean updatePassword(UpdatePasswordRequestDTO request) {
        String sql = "Update agente set password=? where email=?";
        return updatePassDB(request, sql, connection);
    }

    static boolean updatePassDB(UpdatePasswordRequestDTO request, String sql, Connection connection) {
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1,request.getPassword());
            ps.setString(2, request.getEmail());
            int rows=ps.executeUpdate();
            return rows > 0;
        }
        catch (SQLException e) {
            logger.error("Si è verificato un errore: nel db ", e);
            return false;
        }
    }




}
