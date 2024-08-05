package ui;

import dataaccess.ResponseException;
import request.LoginRequest;
import request.RegisterRequest;
import serverFacade.ServerFacade;
import serverFacade.State;

import java.util.Arrays;

public class MenuUI {

    private final ServerFacade server;
    public static String username;
    public static String authToken;
    private State state = State.SIGNEDOUT;

    public MenuUI(String url) {
        server = new ServerFacade(url);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
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

    public String help() {
        return """
                - register <username> <password> <email> - to create an account
                - login <username> <password> - to play chess
                - quit - playing chess
                - help - with possible commands
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }

}
