package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameDAO implements GameDAO {

    private final AuthDAO authData;

    public SQLGameDAO(AuthDAO authDAO) {
        authData = authDAO;
        String[] createStatements = {
        """
        CREATE TABLE IF NOT EXISTS gameData (
          `id` int NOT NULL AUTO_INCREMENT,
          `whiteUsername` varchar(256),
          `blackUsername` varchar(256),
          `gameName` varchar(256) NOT NULL,
          `game` TEXT NOT NULL,
          PRIMARY KEY (`id`)
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
    public GameData createGame(String gameName) throws DataAccessException {
        String statement = "INSERT INTO gameData (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?);";
        ChessGame newGame = new ChessGame();
        String json = new Gson().toJson(newGame);
        int id = executeUpdate(statement, null, null, gameName, json);
        return getGame(id);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM gameData WHERE id=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        String game = rs.getString("game");
                        ChessGame newGame = new Gson().fromJson(game, ChessGame.class);
                        int id = rs.getInt("id");
                        return new GameData(id, whiteUsername, blackUsername, gameName, newGame);
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
    public Collection<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, whiteUsername, blackUsername, gameName, game FROM gameData";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        String game = rs.getString("game");
                        ChessGame newGame = new Gson().fromJson(game, ChessGame.class);
                        games.add(new GameData(id, whiteUsername, blackUsername, gameName, newGame));
                    }
                }
            }
        } catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
        return games;
    }

    @Override
    public void updateGame(int gameID, String color, String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "";
            if (color.equals("BLACK")) {
                statement = "UPDATE gameData SET blackUsername=? WHERE id=?";
            } else if (color.equals("WHITE")) {
                statement = "UPDATE gameData SET whiteUsername=? WHERE id=?";
            }
            try (var ps = conn.prepareStatement(statement)) {
                AuthData auth = this.authData.getAuth(authToken);
                ps.setString(1, auth.username());
                ps.setInt(2, gameID);
                if (getGame(gameID) != null) {
                    ps.executeUpdate();
                }
            }
        } catch (SQLException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "TRUNCATE gameData";
        executeUpdate(statement);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) { ps.setString(i + 1, p); }
                    else if (param instanceof Integer p) { ps.setInt(i + 1, p); }
                    else if (param instanceof GameData p) { ps.setString(i + 1, p.toString()); }
                    else if (param == null) { ps.setNull(i + 1, NULL); }
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException exception) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, exception.getMessage()));
        }
        return 0;
    }
}
