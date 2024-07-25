package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MemoryGameDAO implements GameDAO {

    private int gameID = 1;
    private HashMap<Integer, GameData> games = new HashMap<>();

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
    public GameData updateGame(GameData game, String whiteUsername, String blackUsername) {
        GameData newGame = new GameData(game.gameID(), whiteUsername, blackUsername, game.gameName(), game.game());
        if (blackUsername == null) {
            newGame = new GameData(game.gameID(), whiteUsername, game.blackUsername(), game.gameName(), game.game());
        }
        if (whiteUsername == null) {
            newGame = new GameData(game.gameID(), game.whiteUsername(), blackUsername, game.gameName(), game.game());
        }
        return newGame;
    }

    @Override
    public void clear() {
        games.clear();
    }


    private void deleteGame(int gameID) {
        games.remove(gameID);
    }

    public void joinGame(int gameID, String whiteUsername, String blackUsername) throws DataAccessException {
        if (blackUsername == null && getGame(gameID).blackUsername() == null) {
            throw new DataAccessException("Can't do that");
        }
        if (whiteUsername == null && getGame(gameID).whiteUsername() == null) {
            throw new DataAccessException("Can't do that");
        }
        GameData newGame = updateGame(getGame(gameID), whiteUsername, blackUsername);
        deleteGame(gameID);
        games.put(newGame.gameID(), newGame);
    }
}
