package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    private HashMap<String, AuthData> authTokens = new HashMap<>();

    @Override
    public String createAuth(String username) {
        String newTokenString = UUID.randomUUID().toString();
        AuthData newAuthToken = new AuthData(newTokenString, username);
        authTokens.put(newTokenString, newAuthToken);
        return newTokenString;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return authTokens.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        authTokens.remove(authToken);
    }

    @Override
    public void clear() {
        authTokens.clear();
    }
}
