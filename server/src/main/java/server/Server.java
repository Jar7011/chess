package server;

import dataaccess.*;
import handlers.ClearHandler;
import handlers.UserHandler;
import service.ClearService;
import service.UserService;
import spark.*;

public class Server {

    UserDAO user = new MemoryUserDAO();
    AuthDAO authorization = new MemoryAuthDAO();
    GameDAO game = new MemoryGameDAO();
    UserService userService = new UserService(user,authorization);
    ClearService clearService = new ClearService(user, authorization, game);
    UserHandler userHandler = new UserHandler(userService);
    ClearHandler clearHandler = new ClearHandler(clearService);


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        //Spark.delete("/db", );
        Spark.post("/user", ((request, response) -> userHandler.registerHandler(request, response)));
        Spark.post("/session", ((request, response) -> userHandler.loginHandler(request, response)));
        Spark.delete("/session", ((request, response) -> userHandler.logoutHandler(request, response)));
        Spark.delete("/db", ((request, response) -> clearHandler.ClearDatabase(request, response)));

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
