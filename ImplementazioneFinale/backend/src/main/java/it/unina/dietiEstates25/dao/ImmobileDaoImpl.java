package it.unina.dietiEstates25.dao;
import it.unina.dietiEstates25.db.DatabaseConnection;
import it.unina.dietiEstates25.dto.request.AddImmobileRequestDTO;
import it.unina.dietiEstates25.dto.request.RicercaImmobileDTORequest;
import it.unina.dietiEstates25.dto.response.DatiImmobileDTO;
import it.unina.dietiEstates25.dto.response.ImmobileDTO;
import it.unina.dietiEstates25.dto.response.ImmobileResponseRicercaDTO;
import it.unina.dietiEstates25.model.*;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImmobileDaoImpl implements ImmobileDao {
    private Connection connection;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ImmobileDaoImpl.class);

    private static final String ERRORE = "Si è verificato un errore nel db";

    public ImmobileDaoImpl() throws SQLException {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            logger.error(ERRORE, e);
        }
    }

    @Override
    public ImmobileDTO addImmobile(AddImmobileRequestDTO immobile) {
        String sql = "insert into immobile(tipoImmobile,tipoVendita,descrizione,indirizzo," +
                "n°stanze,piano,classeenergetica,superficie,arredamento,prezzo,spesecondominiali,n°bagni," +
                "n°cucine,n°soggiorni,agenteproprietario,città,lat,long,titolo,comune,numeroCivico,presenza_ascensore) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            setSQL(immobile, ps);
            int nRows=ps.executeUpdate();
            if(nRows>0) {
                aggiungiFoto(ps,immobile.getFotoDelImmobile());
                return new ImmobileDTO(true,false,false);
            }else{
                return new ImmobileDTO(false,true,true);
            }
        } catch (SQLException e) {
                logger.error(ERRORE, e);
                return new ImmobileDTO(false,true,true);
        }
    }

    @Override
    public boolean deleteImmobile(int idImmobile) {
        String sql =
                """
                        delete
                        from immobile
                        where id_immobile = ?
                """;
        return update(idImmobile, sql, connection, logger, ERRORE);
    }

    static boolean update(int idImmobile, String sql, Connection connection, org.slf4j.Logger logger, String errore) {
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,idImmobile);
            int rows=ps.executeUpdate();
            return rows>0;
        }catch (SQLException e) {
            logger.error(errore, e);
            return false;
        }
    }

    private void aggiungiFoto(PreparedStatement ps,List<String> fotoImmobile) throws SQLException {
        ResultSet rs= ps.getGeneratedKeys();
        if(rs.next()) {
            int id = rs.getInt(1);
            if(!fotoImmobile.isEmpty()){
                aggiungiFotoAdImmobile(id, fotoImmobile);
            }
        }
    }

    private void aggiungiFotoAdImmobile(int idImmobile, List<String> fotos) {
        String sql = "INSERT INTO foto (assert_id, immobileriferimento) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(2, idImmobile);
            for (String fotoPath : fotos) {
                ps.setString(1,fotoPath);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            logger.error(ERRORE, e);
        }
    }


    private static void setSQL(AddImmobileRequestDTO immobile, PreparedStatement ps) throws SQLException {
        ps.setObject(1,immobile.getTipoimmobile(),Types.OTHER);
        ps.setObject(2,immobile.getTipovendita(),Types.OTHER);
        ps.setString(3,immobile.getDescrizione());
        ps.setString(4,immobile.getVia());
        ps.setInt(5,immobile.getNumeroStanze());
        ps.setInt(6,immobile.getPiano());
        ps.setObject(7,immobile.getClasseEnergetica(),Types.OTHER);
        ps.setInt(8,immobile.getSuperficie());
        ps.setObject(9,immobile.getArredamento(),Types.OTHER);
        ps.setBigDecimal(10,immobile.getPrezzo());
        ps.setBigDecimal(11,immobile.getSpeseCondominiali());
        ps.setInt(12,immobile.getNumeroBagni());
        ps.setInt(13,immobile.getNumeroCucine());
        ps.setInt(14,immobile.getNumeroSoggiorni());
        ps.setString(15,immobile.getCfAgente());
        ps.setString(16,immobile.getCitta());
        ps.setDouble(17,immobile.getLatitudine());
        ps.setDouble(18,immobile.getLongitudine());
        ps.setString(19,immobile.getTitolo());
        ps.setString(20,immobile.getComune());
        ps.setString(21,immobile.getNumeroCivico());
        ps.setBoolean(22,immobile.isAscensore());
    }


    public List<Immobile> getAnnunciByEmail(String email) {
        List<Immobile> immobiles = new ArrayList<>();
        String sql = "SELECT immobile.titolo,immobile.tipoimmobile,immobile.città FROM immobile INNER JOIN agente ON immobile.agenteproprietario = agente.cf where agente.email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            aggiungiAnnunci(immobiles, resultSet);
        } catch (SQLException e) {
            logger.error(ERRORE, e);
        }
        return immobiles;
    }

    @Override
    public List<ImmobileResponseRicercaDTO> getImmobiliRicerca(RicercaImmobileDTORequest ricerca) {
        List<ImmobileResponseRicercaDTO> immobili = new ArrayList<>();
        String sql = """
                SELECT i.id_immobile,i.tipoImmobile,i.tipoVendita,i.descrizione,i.indirizzo,i.comune,i.n°stanze,i.piano,i.classeEnergetica,
           i.superficie,i.arredamento,i.prezzo,i.speseCondominiali,i.n°bagni,i.n°cucine,i.n°soggiorni,i.agenteProprietario,i.città,i.lat,i.long,i.titolo,
            i.numerocivico,a.nome,a.cognome,i.presenza_ascensore
                FROM immobile i
                JOIN agente a ON i.agenteProprietario = a.cf
               WHERE tipovendita = COALESCE(?, tipovendita)
               AND "n°stanze" = COALESCE(?, "n°stanze")
               AND classeenergetica = COALESCE(?, CLASSEENERGETICA)
              AND CAST(prezzo AS DECIMAL(15, 0)) >= CAST(? AS DECIMAL(15, 0))
             AND CAST(prezzo AS DECIMAL(15, 0)) <= CAST(? AS DECIMAL(15, 0))
             AND LOWER(comune) LIKE LOWER(COALESCE(CONCAT('%', ?, '%'), '%'))
            AND LOWER(città) = LOWER(COALESCE(?, città))
           """;
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setObject(1,(ricerca.getTipologiaVendita() != null) ? ricerca.getTipologiaVendita() : null,Types.OTHER);
            ps.setObject(2, (ricerca.getNumeroStanze() != null) ? ricerca.getNumeroStanze() : null, Types.INTEGER);
            ps.setObject(3,(ricerca.getClasseEnergetica() != null) ? ricerca.getClasseEnergetica() : null,Types.OTHER);
            ps.setObject(4,(ricerca.getPrezzoMinimo() != null) ? ricerca.getPrezzoMinimo() : 0, Types.DECIMAL);
            ps.setObject(5,(ricerca.getPrezzoMaximo() != null) ? ricerca.getPrezzoMaximo() : 999999999, Types.DECIMAL);
            ps.setString(6,
                    (ricerca.getComune() != null && !ricerca.getComune().trim().isEmpty()) ? ricerca.getComune() : null);
            ps.setObject(7,
                    (ricerca.getCitta() != null && !ricerca.getCitta().trim().isEmpty()) ? ricerca.getCitta() : null,
                    Types.OTHER);
            ResultSet rs = ps.executeQuery();
            aggiungiImmobili(rs, immobili);
            aggiungiImmobileAllaRicercaDellUtente(ricerca);
        }catch (SQLException e){
            e.printStackTrace();
            logger.error(ERRORE, e);
            return Collections.emptyList();
        }
        return immobili;
    }

    @Override
    public DatiImmobileDTO getInfoAboutImmobile(int idImmobile) {
        String sql = """
             SELECT indirizzo,comune,città,agenteProprietario,numeroCivico
              from immobile where id_immobile=?
         """;
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,idImmobile);
            ResultSet resultSet = ps.executeQuery();
            if(!resultSet.next()){
                return null;
            }
            return new DatiImmobileDTO(resultSet.getString("indirizzo"),
                        resultSet.getString("comune"),resultSet.getString("agenteProprietario"),
                        resultSet.getString("città"),resultSet.getString("numeroCivico"));
        }catch (SQLException e){
            logger.error(ERRORE, e);
            return null;
        }
    }

    private void aggiungiImmobili(ResultSet rs, List<ImmobileResponseRicercaDTO> immobili) throws SQLException {
        while (rs.next()) {
            ImmobileResponseRicercaDTO immobile = makeImmobile(rs);
            immobile.setPrezzo(ImmobileResponseRicercaDTO.formattaStringa(immobile.getPrezzo()));
            immobile.setSpeseCondominiali(ImmobileResponseRicercaDTO.formattaStringa(immobile.getSpeseCondominiali()));
            immobili.add(immobile);
        }
    }

    private void aggiungiImmobileAllaRicercaDellUtente(RicercaImmobileDTORequest ricerca) {
        if(ricerca.getSessioneUtente() != null && !ricerca.getUtenteCheRicerca().isEmpty()) {
            CompletableFuture.runAsync(
                    () -> {
                        try {
                            insertInRicerca(ricerca, connection);
                        } catch (SQLException e) {
                            logger.error(ERRORE, e);
                        }
                    }
            );
        }
    }

    private void insertInRicerca(RicercaImmobileDTORequest ricerca,Connection connection) throws SQLException {
        String sql = "INSERT INTO ricerca (clientechecerca,agentechecerca,gestorechecerca,n°stanze,classe_energetica,prezzo,comune,città,tipovendita,prezzo_max) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            if (isUtenteAGestore(ricerca.getSessioneUtente())) {
                ps.setNull(1, Types.VARCHAR);
                ps.setNull(2, Types.VARCHAR);
                ps.setString(3, ricerca.getUtenteCheRicerca());
            } else if (isUtenteAgente(ricerca.getSessioneUtente())) {
                ps.setNull(1, Types.VARCHAR);
                ps.setString(2, ricerca.getUtenteCheRicerca());
                ps.setNull(3, Types.VARCHAR);
            } else {
                ps.setString(1, ricerca.getUtenteCheRicerca());
                ps.setNull(2, Types.VARCHAR);
                ps.setNull(3, Types.VARCHAR);
            }
            settaGliAltriParametri(ricerca, ps);
            ps.executeUpdate();
        }catch (SQLException e) {
            logger.error(ERRORE, e);
        }

    }

    private static void settaGliAltriParametri(RicercaImmobileDTORequest ricerca, PreparedStatement ps) throws SQLException {
        setParametro(ps, 4, ricerca.getNumeroStanze(), Types.INTEGER);
        setParametro(ps, 5, ricerca.getClasseEnergetica() != null ? ricerca.getClasseEnergetica().toString() : null, Types.VARCHAR);
        setParametro(ps, 6, ricerca.getPrezzoMinimo(), Types.DOUBLE);
        setParametro(ps, 7, ricerca.getComune(), Types.VARCHAR);
        setParametro(ps, 8, ricerca.getCitta(), Types.VARCHAR);
        setParametro(ps, 9, ricerca.getTipologiaVendita(), Types.OTHER);
        setParametro(ps,10, ricerca.getPrezzoMaximo(), Types.DOUBLE);
    }

    private static <T> void setParametro(PreparedStatement ps, int index, T valore, int sqlType) throws SQLException {
        if (valore != null) {
            ps.setObject(index, valore, sqlType);
        } else {
            ps.setNull(index, sqlType);
        }
    }


    private boolean isUtenteAgente(Integer sessione) {
        return sessione==1;
    }

    private boolean isUtenteAGestore(Integer sessione) {
        return sessione == 0;
    }



    private static ImmobileResponseRicercaDTO makeImmobile(ResultSet rs) throws SQLException {
        return ImmobileResponseRicercaDTO.builder()
                .idImmobile(rs.getInt(1))
                .tipoimmobile(TipoImmobile.valueOf(rs.getString(2)))
                .tipovendita(TipoVendita.valueOf(rs.getString(3)))
                .descrizione(rs.getString(4))
                .via(rs.getString(5))
                .comune(rs.getString(6))
                .numeroStanze(rs.getInt(7))
                .piano(rs.getInt(8))
                .classeEnergetica(ClasseEnergetica.fromString(rs.getString(9)))
                .superficie(rs.getInt(10))
                .arredamento(Arredamento.fromString(rs.getString(11)))
                .prezzo(rs.getString(12).replaceAll("\\.000$", ""))
                .speseCondominiali(rs.getString(13).replaceAll("\\.000$", ""))
                .numeroBagni(rs.getInt(14))
                .numeroCucine(rs.getInt(15))
                .numeroSoggiorni(rs.getInt(16))
                .cfAgente(rs.getString(17))
                .citta(rs.getString(18))
                .latitudine(rs.getDouble(19))
                .longitudine(rs.getDouble(20))
                .titolo(rs.getString(21))
                .numeroCivico(rs.getString(22))
                .nomeAgente(rs.getString("nome"))
                .cognomeAgente(rs.getString("cognome"))
                .ascensore(rs.getBoolean("presenza_ascensore"))
                .build();
    }


    private void aggiungiAnnunci(List<Immobile> immobiles, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String titolo = resultSet.getString("titolo");
            String citta = resultSet.getString("città");
            String tipologia = resultSet.getString("TipoImmobile");
            Immobile immobile = new Immobile(titolo, convertiStringConEnum(tipologia),citta);
            immobiles.add(immobile);
        }
    }

    private static TipoImmobile convertiStringConEnum(String tipologia) {
        TipoImmobile tipo = null;
        if (tipologia != null) {
            try {
                tipo = TipoImmobile.valueOf(tipologia.toUpperCase()); // Converte la stringa in enum
            } catch (IllegalArgumentException e) {
                logger.error(ERRORE, e);
            }
        }
        return tipo;
    }

    public List<ImmobileResponseRicercaDTO> getImmobiliForAgente(String mail) {
        List<ImmobileResponseRicercaDTO> immobiles = new ArrayList<>();
        String sql = """
        SELECT i.id_immobile,i.tipoImmobile,i.tipoVendita,i.descrizione,i.indirizzo,i.comune,i.n°stanze,i.piano,i.classeEnergetica,
           i.superficie,i.arredamento,i.prezzo,i.speseCondominiali,i.n°bagni,i.n°cucine,i.n°soggiorni,i.agenteProprietario,i.città,i.lat,i.long,i.titolo,
            i.numerocivico,a.nome,a.cognome,i.presenza_ascensore
        FROM immobile i INNER JOIN agente a ON i.agenteproprietario = a.cf WHERE a.email = ?
       """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, mail);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ImmobileResponseRicercaDTO immobile = makeImmobile(resultSet);
                immobiles.add(immobile);
            }
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Errore SQL", e);
        }

        return immobiles;
    }

    @Override
    public ImmobileResponseRicercaDTO getInfoAboutImmobileById(int idImmobile) {
        ImmobileResponseRicercaDTO immobile = null;
        String sql =
                """
                SELECT i.id_immobile,i.tipoImmobile,i.tipoVendita,i.descrizione,i.indirizzo,i.comune,i.n°stanze,i.piano,i.classeEnergetica,
                i.superficie,i.arredamento,i.prezzo,i.speseCondominiali,i.n°bagni,i.n°cucine,i.n°soggiorni,i.agenteProprietario,i.città,i.lat,i.long,i.titolo,
                i.numerocivico,a.nome,a.cognome,i.presenza_ascensore FROM immobile i JOIN agente a ON i.agenteProprietario = a.cf where id_immobile=?
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idImmobile);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                immobile = makeImmobile(resultSet);
            }
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Errore SQL", e);
        }
        return immobile;
    }
}
