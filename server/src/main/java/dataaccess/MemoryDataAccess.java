package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {

    private HashMap<String, AuthData> authTokens = new HashMap<>();
    private HashMap<Integer, GameData> games = new HashMap<>();
    private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        authTokens.clear();
        games.clear();
        users.clear();
    }
}
