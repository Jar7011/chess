package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator extends MoveFunctionsCalculator {

    private final int[][] possibleDirections = { {1,1}, {1,-1}, {-1,1}, {-1,-1} };


    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return calculateMovesQRB(board, myPosition, possibleDirections);
    }
}
