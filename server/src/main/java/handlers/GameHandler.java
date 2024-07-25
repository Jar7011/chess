package handlers;

import com.google.gson.Gson;
import dataaccess.BadRequestException;
import dataaccess.ColorException;
import dataaccess.DataAccessException;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResult;
import response.JoinGameResult;
import response.ListGamesResult;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.Map;

public class GameHandler {

    private GameService service;

    public GameHandler(GameService gameService) {
        service = gameService;
    }

    public String listGameHandler(Request request, Response response) throws DataAccessException {
        try {
            ListGamesResult result = service.listGames(request.headers("authorization"));
            response.status(200);
            return new Gson().toJson(result);
        }
        catch (DataAccessException exception) {
            response.status(401);
            return new Gson().toJson(Map.of("message", exception.getMessage()));
        }
    }

    public String createGameHandler(Request request, Response response) throws DataAccessException {
        try {
            CreateGameRequest createRequest = new Gson().fromJson(request.body(), CreateGameRequest.class);
            CreateGameResult result = service.createGame(createRequest, request.headers("authorization"));
            response.status(200);
            return new Gson().toJson(result);
        }
        catch (DataAccessException exception) {
            response.status(401);
            return new Gson().toJson(Map.of("message", exception.getMessage()));
        }
    }

    public String joinGameHandler(Request request, Response response) throws DataAccessException, BadRequestException, ColorException {
        try {
            JoinGameRequest joinRequest = new Gson().fromJson(request.body(), JoinGameRequest.class);
            JoinGameResult result = service.joinGame(joinRequest, request.headers("authorization"));
            response.status(200);
            return new Gson().toJson(result);
        } catch (DataAccessException accessException) {
            response.status(401);
            return new Gson().toJson(Map.of("message", accessException.getMessage()));
        }
    }

}
