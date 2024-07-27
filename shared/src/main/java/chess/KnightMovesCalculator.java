package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator extends MoveFunctionsCalculator {

    private final int[][] possibleDirections = { {2,1}, {2,-1}, {1,2}, {-1,2}, {-2,1}, {-2,-1}, {1,-2}, {-1,-2} };

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return calculateMovesKK(board, myPosition, possibleDirections);
    }
}
