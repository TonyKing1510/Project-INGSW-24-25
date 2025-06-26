package it.unina.dietiEstates25.dao;
import it.unina.dietiEstates25.dto.response.GestoreAgenziaImmobiliareDTO;
import it.unina.dietiEstates25.model.AccountSemplice;
import it.unina.dietiEstates25.model.GestoreAgenziaImmobiliare;
import it.unina.dietiEstates25.service.SecurityPasswordService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthDaoImpl implements AuthDao {

    private Connection connection;

    @Override
    public boolean checkIfExists(AccountSemplice account) throws SQLException {
            String sql = "SELECT 1 FROM Cliente c WHERE c.email = ? AND c.password = ?";
            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, account.getEmail());
                ps.setString(2, account.getPassword());
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
            catch (SQLException e) {
            return false;
        }
        finally {
            connection.close();
        }
    }

    @Override
    public GestoreAgenziaImmobiliareDTO checkGestoreExists(GestoreAgenziaImmobiliare g, String psw) throws SQLException {
        String sql = "SELECT g.password FROM GestoreAgenziaImmobiliare g WHERE g.username = ? AND g.nomeAgenzia = ? AND g.cf = ?  AND g.admin = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1,g.getAccountGestore().getUsername());
            ps.setString(2, g.getAgenziaAppartenente().getNomeAgenzia());
            ps.setString(3, g.getCf());
            ps.setBoolean(4, g.getisAdmin());
            try (ResultSet rs = ps.executeQuery()) {
                if(ciSonoRisultati(rs)){
                    GestoreAgenziaImmobiliareDTO dto = new GestoreAgenziaImmobiliareDTO();
                    dto.setCredSbagliate(!isPasswordGiusta(psw, rs));
                    return dto;
                }else{
                    GestoreAgenziaImmobiliareDTO dto = new GestoreAgenziaImmobiliareDTO();
                    dto.setCredSbagliate(true);
                    return dto;
                }
            }
        }
        catch (SQLException e) {
            return null;
        }
        finally {
            connection.close();
        }
    }

    private static boolean ciSonoRisultati(ResultSet rs) throws SQLException {
        return rs.next();
    }


    private static boolean isPasswordGiusta(String psw, ResultSet rs) throws SQLException {
        return SecurityPasswordService.checkPassword(psw, rs.getString("password"));
    }
}
