package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    private HashMap<String, AuthData> authTokens = new HashMap<>();

    @Override
    public void createAuth(AuthData authData) {
        String newTokenString = UUID.randomUUID().toString();
        AuthData newAuthToken = new AuthData(newTokenString, authData.username());
        authTokens.put(newTokenString, newAuthToken);
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
