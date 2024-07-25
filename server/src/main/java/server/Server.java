package server;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import handlers.UserHandler;
import service.UserService;
import spark.*;

public class Server {

    UserDAO user = new MemoryUserDAO();
    AuthDAO authorization = new MemoryAuthDAO();
    UserService service = new UserService(user,authorization);
    UserHandler userHandler = new UserHandler(service);

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        //Spark.delete("/db", );
        Spark.post("/user", ((request, response) -> userHandler.registerHandler(request, response)));
        Spark.post("/session", ((request, response) -> userHandler.loginHandler(request, response)));
        Spark.delete("/session", ((request, response) -> userHandler.logoutHandler(request, response)));

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
