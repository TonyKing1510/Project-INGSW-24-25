package it.unina.dietiEstates25.dao;
import it.unina.dietiEstates25.db.DatabaseConnection;
import it.unina.dietiEstates25.dto.request.AddAdminRequestDTO;
import it.unina.dietiEstates25.dto.request.UpdatePasswordRequestDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static it.unina.dietiEstates25.dao.AgenteDaoImpl.updatePassDB;

public class AdminDaoImpl implements AdminDao {

    private Connection connection;

    private static final Logger logger = LoggerFactory.getLogger(AdminDaoImpl.class);


    public AdminDaoImpl() throws SQLException {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            logger.error("Si è verificato un errore nel db: ", e);
        }
    }


    @Override
    public boolean addAdmin(AddAdminRequestDTO admin) {
        String sql = "insert into gestoreagenziaimmobiliare" +
                "(nome,cognome,cf,telefono,password,email,admin,nomeagenzia,ruolo)"+
                " values(?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            setSql(ps, admin.getNome(), admin.getCognome(), admin.getCf(), admin.getTelefono(), admin.getPassword(), admin.getEmail());
            ps.setBoolean(7,true);
            ps.setString(8,admin.getNomeAgenzia());
            ps.setString(9,"Admin");
            int rows=ps.executeUpdate();
            return rows > 0;
        }catch (SQLException e) {
            e.printStackTrace();
            logger.error("Si è verificato un errore nel db: ", e);
            return false;
        }
    }

    static void setSql(PreparedStatement ps, String nome, String cognome, String cf, String telefono, String password, String email) throws SQLException {
        ps.setString(1, nome);
        ps.setString(2, cognome);
        ps.setString(3, cf);
        ps.setString(4, telefono);
        ps.setString(5, password);
        ps.setString(6, email);
    }

    @Override
    public boolean updatePassword(UpdatePasswordRequestDTO request) {
        String sql = "Update gestoreagenziaimmobiliare set password=? where email=?";
        return updatePassDB(request, sql, connection);
    }
}
