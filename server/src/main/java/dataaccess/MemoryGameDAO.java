package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {

    private int gameID = 1;
    private HashMap<Integer, GameData> games = new HashMap<>();
    private AuthDAO authData;

    public MemoryGameDAO(AuthDAO authDAO) {
        authData = authDAO;
    }

    @Override
    public GameData createGame(String gameName) {
        gameID += 1;
        GameData newGame = new GameData(gameID++, null, null, gameName, new ChessGame());
        games.put(newGame.gameID(), newGame);
        return newGame;
    }

    @Override
    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() {
        return games.values();
    }

    @Override
    public void updateGame(int gameID, String color, String authToken) {
        GameData game = games.get(gameID);
        AuthData data = authData.getAuth(authToken);
        if (Objects.equals(color, "BLACK")) {
            GameData newGame = new GameData(gameID, game.whiteUsername(), data.username(),
                    game.gameName(), game.game());
            games.put(gameID, newGame);
        } else if (Objects.equals(color, "WHITE")) {
            GameData newGame = new GameData(gameID, data.username(), game.blackUsername(),
                    game.gameName(), game.game());
            games.put(gameID, newGame);
        }
    }

    @Override
    public void clear() {
        games.clear();
    }
}
