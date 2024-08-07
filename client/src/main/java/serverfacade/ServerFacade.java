package serverfacade;

import com.google.gson.Gson;
import exception.ResponseException;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    private String authToken;
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(RegisterRequest request) throws ResponseException {
        var path = "/user";
        RegisterResult response = this.makeRequest("POST", path, request, RegisterResult.class);
        authToken = response.authToken();
        return response;
    }

    public void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, ClearResult.class);
    }

    public LoginResult login(LoginRequest request) throws ResponseException {
        var path = "/session";
        LoginResult response = this.makeRequest("POST", path, request, LoginResult.class);
        authToken = response.authToken();
        return response;
    }

    public void logout() throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, LogoutResult.class);
        authToken = null;
    }

    public void createGame(CreateGameRequest request) throws ResponseException {
        var path = "/game";
        this.makeRequest("POST", path, request, CreateGameResult.class);
    }

    public ListGamesResult listGames() throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null, ListGamesResult.class);
    }

    public void joinGame(JoinGameRequest request) throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, request, JoinGameResult.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass)
            throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            http.setRequestProperty("authorization", authToken);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, "Can't do that");
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    public String getAuthToken() {
        return authToken;
    }
}
