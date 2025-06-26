package it.unina.dietiEstates25.dao;
import it.unina.dietiEstates25.db.DatabaseConnection;
import it.unina.dietiEstates25.dto.response.FotoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ImageDaoImpl implements ImageDao {

    private Connection connection;

    private static final Logger logger = LoggerFactory.getLogger(ImageDaoImpl.class);

    private static final String ERRORE = "Si è verificato un errore nel db";

    public ImageDaoImpl() throws SQLException {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            logger.error(ERRORE, e);
        }
    }

    @Override
    public FotoDTO addImage(String assertId, String cf) {
        List<String> quantitaFoto = getPublicIdByCf(cf);
        String query;
        if (agenteNonHaFoto(quantitaFoto)) {
            query = "INSERT INTO FOTO(assert_id,agente) values(?,?)";
        }else {
            query = "UPDATE FOTO SET assert_id = ? where agente= ?";
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1,assertId);
            preparedStatement.setString(2,cf);
            int rows=preparedStatement.executeUpdate();
            if(rows>0){
                return new FotoDTO(true,false);
            }
            return new FotoDTO(false,true);
        } catch (SQLException e) {
            logger.error(ERRORE, e);
            return new FotoDTO(false,true);
        }
    }

    private static boolean agenteNonHaFoto(List<String> quantitaFoto) {
        return quantitaFoto.isEmpty();
    }

    @Override
    public List<String> getPublicIdByCf(String cf) {
        String sql = "select assert_id from foto where agente = ?";
        return getFoto(cf, sql, connection);
    }

    @Override
    public List<String> getImageOfImmobile(int idImmobile) {
        return getFotoById(idImmobile);
    }

    public List<String> getFotoById(int id){
        String sql = "select assert_id from foto where immobileRiferimento = ?";
        List<String> fotos = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fotos.add(rs.getString("assert_id"));
            }
        }catch (SQLException e){
            logger.error(ERRORE, e);
        }
        return fotos;
    }

    static List<String> getFoto(String id, String sql, Connection connection) {
        List<String> fotos = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fotos.add(rs.getString("assert_id"));
            }
        }catch (SQLException e){
            logger.error(ERRORE, e);
        }
        return fotos;
    }


}
