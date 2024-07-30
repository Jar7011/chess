package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class ClearService {

    private AuthDAO authData;
    private GameDAO gameData;
    private UserDAO userData;

    public ClearService(UserDAO user, AuthDAO authorization, GameDAO game) {
        userData = user;
        gameData = game;
        authData = authorization;
    }

    public void clear() throws DataAccessException {
        userData.clear();
        gameData.clear();
        authData.clear();
    }
}
