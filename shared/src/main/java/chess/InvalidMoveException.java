package chess;

/**
 * Indicates an invalid move was made in a game
 */
public class InvalidMoveException extends Exception {

    public InvalidMoveException() {
        super();
    }

    public InvalidMoveException(String message) {
        super(message);
    }

    public InvalidMoveException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMoveException(Throwable cause) {
        super(cause);
    }

    public InvalidMoveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
