package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MoveFunctionsCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return List.of();
    }

    public Collection<ChessMove> calculateMovesQRB(ChessBoard board, ChessPosition myPosition, int[][] directions) {
        Collection<ChessMove> possMoves = new ArrayList<>();

        for (int[] direction : directions) {
            int possNewRow = myPosition.getRow() + direction[0];
            int possNewCol = myPosition.getColumn() + direction[1];

            while ((possNewRow >= 1 && possNewRow <= 8) && (possNewCol >= 1 && possNewCol <= 8)) {
                ChessPosition possPosition = new ChessPosition(possNewRow, possNewCol);
                ChessPiece pieceAlreadyThere = board.getPiece(possPosition);
                if (pieceAlreadyThere == null) {
                    possMoves.add(new ChessMove(myPosition, possPosition, null));
                } else {
                    if (pieceAlreadyThere.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        possMoves.add(new ChessMove(myPosition, possPosition, null));
                    }
                    break;
                }
                possNewRow += direction[0];
                possNewCol += direction[1];
            }
        }
        return possMoves;
    }

    public Collection<ChessMove> calculateMovesKK(ChessBoard board, ChessPosition myPosition, int[][] directions) {
        Collection<ChessMove> possMoves = new ArrayList<>();
        for (int[] direction : directions) {
            int possNewRow = myPosition.getRow() + direction[0];
            int possNewCol = myPosition.getColumn() + direction[1];

            if ((possNewRow >= 1 && possNewRow <= 8) && (possNewCol >= 1 && possNewCol <= 8)) {
                ChessPosition possPosition = new ChessPosition(possNewRow, possNewCol);
                ChessPiece pieceAlreadyThere = board.getPiece(possPosition);
                if ((pieceAlreadyThere == null) || (pieceAlreadyThere.getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
                    possMoves.add(new ChessMove(myPosition, possPosition, null));
                }
            }
        }
        return possMoves;
    }

}
