package websocket;

import org.eclipse.jetty.websocket.api.Session;

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
}
