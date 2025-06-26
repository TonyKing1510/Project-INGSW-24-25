package it.unina.dietiEstates25.dao;
import it.unina.dietiEstates25.db.DatabaseConnection;
import it.unina.dietiEstates25.dto.request.AddGestoreRequestDTO;
import it.unina.dietiEstates25.dto.response.DatiDTO;
import it.unina.dietiEstates25.dto.response.GestoreAgenziaImmobiliareDatiDTO;
import it.unina.dietiEstates25.dto.response.GestoreDTO;
import it.unina.dietiEstates25.dto.response.GetAllRicercheResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static it.unina.dietiEstates25.dao.AgenteDaoImpl.getRicerche;

public class GestoreDaoImpl implements GestoreDao{
    private Connection connection;

    private static final Logger logger = LoggerFactory.getLogger(GestoreDaoImpl.class);

    private static final String ERRORE = "Si è verificato un errore nel db";

    public GestoreDaoImpl() throws SQLException{
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            logger.error(ERRORE, e);
        }
    }

    @Override
    public List<String> getAllCF()  {
        List<String> cf = new ArrayList<>();
        String sql = "SELECT cf FROM GestoreAgenziaImmobiliare g union SELECT cf from agente UNION select email from cliente";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cf.add(rs.getString("cf"));
            }
        } catch (SQLException e) {
            return new ArrayList<>();
        }
        return cf;
    }

    @Override
    public List<String> getAllEmail() {
        List<String> emails = new ArrayList<>();
        String sql = "SELECT email FROM GestoreAgenziaImmobiliare g union select email from agente union select email from cliente";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                emails.add(rs.getString("email"));
            }
        } catch (SQLException e) {
            return new ArrayList<>();
        }
        return emails;
    }


    @Override
    public GestoreDTO addGestore(AddGestoreRequestDTO g) {
        if(isCFGiaReg(g.getCf())){
            return new GestoreDTO(true,false,false);
        }
        if(isEmailGiaReg(g.getEmail())){
            return new GestoreDTO(false,true,false);
        }
        String sql = """
        INSERT INTO GestoreAgenziaImmobiliare
        (nome, cognome, cf, telefono, password, email, admin, gestoricreati, nomeagenzia)
        VALUES
        (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;
        try( PreparedStatement ps = connection.prepareStatement(sql)) {
            AdminDaoImpl.setSql(ps, g.getNome(), g.getCognome(), g.getCf(), g.getTelefono(), g.getPassword(), g.getEmail());
            ps.setBoolean(7,false);
            ps.setString(8,g.getCfGestoreAdmin());
            ps.setString(9,g.getNomeAgenzia());
            int flag=ps.executeUpdate();
            if(flag > 0){
                return new GestoreDTO(false,false,false);
            }
        }
        catch (SQLException e) {
            logger.error(ERRORE, e);
        }
        return new GestoreDTO(false,false,true);
    }

    private boolean isCFGiaReg(String cf){
        List<String> list = getAllCF();
        for (String s:list){
            if(s.equals(cf)){
                return true;
            }
        }
        return false;
    }

    private boolean isEmailGiaReg(String email){
        List<String> list = getAllEmail();
        for (String s:list){
            if(s.equals(email)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String getSecurityPassword(String email) {
        String sql = """
             SELECT password
             FROM (
             SELECT email, password FROM agente
             UNION
             SELECT email, password FROM gestoreagenziaimmobiliare
             UNION
             SELECT email,password from cliente
              ) AS subquery
             WHERE email = ?
           """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            }
            return null;
        } catch (SQLException e) {
            logger.error(ERRORE, e);
            return null;
        }
    }


    @Override
    public GestoreAgenziaImmobiliareDatiDTO getGestoreByEmail(String email) {
        String sql = "SELECT nome, cognome, cf, telefono, email FROM gestoreagenziaimmobiliare WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                GestoreAgenziaImmobiliareDatiDTO dto = new GestoreAgenziaImmobiliareDatiDTO();
                dto.setNome((rs.getString("nome")));
                dto.setCognome(rs.getString("cognome"));
                dto.setCf(rs.getString("cf"));
                dto.setTelefono(rs.getString("telefono"));
                dto.setEmail(rs.getString("email"));
                return dto;
            }else{
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public List<GetAllRicercheResponse> getAllRicerche(String cf) {
        String sql = "select id_ricerca,gestorechecerca,n°stanze,classe_energetica,prezzo,comune,città,tipovendita,prezzo_max from ricerca r where r.gestorechecerca=?";
        return getGetAllRicercheResponses(cf, sql, connection);
    }

    @Override
    public List<DatiDTO> getAllAccountAgenti(String cf) {
        String sql = "select nome,cognome,email from agente where gestoreriferimento = ?";
        return getAccount(cf, sql);
    }

    @Override
    public List<DatiDTO> getAllAccountGestori(String cf) {
        String sql = "select nome,cognome,email from gestoreagenziaimmobiliare where gestoricreati = ?";
        return getAccount(cf, sql);
    }

    @Override
    public boolean deleteGestoreByEmail(String email) {
        String sql = "delete from gestoreagenziaimmobiliare where email = ?";
        return delete(email, sql);
    }

    private boolean delete(String email, String sql) {
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, email);
            int r=ps.executeUpdate();
            return r > 0;
        }catch (SQLException e) {
            logger.error(ERRORE, e);
        }
        return false;
    }

    @Override
    public boolean deleteAgenteByEmail(String email) {
        String sql = "delete from agente where email = ?";
        return delete(email, sql);
    }

    private List<DatiDTO> getAccount(String cf, String sql) {
        List<DatiDTO> accountRecuperati = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,cf);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DatiDTO dto = new DatiDTO();
                dto.setNome(rs.getString("nome"));
                dto.setCognome(rs.getString("cognome"));
                dto.setEmail(rs.getString("email"));
                accountRecuperati.add(dto);
            }
            return accountRecuperati;
        }catch (SQLException e) {
            logger.error(ERRORE, e);
        }
        return accountRecuperati;
    }

    static List<GetAllRicercheResponse> getGetAllRicercheResponses(String cf, String sql, Connection connection) {
        List<GetAllRicercheResponse> ricerche = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            getRicerche(cf, ricerche, ps);
        }catch (SQLException e) {
            logger.error(ERRORE, e);
            return Collections.emptyList();
        }
        return ricerche;
    }


}
