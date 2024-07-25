package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import response.ClearResult;

public class ClearService {

    AuthDAO authData;
    GameDAO gameData;
    UserDAO userData;

    public ClearService(UserDAO user, AuthDAO authorization, GameDAO game) {
        userData = user;
        gameData = game;
        authData = authorization;
    }

    public void clear() {
        userData.clear();
        gameData.clear();
        authData.clear();
    }
}
