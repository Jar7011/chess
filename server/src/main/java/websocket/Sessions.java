package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class Sessions {

    ConcurrentHashMap<Integer, ConcurrentHashMap<String, Session>> sessionsInGame;

    public Sessions() {
        sessionsInGame = new ConcurrentHashMap<>();
    }

    public void add(String authToken, int gameID, Session session) {
        ConcurrentHashMap<String, Session> game = sessionsInGame.get(gameID);
        if (game != null) {
            game.put(authToken, session);
            sessionsInGame.put(gameID, game);
        }
        else {
            ConcurrentHashMap<String, Session> newGame = new ConcurrentHashMap<>();
            newGame.put(authToken, session);
            sessionsInGame.put(gameID, newGame);
        }
    }

    public void remove(String authToken, int gameID, Session session) {
        ConcurrentHashMap<String, Session> game = sessionsInGame.get(gameID);
        if (session.isOpen()) {
            game.remove(authToken, session);
            session.close();
        }
    }

    public void sendMessage(String authToken, int gameID, ServerMessage serverMessage) throws IOException {
        Session session = sessionsInGame.get(gameID).get(authToken);
        String message = new Gson().toJson(serverMessage);
        if (session.isOpen()) {
            session.getRemote().sendString(message);
        }
    }

    public void broadcastMessage(int gameID, ServerMessage message, String exceptThisAuth) throws IOException {
        sendMessageToAllSessions(gameID, message, exceptThisAuth);
    }

    public void broadcastMessageAll(int gameID, ServerMessage message) throws IOException {
        sendMessageToAllSessions(gameID, message, null);
    }

    private void sendMessageToAllSessions(int gameID, ServerMessage message, String notThisAuth) throws IOException {
        String messageJSON = new Gson().toJson(message);
        ConcurrentHashMap<String, Session> relevantSessions = sessionsInGame.get(gameID);

        for (String authToken : relevantSessions.keySet()) {
            Session session = sessionsInGame.get(gameID).get(authToken);
            if (session.isOpen()) {
                if (notThisAuth == null || !authToken.equals(notThisAuth)) {
                    session.getRemote().sendString(messageJSON);
                }
            }
        }
    }
}
