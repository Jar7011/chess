package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResult;
import response.LogoutResult;
import response.RegisterResult;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.Map;

public class UserHandler {

    private UserService service;

    public UserHandler(UserService userService) {
        service = userService;
    }

    public String registerHandler(Request request, Response response) throws DataAccessException {
        try {
            RegisterRequest register = new Gson().fromJson(request.body(), RegisterRequest.class);
            RegisterResult result = service.registerUser(register);
            response.status(200);
            return new Gson().toJson(result);
        }
        catch (DataAccessException exception) {
            response.status(403);
            return new Gson().toJson(Map.of("message", exception.getMessage()));
        }
    }

    public String loginHandler(Request request, Response response) throws DataAccessException {
        try {
            LoginRequest login = new Gson().fromJson(request.body(), LoginRequest.class);
            LoginResult result = service.loginUser(login);
            response.status(200);
            return new Gson().toJson(result);
        }
        catch (DataAccessException exception) {
            response.status(401);
            return new Gson().toJson(Map.of("message", exception.getMessage()));
        }
    }

    public String logoutHandler(Request request, Response response) throws DataAccessException {
        try {
            LogoutResult result = service.logoutUser(request.headers("authorization"));
            response.status(200);
            return new Gson().toJson(result);
        }
        catch (DataAccessException exception) {
            response.status(401);
            return new Gson().toJson(Map.of("message", exception.getMessage()));
        }
    }
}
