package websocket.messages;

import chess.ChessGame;
import model.GameData;

public class LoadGame extends ServerMessage {

    GameData game;

    public LoadGame(GameData gameData) {
        super(ServerMessageType.LOAD_GAME);
        game = gameData;
    }

    public GameData getGame() {
        return game;
    }
}
