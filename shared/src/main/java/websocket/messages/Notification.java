package websocket.messages;

public class Notification extends ServerMessage {

    String notification;

    public Notification(String message) {
        super(ServerMessageType.NOTIFICATION);
        notification = message;
    }

    public String getNotification() {
        return notification;
    }
}
