package model;

import chess.ChessGame;

public class GameData {

    private final int gameID;
    private String whiteUsername;
    private String blackUsername;
    private final String gameName;
    private final ChessGame game;

    public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }

    public int getGameID() {
        return gameID;
    }

    public void setWhiteUsername(String username) {
        whiteUsername = username;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setBlackUsername(String username) {
        blackUsername = username;
    }

    public String setBlackUser() {
        return blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return game;
    }
}
