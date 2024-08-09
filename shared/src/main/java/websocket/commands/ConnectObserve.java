package websocket.commands;

public class ConnectObserve extends UserGameCommand {

    public ConnectObserve(String authToken, int gameID) {
        super(CommandType.CONNECT, authToken, gameID);
    }
}
