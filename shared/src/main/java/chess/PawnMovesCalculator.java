package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possMoves = new ArrayList<>();
        ChessPiece pawn = board.getPiece(myPosition);
        if ((pawn.getPieceType() != ChessPiece.PieceType.PAWN)) {
            return possMoves;
        }
        int possDirection = 0;
        int initialRow = 0;
        int promoteRow = 0;
        if (pawn.getTeamColor() == ChessGame.TeamColor.WHITE) {
            possDirection = 1;
            initialRow = 2;
            promoteRow = 8;
        }
        if (pawn.getTeamColor() == ChessGame.TeamColor.BLACK) {
            possDirection = -1;
            initialRow = 7;
            promoteRow = 1;
        }
        int[][] possibleDirections = {{possDirection, 0}, {possDirection * 2, 0}};

        for (int[] possibleDirection : possibleDirections) {
            int possNewRow = myPosition.getRow() + possibleDirection[0];
            int possNewCol = myPosition.getColumn() + possibleDirection[1];
            if ((possNewRow >= 1 && possNewRow <= 8) && (possNewCol >= 1 && possNewCol <= 8)) {
                ChessPosition possPosition = new ChessPosition(possNewRow, possNewCol);
                ChessPiece pieceAlreadyThere = board.getPiece(possPosition);
                if ((possibleDirection[0] == possDirection * 2) && (myPosition.getRow() != initialRow)) {
                    continue;
                }
                if (pieceAlreadyThere == null) {
                    if (promoteRow == possNewRow) {
                        possMoves.add(new ChessMove(myPosition, possPosition, ChessPiece.PieceType.BISHOP));
                        possMoves.add(new ChessMove(myPosition, possPosition, ChessPiece.PieceType.ROOK));
                        possMoves.add(new ChessMove(myPosition, possPosition, ChessPiece.PieceType.QUEEN));
                        possMoves.add(new ChessMove(myPosition, possPosition, ChessPiece.PieceType.KNIGHT));
                    } else {
                        possMoves.add(new ChessMove(myPosition, possPosition, null));
                    }
                } else {
                    break;
                }
            }
        }
        int[][] eliminateDirections = {{possDirection, -1}, {possDirection, 1}};

        for (int[] eliminateDirection : eliminateDirections) {
            int possNewRow = myPosition.getRow() + eliminateDirection[0];
            int possNewCol = myPosition.getColumn() + eliminateDirection[1];
            if ((possNewRow >= 1 && possNewRow <= 8) && (possNewCol >= 1 && possNewCol <= 8)) {
                ChessPosition possPosition = new ChessPosition(possNewRow, possNewCol);
                ChessPiece otherPieceAlreadyThere = board.getPiece(possPosition);
                if ((otherPieceAlreadyThere != null) && (otherPieceAlreadyThere.getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
                    if (promoteRow == possNewRow) {
                        possMoves.add(new ChessMove(myPosition, possPosition, ChessPiece.PieceType.BISHOP));
                        possMoves.add(new ChessMove(myPosition, possPosition, ChessPiece.PieceType.ROOK));
                        possMoves.add(new ChessMove(myPosition, possPosition, ChessPiece.PieceType.QUEEN));
                        possMoves.add(new ChessMove(myPosition, possPosition, ChessPiece.PieceType.KNIGHT));
                    } else {
                        possMoves.add(new ChessMove(myPosition, possPosition, null));
                    }
                }
            }
        }
        return possMoves;
    }
}
