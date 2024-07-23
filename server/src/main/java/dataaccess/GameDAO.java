package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {

    public GameData createGame(GameData gameData);

    public GameData getGame(int gameID);

    public Collection<GameData> listGames();

    public GameData updateGame(GameData game, String whiteUsername, String blackUsername);

    public void clear();
}
