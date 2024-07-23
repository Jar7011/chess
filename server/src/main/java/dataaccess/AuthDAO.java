package dataaccess;

import model.AuthData;

public interface AuthDAO {

    public AuthData createAuth(String username);

    public AuthData getAuth(int authToken);

    public void deleteAuth(int authToken);

    public void clear();
}
