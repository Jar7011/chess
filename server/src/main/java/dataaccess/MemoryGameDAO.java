package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MemoryGameDAO implements GameDAO {

    private int gameID = 1;
    final private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public GameData createGame(String gameName) {
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
    public void updateGame(GameData gameToUpdate) {
        int hi = 1;
    }
}
