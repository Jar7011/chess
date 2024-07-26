package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {

    public GameData createGame(String gameName);

    public GameData getGame(int gameID);

    public Collection<GameData> listGames();

    public void updateGame(int gameID, String color, String authToken);

    //public void joinGame(int gameID, String color, String authToken);

    public void clear();
}
