package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResult;
import response.JoinGameResult;
import response.ListGamesResult;

import java.util.Objects;

public class GameService {

    AuthDAO authData;
    GameDAO gameData;

    public GameService(AuthDAO authorization, GameDAO game) {
        authData = authorization;
        gameData = game;
    }

    public ListGamesResult listGames(String authToken) throws DataAccessException {
        if (isTokenNull(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        return new ListGamesResult(gameData.listGames());
    }

    public CreateGameResult createGame(CreateGameRequest request, String authToken) throws DataAccessException {
        if (isTokenNull(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        if (request.gameName() == null) {
            throw new DataAccessException("Error: bad request");
        }
        return new CreateGameResult(gameData.createGame(request.gameName()).gameID());
    }

    private boolean isTokenNull(String authToken) {
        return authData.getAuth(authToken) == null;
    }

    private boolean isGameNull(JoinGameRequest request) {
        return gameData.getGame(request.gameID()) == null;
    }

    private boolean isUsernameNull(JoinGameRequest request, String color) {
        if (Objects.equals(color, "WHITE")) {
            return gameData.getGame(request.gameID()).whiteUsername() == null;
        } else {
            return gameData.getGame(request.gameID()).blackUsername() == null;
        }
    }

    private boolean colorNameEqualsUsername(JoinGameRequest request, String color) {
        if (Objects.equals(color, "WHITE")) {

        }
    }

    public JoinGameResult joinGame(JoinGameRequest request, String authToken) throws DataAccessException {
        if (isTokenNull(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        if ()
    }
}
