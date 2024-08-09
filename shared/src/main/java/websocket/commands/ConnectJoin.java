package websocket.commands;

import chess.ChessGame;

public class ConnectJoin extends UserGameCommand {

    ChessGame.TeamColor teamColor;

    public ConnectJoin(String authToken, int gameID, ChessGame.TeamColor color) {
        super(CommandType.CONNECT, authToken, gameID);
        teamColor = color;
    }

    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }
}
