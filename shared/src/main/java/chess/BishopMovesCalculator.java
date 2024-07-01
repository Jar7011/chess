package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator implements PieceMovesCalculator {


    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possMoves = new ArrayList<>();
        int[][] fourPossDirections = { {1,1}, {1,-1}, {-1,1}, {-1,-1} };

        for (int[] possDirection : fourPossDirections) {
            int possNewRow = myPosition.getRow() + possDirection[0];
            int possNewCol = myPosition.getColumn() + possDirection[1];

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
                possNewRow += possDirection[0];
                possNewCol += possDirection[1];
            }
        }
        return possMoves;
    }
}
