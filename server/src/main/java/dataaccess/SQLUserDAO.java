package dataaccess;

import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() {
        String[] createStatements = {
        """
        CREATE TABLE IF NOT EXISTS userData (
          `username` varchar(256) NOT NULL,
          `password` varchar(256) NOT NULL,
          `email` varchar(256) NOT NULL,
          PRIMARY KEY (`username`)
        );
        """
        };
        try {
            DatabaseManager.configureDatabase(createStatements);
        } catch (ResponseException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        String statement = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?);";
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        executeUpdate(statement, userData.username(), hashedPassword, userData.email());
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT username, password, email FROM userData WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String newUsername = rs.getString("username");
                        String password = rs.getString("password");
                        String email = rs.getString("email");
                        return new UserData(newUsername, password, email);
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException{
        String statement = "TRUNCATE userData";
        executeUpdate(statement);
    }

    private void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) { ps.setString(i + 1, p); }
                    else if (param instanceof Integer p) { ps.setInt(i + 1, p); }
                    else if (param instanceof UserData p) { ps.setString(i + 1, p.toString()); }
                    else if (param == null) { ps.setNull(i + 1, NULL); }
                }
                ps.executeUpdate();
            }
        } catch (SQLException exception) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, exception.getMessage()));
        }
    }
}
