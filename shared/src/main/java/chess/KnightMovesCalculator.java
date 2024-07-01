package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possMoves = new ArrayList<>();
        int[][] possibleDirections = { {2,1}, {2,-1}, {1,2}, {-1,2}, {-2,1}, {-2,-1}, {1,-2}, {-1,-2} };

        for (int[] possibleDirection : possibleDirections) {
            int possNewRow = myPosition.getRow() + possibleDirection[0];
            int possNewCol = myPosition.getColumn() + possibleDirection[1];

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
