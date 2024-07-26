package service;

import dataaccess.*;
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

    public JoinGameResult joinGame(JoinGameRequest request, String authToken) throws DataAccessException {
        if (isTokenNull(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        }
        if (isGameNull(request)) {
            throw new BadRequestException("Error: bad request");
        }
        if (isColorNotValid(request)) {
            throw new BadRequestException("Error: bad request");
        }
        if (request.playerColor() == null) {
            throw new BadRequestException("Spectating");
        }
        if (Objects.equals(request.playerColor(), "BLACK")) {
            if (!isBlackNull(request) && !blackNameEqualsUsername(request, authToken)) {
                throw new ColorException("Error: already there");
            }
        }
        if (Objects.equals(request.playerColor(), "WHITE")) {
            if (!isWhiteNull(request) && !whiteNameEqualsUsername(request, authToken)) {
                throw new ColorException("Error: already there");
            }

        }
        gameData.updateGame(request.gameID(), request.playerColor(), authToken);
        return new JoinGameResult("Joined as " + request.playerColor());
    }

    private boolean isTokenNull(String authToken) {
        return authData.getAuth(authToken) == null;
    }

    private boolean isGameNull(JoinGameRequest request) {
        return gameData.getGame(request.gameID()) == null;
    }

    private boolean isWhiteNull(JoinGameRequest request) {
        return gameData.getGame(request.gameID()).whiteUsername() == null;
    }

    private boolean isBlackNull(JoinGameRequest request) {
        return gameData.getGame(request.gameID()).blackUsername() == null;
    }

    private boolean whiteNameEqualsUsername(JoinGameRequest request, String authToken) {
        return Objects.equals(gameData.getGame(request.gameID()).whiteUsername(), authData.getAuth(authToken).username());
    }

    private boolean blackNameEqualsUsername(JoinGameRequest request, String authToken) {
        return Objects.equals(gameData.getGame(request.gameID()).blackUsername(), authData.getAuth(authToken).username());
    }

    private boolean isColorNotValid(JoinGameRequest request) {
        return !Objects.equals(request.playerColor(), "WHITE") && !Objects.equals(request.playerColor(), "BLACK");
    }
}
