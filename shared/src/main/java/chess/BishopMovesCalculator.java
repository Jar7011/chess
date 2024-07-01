package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {


    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possMoves = new ArrayList<>();
        int[][] possibleDirections = { {1,1}, {1,-1}, {-1,1}, {-1,-1} };

        for (int[] possibleDirection : possibleDirections) {
            int possNewRow = myPosition.getRow() + possibleDirection[0];
            int possNewCol = myPosition.getColumn() + possibleDirection[1];

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
                possNewRow += possibleDirection[0];
                possNewCol += possibleDirection[1];
            }
        }
        return possMoves;
    }
}
