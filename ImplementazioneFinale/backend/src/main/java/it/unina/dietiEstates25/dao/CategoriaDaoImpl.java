package it.unina.dietiEstates25.dao;

import it.unina.dietiEstates25.db.DatabaseConnection;
import it.unina.dietiEstates25.dto.request.UpdateCategoriaNotificaDTO;
import it.unina.dietiEstates25.dto.response.UpdateCategoriaResponse;
import it.unina.dietiEstates25.model.CategoriaNotifica;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoriaDaoImpl implements CategoriaDao {

    private Connection connection;

    private static final Logger logger = LoggerFactory.getLogger(CategoriaDaoImpl.class);

    private static final String ERRORE = "Si è verificato un errore nel db";

    public CategoriaDaoImpl(){
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            logger.error(ERRORE, e);
        }
    }

    public UpdateCategoriaResponse setInviaCategoriaCliente(String email, UpdateCategoriaNotificaDTO categoriaDaAttivare) {
        String sql = "UPDATE NOTIFICA SET da_inviare=true WHERE clientenotificato=? and categoria=?";
        return updateCategoria(email, categoriaDaAttivare, sql);
    }

    public UpdateCategoriaResponse setNonInviareCategoriaCliente(String email, UpdateCategoriaNotificaDTO categoriaDaAttivare) {
        String sql = "UPDATE NOTIFICA SET da_inviare=false WHERE clientenotificato=? and categoria=?";
        return updateCategoria(email, categoriaDaAttivare, sql);
    }


    public UpdateCategoriaResponse setNonInviareCategoriaAgente(String cf, UpdateCategoriaNotificaDTO categoriaDaDisattivare) {
        String sql = "UPDATE NOTIFICA SET da_inviare=false WHERE agentenotificato=? and categoria=?";
        return updateCategoria(cf, categoriaDaDisattivare, sql);
    }

    public UpdateCategoriaResponse setInviareCategoriaAgente(String cf, UpdateCategoriaNotificaDTO categoriaDaDisattivare) {
        String sql = "UPDATE NOTIFICA SET da_inviare=true WHERE agentenotificato=? and categoria=?";
        return updateCategoria(cf, categoriaDaDisattivare, sql);
    }

    private UpdateCategoriaResponse updateCategoria(String idUtente, UpdateCategoriaNotificaDTO categoriaDaDisattivare, String sql) {
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, idUtente);
            ps.setObject(2, categoriaDaDisattivare.getCategoriaNotifica(), Types.OTHER);
            int nRows=ps.executeUpdate();
            if(nonFattoUpdateDiNiente(nRows)){
                return new UpdateCategoriaResponse(true);
            }
            if(updateQualcosa(nRows)){
                return new UpdateCategoriaResponse(true,true);
            }
            return new UpdateCategoriaResponse(false,false);
        } catch (SQLException e) {
            logger.error(ERRORE, e);
            return new UpdateCategoriaResponse(false,false);
        }
    }

    @Override
    public List<CategoriaNotifica> getCategorieDisattivateByCFAgente(String cf) {
        List<CategoriaNotifica> categorieDisattivate = new ArrayList<>();
        String sql = "SELECT distinct categoria FROM notifica where agentenotificato = ? and da_inviare = false";
        return getCategoriaNotificas(cf, categorieDisattivate, sql);
    }

    @Override
    public List<CategoriaNotifica> getCategorieDisattivateByEmailCliente(String email) {
        List<CategoriaNotifica> categorieDisattivate = new ArrayList<>();
        String sql = "SELECT distinct categoria FROM notifica where clientenotificato = ? and da_inviare = false";
        return getCategoriaNotificas(email, categorieDisattivate, sql);
    }

    @Override
    public List<CategoriaNotifica> getAllCategoriaByEmail(String email) {
        List<CategoriaNotifica> categorieDisattivate = new ArrayList<>();
        String sql = "SELECT distinct categoria FROM notifica where clientenotificato = ?";
        return getCategoriaNotificas(email, categorieDisattivate, sql);
    }

    @Override
    public List<CategoriaNotifica> getAllCategoriaByCFAgente(String cf) {
        List<CategoriaNotifica> categorieDisattivate = new ArrayList<>();
        String sql = "SELECT distinct categoria FROM notifica where agentenotificato = ?";
        return getCategoriaNotificas(cf, categorieDisattivate, sql);
    }

    private List<CategoriaNotifica> getCategoriaNotificas(String email, List<CategoriaNotifica> categorieDisattivate, String sql) {
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, email);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()){
                String categoriaNotifica=resultSet.getString("categoria");
                CategoriaNotifica categoriaNotifica1 = CategoriaNotifica.fromString(categoriaNotifica);
                categorieDisattivate.add(categoriaNotifica1);
            }
        }catch(SQLException e){
            logger.error(ERRORE, e);
            return Collections.emptyList();
        }
        return categorieDisattivate;
    }

    private static boolean updateQualcosa(int nRows) {
        return nRows > 0;
    }

    private static boolean nonFattoUpdateDiNiente(int nRows) {
        return nRows == 0;
    }

}
