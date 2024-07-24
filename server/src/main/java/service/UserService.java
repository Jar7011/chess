package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResult;
import response.RegisterResult;

import java.util.Objects;

public class UserService {

    AuthDAO authData;
    UserDAO userData;

    public UserService(UserDAO user, AuthDAO authorization) {
        userData = user;
        authData = authorization;
    }

    public RegisterResult registerUser(RegisterRequest register) throws DataAccessException {
        if (userData.getUser(register.username()) != null) {
            throw new DataAccessException("Error: already taken");
        }
        UserData newUser = new UserData(register.username(), register.password(), register.email());
        userData.createUser(newUser);
        return new RegisterResult(newUser.username(), getNewAuthToken(newUser.username()));
    }

    public LoginResult loginUser(LoginRequest login) throws DataAccessException {
        if (!Objects.equals(userData.getUser(login.username()).password(), login.password())) {
            throw new DataAccessException("Error: unauthorized");
        }
        if (userData.getUser(login.username()) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        return new LoginResult(login.username(), getNewAuthToken(login.username()));
    }

    private String getNewAuthToken(String username) {
        return authData.createAuth(username);
    }

}
