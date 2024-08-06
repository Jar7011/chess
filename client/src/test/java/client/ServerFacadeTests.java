package client;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResult;
import response.RegisterResult;
import server.Server;
import serverFacade.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    AuthDAO authorization = new SQLAuthDAO();
    GameDAO gameData = new SQLGameDAO(authorization);

    private final UserData testUser = new UserData("username", "password", "email");

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        var url = "http://localhost:" + port;
        serverFacade = new ServerFacade(url);
    }

    @BeforeEach
    public void clear() throws ResponseException {
        serverFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void passRegister() throws ResponseException {
        RegisterRequest user = new RegisterRequest(testUser.username(), testUser.password(), testUser.email());
        RegisterResult response = serverFacade.register(user);
        assertEquals("username", response.username());
    }

    @Test
    public void failRegister() throws ResponseException {
        RegisterRequest user = new RegisterRequest(testUser.username(), testUser.password(), testUser.email());
        serverFacade.register(user);
        assertThrows(ResponseException.class, () -> serverFacade.register(user));
    }

    @Test
    public void passLogin() throws ResponseException {
        RegisterRequest user = new RegisterRequest(testUser.username(), testUser.password(), testUser.email());
        serverFacade.register(user);
        LoginRequest request = new LoginRequest(user.username(), user.password());
        LoginResult response = serverFacade.login(request);
        assertEquals("username", response.username());
    }

    @Test
    public void failLogin() throws ResponseException {
        LoginRequest request = new LoginRequest(testUser.username(), testUser.password());
        assertThrows(ResponseException.class, () -> serverFacade.login(request));
    }

    @Test
    public void passLogout() throws ResponseException {
        RegisterRequest user = new RegisterRequest(testUser.username(), testUser.password(), testUser.email());
        serverFacade.register(user);
        LoginRequest request = new LoginRequest(user.username(), user.password());
        serverFacade.login(request);
        serverFacade.logout();
        assertNull(serverFacade.getAuthToken());
    }

    @Test
    public void failLogout() throws ResponseException {
        assertThrows(ResponseException.class, () -> serverFacade.logout());
    }

    @Test
    public void passCreateGame() throws ResponseException {
        RegisterRequest user = new RegisterRequest(testUser.username(), testUser.password(), testUser.email());
        serverFacade.register(user);
        LoginRequest request = new LoginRequest(user.username(), user.password());
        serverFacade.login(request);
        CreateGameRequest game = new CreateGameRequest("new game");
        serverFacade.createGame(game);
        assertEquals("new game", game.gameName());
    }

    @Test
    public void failCreateGame() throws ResponseException {
        CreateGameRequest game = new CreateGameRequest("new game");
        assertThrows(ResponseException.class, () -> serverFacade.createGame(game));
    }

    @Test
    public void passListGames() throws ResponseException {
        RegisterRequest user = new RegisterRequest(testUser.username(), testUser.password(), testUser.email());
        serverFacade.register(user);
        LoginRequest request = new LoginRequest(user.username(), user.password());
        serverFacade.login(request);
        CreateGameRequest game = new CreateGameRequest("new game");
        serverFacade.createGame(game);
        CreateGameRequest otherGame = new CreateGameRequest("another game");
        serverFacade.createGame(otherGame);
        assertEquals(2, serverFacade.listGames().games().size());
        System.out.println(serverFacade.listGames());
    }

    @Test
    public void failListGames() throws ResponseException {
        assertThrows(ResponseException.class, () -> serverFacade.listGames());
    }

    @Test
    public void passJoinGame() throws ResponseException, DataAccessException {
        RegisterRequest user = new RegisterRequest(testUser.username(), testUser.password(), testUser.email());
        serverFacade.register(user);
        LoginRequest request = new LoginRequest(user.username(), user.password());
        serverFacade.login(request);
        CreateGameRequest game = new CreateGameRequest("new game");
        serverFacade.createGame(game);
        JoinGameRequest joinRequest = new JoinGameRequest("BLACK", 1);
        serverFacade.joinGame(joinRequest);
        assertEquals(testUser.username(), gameData.getGame(1).blackUsername());
    }

    @Test
    public void failJoinGame() throws ResponseException {
        RegisterRequest user = new RegisterRequest(testUser.username(), testUser.password(), testUser.email());
        serverFacade.register(user);
        LoginRequest request = new LoginRequest(user.username(), user.password());
        serverFacade.login(request);
        CreateGameRequest game = new CreateGameRequest("new game");
        serverFacade.createGame(game);
        JoinGameRequest joinRequest = new JoinGameRequest("BLACK", 2);
        assertThrows(ResponseException.class, () -> serverFacade.joinGame(joinRequest));
    }
}
