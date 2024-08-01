package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {

    public GameData createGame(String gameName) throws DataAccessException;

    public GameData getGame(int gameID) throws DataAccessException;

    public Collection<GameData> listGames() throws DataAccessException;

    public void updateGame(int gameID, String color, String authToken) throws DataAccessException;

    //public void joinGame(int gameID, String color, String authToken);

    public void clear() throws DataAccessException;
}
