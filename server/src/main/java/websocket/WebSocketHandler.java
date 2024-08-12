package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.Connect;
import websocket.commands.UserGameCommand;
import websocket.messages.Error;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {

    UserDAO userData;
    AuthDAO authData;
    GameDAO gameData;
    Sessions sessionsData;

    public WebSocketHandler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO, Sessions sessions) {
        userData = userDAO;
        authData = authDAO;
        gameData = gameDAO;
        sessionsData = sessions;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
//        switch (command.getCommandType()) {
//            case CONNECT ->
//        }
    }

    public void connect(Session session, String message) throws IOException, DataAccessException {
        Connect connect = new Gson().fromJson(message, Connect.class);
        int gameID = connect.getGameID();
        String authToken = connect.getAuthToken();
        if (authData.getAuth(authToken) == null) {
            sendError(session, "invalid authToken");
            return;
        }
        if (gameData.getGame(gameID) == null) {
            sendError(session, "invalid gameID");
            return;
        }
        String username = authData.getAuth(authToken).username();
        String whiteUsername = gameData.getGame(gameID).whiteUsername();
        String blackUsername = gameData.getGame(gameID).blackUsername();
        ChessGame.TeamColor color = connect.getTeamColor();
        if (color != null) {
            if (color == ChessGame.TeamColor.BLACK) {
                if (blackUsername == null || !Objects.equals(username, blackUsername)) {
                    sendError(session, "invalid color");
                    return;
                }
            }
            else if (color == ChessGame.TeamColor.WHITE) {
                if (whiteUsername == null || !Objects.equals(username, whiteUsername)) {
                    sendError(session, "invalid color");
                    return;
                }
            }
            sessionsData.add(authToken, gameID, session);

        }
    }

    private void sendError(Session session, String message) throws IOException {
        String error = new Gson().toJson(new Error(message));
        session.getRemote().sendString(error);
    }



}
