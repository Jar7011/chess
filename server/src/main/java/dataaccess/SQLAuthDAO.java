package dataaccess;

import exception.ResponseException;
import model.AuthData;

import java.sql.SQLException;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() {
        String[] createStatements = {
        """
        CREATE TABLE IF NOT EXISTS authData (
          `authToken` varchar(256) NOT NULL,
          `username` varchar(256) NOT NULL,
          PRIMARY KEY (`authToken`)
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
    public String createAuth(String username) throws DataAccessException {
        String newTokenString = UUID.randomUUID().toString();
        String statement = "INSERT INTO authData (authToken, username) VALUES (?, ?)";
        executeUpdate(statement, newTokenString, username);
        return newTokenString;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT authToken, username FROM authData WHERE authToken=?;";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String newAuthToken = rs.getString("authToken");
                        String username = rs.getString("username");
                        return new AuthData(newAuthToken, username);
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
    public void deleteAuth(String authToken) throws DataAccessException {
        String statement = "DELETE FROM authData WHERE authToken=?;";
        executeUpdate(statement, authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "TRUNCATE authData";
        executeUpdate(statement);
    }

    private void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) { ps.setString(i + 1, p); }
                    else if (param instanceof Integer p) { ps.setInt(i + 1, p); }
                    else if (param instanceof AuthData p) { ps.setString(i + 1, p.toString()); }
                    else if (param == null) { ps.setNull(i + 1, NULL); }
                }
                ps.executeUpdate();
            }
        } catch (SQLException exception) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, exception.getMessage()));
        }
    }
}
