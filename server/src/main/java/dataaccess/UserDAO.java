package dataaccess;

import model.UserData;

public interface UserDAO {

    public void createUser(UserData userData) throws ResponseException;

    public UserData getUser(String username) throws DataAccessException;

    public void clear();
}
