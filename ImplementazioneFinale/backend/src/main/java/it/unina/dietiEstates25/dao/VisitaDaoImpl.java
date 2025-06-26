package it.unina.dietiEstates25.dao;
import it.unina.dietiEstates25.db.DatabaseConnection;
import it.unina.dietiEstates25.dto.request.AddVisitaRequestDTO;
import it.unina.dietiEstates25.dto.response.*;
import it.unina.dietiEstates25.model.TipoImmobile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VisitaDaoImpl implements VisitaDao {
    private Connection connection;

    private static final Logger logger = LoggerFactory.getLogger(VisitaDaoImpl.class);

    private static final String ERRORE = "Si è verificato un errore nel db";

    public VisitaDaoImpl() throws SQLException {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            logger.error(ERRORE, e);
        }
    }

    @Override
    public VisitaResponseDTO addVisita(AddVisitaRequestDTO visita) {
        String insertSQL = "INSERT INTO visita (datavisita, orainiziovisita,accettata, descrizione, clienteprenotatovisita, agenteriferimento, immobilevisita,orafineVisita) " +
                "VALUES (?, ?,?, ?, ?, ?, ?, ?)";
        List<Integer> immobiliDiAgente = getAllImmobileByCf(visita.getCfAgente());
        if(!controllaSeImmobileInseritoEDellagente(visita.getImmobileDaVisitare(),immobiliDiAgente)){
            return new VisitaResponseDTO(false,true,true);
        }
        try(PreparedStatement ps = connection.prepareStatement(insertSQL)){
            ps.setObject(1,visita.getDataVisita());
            ps.setObject(2,visita.getHoraInizioVisita());
            ps.setObject(3,null);
            ps.setString(4,visita.getDescrizione());
            ps.setString(5, visita.getEmailClienteChePrenotaVisita());
            ps.setString(6, visita.getCfAgente());
            ps.setInt(7,visita.getImmobileDaVisitare());
            ps.setObject(8,visita.getHoraFineVisita());
            int nRows=ps.executeUpdate();
            if(nRows>0){
                return new VisitaResponseDTO(true,false);
            }
        } catch (SQLException e) {
            logger.error(ERRORE, e);
            return new VisitaResponseDTO(false,true);
        }
        return new VisitaResponseDTO(false,true);
    }


    private boolean controllaSeImmobileInseritoEDellagente(int immobileInserito,List<Integer> immobiliDiAgente) {
        for (int immobile : immobiliDiAgente) {
            if(immobile==immobileInserito){
                return true;
            }
        }
        return false;
    }



    @Override
    public List<GetVisiteResponse> getAllVisiteAccettateByCF(String cf) {
        String sql = "select datavisita,orainiziovisita,orafineVisita,descrizione,clienteprenotatovisita from visita where agenteriferimento = ? and accettata=true";
        List<GetVisiteResponse> visitas = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,cf);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                LocalDate dataVisita = rs.getObject(1, LocalDate.class);
                LocalTime horaVisita = rs.getObject(2, LocalTime.class);
                LocalTime horaFineVisita = rs.getObject(3, LocalTime.class);
                String descrizione = rs.getString(4);
                String emailClienteChePrenotaVisita = rs.getString(5);
                GetVisiteResponse visita = new GetVisiteResponse(emailClienteChePrenotaVisita,dataVisita,horaVisita,descrizione,horaFineVisita);
                visitas.add(visita);
            }
        }catch (SQLException e){
            logger.error(ERRORE, e);
            return Collections.emptyList();
        }
        return visitas;
    }


    @Override
    public List<Integer> getAllImmobileByCf(String cf) {
        String sql = "select id_immobile from immobile where agenteproprietario = ?";
        List<Integer> immobili = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,cf);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                immobili.add(rs.getInt(1));
            }
            return immobili;
        }catch (SQLException e){
            logger.error(ERRORE, e);
            return Collections.emptyList();
        }
    }

    @Override
    public InformazioniVisitaDTO getInformazioniVisita(int idNotifica) {
        String sql = """
                select f.assert_id, a.nome,a.cognome,clienteproprietarioNotifica, i.tipoimmobile,i.indirizzo,i.comune,i.numeroCivico,i.id_immobile, v.datavisita,orainiziovisita,orafinevisita from notifica n join visita v on n.id_visita=v.id_visita
                join immobile i on v.immobilevisita = i.id_immobile join agente a on i.agenteProprietario =a.cf join foto f on a.cf = f.agente
                where n.id_notifica = ?;
            """;
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,idNotifica);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                ArrayList<String> fotoAgente = new ArrayList<>();
                recuperaFotoAgente(rs,fotoAgente);
                return new InformazioniVisitaDTO.Builder()
                        .fotoProfiloAgente(fotoAgente.get(0))
                        .agente(rs.getString(2)+" "+rs.getString(3))
                        .emailClientePrenotatoVisita(rs.getString((4)))
                        .tipoImmobile(TipoImmobile.valueOf(rs.getString(5)))
                        .viaImmobile(rs.getString(6))
                        .comune(rs.getString(7))
                        .numeroCivico(rs.getString(8))
                        .idImmobile(rs.getInt(9))
                        .dataVisita(rs.getDate(10).toLocalDate())
                        .oraVisita(rs.getTime(11).toLocalTime())
                        .oraFineVisita(rs.getTime(12).toLocalTime())
                        .build();
            }
        }catch (SQLException e){
            logger.error(ERRORE, e);
            return null;
        }
        return null;
    }

    @Override
    public List<DateEOreOccupateAgente> getDateEOreOccupateAgente(String cf) {
        String sql = "select datavisita,orainiziovisita,orafinevisita from visita v where v.agenteriferimento=? and accettata=true";
        List<DateEOreOccupateAgente> dateAgente=new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,cf);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                DateEOreOccupateAgente date= new DateEOreOccupateAgente(rs.getDate(1).toLocalDate()
                        , rs.getTime(2).toLocalTime(), rs.getTime(3).toLocalTime());
                dateAgente.add(date);
            }
            return dateAgente;
        }catch (SQLException e){
            logger.error(ERRORE, e);
            return dateAgente;
        }
    }

    @Override
    public CheckVisitaPerClienteDTO controllaSeClienteHaGiaVisitaPerImmobile(String email, int idImmobile) {
        String sql = "select 1 from visita v where v.accettata is NULL and v.clienteprenotatovisita=? and v.immobilevisita=?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1,email);
            ps.setInt(2,idImmobile);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return new CheckVisitaPerClienteDTO(false,false,true);
            }else{
                return new CheckVisitaPerClienteDTO(false,true,false);
            }
        }catch (SQLException e){
            logger.error(ERRORE, e);
            return new CheckVisitaPerClienteDTO(true,false,false);
        }
    }

    @Override
    public boolean eliminaVisita(AddVisitaRequestDTO visita) {
        String sqlNotifica = """
        DELETE FROM notifica
        WHERE id_visita IN (
            SELECT id_visita FROM visita
            WHERE datavisita = ?
            AND oraInizioVisita = ?
            AND clientePrenotatoVisita = ?
            AND agenteRiferimento = ?
            AND immobileVisita = ?
            AND oraFineVisita = ?
        );
    """;

        String sqlVisita = """
        DELETE FROM visita
        WHERE datavisita = ?
        AND oraInizioVisita = ?
        AND clientePrenotatoVisita = ?
        AND agenteRiferimento = ?
        AND immobileVisita = ?
        AND oraFineVisita = ?;
    """;

        try (PreparedStatement psNotifica = connection.prepareStatement(sqlNotifica);
             PreparedStatement psVisita = connection.prepareStatement(sqlVisita)) {
            for (int i = 1; i <= 6; i++) {
                psNotifica.setObject(i, getParamByIndex(visita, i));
                psVisita.setObject(i, getParamByIndex(visita, i));
            }
            psNotifica.executeUpdate();

            int rowsAffected = psVisita.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Object getParamByIndex(AddVisitaRequestDTO visita, int index) {
        return switch (index) {
            case 1 -> visita.getDataVisita();
            case 2 -> visita.getHoraInizioVisita();
            case 3 -> visita.getEmailClienteChePrenotaVisita();
            case 4 -> visita.getCfAgente();
            case 5 -> visita.getImmobileDaVisitare();
            case 6 -> visita.getHoraFineVisita();
            default -> throw new IllegalArgumentException("Indice parametro non valido");
        };
    }


    private static void recuperaFotoAgente(ResultSet rs,ArrayList<String> urlsFinali) throws SQLException {
        String assertId= rs.getString(1);
        urlsFinali.add(assertId);
    }


}
