package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGame() {
        teamTurn = TeamColor.WHITE;
        board = new ChessBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece currentPiece = board.getPiece(startPosition);
        if (currentPiece == null) {
            return new ArrayList<>();
        }
        Collection<ChessMove> allMoves = board.getPiece(startPosition).pieceMoves(board, startPosition);
        Collection<ChessMove> possibleMoves = new ArrayList<>();

        for (ChessMove move : allMoves) {
            ChessBoard boardClone = board.cloneBoard();
            if (move.getPromotionPiece() != null) {
                boardClone.addPiece(move.getEndPosition(), new ChessPiece(this.getTeamTurn(), move.getPromotionPiece()));
            } else {
                boardClone.addPiece(move.getEndPosition(), currentPiece);
            }
            boardClone.removePiece(move.getStartPosition());
            if (!isInCheckBoardCopy(boardClone, currentPiece.getTeamColor())) {
                possibleMoves.add(move);
            }
        }
        return possibleMoves;
    }

    //
    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition position = move.getStartPosition();
        Collection<ChessMove> validMoves = validMoves(position);
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("Your move is not valid");
        }
        ChessPiece pieceMoving = board.getPiece(move.getStartPosition());
        if (pieceMoving.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("It's not your turn");
        }
        if (move.getPromotionPiece() != null) {
            board.addPiece(move.getEndPosition(), new ChessPiece(teamTurn, move.getPromotionPiece()));
        } else {
            board.addPiece(move.getEndPosition(), pieceMoving);
        }
        board.removePiece(move.getStartPosition());
        if (isInCheck(teamTurn)) {
            throw new InvalidMoveException("After moving you are still in check");
        }
        if (teamTurn == TeamColor.WHITE) {
            teamTurn = TeamColor.BLACK;
        } else {
            teamTurn = TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKingPosition(board, teamColor);
        if (kingPosition == null) {
            return false;
        }
        return isInDanger(board, kingPosition, teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        return isStillInDanger(teamColor, 0);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        int stalemate = 1;
        if (isInCheck(teamColor)) {
            return false;
        }
        return isStillInDanger(teamColor, 1);
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    // Some helper functions
    private ChessPosition findKingPosition(ChessBoard board, TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPiece currentPiece = board.getPiece(new ChessPosition(row, col));
                if (currentPiece != null && currentPiece.getTeamColor() == teamColor && currentPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    return new ChessPosition(row, col);
                }
            }
        }
        return null;
    }

    private boolean isInDanger(ChessBoard board, ChessPosition position, TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPiece currentPiece = board.getPiece(new ChessPosition(row, col));
                if (currentPiece != null && currentPiece.getTeamColor() != teamColor) {
                    Collection<ChessMove> possibleMoves = currentPiece.pieceMoves(board, new ChessPosition(row, col));

                    for (ChessMove possibleMove : possibleMoves) {
                        if (possibleMove.getEndPosition().getRow() == position.getRow() && possibleMove.getEndPosition().getColumn() == position.getColumn()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isStillInDanger(TeamColor teamColor, int stalemate) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPiece currentPiece = board.getPiece(new ChessPosition(row, col));
                if (stalemate == 1) {
                    if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                        if (!validMoves(findKingPosition(board, teamColor)).isEmpty()){
                            return false;
                        }
                    }
                } else {
                    if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                        if (!validMoves(new ChessPosition(row, col)).isEmpty()){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean isInCheckBoardCopy(ChessBoard board, TeamColor teamColor) {
        ChessPosition kingPosition = findKingPosition(board, teamColor);
        if (kingPosition == null) {
            return false;
        }
        return isInDanger(board, kingPosition, teamColor);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }
}
