package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.Connect;
import websocket.commands.Leave;
import websocket.commands.MakeMove;
import websocket.commands.Resign;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    notificationHandler.notify(message);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinGame(String authToken, int gameID, ChessGame.TeamColor teamColor) throws ResponseException {
        try {
            var action = new Connect(authToken, gameID, teamColor);
            session.getBasicRemote().sendText(new Gson().toJson(action));
        }
        catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void observeGame(String authToken, int gameID) throws ResponseException {
        try {
            var action = new Connect(authToken, gameID);
            session.getBasicRemote().sendText(new Gson().toJson(action));
        }
        catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

//    public void leaveGame(String authToken, int gameID) throws ResponseException {
//        try {
//            var action = new Leave(authToken, gameID);
//            session.getBasicRemote().sendText(new Gson().toJson(action));
//        }
//        catch (IOException ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
//
//    public void resignGame(String authToken, int gameID) throws ResponseException {
//        try {
//            var action = new Resign(authToken, gameID);
//            session.getBasicRemote().sendText(new Gson().toJson(action));
//        }
//        catch (IOException ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }

    public void makeMove(String authToken, int gameID, ChessMove chessMove) throws ResponseException {
        try {
            var action = new MakeMove(authToken, gameID, chessMove);
            session.getBasicRemote().sendText(new Gson().toJson(action));
        }
        catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

}
