package it.unina.dietiEstates25.dao;
import it.unina.dietiEstates25.db.DatabaseConnection;
import it.unina.dietiEstates25.dto.request.AddClienteRequestDTO;
import it.unina.dietiEstates25.dto.request.LasciaRecensioneRequestDTO;
import it.unina.dietiEstates25.dto.request.UpdatePasswordRequestDTO;
import it.unina.dietiEstates25.dto.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static it.unina.dietiEstates25.dao.AgenteDaoImpl.updatePassDB;
public class ClienteDaoImpl implements ClienteDao {

    private Connection connection;

    private static final Logger logger = LoggerFactory.getLogger(ClienteDaoImpl.class);

    private static final String ERRORE = "Si è verificato un errore nel db";

    public ClienteDaoImpl() throws SQLException {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            logger.error(ERRORE, e);
        }
    }


    @Override
    public ClienteDTO insert(AddClienteRequestDTO cliente) {
        if(exists(cliente)) {
            return new ClienteDTO(true);
        }
        cliente.setTelefono("1234567890");
        String sql = "INSERT INTO cliente (nome,cognome,telefono,password,email) VALUES (?,?,?,?,?)";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCognome());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4,cliente.getPassword());
            ps.setString(5,cliente.getEmail());
            int nRows=ps.executeUpdate();
            if(nRows > 0) {
                return new ClienteDTO(false);
            }
            ClienteDTO clienteDTO = new ClienteDTO();
            clienteDTO.setErroreInterno(true);
            return clienteDTO;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(ERRORE, e);
            ClienteDTO clienteDTO = new ClienteDTO();
            clienteDTO.setErroreInterno(true);
            return clienteDTO;
        }
    }

    @Override
    public boolean exists(AddClienteRequestDTO cliente) {
        String sql = "SELECT 1 FROM (SELECT email FROM cliente UNION SELECT email FROM agente UNION SELECT email from gestoreagenziaimmobiliare) WHERE email=?";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1,cliente.getEmail());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public ClienteDatiDTO getClienteByEmail(String email) {
        String sql = "SELECT nome, cognome, telefono, email,password FROM cliente WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ClienteDatiDTO(
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("email"),
                            rs.getString("telefono")
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            return null;
        }
    }



    @Override
    public boolean lasciaRecensione(LasciaRecensioneRequestDTO recensione) {
        String sql = "UPDATE AGENTE SET VALUTAZIONE = ? WHERE cf =?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, recensione.getValutazione());
            ps.setString(2,recensione.getAgenteDaRecensire());
            int nRows=ps.executeUpdate();
            return nRows > 0;
        }catch (SQLException e) {
            logger.error(ERRORE, e);
        }
        return false;
    }

    @Override
    public List<GetAllRicercheResponse> getAllRicerche(String email) {
        String sql = "select id_ricerca,clientechecerca,n°stanze,classe_energetica,prezzo,comune,città,tipovendita,prezzo_max from ricerca where clientechecerca=? LIMIT 5";
        List<GetAllRicercheResponse> listaRicerche = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            AgenteDaoImpl.getRicerche(email, listaRicerche, ps);
            return listaRicerche;
        }catch (SQLException e){
            return Collections.emptyList();
        }
    }

    @Override
    public boolean updatePassword(UpdatePasswordRequestDTO request) {
        String sql = "Update cliente set password=? where email=?";
        return updatePassDB(request, sql, connection);
    }


}
