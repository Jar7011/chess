package dataaccess;

public class ResponseException extends Throwable {

    public ResponseException(int statusCode, String message) {
        super(statusCode + ": " + message);
    }

}
