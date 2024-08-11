package websocket.commands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {

    ChessMove move;

    public MakeMove(String authToken, int gameID, ChessMove chessMove) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        move = chessMove;
    }

    public ChessMove getMove() {
        return move;
    }

}
