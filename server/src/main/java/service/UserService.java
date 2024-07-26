package service;

import dataaccess.AuthDAO;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResult;
import response.LogoutResult;
import response.RegisterResult;

import javax.xml.crypto.Data;
import java.util.Objects;

public class UserService {

    private AuthDAO authData;
    private UserDAO userData;

    public UserService(UserDAO user, AuthDAO authorization) {
        userData = user;
        authData = authorization;
    }

    public RegisterResult registerUser(RegisterRequest register) throws DataAccessException {
        if (userData.getUser(register.username()) != null) {
            throw new DataAccessException("Error: already taken");
        }
        if (register.username() == null || register.password() == null || register.email() == null) {
            throw new BadRequestException("Error: bad request");
        }
        UserData newUser = new UserData(register.username(), register.password(), register.email());
        userData.createUser(newUser);
        return new RegisterResult(newUser.username(), getNewAuthToken(newUser.username()));
    }

    public LoginResult loginUser(LoginRequest login) throws DataAccessException {
        if (userData.getUser(login.username()) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        if (!Objects.equals(userData.getUser(login.username()).password(), login.password())) {
            throw new DataAccessException("Error: unauthorized");
        }
        return new LoginResult(login.username(), getNewAuthToken(login.username()));
    }

    public LogoutResult logoutUser(String authToken) throws DataAccessException {
        if (authData.getAuth(authToken) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        authData.deleteAuth(authToken);
        return new LogoutResult("Logged out");
    }

    private String getNewAuthToken(String username) {
        return authData.createAuth(username);
    }

}
