package ui;

import dataaccess.ResponseException;
import handlers.GameHandler;
import model.GameData;
import request.CreateGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import serverFacade.ServerFacade;
import serverFacade.State;

import java.util.Arrays;
import java.util.Collection;

public class MenuUI {

    private final ServerFacade server;
    private static String username;
    private static String authToken;
    private State state = State.SIGNEDOUT;
    private Collection<GameData> gameList;

    public MenuUI(String url) throws ResponseException {
        server = new ServerFacade(url);
        gameList = server.listGames().games();
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout(params);
                case "create" -> createGame(params);
                case "list" -> listGames(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 2) {
            RegisterRequest request = new RegisterRequest(params[0], params[1], params[2]);
            server.register(request);
            username = params[0];
            authToken = server.getAuthToken();
            return String.format("You registered as %s.", username);
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    public String login(String... params) throws ResponseException {
        if (params.length >= 1) {
            LoginRequest request = new LoginRequest(params[0], params[1]);
            server.login(request);
            username = params[0];
            authToken = server.getAuthToken();
            state = State.SIGNEDIN;
            return String.format("You logged in as %s.", username);
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    public String logout(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 0) {
            server.logout();
            state = State.SIGNEDOUT;
            return "You successfully logged out.";
        }
        throw new ResponseException(400, "There was an error logging you out.");
    }

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 0) {
            StringBuilder name = new StringBuilder(params[0]);
            for (int i = 1; i < params.length; i++) {
                name.append(" ");
                name.append(params[i]);
            }
            CreateGameRequest newGame = new CreateGameRequest(params[0]);
            server.createGame(newGame);
            gameList = server.listGames().games();
            return String.format("Created game with the following name: %s.", name);
        }
        throw new ResponseException(400, "Couldn't create game");
    }

    public String listGames(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 0) {
            StringBuilder gameInfo = new StringBuilder();
            gameList = server.listGames().games();
            int i = 1;

            for (GameData game : gameList) {
                gameInfo.append("Game ");
                gameInfo.append(i);
                gameInfo.append("\n");
                gameInfo.append("Game ID - ");
                gameInfo.append(game.gameID());
                gameInfo.append("\n");
                gameInfo.append("White username - ");
                gameInfo.append(game.whiteUsername());
                gameInfo.append("\n");
                gameInfo.append("Black username - ");
                gameInfo.append(game.blackUsername());
                gameInfo.append("\n");
                gameInfo.append("Game name - ");
                gameInfo.append(game.gameName());
                gameInfo.append("\n");
                i++;
            }
            return gameInfo.toString();
        }
        throw new ResponseException(400, "There was an error printing list of games.");
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                - register <username> <password> <email> - to create an account
                - login <username> <password> - to play chess
                - quit - playing chess
                - help - with possible commands
                """;
        }
        return """
                - create <name> - game
                - list - games
                - join <ID> [WHITE | BLACK] - a game
                - observe <ID> - a game
                - logout - when you're done
                - quit playing chess
                - help - with possible commands
                """;

    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }

}
