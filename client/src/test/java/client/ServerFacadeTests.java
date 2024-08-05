package client;

import dataaccess.ResponseException;
import model.UserData;
import org.junit.jupiter.api.*;
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
}
