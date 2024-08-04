package ui;

import dataaccess.ResponseException;
import request.RegisterRequest;
import serverFacade.Repl;
import serverFacade.ServerFacade;

import java.util.Arrays;

public class PreloginUI {

    private final ServerFacade server;
    private final String serverUrl;

    public PreloginUI(String url) {
        serverUrl = url;
        server = new ServerFacade(serverUrl);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 2) {
            server.register(new RegisterRequest(params[0], params[1], params[2]));
            Repl.username = params[0];
            Repl.authToken = server.getAuthToken();
            return String.format("You signed in as %s.", Repl.username);
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    public String help() {
        return """
                - register <username> <password> <email> - to create an account
                - login <username> <password> - to play chess
                - quit - playing chess
                - help - with possible commands
                """;
    }

}
