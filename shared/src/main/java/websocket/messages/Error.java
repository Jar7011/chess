package websocket.messages;

public class Error extends ServerMessage {

    String error;

    public Error(String message) {
        super(ServerMessageType.ERROR);
        error = message;
    }

    public String getError() {
        return error;
    }
}
