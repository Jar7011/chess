package websocket.commands;

import chess.ChessGame;

public class Connect extends UserGameCommand {

    ChessGame.TeamColor teamColor;

    public Connect(String authToken, int gameID, ChessGame.TeamColor color) {
        super(CommandType.CONNECT, authToken, gameID);
        teamColor = color;
    }

    public Connect(String authToken, int gameID) {
        super(CommandType.CONNECT, authToken, gameID);
    }

    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }
}
