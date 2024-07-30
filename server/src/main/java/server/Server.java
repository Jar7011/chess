package server;

import dataaccess.*;
import handlers.ClearHandler;
import handlers.GameHandler;
import handlers.UserHandler;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    UserDAO user = new SQLUserDAO();
    AuthDAO authorization = new SQLAuthDAO();
    GameDAO game = new MemoryGameDAO(authorization);
    UserService userService = new UserService(user,authorization);
    ClearService clearService = new ClearService(user, authorization, game);
    GameService gameService = new GameService(authorization, game);
    UserHandler userHandler = new UserHandler(userService);
    ClearHandler clearHandler = new ClearHandler(clearService);
    GameHandler gameHandler = new GameHandler(gameService);


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        //Spark.delete("/db", );
        Spark.delete("/db", ((request, response) -> clearHandler.clearDatabase(request, response)));
        Spark.post("/user", ((request, response) -> userHandler.registerHandler(request, response)));
        Spark.post("/session", ((request, response) -> userHandler.loginHandler(request, response)));
        Spark.delete("/session", ((request, response) -> userHandler.logoutHandler(request, response)));
        Spark.get("/game", ((request, response) -> gameHandler.listGameHandler(request, response)));
        Spark.post("/game", ((request, response) -> gameHandler.createGameHandler(request, response)));
        Spark.put("/game", ((request, response) -> gameHandler.joinGameHandler(request, response)));

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
